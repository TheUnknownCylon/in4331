package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class TestResultsMap {
	private Vector<HashMap<TPENode, String>> excpetedvalues;
	
	public TestResultsMap(int count) {
		excpetedvalues = new Vector<HashMap<TPENode, String>>(count);
		for(int i = 0; i < count; i++) {
			excpetedvalues.add(new HashMap<TPENode, String>());
		}
	}
	
	public void put(int index,  TPENode key, String value) {
		excpetedvalues.get(index).put(key, value);
	}
	
	
	/**
	 * A little dirty method which will check if the expected values correspond
	 * with the method parameter matches
	 * @param matches
	 * @return
	 */
	public boolean hasCorrectMapping(ArrayList<HashMap<TPENode, Match>> matches) {
		boolean[] matched = new boolean[excpetedvalues.size()]; 
		
		//for each match, try to find a correct match
		for(HashMap<TPENode, Match> aMatch : matches) {
			
			for(int i = 0; i < excpetedvalues.size(); i++) {
				boolean allmatched = true;
				HashMap<TPENode, String> nodesToMatch = excpetedvalues.get(i);
				
				for(TPENode nodeToMatch : nodesToMatch.keySet()) {
					String valueToMatch = nodesToMatch.get(nodeToMatch);
					
					try {
						if(!aMatch.get(nodeToMatch).toString().equals(valueToMatch)) {
							allmatched = false;
						}
					} catch (NullPointerException e) {
						if(!(aMatch.get(nodeToMatch) == null && valueToMatch == null)) {
							allmatched = false;
						}
					}
					
					
				}
				
				if(allmatched == true) {
					if(matched[i] == false) {
						matched[i] = true;
					} else {
						//TODO Maybe raise a useful exception
						return false;
					}
				}
				
			}
			
		}
		
		//assert that all matched are set to true :)
		for(int i = 0; i < matched.length; i++) {
			if(!matched[i]) return false;
		}
		
		return true;
	}
}
