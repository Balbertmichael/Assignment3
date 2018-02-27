package assignment3;
/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * Albert Bautista
 * abb2639
 * 15505
 * Slip days used: <0>
 * Git URL:
 * Spring 2018
 */

import java.util.*;

import java.io.*;
/**
 * This class runs the wordLadder for BFS and DFS
 * @author Albert Bautista
 * @version 1.0
 */
public class Main {
	/**
	 * This private class was made to store whatever the ladders needed
	 * for passing to recursive functions
	 * @author Albert Bautista
	 * 
	 */
	private static class wordLadder {
		String end;
		Set<String> dict;
		int upperBound;
		int iterations;
		/**
		 * It takes in all the values needed to pass into the recursive wordladder
		 * and sets up control flow like previous word list and cap on iterations for faster time
		 * @param end Necessary to check if the vertex matches the goal word
		 * @param upperBound Sets a limit to the depth to avoid stack overflow
		 */
		public wordLadder(String end, int upperBound, int iterations) {
			this.end = end;
			this.dict = new HashSet<String>();
			this.upperBound = upperBound;
			this.iterations = iterations;
		}
	}

	// static variables and constants only here.
	final static int SET_HIGHEST_BOUND = 150; //Constant to set initial bound
	final static Random rand = new Random();
	final static int NO_ITERATIONS = -1;

	public static void main(String[] args) throws Exception {
		Scanner kb; // input Scanner for commands
		PrintStream ps; // output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps); // redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out; // default output to Stdout
		}
		initialize();
		ArrayList<String> words = new ArrayList<String>();
		do {
			words = parse(kb);
			if (!words.isEmpty()) {
				String start = words.get(0).toUpperCase(), end = words.get(1).toUpperCase();
				ArrayList<String> dfsLadder = getWordLadderDFS(start, end), bfsLadder = getWordLadderBFS(start, end);
				printLadder(dfsLadder);
			    printLadder(bfsLadder);
			}
		} while (!words.isEmpty());
	}

	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests. So call it
		// only once at the start of main.
	}

	/**
	 * @param keyboard
	 *            Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word. If command
	 *         is /quit, return empty ArrayList.
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		ArrayList<String> ret = new ArrayList<String>();
		String read = keyboard.next();
		if (!read.contentEquals("/quit")) {
			ret.add(read);
			ret.add(keyboard.next());
		}
		return ret;
	}
	/**
	 * This methods implements depth first search for the word ladder
	 * @param start Start of the word ladder
	 * @param end End or goal of the word ladder
	 * @return Either the word ladder if it exists or an empty 2 rung ladder with just the start and end
	 */
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		// Returned list should be ordered start to end. Include start and end.
		// If ladder is empty, return list with just start and end.
		ArrayList<String> ladder; //Placed for the return ladder b/c running multiple dfs using different bounds to check
		Set<String> dict = makeDictionary();
		
		start = start.toUpperCase(); //Uppercase both to match dict since all words in dict are upper case
		end = end.toUpperCase();
		
		Vertex startVertex = wordGraph(dict, start, end); //Takes just the startVertex from the wordGraph to get rid of not needed nodes in graph
		
		wordLadder word = new wordLadder(end, SET_HIGHEST_BOUND, 0);
		//Setting max depth with iterations to bound runtime
		//Setting a higher upper bound and iterating on lower bounds if 150 depth is taking too long
		ladder = dfs(word, startVertex, 0);
		int [] bounds = {25, 50, 75}; //Starting really low and going up to have less effect on runtime
		for(int i = 0; i < bounds.length; ++i) {
			if(ladder != null) {
				break; //Basically tries to keep pushing for the presence of a word ladder 
				//That's why I left the bounds pretty small
			}
			System.out.println("Trying different bound: " + bounds[i]); //Error checking output
			word = new wordLadder(end, bounds[i], NO_ITERATIONS); //Also with pruning with depth the ladder gets very inconsistent but runtime is short
			ladder = dfs(word, startVertex, 0);
		}
		
		if (ladder == null) {
			ladder = new ArrayList<String>(); //Handles the empty ladder case because dfs returns null for empty ladders
			ladder.add(start);
			ladder.add(end);
		}

		return ladder;
	}
	/**
	 * This method implements breadth first search to find connecting wordLadders
	 * Though doesn't optimize for best word ladder just first word ladder found
	 * So tradeoff for shortest word ladder with the fastest word ladder with bfs
	 * @param start Start of the word ladder
	 * @param end End of the word ladder
	 * @return ArrayList with the word ladder empty if wordLadder cannot be found
	 */
	public static ArrayList<String> getWordLadderBFS(String start, String end) {
		Set<String> dict = makeDictionary();
		start = start.toUpperCase();
		end = end.toUpperCase();
		ArrayList<String> ladder = new ArrayList<String>();
		Vertex startVertex = wordGraph(dict, start, end);
		Queue<Vertex> find = new LinkedList<Vertex>();
		find.add(startVertex); //Adding to initialize queue to search for
		while (!find.isEmpty()) {
			Vertex node = find.remove();
			if(node == null) {
				continue;
			}
			if(node.getName().equals(end)) {
				ladder = bfsBacktrack(node, ladder); //Returns first ladder found
				return ladder;
			}
			//Using the dictionary remove boolean return to serve dual purposes
			//First remove the node if it exists in dictionary to not encounter again
			//Second to skip the encounter beyond the first one
			if (!dict.remove(node.getName())) {
				continue; 
			}
			Vertex neighbor = node.getNextFromAdjList(); 
			while (neighbor != null) {
				if(dict.contains(neighbor.getName())) {
					find.add(neighbor);
					neighbor.setPrev(node);
				}
				neighbor = node.getNextFromAdjList(); //If at the bounds of adjList neighber = null
			}
		}
		ladder.add(start); //Default if wordLadder can't be found
		ladder.add(end);
		return ladder;
	}

	/**
	 * Prints the specified output with the ladder given
	 * 
	 * @param ladder
	 *            The ladder to be printed out to console
	 */
	public static void printLadder(ArrayList<String> ladder) {
		if (ladder.size() == 2) {
			System.out.println("no word ladder can be found between " + ladder.get(0).toLowerCase() + " and " + ladder.get(1).toLowerCase());
		} else {
			System.out.println("a " + (ladder.size() - 2) + "-rung ladder exists between " + ladder.get(0).toLowerCase() + " and "
					+ ladder.get(ladder.size() - 1).toLowerCase() + ".");
			for (int i = 0; i < ladder.size(); ++i) {
				System.out.println(ladder.get(i));
			}
		}
	}

	// Other private static methods here
	/**
	 * This private method builds a graph by implementing an adjacency list due to sparse connections
	 * @param dict The set of words to pull from
	 * @param start The start word to send back the startVertex from the map
	 * @param end The end word to calculate the weight of the vertex by comparing the vertex name with the endWord
	 * @return The startVertex to traverse the graph with
	 */
	private static Vertex wordGraph(Set<String> dict, String start, String end) {
		Hashtable<String, Vertex> graph = new Hashtable<String, Vertex>();
		for (String s : dict) {
			Vertex addVertex = new Vertex(s, end);
			for (Vertex v : graph.values()) {
				String edge = addVertex.checkAdjacency(v);
				if (edge != null) {
					addVertex.addEdge(v);
				}
			}
			graph.put(s, addVertex);
		}
		for(Vertex v : graph.values()) {
				v.getAdjList().sort(new SortVertexByWeight());
		}
		return graph.get(start);
	}
	/**
	 * This private method is the helper recursive function that getWordLadderBFS calls
	 * @param ladder Object that holds static variables needed to traverse the graph
	 * @param v Vertex with the start word to traverse the graph
	 * @param depth Basically a check to not get too deep into the graph that would lead to stack overflow
	 * @return ArrayList with either null if nothing is found or the wordLadder connecting words start and end 
	 */
	private static ArrayList<String> dfs(wordLadder ladder, Vertex v, int depth) {
		if(ladder.iterations >= 0) {
			if(++ladder.iterations == Integer.MAX_VALUE) {
				return null; //Sets an iteration to reduce seemingly never-ending runtime issue
			}
		}
		
		if (v != null) {
			
			if (v.getName().equals(ladder.end)) {
				ArrayList<String> found = new ArrayList<String>();
				found.add(v.getName());
				return found;
			}
			
			if(depth >= ladder.upperBound) {
				return null; //Implemented to stop function from running forever due to breadth of graph
			}
			
			if (!ladder.dict.contains(v.getName())) {
				ArrayList<Vertex> currList = v.getAdjList();
				for(int i = 0; i < currList.size(); ++i) {
					
					ladder.dict.add(v.getName()); //Bars from repeating words but has a weird pruning effect because of having a depth bound
					ArrayList<String> ret = dfs(ladder, currList.get(i), depth + 1);
					
					if(ladder.iterations >= 0) {
						//This removes the strange pruning if the word is found at a lower depth but stops due to depth bounds
						//Implemented only at highest bound
						ladder.dict.remove(v.getName()); 
					}
					
					if(ret != null) {
						ret.add(0, v.getName());
						return ret;
					}
					
				}
			}
			
		}
		
		return null; //Default case to return for any event that doesn't meet find the wordLadder
		
	}
	
	private static ArrayList<String> bfsBacktrack(Vertex endVertex, ArrayList<String> retArray){
		Vertex v = endVertex;
		while(v != null) {
			retArray.add(0,v.getName());
			v = v.getPrev();
		}
		return retArray;
	}
	
	/* Do not modify makeDictionary */
	public static Set<String> makeDictionary() {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner(new File(
					"C:\\Users\\balbe\\OneDrive\\Current_Semester\\EE_422C\\Assignment3\\WordLadder\\five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());

		}
		return words;
	}
}
