package assignment3;

import java.util.Comparator;
/**
 * This class implements the comparator to compare the weight between two vertices for the adjacency list for the ArrayList.sort method
 * @author Albert Bautista
 * @version 1.0
 */
public class VertexWeightSort implements Comparator<Vertex>{
	public int compare(Vertex a, Vertex b) {
		return b.getWeight() - a.getWeight();
	}
}
