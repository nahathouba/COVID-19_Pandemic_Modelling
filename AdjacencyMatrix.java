import java.io.PrintWriter;;


/**
 * Adjacency matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */
public class AdjacencyMatrix extends AbstractGraph
{
	/**
	 * Constructs empty graph.
	 */
    private final int SIZE_ARRAY = 10000;
    private int arrayMatrix[][];
    private int size = 0;

    public AdjacencyMatrix() {
    	arrayMatrix = new int[SIZE_ARRAY][SIZE_ARRAY];
    	
    } // end of AdjacencyMatrix()


    public void addVertex(String vertLabel) {
        Vertex newVertex;
    	if (size != SIZE_ARRAY) {
            if (!vertexMap.containsKey(vertLabel)) {
                arrayMatrix[size][size] = 1;
                newVertex = new Vertex(size, size);
                newVertex.setVertName(vertLabel);
                vertexMap.put(vertLabel, newVertex);
                size++;
            }
        }
    	
    } // end of addVertex()


    public void addEdge(String srcLabel, String tarLabel) {
        int srcRow;
        int tarRow;
        Vertex srcVertex;

        if (vertexMap.containsKey(srcLabel) && vertexMap.containsKey(tarLabel)) {
            srcRow = vertexMap.get(srcLabel).getRow();
            tarRow = vertexMap.get(tarLabel).getRow();
            arrayMatrix[srcRow][tarRow] = 1;
            arrayMatrix[tarRow][srcRow] = 1;
            srcVertex = vertexMap.get(srcLabel);
            srcVertex.setNeighbour(vertexMap.get(tarLabel));
        } else {
            System.err.println("One or more vertex not found in the graph, when adding edge!");
        }
    	
    } // end of addEdge()


    public void toggleVertexState(String vertLabel) {
       super.toggleVertexState(vertLabel);    	
    } // end of toggleVertexState()


    public void deleteEdge(String srcLabel, String tarLabel) {
       int srcRowCol;
       int tarRowCol;

        if (vertexMap.containsKey(srcLabel) && vertexMap.containsKey(tarLabel)) {
            srcRowCol = arrayMatrix[vertexMap.get(srcLabel).getRow()][vertexMap.get(tarLabel).getCol()];
            tarRowCol = arrayMatrix[vertexMap.get(tarLabel).getCol()][vertexMap.get(srcLabel).getRow()];

            if (srcRowCol == 1 && tarRowCol == 1) {
                arrayMatrix[vertexMap.get(srcLabel).getRow()][vertexMap.get(tarLabel).getCol()] = 0;
                arrayMatrix[vertexMap.get(tarLabel).getCol()][vertexMap.get(srcLabel).getRow()] = 0;
                deleteEdgeArray(vertexMap.get(srcLabel), vertexMap.get(tarLabel));
                
            } else {
                System.err.println("Edges not found, when deleting the edge!");
            }
       } else {
            System.err.println("Vertex not found, when deleting the edge!");
       }
    	
    } // end of deleteEdge()
    
    protected void deleteEdgeArray(Vertex srcVertex, Vertex tarLabel) {
    	super.deleteEdgeArray(srcVertex, tarLabel);
    }


    public void deleteVertex(String vertLabel) {
        if (vertexMap.containsKey(vertLabel)) {
            arrayMatrix[vertexMap.get(vertLabel).getRow()][vertexMap.get(vertLabel).getCol()] = 0;
            vertexMap.remove(vertLabel);
        } else {
            System.err.println("Vertex not found, when deleting the vertex!");
        }
    	
    } // end of deleteVertex()


    public String[] kHopNeighbours(int k, String vertLabel) {
        return super.kHopNeighbours(k, vertLabel);
    } // end of kHopNeighbours()
    
    public void printVertices(PrintWriter os) {
        super.printVertices(os);
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
    	super.printEdges(os);
    } // end of printEdges()


	@Override
	public Object getVertices() {
		return vertexMap;
	}
	
	@Override
	public int getSize() {
		return size;
	}

} // end of class AdjacencyMatrix
