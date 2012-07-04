package tpe.parser.collectors;

import java.util.ArrayList;
import java.util.HashMap;

import tpe.parser.Match;
import tpe.parser.nodes.TPENode;

/**
 * Enables printing of queries in a completely free XML format of choice.
 * It could be used to format the output of a generated XQuery return statement.
 * 
 * TODO, add support to cast data, e.g. dateformat.
 */
public class ResultsXMLFormat {
	private ArrayList<String> xmlTexts= new ArrayList<String>();
	private ArrayList<TPENode> xmlNodes = new ArrayList<TPENode>();
	private ArrayList<TPENode> xmlNodesData = new ArrayList<TPENode>();
	
	/**
	 * Add some text to the query.
	 * @param v
	 */
	public void append(String v) {
		xmlTexts.add(v);
		xmlNodes.add(null);
		xmlNodesData.add(null);
	}
	
	/**
	 * Add a node. Open and close-tags are appended to the output as well.
	 * @param n
	 */
	public void append(TPENode n) {
		xmlTexts.add(null);
		xmlNodes.add(n);
		xmlNodesData.add(null);
	}
	
	/**
	 * Append the content of a node here, without its open and ending tags.
	 * @param n
	 */
	public void appendContentOf(TPENode n) {
		xmlTexts.add(null);
		xmlNodes.add(null);
		xmlNodesData.add(n);		
	}
	
	
	/**
	 * Based on the input XML Structure, and a query result row, build the
	 * output string.
	 * @param r
	 * @return
	 */
	public String buildString(HashMap<TPENode, Match> r) {
		StringBuilder str = new StringBuilder();
		
		//assert that xmlTexts has the same size as XMLNodes
		if(xmlTexts.size() != xmlNodes.size())
			throw new RuntimeException("Bug in ResultsMLFormat: Lists of unequal length!");
		
		
		for(int i = 0; i < xmlTexts.size(); i++) {
			if(xmlTexts.get(i) != null) {
				str.append(xmlTexts.get(i));
				
			} else if(xmlNodes.get(i) != null) {
				try {
					str.append(r.get(xmlNodes.get(i)).toString());
				} catch (Exception e) {/* no value for the given node, NULL is assumed */}
				
			} else {
				try {
					str.append(r.get(xmlNodesData.get(i)).data());
				} catch (Exception e) {/* no value for the given node, NULL is assumed */}
			}
		}
		
		
		return str.toString();
	}
}
