package main;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultsCollector {
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
			
			HashMap<TPENode, Integer> r = new HashMap<TPENode, Integer>();
			for(TPENode noderesult : result.keySet()) {
				if(result.get(noderesult) != null)
					r.put(noderesult, result.get(noderesult).prenumber());
				else
					r.put(noderesult, null);

			}
			System.out.println(r);
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
			for(HashMap<TPENode, Match> line : m.mapResults(buildEmptyResultMatch())) {
				myresults.add(line);
			}
		}
		
		return myresults;
	}
	
	/**
	 * Creates a holder for all results: for each in the solution space,
	 * we create a placeholder, which can be filled in later by other queryes.
	 * 
	 * precondition: results.size > 0
	 * @return 
	 */
	private HashMap<TPENode, Match> buildEmptyResultMatch() {
		TPENode rootnode = results.get(0).tpenode;
		ArrayList<TPENode> nodes = TPENode.getDescendents(rootnode);
		
		//build the tree of nodes and set their default values with null,
		// the real values will be set later if possible.
		HashMap<TPENode, Match> resultsmap = new HashMap<TPENode, Match>();
		for(TPENode node : nodes) {
			if(node.resultvalue == true) {
				resultsmap.put(node, null);
			}
		}
		
		return resultsmap;
	}
}
