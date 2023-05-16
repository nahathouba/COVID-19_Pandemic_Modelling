import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Abstract class to allow you to implement common functionality or definitions.
 * All three graph implement classes extend this.
 *
 * Note, you should make sure to test after changes.  Note it is optional to
 * use this file.
 *
 * @author Jeffrey Chan, 2021.
 */
public abstract class AbstractGraph implements ContactsGraph
{
	protected Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();
	
	public void toggleVertexState(String vertLabel) {
		Vertex ver;
	    if (vertexMap.containsKey(vertLabel)) {
	    	ver = vertexMap.get(vertLabel);
	        if (ver.getSirState() == SIRState.R) {
	        	ver.setSirState(SIRState.R);
	        } else if (ver.getSirState() == SIRState.I) {
	            ver.setSirState(SIRState.R);
	        } else {
	            ver.setSirState(SIRState.I);
	        }
	    } else {
	    	System.err.println("Vertex doesn't exist, when toggling the vertex!");
	    }
	    	
	} // end of toggleVertexState()
	
	public String[] kHopNeighbours(int k, String vertLabel) {
    	// Vertex for printing 
    	Vertex countNeighbours = new Vertex(-1, -1);
    	String[] neighbourSet = null;
    	int counter = 1;
        
    	if (vertexMap.containsKey(vertLabel) == true) {
    		countNeighbours = findKHop(k, vertexMap.get(vertLabel), countNeighbours, counter);
    		neighbourSet = new String[countNeighbours.getNumNeighbours()];
    		for (int i = 0; i < countNeighbours.getNumNeighbours(); i++) {
    			neighbourSet[i] = countNeighbours.getVertNeighbours()[i].getVertName();
    		}
        } else { 
        	System.err.println("Vertex not found, while counting neighbours!"); 
        }
    	
        return neighbourSet;
    } // end of kHopNeighbours()
    
    private Vertex findKHop(int k, Vertex vertLabel, Vertex countNeighbours, int counter) {
    	Vertex[] neighbours = vertLabel.getVertNeighbours();
    	
    	if (k == counter) {
    		if (neighbours != null) {
	    		for (int i = 0; i < vertLabel.getNumNeighbours(); i++) {
	    			countNeighbours.setNeighbour(neighbours[i]);
	    		}
    		}
    		return countNeighbours;
    	} else {
    		for (int i = 0; i < vertLabel.getNumNeighbours(); i++) {
				countNeighbours = findKHop(k, neighbours[i], countNeighbours, counter + 1);
			}
    		return countNeighbours;
    	}
    }
    
    protected void deleteEdgeArray(Vertex srcVertex, Vertex tarLabel) {
    	Vertex[] neighbours = srcVertex.getVertNeighbours();
    	
    	for (int i = 0; i < srcVertex.getNumNeighbours(); i++) {
    		if (neighbours[i] == tarLabel) {
    			neighbours[i] = null;
    			break;
    		}
    	}
    }
    
    public void printVertices(PrintWriter os) {
    	Set<String> vertices = vertexMap.keySet();
        String format = "(%s %s) ";
        
    	for (Iterator<String> iterator = vertices.iterator(); iterator.hasNext();) {
     	   String vertex = iterator.next();
     	   System.out.printf(format, vertex, vertexMap.get(vertex).getSirState());
        }
    } // end of printVertices()
    
    public void printEdges(PrintWriter os) {
    	Set<String> vertices = vertexMap.keySet();
        
    	for (Iterator<String> iterator = vertices.iterator(); iterator.hasNext();) {
     	   String vertex = iterator.next();
     	   helpPrintEdges(vertexMap.get(vertex));
        }
        
    } // end of printEdges()
    
    private void helpPrintEdges(Vertex currentVertex) {
    	Vertex[] curVertNeighbours = currentVertex.getVertNeighbours();
    	 String format = "(%s %s) ";
    	if (currentVertex.getNumNeighbours() != 0) {
	    	for (int i = 0; i < currentVertex.getNumNeighbours(); i++) {
	    		if (curVertNeighbours[i] != null) {
	    			System.out.printf(format, currentVertex.getVertName(), curVertNeighbours[i].getVertName());
	    		}
	    	}
    	}
    }
	 
	// Using for Matrix
	protected class Vertex {
        private Vertex[] neighbours;
        private String vertName;
    	private int row;
        private int col;
        private int numNeighbours = 0;
        private final int NEIGH_SIZE = 10000;
        private SIRState sirState;

        public Vertex(int row, int col) {
            this.row = row;
            this.col = col;
            this.neighbours = new Vertex[NEIGH_SIZE];
            this.sirState = SIRState.S;
        }

        public int getRow() { return row; }

        public int getCol() { return col; }
        
        public Vertex[] getVertNeighbours() { return neighbours; }
        
        public String getVertName() {
        	return vertName;
        }
        
        public int getNumNeighbours() {
        	return numNeighbours;
        }

        public SIRState getSirState() { return sirState; }

        public void setSirState(SIRState sirState) { this.sirState = sirState; }
        
        public void setVertName(String vertName) {
        	this.vertName = vertName;
        }
        
        public void setNeighbour(Vertex newNeighbour) { 
        	this.neighbours[numNeighbours] = newNeighbour; 
        	numNeighbours++;
        }
    }

} // end of abstract class AbstractGraph
