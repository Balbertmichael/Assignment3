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
	private static class wordLadder{
		String start;
		String end;
		Set<String> dict;
		public wordLadder(String start, String end, Set<String> dict) {
			this.start = start;
			this.end = end;
			this.dict = dict;
		}
	}
	// static variables and constants only here.
	static String[] test = { "smart", "start", "stars", "soars", "soaks", "socks", "cocks", "conks", "cones", "coney",
			"money" };
	final static ArrayList<String> notDone = new ArrayList<String>();

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
		// TODO some code
		Set<String> dict = makeDictionary();
		ArrayList<String> ret = new ArrayList<String>();
		Vertex startVertex = wordGraph(dict, start, end);
		// TODO more code
		wordLadder word = new wordLadder(start, end, dict);
		ret = dfs(start, end, dict, startVertex);
		return ret; // replace this line later with real return
	}

	public static ArrayList<String> getWordLadderBFS(String start, String end) {

		// TODO some code
		Set<String> dict = makeDictionary();
		// TODO more code
		ArrayList<String> ret = new ArrayList<String>();
		Vertex startVertex = wordGraph(dict, start, end);
		return ret; // replace this line later with real return
	}

	/**
	 * Prints the specified output with the ladder given
	 * 
	 * @param ladder
	 *            The ladder to be printed out to console
	 */
	public static void printLadder(ArrayList<String> ladder) {
		System.out.println("a " + (ladder.size() - 2) + "-rung ladder exists between " + ladder.get(0) + " and "
				+ ladder.get(ladder.size() - 1) + ".");
		for (int i = 0; i < ladder.size(); ++i) {
			System.out.println(ladder.get(i));
		}
	}

	// TODO
	// Other private static methods here

	/**
	 * Function to call to test ArrayList<String>
	 * 
	 * @param ret
	 *            The ArrayList to be changed to a constant
	 */
	private static void stringTester(ArrayList<String> ret) {
		for (String s : test) {
			ret.add(s);
		}
	}

	private static Vertex wordGraph(Set<String> dict, String start, String end) {
		Hashtable<String, Vertex> graph = new Hashtable<String, Vertex>();
		for (String s : dict) {
			Vertex addVertex = new Vertex(s);
			for (Vertex v : graph.values()) {
				String edge = addVertex.checkAdjacency(v);
				if (edge != null) {
					int eMatch = numMatch(edge, end);
					addVertex.addEdge(v, eMatch);
				}
			}
			graph.put(s, addVertex);
		}
		Vertex test = graph.get("JAMBE");
		return graph.get(start);
	}

	private static int numMatch(String vertWord, String searchWord) {
		int lowerBound = 0, match = 0;
		if (vertWord.length() > searchWord.length()) {
			lowerBound = searchWord.length();
		} else {
			lowerBound = vertWord.length();
		}
		for (int i = 0; i < lowerBound; ++i) {
			if (vertWord.charAt(i) == searchWord.charAt(i)) {
				match++;
			}
		}
		return match;
	}

	private static ArrayList<String> dfs(String start, String end, Set<String> dict, Vertex v) {
		if (v == null) {
			return null;
		}
		if (dict.contains(v.getName())) {
			dict.remove(v.getName());
			if (v.getName().compareTo(end) == 0) {
				ArrayList<String> found = new ArrayList<String>();
				found.add(v.getName());
				return found;
			}
			Vertex next = v.getNextFromAdjList();
			if(next == null) {
				return null;
			}
			ArrayList<String> findEnd = dfs(start, end, dict, next);
			if (findEnd == null) {
				if(v.getIndex() < v.getAdjList().size()) {
					dict.add(v.getName());
					return dfs(start, end, dict, v);
				}
				else {
					return null;
				}
			}
			else {
				findEnd.add(0, v.getName());
				return findEnd;
			}
		}
		else {
			return null;
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
