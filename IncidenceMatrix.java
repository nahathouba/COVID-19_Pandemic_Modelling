import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Incidence matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */
public class IncidenceMatrix extends AbstractGraph
{
	private Map<String, Vertex> edgeMap = new HashMap<String, Vertex>();
	private int[][] arrayMatrix;
	private final int vertex = 10000;
	private int edges = 10000;
	private int numVertex = 0;
	private int numEdge = 0;
	/**
	 * Contructs empty graph.
	 */
	
	// implement map class for 2D array
    public IncidenceMatrix() {
    	arrayMatrix = new int[vertex][edges];
    } // end of IncidenceMatrix()


    public void addVertex(String vertLabel) {
        Vertex newVertex;
    	if (numVertex != vertex) {
        	// Column value is -1 as we don't know it's edges yet.
    		newVertex = new Vertex(numVertex, -1);
    		newVertex.setVertName(vertLabel);
    		vertexMap.put(vertLabel, newVertex);
    		numVertex++;
        }
    } // end of addVertex()


    public void addEdge(String srcLabel, String tarLabel) {
        Vertex srcVer;
        Vertex tarVer;
        Vertex newVertex;
        
    	if (vertexMap.containsKey(srcLabel) && vertexMap.containsKey(tarLabel)) {
        	srcVer = vertexMap.get(srcLabel);
        	tarVer = vertexMap.get(tarLabel);        	
        	arrayMatrix[srcVer.getRow()][numEdge] = 1;
        	arrayMatrix[tarVer.getRow()][numEdge] = 1;
        	newVertex = new Vertex(-1, numEdge);
        	newVertex.setVertName(srcLabel+tarLabel);
        	edgeMap.put(srcLabel+tarLabel, newVertex);
        	srcVer.setNeighbour(tarVer);
        	numEdge++;
        } else {
        	System.err.println("One or more vertex not found in the graph, when adding edge!");
        }
    } // end of addEdge()


    public void toggleVertexState(String vertLabel) {
        super.toggleVertexState(vertLabel);
    } // end of toggleVertexState()


    public void deleteEdge(String srcLabel, String tarLabel) {
    	int srcRow;
    	int tarRow;
    	int verCol;
    	if (vertexMap.containsKey(srcLabel) && (vertexMap.containsKey(tarLabel))) {
    		srcRow = vertexMap.get(srcLabel).getRow();
    		tarRow = vertexMap.get(tarLabel).getRow();
    		
    		if (edgeMap.containsKey(srcLabel+tarLabel)) {
    			verCol = edgeMap.get(srcLabel+tarLabel).getCol();
    			arrayMatrix[srcRow][verCol] = 0;
    			arrayMatrix[tarRow][verCol] = 0;
    			edgeMap.remove(srcLabel+tarLabel);
    			deleteEdgeArray(vertexMap.get(srcLabel), vertexMap.get(tarLabel));
    			shiftElements(verCol);
    			numEdge--;
    		} else {
    			System.err.println("Edges not found, when deleting the edge!");
    		}
    	} else {
    		System.err.println("Vertex not found, when deleting the edge!");
    	}
    } // end of deleteEdge()
    
    private void shiftElements(int vertexCol) {
    	for ( int i = 0; i < numVertex; i++) {
    		for (int j = vertexCol; j < numEdge; j++) {
    			arrayMatrix[i][j] = arrayMatrix[i][j + 1];
    		}
    	}
    }
    
    protected void deleteEdgeArray(Vertex srcVertex, Vertex tarLabel) {
    	super.deleteEdgeArray(srcVertex, tarLabel);
    }


    public void deleteVertex(String vertLabel) {
       int vertRow;
    	if (vertexMap.containsKey(vertLabel)) {
    	   vertRow = vertexMap.get(vertLabel).getRow();
    	   for (int i = 0; i < numEdge; i++) {
    		   arrayMatrix[vertRow][i] = 0;
    	   }
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
    	return numVertex;
    }

} // end of class IncidenceMatrix
