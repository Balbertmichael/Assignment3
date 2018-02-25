package assignment3;

import java.util.*;

public class Vertex {
	private String name;
	private int index;
	private ArrayList<Vertex> adjList;

	public Vertex(String name) {
		this.name = name;
		index = 0;
		adjList = new ArrayList<Vertex>();
	}

	public String checkAdjacency(Vertex word) {
		for (int i = 0; i < name.length(); ++i) {
			if (i > 0) {
				if (name.substring(0, i).compareTo(word.getName().substring(0, i)) == 0) {
					if (name.substring(i + 1).compareTo(word.getName().substring(i + 1)) == 0) {
						return name.substring(0, i) + '_' + name.substring(i + 1);
					}
				}
			} 
			else {
				if (name.substring(i + 1) == word.getName().substring(i + 1)) {
					return '_' + name.substring(i);
				}
			}
		}
		return null;
	}

	public void addEdge(Vertex word, int priority) {
		if(priority > 0) {
			adjList.add(0, word);
			word.getAdjList().add(0, this);
		}
		else {
			adjList.add(word);
			word.getAdjList().add(this);
		}
	}
	
	public Vertex getNextFromAdjList() {
		if(index == adjList.size()) {
			return null;
		}
		int retInd = index++;
		return adjList.get(retInd);
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
}
