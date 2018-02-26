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

public class Main {
	private static class wordLadder {
		String start;
		String end;
		Set<String> dict;
		int upperBound;
		Vertex startVertex;
		Vertex endVertex;

		public wordLadder(String start, String end, int upperBound, Vertex startVertex, Vertex endVertex) {
			this.start = start;
			this.end = end;
			this.dict = new HashSet<String>();
			this.upperBound = upperBound;
			this.startVertex = startVertex;
			this.endVertex = endVertex;
		}
	}

	// static variables and constants only here.
	static String[] test = { "smart", "start", "stars", "soars", "soaks", "socks", "cocks", "conks", "cones", "coney",
			"money" };
	final static int setUpperBound = 75;
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
		initialize();
		ArrayList<String> words = new ArrayList<String>();
		do {
			words = parse(kb);
			if (!words.isEmpty()) {
				String start = words.get(0).toUpperCase(), end = words.get(1).toUpperCase();
				ArrayList<String> dfsLadder = getWordLadderDFS(start, end), bfsLadder = getWordLadderBFS(start, end);
				printLadder(dfsLadder);
			    printLadder(bfsLadder);
			    //break;
			}
		} while (!words.isEmpty());
		// TODO methods to read in words, output ladder
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

	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		// Returned list should be ordered start to end. Include start and end.
		// If ladder is empty, return list with just start and end.
		ArrayList<String> bfsfind, ladder;
		int numTest = 0;
		do {
		Set<String> dict = makeDictionary();
		++numTest;
		
		int index = rand.nextInt(dict.size()), index2 = rand.nextInt(dict.size());
		Iterator<String> iter = dict.iterator();
		for(int i = 0; i < index; ++i) {
			iter.next();
		}
		start = iter.next();
		Iterator<String> iter2 = dict.iterator();
		for(int i = 0; i < index2; ++i) {
			iter2.next();
		}
		end  = iter2.next();
		
		start = start.toUpperCase();
		end = end.toUpperCase();
		
		ArrayList<Vertex> v = wordGraph(dict, start, end);
		bfsfind = getWordLadderBFS(start, end);
		printLadder(bfsfind);
		int testBound = bfsfind.size();
		System.out.println(testBound);
		wordLadder word = new wordLadder(start, end, setUpperBound, v.get(0), v.get(1));
		ladder = dfs(word, word.startVertex, 0);
		if (ladder == null) {
			ladder = new ArrayList<String>();
			ladder.add(start);
			ladder.add(end);
		}
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

	public static ArrayList<String> getWordLadderBFS(String start, String end) {
		Set<String> dict = makeDictionary();
		
//		int index = rand.nextInt(dict.size()), index2 = rand.nextInt(dict.size());
//		Iterator<String> iter = dict.iterator();
//		for(int i = 0; i < index; ++i) {
//			iter.next();
//		}
//		start = iter.next();
//		Iterator<String> iter2 = dict.iterator();
//		for(int i = 0; i < index2; ++i) {
//			iter2.next();
//		}
//		end  = iter2.next();
		
		start = start.toUpperCase();
		end = end.toUpperCase();
		ArrayList<String> ladder = new ArrayList<String>();
		ArrayList<Vertex> v = wordGraph(dict, start, end);
		Queue<Vertex> find = new LinkedList<Vertex>();
		find.add(v.get(0));
		while (!find.isEmpty()) {
			Vertex node = find.remove();
			if(node == null) {
				continue;
			}
			if(node.getName().equals(end)) {
				ladder = bfsBacktrack(node, ladder);
//				HashSet<String> chkDupes = new HashSet<String>();
//				for (String s : ladder) {
//					if (!chkDupes.add(s)) {
//						System.out.println("Duplicate: " + s);
//					}
//				}
				return ladder;
			}
			if (!dict.remove(node.getName())) {
				continue;
			}
			Vertex neighbor = node.getNextFromAdjList();
			while (neighbor != null) {
				if(dict.contains(neighbor.getName())) {
					find.add(neighbor);
					neighbor.setPrev(node);
				}
				neighbor = node.getNextFromAdjList();
			}
		}
		ladder.add(start);
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

	// TODO
	// Other private static methods here

//	/**
//	 * Function to call to test ArrayList<String>
//	 * 
//	 * @param ret
//	 *            The ArrayList to be changed to a constant
//	 */
//	private static void stringTester(ArrayList<String> ret) {
//		for (String s : test) {
//			ret.add(s);
//		}
//	}

	private static ArrayList<Vertex> wordGraph(Set<String> dict, String start, String end) {
		Hashtable<String, Vertex> graph = new Hashtable<String, Vertex>();
		Vertex endVertex = new Vertex(end, end);
		graph.put(end, endVertex);
		for (String s : dict) {
			if(s.equals(end)) {
				continue;
			}
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
		ArrayList<Vertex> v = new ArrayList<Vertex>();
		v.add(graph.get(start));
		v.add(graph.get(end));
		return v;
	}

	private static ArrayList<String> dfs(wordLadder ladder, Vertex v, int depth) {
		if (v != null) {
			if(depth >= ladder.upperBound) {
				return null;
			}
			if (v.getAdjList().contains(ladder.endVertex)) {
				ArrayList<String> found = new ArrayList<String>();
				found.add(v.getName());
				found.add(ladder.end);
				return found;
			}
			if (!ladder.dict.contains(v.getName())) {
				ladder.dict.add(v.getName());
				ArrayList<String> findEnd = dfs(ladder, v.getNextFromAdjList(), depth + 1);

				ladder.dict.remove(v.getName());
				
				if(v.getName().equals("QUITS")) {
				//	System.out.println("Test");
				};
				if (findEnd == null) {
					if (v.getIndex() < v.getAdjList().size()) {
						findEnd = dfs(ladder, v, depth);
						return findEnd;
					}
					else {
						if(v.getName().equals(ladder.start)) {
							return null;
						}
						ladder.dict.add(v.getName());
						v.resetIndex();
					}
				} else {
					findEnd.add(0, v.getName());
					return findEnd;
				}
			}
		}
		return null;
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
