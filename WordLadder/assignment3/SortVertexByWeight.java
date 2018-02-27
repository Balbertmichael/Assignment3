package assignment3;

import java.util.Comparator;
/**
 * This function implements the comparator to compare the weight between two vertices for the adjacency list
 * @author Albert Bautista
 * @version 1.0
 */
public class SortVertexByWeight implements Comparator<Vertex>{
	public int compare(Vertex a, Vertex b) {
		return b.getWeight() - a.getWeight();
	}
}
