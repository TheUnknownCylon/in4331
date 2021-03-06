package tpe.parser.collectors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


import tpe.parser.Match;
import tpe.parser.nodes.TPENode;

public class ResultsCollectorMemory extends ResultsCollector {
	private ArrayList<Match> results = new ArrayList<Match>();
	
	/**
	 * Add a new root-match for this collector.
	 * @param m
	 */
	public void addMatch(Match m) {
		results.add(m);
	}
	
	/**
	 * Returns a list of all added matches
	 * @return
	 */
	public ArrayList<Match> getResults() {
		return results;
	}
	
	public ArrayList<HashMap<String, String>> getResultsByNodeName() {
		ArrayList<HashMap<String, String>> r = new ArrayList<HashMap<String, String>>();

		for(HashMap<TPENode, Match> m : getResultMatches()) {
			HashMap<String, String> row = new HashMap<String, String>();
			for(TPENode n : m.keySet()) {
				row.put(n.toString(), m.get(n).data());
			}
			r.add(row);
		}
		
		return r;
	}
	
	
	/**
	 * Prints simply all pre-numbers for each root match.
	 */
	public void printResultsPre() {
		for(Match m : results) {
			for(String line : m.allPrestoString("")) {
				System.out.println(line);
			}
		}
	}
	
	
	/**
	 * Print each result as a String. The XML contained within the nodes
	 * are printed. e.g. when there is a match for <a> in 
	 * <a><b><c>x</c></b></a>, then <b><c>x</b></c> is printed.
	 */
	public void printResultsStrings() {
		for(HashMap<TPENode, Match> result : getResultMatches()) {
			System.out.println(result);
		}
	}
	
	/**
	 * Print each result as a dict of nodename and pre-number.
	 */
	public void printResultsPres() {
		for(HashMap<TPENode, Match> result : getResultMatches()) {
			System.out.println(rewriteResultToPreNumbers(result));
		}
	}
	
	
	/**
	 * This method returns all result sets.
	 * For each result a Hashmap with the nodes and corresponding matches are
	 * returned.
	 * 
	 * @TODO Remove duplicates 
	 * @return
	 */
	public ArrayList<HashMap<TPENode, Match>> getResultMatches() {
		ArrayList<HashMap<TPENode, Match>> myresults = new ArrayList<HashMap<TPENode, Match>>();
		
		if(results.size() > 0) for(Match m : results) {
			myresults.addAll(matchToResults(m));
		}
		
		//remove all duplicates (let's see if this is possible)
		HashSet<HashMap<TPENode, Match>> x = new HashSet<HashMap<TPENode, Match>>(myresults);
		myresults.clear();
		myresults.addAll(x);
		
		return myresults;
	}
	

}
