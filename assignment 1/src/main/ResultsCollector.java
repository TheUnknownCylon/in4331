package main;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultsCollector {
	private ArrayList<Match> results = new ArrayList<Match>();
	
	public void addMatch(Match m) {
		results.add(m);
	}
	
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
	 * We are going to retrieve all results. Parsing only the matches
	 * will not give the perfect results, when null is required.
	 * 
	 * Solution: First collect all TPENodes we are expecting a result for.
	 *  then simply find the corresponding matches. The TPENodes without a match
	 *  will have a null value.
	 */
	public void printResults() {

		if(results.size() == 0)
			return;
		
		TPENode rootnode = results.get(0).tpenode;
		ArrayList<TPENode> nodes = TPENode.getDescendents(rootnode);

		for(Match m : results) {
			//TODO: MAatch
			HashMap<TPENode, Integer> resultsmap = new HashMap<TPENode, Integer>();
			for(TPENode node : nodes) {
				if(node.resultvalue) {
					resultsmap.put(node, null);
				}
			}
			
			for(HashMap<TPENode, Integer> line : m.mapResults(resultsmap)) {
				System.out.println(line);
			}
		}
	}
}
