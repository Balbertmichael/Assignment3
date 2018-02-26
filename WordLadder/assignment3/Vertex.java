package assignment3;

import java.util.*;

public class Vertex {
	private String name;
	private int index;
	private ArrayList<Vertex> adjList;
	private Vertex prev;
	private int weight;

	final static int END_WEIGHT = 1000000;

	public Vertex(String name, String endWord) {
		this.name = name;
		index = 0;
		adjList = new ArrayList<Vertex>();
		prev = null;
		weight = 0;
//		if (name.equals(endWord)) {
//			weight = END_WEIGHT;
//		}
		if(name.length() == endWord.length()) {
			for(int i = 0; i < name.length(); ++i) {
				++weight;
			}
		}
	}

	public String checkAdjacency(Vertex word) {
		if(name.length() != word.getName().length()) {
			return null;
		}
		for (int i = 0; i < name.length(); ++i) {
			if (i > 0) {
				if (name.substring(0, i).compareTo(word.getName().substring(0, i)) == 0) {
					if (name.substring(i + 1).compareTo(word.getName().substring(i + 1)) == 0) {
						return name.substring(0, i) + '_' + name.substring(i + 1);
					}
				}
			} else {
				if (name.substring(i + 1).compareTo(word.getName().substring(i + 1)) == 0) {
					return '_' + name.substring(i);
				}
			}
		}
		return null;
	}

	public void addEdge(Vertex word) {
		//weight = word.setWeight(weight);
		adjList.add(word);
		word.getAdjList().add(this);
	}

	public Vertex getNextFromAdjList() {
		if (index == adjList.size()) {
			return null;
		}
		int retInd = index;
		index++;
		Vertex retVert = adjList.get(retInd);
		return retVert;
	}
	
	public void resetIndex() {
		index = 0;
	}

	public ArrayList<Vertex> getAdjList() {
		return adjList;
	}

	public String getName() {
		return name;
	}

	public int getIndex() {
		return index;
	}

	public void setPrev(Vertex v) {
		prev = v;
	}

	public Vertex getPrev() {
		return prev;
	}

	public int getWeight() {
		return weight;
	}

	public int setWeight(int oWeight) {
		if (weight > 0) {
			if (oWeight < (weight - 1)) {
				return weight - 1;
			} else if((oWeight - 1) > weight){
				weight = oWeight - 1;
				return oWeight;
			}
		}
		return oWeight;
	}
}
