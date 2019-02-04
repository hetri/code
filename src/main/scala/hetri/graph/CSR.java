package hetri.graph;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import hetri.type.IntPair;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.IntStream;

public class CSR implements Graph {

    Logger logger = Logger.getLogger(getClass());

    private static double BACKUP_EDGE_RATIO = 1.11;
    private static double BACKUP_NODE_RATIO = 1.01;

    public int numEdges = 0;
    public int numNodes = 0;
    public int[] edges, nodes_val;

    public CSR(){}

    public CSR(Path pedge, Path pnode, FileSystem fs) throws IOException {
        read(pedge, pnode, fs);
    }

    public CSR(Input iedge, Input inode){
        read(iedge, inode);
    }

    @Override
    public Iterator<Integer> nodeIterator(){
        return IntStream.range(0, numNodes).iterator();
    }


    public void writeEdges(Output out) {
        out.writeInt(numEdges);

        for(int i = 0; i < numEdges; i++)
            out.writeInt(edges[i]);
    }

    public void writeNodes(Output out) {
        out.writeInt(numNodes);

        for(int i = 0; i < numNodes; i++) {
            out.writeInt(nodes_val[i]);
        }

        out.writeInt(nodes_val[numNodes]);
    }

    @Override
    public void write(Output out_edge, Output out_node) {
        writeEdges(out_edge);
        writeNodes(out_node);
    }

    private void _read(Input iedge, Input inode) {
        readEdges(iedge);
        readNodes(inode);
    }

    @Override
    public void read(Input iedge, Input inode) {
        _read(iedge, inode);
        iedge.close();
        inode.close();
    }

    @Override
    public void read(Path pedge, Path pnode, FileSystem fs) throws IOException {
        Input iedge = new Input(fs.open(pedge));
        Input inode = new Input(fs.open(pnode));

        _read(iedge, inode);

        iedge.close();
        inode.close();
    }


    private void readEdges(Input in) {
        if(in == null) return;

        int numEdges_new = in.readInt();

        if(edges == null || numEdges_new > edges.length){
            int sizeBefore = edges == null ? 0 : edges.length;
            int sizeAfter = (int) (numEdges_new * BACKUP_EDGE_RATIO);
            logger.info("load new edges size from " + sizeBefore + "to " + sizeAfter);
            edges = new int[sizeAfter];
        }

        for (int i = 0; i < numEdges_new; i++) {
            edges[i] = in.readInt();
        }

        numEdges = numEdges_new;
    }

    private void readNodes(Input in) {
        if(in == null) return;

        int numNodes_new = in.readInt();

        if(nodes_val == null || numNodes_new + 1 > nodes_val.length){
            int sizeBefore = nodes_val == null ? 0 : nodes_val.length;
            int sizeAfter = (int) (numNodes_new * BACKUP_NODE_RATIO) + 1;
            logger.info("load new nodes size from " + sizeBefore + "to " + sizeAfter);
            nodes_val = new int[sizeAfter];
        }

        for(int i = 0; i < numNodes_new; i++){
            nodes_val[i] = in.readInt();
        }

        nodes_val[numNodes_new] = in.readInt();

        numNodes = numNodes_new;
    }

    @Override
    public void writeFrom(Iterable<? extends IntPair> edges, Output oedge, Output onode){
        LongArrayList arr = new LongArrayList();

        int maxNodeId = -1;

        for(IntPair pair : edges){
            int u = pair.get_u();
            int v = pair.get_v();

            int max = u > v ? u : v;
            maxNodeId = max > maxNodeId ? max : maxNodeId;

            arr.add(pair.getAsLong());
        }

        arr.sort(null);

        int len = arr.size();
        int numNodes = (int) (arr.getLong(len - 1) >> 32) + 1;

        onode.writeInt(numNodes);
        oedge.writeInt(len);

        int ecur = 0;
        for (int i = 0; i <= numNodes; i++) {

            while ( ecur < len ){

                long e = arr.getLong(ecur);
                int u = (int) (e >> 32);
                int v = (int) e;

                if( u < i ){
                    oedge.writeInt(v);
                    ecur++;
                }
                else break;
            }

            onode.writeInt(ecur);
        }

        oedge.close();
        onode.close();
    }

}
