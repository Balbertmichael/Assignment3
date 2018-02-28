package assignment3;

import java.util.*;

/**
 * This class represents the vertices for each word with an adjacency list to
 * other words
 * 
 * @author Albert Bautista
 * @version 1.0
 */
public class Vertex {
	private String name; // Word name to check for
	private int index; // Index used to move through adjList while bounded
	private ArrayList<Vertex> adjList; // Adjacency list to represent graph
	private Vertex prev; // prev vertex used for BFS implementation
	private int weight; // Weight for sorting words closer to the solution

	/**
	 * This class constructor takes the word and calculates its weight from the
	 * endWord
	 * 
	 * @param name
	 *            Word of the vertex
	 * @param endWord
	 *            Word used to compare and calculate the weight for the instantiated Vertex
	 */
	public Vertex(String name, String endWord) {
		this.name = name;
		index = 0;
		adjList = new ArrayList<Vertex>();
		prev = null;
		weight = 0;
		if (name.length() == endWord.length()) {
			for (int i = 0; i < name.length(); ++i) {
				if (endWord.charAt(i) == name.charAt(i)) {
					++weight;
				}
			}
		}
	}

	/**
	 * This class compares and returns the string (Inefficient because of earlier
	 * implementation but it works)
	 * 
	 * @param word
	 *            Vertex to see if they are adjacent
	 * @return Returns a null if they aren't adjacent else returns the letters that
	 *         are the same with an underscore at the different word
	 */
	public String checkAdjacency(Vertex word) {
		if (name.length() != word.getName().length()) {
			return null; // To check if the two can be compared
		}
		for (int i = 0; (i < name.length()) && (name.length() == word.getName().length()); ++i) {
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

	/**
	 * Adds the edge between the two Vertices
	 * 
	 * @param word
	 */
	public void addEdge(Vertex word) {
		// weight = word.setWeight(weight);
		adjList.add(word);
		word.getAdjList().add(this);
	}

	/**
	 * Bounded index that takes from adjacency list
	 * 
	 * @return Vertex[index] from adjacency list
	 */
	public Vertex getNextFromAdjList() {
		if (index == adjList.size()) {
			return null;
		}
		int retInd = index;
		index++;
		Vertex retVert = adjList.get(retInd);
		return retVert;
	}

	/**
	 * Getter to iterate through adjacency List
	 * 
	 * @return adjList
	 */
	public ArrayList<Vertex> getAdjList() {
		return adjList;
	}

	/**
	 * Getter to compare word for wordladder
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter used for BFS method
	 * 
	 * @param v
	 *            vertex to set prev to
	 */
	public void setPrev(Vertex v) {
		if(prev == null) {
			prev = v;
		}
	}

	/**
	 * Getter for prev for BFS method
	 * 
	 * @return Vertex that is the BFS parent node
	 */
	public Vertex getPrev() {
		return prev;
	}

	/**
	 * Getter for SortVertexByWeight
	 * 
	 * @return weight
	 */
	public int getWeight() {
		return weight;
	}
	
	public void setLowerWeight(int oWeight) {
		if(oWeight - 1 > weight) {
			weight = oWeight - 1;
		}
	}
}
