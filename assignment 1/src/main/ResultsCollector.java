package main;

import java.util.ArrayList;

public class ResultsCollector {
	private ArrayList<Match> results = new ArrayList<Match>();
	
	public void addMatch(Match m) {
		results.add(m);
	}
	
	public ArrayList<Match> getResults() {
		return results;
	}
	
	public void printResults() {
		for(Match m : results) {
			for(String line : m.sringyfyResults("")) {
				System.out.println(line);
			}
		}
	}
}
