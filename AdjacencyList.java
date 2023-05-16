import java.io.PrintWriter;
// import java.util.*;

/**
 * Adjacency list implementation for the AssociationGraph interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 */
public class AdjacencyList extends AbstractGraph
{
	private Node array[];
	private Node head;
	private Node tail;
	private int aLength;
	private final int SIZE_ARRAY = 10000;

    /**
	 * Contructs empty graph.
	 */
    public AdjacencyList() {
    	 // Implement me!
    	array = new Node[SIZE_ARRAY];
    	head = null;
    	tail = null;
    	aLength = 0;
    } // end of AdjacencyList()


    public void addVertex(String vertLabel) {
        // Implement me! array add

    	//If the array is null
    	if(aLength == 0){
    		array[0] = new Node(vertLabel);
    		head = array[0];
    		tail = array[0];
    	} else {
    		//add the vertices to the array of Linked List
    		for(int i = 0; i < aLength; i++) {
    			if(array[i].getValue().equalsIgnoreCase(vertLabel)) {
    				break;
    			}
    		}
    		array[aLength] = new Node(vertLabel);
    		tail = array[aLength];
    		
    	}
    	aLength++;
    } // end of addVertex()


    public void addEdge(String srcLabel, String tarLabel) {
    	boolean isSrcExist = checkNodeExist(srcLabel);
    	boolean isTarExist = checkNodeExist(tarLabel);
    	boolean isNeighbour = false;

    	if (isSrcExist == true && isTarExist == true) {
        	for(int i = 0; i < aLength; i++) {
        		for(int j = i + 1; j < aLength; j++) {
        			if(array[i].getValue().equalsIgnoreCase(srcLabel) && array[j].getValue().equalsIgnoreCase(tarLabel)) {
						isNeighbour = checkNextNode(array[i], array[j]);
						if (isNeighbour == true) {
							break;
						} else {
							array[i].setNext(array[j]);
							array[j].setPrevious(array[i]);
        					break;
						}
        			}
        		}
        	}
    		
    	} else {
    		System.err.println("One or more vertex not found in the graph!");
    	}
    	
    	//if head is empty add the vertex with the edge
    	
    	
    } // end of addEdge()

	/**
	 * Checks whether the given vertex's neighbours exist or not.
	 * @param srcNode The source vertex.
	 * @param tarNode The target vertex.
	 * @return true if exist or false if not.
	 */
	private boolean checkNextNode(Node srcNode, Node tarNode) {
		Node[] srcNeighbours = srcNode.getNext();
		
		if (srcNode.getNumNeighbours() != 0) {
	    	for (int i = 0; i < srcNode.getNumNeighbours(); i++) {
				if (srcNeighbours[i].getValue().equalsIgnoreCase(tarNode.getValue())) {
					return true;
				}
			}
		}
    	return false;
	}

	/**
	 * Checks whether the given vertex exist or not in the list.
	 * @param label The vertex's name.
	 * @return True if exist or false if not.
	 */
    private boolean checkNodeExist(String label) {
    	for (int i = 0; i < aLength; i++) {
    		if (array[i] == null) {
    			continue;
    		} else if (array[i].getValue().equalsIgnoreCase(label)) {
    			return true;
    		}
    	}
    	return false;
    }


    public void toggleVertexState(String vertLabel) {
        // Implement me!
    	//If vertex does not exist send warning
    	boolean isVertExist = checkNodeExist(vertLabel);
    	if (isVertExist == true) {
        	for(int i = 0; i < aLength; i++) {
            	if(array[i].getValue().equalsIgnoreCase(vertLabel)) {
            		if(array[i].sirState == SIRState.R) {
            			array[i].sirState = SIRState.R;
            		} else if (array[i].sirState == SIRState.I) {
            			array[i].sirState = SIRState.R;
            		} else {
            			array[i].sirState = SIRState.I;
            		}
            	}
            }
    		
    	} else {
    		System.err.println("One or more vertex not found in the graph");
    	}
    	
    } // end of toggleVertexState()


    public void deleteEdge(String srcLabel, String tarLabel) {
    	boolean isSrcExist = checkNodeExist(srcLabel);
    	boolean isTarExist = checkNodeExist(tarLabel);
    	boolean	deleteNeigh = true;
    	
    	if (isSrcExist == true && isTarExist == true) {
    		for (int i = 0; i < aLength; i++) {
    			for (int j = i + 1; j < aLength; j++) {
    				if (array[i].getValue().equalsIgnoreCase(srcLabel) && array[j].getValue().equalsIgnoreCase(tarLabel)) {
    					deleteNeigh = deleteNeighbours(array[i], array[j]);
    					if (deleteNeigh == false) {
							System.err.println("Edges not found!");
						}
    					break;
    				}
    			}
    		}
    		
    	} else {
    		System.err.println("Vertex not found!");
    	}
    	
    } // end of deleteEdge()

	/**
	 * Deletes the given vertex's neighbour.
	 * @param srcNode The source vertex.
	 * @param tarNode The target vertex.
	 * @return true if deleted or false if not deleted.
	 */
	private boolean deleteNeighbours(Node srcNode, Node tarNode) {
    	Node[] neighbours = srcNode.getNext();

    	for (int i = 0; i < srcNode.getNumNeighbours(); i++) {
    		if (neighbours[i] == tarNode) {
    			neighbours[i] = null;
    			srcNode.setnumNeighbours(srcNode.getNumNeighbours() - 1);
    			return true;
			}
		}
    	return false;
	}


    public void deleteVertex(String vertLabel) {
		boolean isvertExist = checkNodeExist(vertLabel);
		if (isvertExist == true) {
			for (int i = 0; i < aLength; i++) {
				if (array[i].getValue().equalsIgnoreCase(vertLabel)) {
					array[i] = null;
					shiftArray(i);
					break;
				}
			}
			
		} else {
			System.err.println("The vertex not found in the graph");
		}
    	
    } // end of deleteVertex()
    
    private void shiftArray(int position) {
    	for (int i = position; i < aLength; i++) {
    			array[i] = array[i+1];
    	}
    	array[aLength - 1] = null;
    	aLength --;
    }


    public String[] kHopNeighbours(int k, String vertLabel) {
		Node kHopNeighbour = new Node("KHopNeighbours");
    	String[] neighbourSets = null;
		boolean noVert = false;

		for (int i = 0; i < aLength; i++) {
			if (array[i].getValue().equalsIgnoreCase(vertLabel)) {
				kHopNeighbour = findKHopNeighbours(k, 1, array[i], kHopNeighbour);
				noVert = false;
				break;
			} else {
				noVert = true;
			}

		}

		if (noVert == true) {
			System.err.println("Vertex doesn't exist!");
		}

		neighbourSets = new String[kHopNeighbour.getNumNeighbours()];

		// Converting the KHopNeighbours into String of arrays.
		for (int i = 0; i < kHopNeighbour.getNumNeighbours(); i++) {
			neighbourSets[i] = kHopNeighbour.getNext()[i].getValue();
		}

        return neighbourSets;
    } // end of kHopNeighbours()

	/**
	 * Finds the given KHop away in the list and adds to the given node for printing out purposes.
	 * @param k The value of the KHop away.
	 * @param counter Counts how many KHop away has reached in the recursion.
	 * @param vertNode The vertex to find it's neighbours.
	 * @param neighboursNode The node to keep track of the neighbours for printing.
	 * @return The node (neighboursNode) with the given KHop away neighbours.
	 */
	private Node findKHopNeighbours(int k, int counter, Node vertNode, Node neighboursNode) {
		Node[] neighbours = vertNode.getNext();

		if (k == counter) {
			if (neighbours.length != 0) {
				for (int i = 0; i < vertNode.getNumNeighbours(); i++) {
					neighboursNode.setNext(neighbours[i]);
				}
			}
			return neighboursNode;
		} else {
			for (int i = 0; i < vertNode.getNumNeighbours(); i++) {
				neighboursNode = findKHopNeighbours(k, counter + 1, neighbours[i], neighboursNode);
			}
			return neighboursNode;
		}
	}


    public void printVertices(PrintWriter os) {
    	if(array != null) {
    		// recursive for loop to print all vertices (A, S) (B,R) (D, S) (E, I) (F, S) (G, S) (H, S)
    		for(int i = 0; i < aLength; i++) {
    			
    			System.out.print("(" + array[i].getValue() + ", " + array[i].getSIRState() +") ");
    		}
    	}
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
    	for (int i = 0; i < aLength; i++) {
    		if (array[i] != null) {
    			helpPrintEdges(array[i]);
    		}
    	}
        
    } // end of printEdges()
    
    private void helpPrintEdges(Node currentNode) {
    	Node[] curNodeNeighbours = currentNode.getNext();
    	 String format = "(%s %s) ";
    	if (currentNode.getNumNeighbours() != 0) {
	    	for (int i = 0; i < currentNode.getNumNeighbours(); i++) {
	    		if (curNodeNeighbours[i] != null) {
	    			System.out.printf(format, currentNode.getValue(), curNodeNeighbours[i].getValue());
	    		}
	    	}
    	}
    }
    // Implementing Linked List nodes
    protected static class Node {
    	// variables for the node
    	private String valueNode;
    	private Node[] nextNodes;
    	private Node previousNode;
    	private SIRState sirState;
    	private final int NEIGH_SIZE = 10000;
    	int numNeighbours = 0;

    	//temp constructor
    	public Node(String value) {
			this.valueNode = value;
			this.sirState = SIRState.S;
			this.nextNodes = new Node[NEIGH_SIZE];
		}
		// getters and setters for the node
    	public String getValue() {
    		return valueNode;
    	}
    	public SIRState getSIRState() {
    		return sirState;
    	}
    	public void setSIRState(SIRState newState) {
    		sirState = newState;
    	}
    	public Node[] getNext() {
    		return nextNodes;
    	}
    	public Node getPrevious() {
    		return previousNode;
    	}
    	public int getNumNeighbours() {
    		return numNeighbours;
		}
    	public void setValue(String value) {
    		valueNode = value;
    	}
    	public void setNext(Node next) {
    		nextNodes[numNeighbours] = next;
    		numNeighbours++;
    	}
    	public void setPrevious(Node previous) {
    		previousNode = previous;
    	}
    	public void setnumNeighbours(int newNumNeighbours) {
    		numNeighbours = newNumNeighbours;
    	}
    }
    
	@Override
	public Object getVertices() {
		return array;
	}
	
	@Override
	public int getSize() {
		return aLength;
	}
} // end of class AdjacencyList

