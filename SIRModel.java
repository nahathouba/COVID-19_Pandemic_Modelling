import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


/**
 * SIR model.
 *
 * @author Jeffrey Chan, 2021.
 */
public class SIRModel
{
	private List<String> newInfected = new LinkedList<String>();
	private List<String> newRecovered = new LinkedList<String>();
	private Set<String> infected = new HashSet<String>();
	private Set<String> recovered = new HashSet<String>();
	private int lastInfectedNum;
	private int lastrecoveredNum;
    /**
     * Default constructor, modify as needed.
     */
    public SIRModel() {

    } // end of SIRModel()


    /**
     * Run the SIR epidemic model to completion, i.e., until no more changes to the states of the vertices for a whole iteration.
     *
     * @param graph Input contracts graph.
     * @param seedVertices Set of seed, infected vertices.
     * @param infectionProb Probability of infection.
     * @param recoverProb Probability that a vertex can become recovered.
     * @param sirModelOutWriter PrintWriter to output the necessary information per iteration (see specs for details).
     */
    public void runSimulation(ContactsGraph graph, String[] seedVertices,
        float infectionProb, float recoverProb, PrintWriter sirModelOutWriter)
    {
    	String format = "%d : [%s] : [%s] %n";
    	String newInfectedNodes;
    	String newRecoveredNodes;
    	boolean notStop = true;
    	int count = 1;
    	
    	//For evaluation that is timing checking.
    	//long startTime = System.nanoTime();
    	while(notStop) {
    		if (count == 1) {
    			toggleInfectedVertices(graph, seedVertices);
    		}
    		newInfected = updateInfected(graph, infectionProb);
    		newRecovered = updateRecovered(graph, recoverProb);
    		lastInfectedNum = infected.size();
    		addNewlyInfected(newInfected, newRecovered);
    		lastrecoveredNum = recovered.size();
    		addNewlyRecovered(newRecovered);
    		graph = updateGraph(newInfected, newRecovered, graph);
    		notStop = updateStop(newInfected, newRecovered, infected, count);
    		
    		newInfectedNodes = makeString(true);
    		newRecoveredNodes = makeString(false);
    		
    		sirModelOutWriter.format(format, count, newInfectedNodes, newRecoveredNodes);
    		count++;
    		// Clear for next iteration
    		newInfected.clear();
    		newRecovered.clear();
    	}
    	//For evaluation that is timing checking.
//    	long stopTime = System.nanoTime();
//    	
//    	double estimatedTime = ((double) stopTime - startTime) / Math.pow(10, 9);
//    	System.out.println("Time taken = " + estimatedTime + " seconds.");
    	
    } // end of runSimulation()
    
    private String makeString(boolean isInfected) {
    	StringBuilder sb = new StringBuilder();
    	
    	if (isInfected == true) {
    		for (int i = 0; i < newInfected.size(); i++) {
    			sb.append(newInfected.get(i) + " ");
    		}
    	} else {
    		for (int i = 0; i < newRecovered.size(); i++) {
    			sb.append(newRecovered.get(i) + " ");
    		}
    	}    	
    	return sb.toString();
    	
    }
    
    private void addNewlyInfected(List<String> newInfected, List<String> newRecovered) {
    	for (int i = 0; i < newInfected.size(); i++) {
    		infected.add(newInfected.get(i));
    	}
    	
    	for (int i = 0; i < newRecovered.size(); i++) {
    		infected.remove(newRecovered.get(i));
    	}
    }
    
    private void addNewlyRecovered(List<String> newRecovered) {
    	for (int i = 0; i < newRecovered.size(); i++) {
    		recovered.add(newRecovered.get(i));
    	}
    }

	/**
     * Toggles the state of the given vertices in seedVertices 
     * to become infected at the first iteration of the simulation.
     * @param graph
     * @param seedVertices
     */
    private void toggleInfectedVertices(ContactsGraph graph, String[] seedVertices) {
    	for(int i = 0; i < seedVertices.length; i++) {
    		graph.toggleVertexState(seedVertices[i]);
    	}
    }
    
    /**
     * Updates the newly infected vertices in the graph.
     * @param graph
     * @param infectProb
     * @return
     */
    @SuppressWarnings("unchecked")
	private List<String> updateInfected(ContactsGraph graph, float infectProb) {
    	List<String> newlyInfected = new LinkedList<String>();
    	Object vertices = graph.getVertices();
    	// For AdjacencyList vertices
    	AdjacencyList.Node[] nodeVertices;
    	// Neighbours of currentVertex
    	AdjacencyList.Node[] neighboursNode;
    	// For Matrix vertices
    	Map<String, AbstractGraph.Vertex> mapVertices;
    	// Only keys for map for Matrix
    	Set<String> mapSetVertices;
    	// Neighbours of currentVertex
    	AbstractGraph.Vertex[] neighboursVertex;
    	// Random generators
    	Random random;
    	
    	if (graph instanceof AdjacencyList) {
    		nodeVertices = (AdjacencyList.Node[]) vertices;
    		
    		for (int i = 0; i < graph.getSize(); i++) {
    			if(nodeVertices[i].getSIRState() == SIRState.S) {
    				neighboursNode = nodeVertices[i].getNext();
    				for (int j = 0; j < nodeVertices[i].getNumNeighbours(); j++) {
    					if (neighboursNode[j].getSIRState() == SIRState.I) {
    						random = new Random();
    						float vertexProb = random.nextFloat();
    						if (vertexProb <= infectProb) {
    							newlyInfected.add(nodeVertices[i].getValue());
    							break;
    						}
    					}
    				}
    			}
    		}
    		
    	} else if (graph instanceof AdjacencyMatrix || graph instanceof IncidenceMatrix) {
    		mapVertices = (Map<String, AbstractGraph.Vertex>) vertices;
    		mapSetVertices = mapVertices.keySet();
    		for (Iterator<String> iterator = mapSetVertices.iterator(); iterator.hasNext();) {
    			String vertex = iterator.next();
    	     	if (mapVertices.get(vertex).getSirState() == SIRState.S) {
    	     		neighboursVertex = new AbstractGraph.Vertex[mapVertices.get(vertex).getNumNeighbours()];
    	     		neighboursVertex = mapVertices.get(vertex).getVertNeighbours();
    	     		for (int j = 0; j < mapVertices.get(vertex).getNumNeighbours(); j++) {
    	     			if (neighboursVertex[j].getSirState() == SIRState.I) {
    	     				random = new Random();
          			    	float vertexProb = random.nextFloat();
          			    	if (vertexProb <= infectProb) {
          			    		//graph.toggleVertexState(mapVertices.get(vertex).getVertName());
          			    		newlyInfected.add(mapVertices.get(vertex).getVertName());
          			    		break;
          			    	}
    	     			}  
    	     		}
    	     	}
    	   }
        	
    	}
    	return newlyInfected;
    }
    
    /**
     * Updates the newly recovered vertices in the graph.
     * @param graph
     * @param recoverProb
     * @return
     */
    @SuppressWarnings("unchecked")
	private List<String> updateRecovered(ContactsGraph graph, float recoverProb) {
    	List<String> newlyRecovered = new LinkedList<String>();
    	Object vertices = graph.getVertices();
    	// For AdjacencyList vertices
    	AdjacencyList.Node[] nodeVertices;
    	// For Matrix vertices
    	Map<String, AbstractGraph.Vertex> mapVertices;
    	// Only keys for map for Matrix
    	Set<String> mapSetVertices;
    	// Random generators
    	Random random;
    	
    	if (graph instanceof AdjacencyList) {
    		nodeVertices = (AdjacencyList.Node[]) vertices;
    		for(int i = 0; i < graph.getSize(); i++) {
        		if (nodeVertices[i].getSIRState() == SIRState.I) {
        			random = new Random();
  			    	float vertexProb = random.nextFloat();
  			    	
  			    	if (vertexProb <= recoverProb) {
  			    		//graph.toggleVertexState((nodeVertices[i].getValue()));
  			    		newlyRecovered.add(nodeVertices[i].getValue());
  			    	}
        		}
    		}
    	} else if (graph instanceof AdjacencyMatrix || graph instanceof IncidenceMatrix) {
    		mapVertices = (Map<String, AbstractGraph.Vertex>) vertices;
    		mapSetVertices = mapVertices.keySet();
    		for (Iterator<String> iterator = mapSetVertices.iterator(); iterator.hasNext();) {
 	     	   String vertex = iterator.next();
 	     	   if (mapVertices.get(vertex).getSirState() == SIRState.I) {
 	     		   random = new Random();
 	     		   float vertexProb = random.nextFloat();
 	     		   if(vertexProb <= recoverProb) {
 	     			 //graph.toggleVertexState((mapVertices.get(vertex).getVertName()));
 	     			 newlyRecovered.add(mapVertices.get(vertex).getVertName());
 	     		   }
 	     	   }
    		}
    	}
    	
    	
		return newlyRecovered;
	}
    
    /**
     * Updates the graph with the newly infected and newly recovered people.
     * @param newInfected
     * @param newRecovered
     * @param graph
     */
    private ContactsGraph updateGraph(List<String> newInfected, List<String> newRecovered, ContactsGraph graph) {
    	for (int i = 0; i < newInfected.size(); i++) {
    		graph.toggleVertexState(newInfected.get(i));
    	}
    	
    	for (int i = 0; i < newRecovered.size(); i++) {
    		graph.toggleVertexState(newRecovered.get(i));
    	}
    	return graph;
    }
    
    private boolean updateStop(List<String> newInfected, List<String> newRecovered, Set<String> infected, int count) {
    	boolean updateStop = false;
    	
    	if (newInfected.size() >= 0 || newRecovered.size() >= 0) {
    		updateStop = true;
    	}
    	
    	if (infected.size() > 0) {
    		updateStop = true;
    	}
    	
    	if (lastInfectedNum != infected.size() && lastrecoveredNum != recovered.size()) {
    		updateStop = true;
    	}
    	
    	if (count == 10) {
    		updateStop = false;
    	}
    	return updateStop;
    }
} // end of class SIRModel
