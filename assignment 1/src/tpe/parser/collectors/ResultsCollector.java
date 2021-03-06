package tpe.parser.collectors;

import java.util.ArrayList;
import java.util.HashMap;

import tpe.parser.Match;
import tpe.parser.nodes.TPENode;

public abstract class ResultsCollector {
	
	public abstract void addMatch(Match m);
	
	/**
	 * Creates a holder for all results: for each in the solution space,
	 * we create a placeholder, which can be filled in later by other queryes.
	 * 
	 * precondition: results.size > 0
	 * @return 
	 */
	protected static HashMap<TPENode, Match> buildEmptyResultMatch(TPENode rootnode) {
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
	
	
	protected static ArrayList<HashMap<TPENode, Match>> matchToResults(Match m) {
		ArrayList<HashMap<TPENode, Match>> myresults = new ArrayList<HashMap<TPENode, Match>>();

		for(HashMap<TPENode, Match> line : m.mapResults(buildEmptyResultMatch(m.tpenode))) {
			myresults.add(line);
		}
		return myresults;
	}

	
	protected static HashMap<TPENode, Integer> rewriteResultToPreNumbers(HashMap<TPENode, Match> result) {
		HashMap<TPENode, Integer> r = new HashMap<TPENode, Integer>();
		for(TPENode noderesult : result.keySet()) {
			if(result.get(noderesult) != null)
				r.put(noderesult, result.get(noderesult).prenumber());
			else
				r.put(noderesult, null);
		}
		return r;
	}
	
	/**
	 * Rewrite results to a simple XML String
	 * @param result
	 * @return
	 */
	protected static String rewriteResultToXMLString(HashMap<TPENode, Match> result) {
		StringBuffer r = new StringBuffer();
		for(TPENode noderesult : result.keySet()) {
			Match rmatch = result.get(noderesult);
			
			//do not print missing optional elements
			if(rmatch!=null) r.append(rmatch.toString());
			
		}
		return r.toString();
	}
	
}
