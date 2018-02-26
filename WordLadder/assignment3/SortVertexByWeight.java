package assignment3;

import java.util.Comparator;

public class SortVertexByWeight implements Comparator<Vertex>{
	public int compare(Vertex a, Vertex b) {
		return b.getWeight() - a.getWeight();
	}
}
