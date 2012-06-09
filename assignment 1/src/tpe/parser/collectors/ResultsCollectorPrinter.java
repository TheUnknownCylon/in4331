package tpe.parser.collectors;


import java.util.HashMap;

import tpe.parser.Match;
import tpe.parser.nodes.TPENode;

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
