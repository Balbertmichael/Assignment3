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
	
	 // static variables and constants only here.
	static String[] test = {"smart", "start", "stars", "soars", "soaks", "socks", "cocks", "conks", "cones", "coney", "money"};
	
	public static void main(String[] args) throws Exception {
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out;			// default output to Stdout
		}
		initialize();
		ArrayList<String> words = new ArrayList<String>();
		do {
			words = parse(kb);
			if(!words.isEmpty()) {
				String start = words.get(0), end = words.get(1);
				ArrayList<String> dfsLadder = getWordLadderDFS(start, end),
						bfsLadder = getWordLadderBFS(start, end);
				printLadder(dfsLadder);
				printLadder(bfsLadder);
			}
		}while(!words.isEmpty());
		// TODO methods to read in words, output ladder
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		ArrayList<String> ret = new ArrayList<String>();
		String read = keyboard.next();
		if(!read.contentEquals("/quit")) {
			ret.add(read);
			ret.add(keyboard.next());
		}
		return ret;
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		// Returned list should be ordered start to end.  Include start and end.
		// If ladder is empty, return list with just start and end.
		// TODO some code
		Set<String> dict = makeDictionary();
		ArrayList<String> ret = new ArrayList<String>();
		// TODO more code
		return ret; // replace this line later with real return
	}
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		
		// TODO some code
		Set<String> dict = makeDictionary();
		// TODO more code
		ArrayList<String> ret = new ArrayList<String>();
		return ret; // replace this line later with real return
	}
    
	/**
	 * Prints the specified output with the ladder given
	 * @param ladder The ladder to be printed out to console
	 */
	public static void printLadder(ArrayList<String> ladder) {
		System.out.println("a " + (ladder.size() - 2) + "-rung ladder exists between " + 
				ladder.get(0) + " and " + ladder.get(ladder.size() - 1) + ".");
		for(int i = 0; i < ladder.size(); ++i) {
			System.out.println(ladder.get(i));
		}
	}
	
	// TODO
	// Other private static methods here
	/**
	 * Function to call to test ArrayList<String>
	 * @param ret The ArrayList to be changed to a constant
	 */
	private static void stringTester(ArrayList<String> ret) {
		for(String s : test) {
			ret.add(s);
		}
	}

	/* Do not modify makeDictionary */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("C:\\Users\\balbe\\OneDrive\\Current_Semester\\EE_422C\\Assignment3\\WordLadder\\five_letter_words.txt"));
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
