package tpe.parser.collectors;

import java.util.HashMap;

import tpe.parser.Match;
import tpe.parser.nodes.TPENode;

public class ResultsCollectorPrinterFormat extends ResultsCollector {

	private ResultsXMLFormat format;
	
	public ResultsCollectorPrinterFormat(ResultsXMLFormat format) {
		this.format = format;
	}

	@Override
	public void addMatch(Match m) {
		for(HashMap<TPENode, Match> result : matchToResults(m)) {
			System.out.println(format.buildString(result));
		}
	}



}
