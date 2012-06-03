package resultcollectors;


import java.util.HashMap;

import tpenodes.TPENode;
import main.Match;

public class ResultsCollectorPrinter extends ResultsCollector {

	public void printOpen() {
		System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\"");
		System.out.println("<results>");
	}
	
	public void printClose() {
		System.out.println("</results>");
	}
	
	@Override
	public void addMatch(Match m) {
		for(HashMap<TPENode, Match> result : matchToResults(m)) {
			System.out.println(rewriteResultToXMLString(result).replaceAll(">\\s+<", "><"));
		}
	}

}
