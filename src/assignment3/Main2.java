package assignment3;

import java.util.*;

import java.io.*;

public class Main2 {

	private static class wordLadder {
		@SuppressWarnings("unused")
		String start;
		String end;
		Set<String> dict;
		int upperBound;
		Vertex startVertex;
		Vertex endVertex;
		int iterations;

		public wordLadder(String start, String end, int upperBound, Vertex startVertex, Vertex endVertex) {
			this.start = start;
			this.end = end;
			this.dict = new HashSet<String>();
			this.upperBound = upperBound;
			this.startVertex = startVertex;
			this.endVertex = endVertex;
			iterations = 0;
		}
	}

	// static variables and constants only here.
	final static int setUpperBound = 50;
	final static Random rand = new Random();

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
		ArrayList<String> words = new ArrayList<String>();
		words = parse(kb);
		if (!words.isEmpty()) {
			String start = words.get(0).toUpperCase(), end = words.get(1).toUpperCase();
			ArrayList<String> dfsLadder = testgetWordLadderDFS(start, end), bfsLadder = getWordLadderBFS(start, end);
			printLadder(dfsLadder);
			printLadder(bfsLadder);
		}
	}

	public static ArrayList<String> testgetWordLadderDFS(String start, String end) {
		// Returned list should be ordered start to end. Include start and end.
		// If ladder is empty, return list with just start and end.
		ArrayList<String> bfsfind, ladder;
		int numTest = 0;
		do {
			ladder = null;
			Set<String> dict = makeDictionary();
			++numTest;

			int index = rand.nextInt(dict.size()), index2 = rand.nextInt(dict.size());
			Iterator<String> iter = dict.iterator();
			for (int i = 0; i < index; ++i) {
				iter.next();
			}
			start = iter.next();
			Iterator<String> iter2 = dict.iterator();
			for (int i = 0; i < index2; ++i) {
				iter2.next();
			}
			end = iter2.next();

			start = start.toUpperCase();
			end = end.toUpperCase();

			ArrayList<Vertex> v = wordGraph(dict, start, end);
			bfsfind = getWordLadderBFS(start, end);
			printLadder(bfsfind);
			int testBound = bfsfind.size();
			System.out.println("Length of BFS: " + testBound);
			wordLadder word;
			for (int bounds = 25; bounds <= 150; bounds += 5) {
				if (ladder != null) {
					break;
				}
				System.out.println("Trying smaller bound: " + bounds);
				word = new wordLadder(start, end, bounds, v.get(0), v.get(1));
				ladder = dfs(word, word.startVertex, 0);
			}
			if(ladder == null) {
				word = new wordLadder(start, end, setUpperBound, v.get(0), v.get(1));
				ladder = dfsWithIterLimit(word, word.startVertex, 0);
			}
			if (ladder == null) {
				ladder = new ArrayList<String>();
				ladder.add(start);
				ladder.add(end);
			}
			printLadder(ladder);
			System.out.println("Length of DFS: " + ladder.size());
			HashSet<String> chkDupes = new HashSet<String>();
			for (String s : ladder) {
				if (!chkDupes.add(s)) {
					System.out.println("Duplicate: " + s);
				}
			}
		} while ((bfsfind.size() > 2 && ladder.size() > 2) || (bfsfind.size() == 2 && ladder.size() == 2));
		System.out.println(numTest - 1 + " passed tests");
		return ladder;
	}

	private static ArrayList<String> dfsWithIterLimit(wordLadder ladder, Vertex v, int depth) {
		if (ladder.iterations == Integer.MAX_VALUE) {
			return null;
		} else {
			ladder.iterations++;
		}
		if (v != null) {
			if (depth >= ladder.upperBound) {
				return null;
			}
			if (v.getAdjList().contains(ladder.endVertex)) {
				ArrayList<String> found = new ArrayList<String>();
				found.add(v.getName());
				found.add(ladder.end);
				return found;
			}
			if (!ladder.dict.contains(v.getName())) {
				ArrayList<Vertex> currList = v.getAdjList();
				for (int i = 0; i < currList.size(); ++i) {
					ladder.dict.add(v.getName());
					ArrayList<String> ret = dfsWithIterLimit(ladder, currList.get(i), depth + 1);
					ladder.dict.remove(v.getName());
					if (ret == null) {
						continue;
					} else {
						ret.add(0, v.getName());
						return ret;
					}
				}
			}
		}
		return null;
	}

	private static ArrayList<String> dfs(wordLadder ladder, Vertex v, int depth) {
		if (v != null) {

			if (v.getAdjList().contains(ladder.endVertex)) {
				ArrayList<String> found = new ArrayList<String>();
				found.add(v.getName());
				found.add(ladder.end);
				return found;
			}

			if (depth >= ladder.upperBound) {
				return null;
			}

			if (!ladder.dict.contains(v.getName())) {
				ArrayList<Vertex> currList = v.getAdjList();
				for (int i = 0; i < currList.size(); ++i) {

					ladder.dict.add(v.getName());
					ArrayList<String> ret = dfs(ladder, currList.get(i), depth + 1);
					//ladder.dict.remove(v.getName());

					if (ret == null) {
						continue;
					} else {
						ret.add(0, v.getName());
						return ret;
					}

				}
			}

		}

		return null;

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

	public static ArrayList<String> getWordLadderBFS(String start, String end) {
		Set<String> dict = makeDictionary();
		start = start.toUpperCase();
		end = end.toUpperCase();
		ArrayList<String> ladder = new ArrayList<String>();
		Vertex startVertex = wordGraph(dict, start, end).get(0);
		Queue<Vertex> find = new LinkedList<Vertex>();
		find.add(startVertex); // Adding to initialize queue to search for
		while (!find.isEmpty()) {
			Vertex node = find.remove();
			if (node == null) {
				continue;
			}
			
			// Using the dictionary remove boolean return to serve dual purposes
			// First remove the node if it exists in dictionary to not encounter again
			// Second to skip the encounter beyond the first one
			if (!dict.remove(node.getName())) {
				continue;
			}
			Vertex neighbor = node.getNextFromAdjList();
			while (neighbor != null) {
				if (dict.contains(neighbor.getName())) {
					find.add(neighbor);
					neighbor.setPrev(node);
					if(neighbor.getName().equals(end)) {
						ladder = bfsBacktrack(neighbor, ladder);
						return ladder;
					}
				}
				neighbor = node.getNextFromAdjList(); // If at the bounds of adjList neighbor = null
			}
		}
		ladder.add(start); // Default if wordLadder can't be found
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
			System.out.println("no word ladder can be found between " + ladder.get(0).toLowerCase() + " and "
					+ ladder.get(1).toLowerCase());
		} else {
			System.out.println("a " + (ladder.size() - 2) + "-rung ladder exists between " + ladder.get(0).toLowerCase()
					+ " and " + ladder.get(ladder.size() - 1).toLowerCase() + ".");
			for (int i = 0; i < ladder.size(); ++i) {
				System.out.println(ladder.get(i));
			}
		}
	}

	// TODO
	// Other private static methods here

	// /**
	// * Function to call to test ArrayList<String>
	// *
	// * @param ret
	// * The ArrayList to be changed to a constant
	// */
	// private static void stringTester(ArrayList<String> ret) {
	// for (String s : test) {
	// ret.add(s);
	// }
	// }

	private static ArrayList<Vertex> wordGraph(Set<String> dict, String start, String end) {
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
		ArrayList<Vertex> retVertex = new ArrayList<Vertex>();
		retVertex.add(graph.get(start));
		retVertex.add(graph.get(end));
		
		Vertex endVertex = retVertex.get(1);
		smartWeight(endVertex);
		
		for (Vertex v : graph.values()) {
			v.getAdjList().sort(new VertexWeightSort());
		}

		return retVertex;
	}

	private static ArrayList<String> bfsBacktrack(Vertex endVertex, ArrayList<String> retArray) {
		Vertex v = endVertex;
		while (v != null) {
			retArray.add(0, v.getName());
			v = v.getPrev();
		}
		return retArray;
	}
	
	private static void smartWeight(Vertex end) {
		if(end == null) {
			return;
		}
		Queue<Vertex> parse = new LinkedList<Vertex>();
		Set<String> dict = new HashSet<String>();
		end.setLowerWeight(Integer.MAX_VALUE);
		parse.add(end);
		while(!parse.isEmpty()) {
			Vertex parent = parse.remove();
			if(parent.getName() == null) {
				continue;
			}
			if(!dict.add(parent.getName())) {
				continue;
			}
			for(Vertex child : parent.getAdjList()) {
				if(!dict.contains(child.getName())) {
					parse.add(child);
					child.setLowerWeight(parent.getWeight());
				}
			}
		}
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
