package main;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.xml.sax.SAXException;

import predicates.StringCompare;

import resultcollectors.ResultsCollectorMemory;
import tpenodes.TPENode;
import tpenodes.TPENodeAttribute;

public class QueriesWithAttributes extends TestFunctionality {

	@Test
	/**
	 * Get all persons with a lang-attribute set.
	 */
	public void attributeNormal() throws SAXException, IOException {
		TPENode nodeRoot      = new TPENode("people");
		TPENode nodePerson    = new TPENode("person", nodeRoot);
		TPENode nodePersonAtt = new TPENodeAttribute("lang", nodePerson);
		TPENode nodeName      = new TPENode("name", nodePerson);
		TPENode nodeLast      = new TPENode("last", nodeName);
		
		nodePersonAtt.resultvalue = true;
		nodeLast.resultvalue = true;
		
		ResultsCollectorMemory results = getResults("datasets/example-book-with-attributes.xml", nodeRoot);
		
		ArrayList<HashMap<TPENode, Match>> matches = results.getResultMatches();
		assertTrue(matches.size() == 2);
		
		TestResultsMap expectedresults = new TestResultsMap(2);
		expectedresults.put(0, nodeLast, "<last>Jones</last>");
		expectedresults.put(0, nodePersonAtt, "en");

		expectedresults.put(1, nodeLast, "<last>Lang</last>");
		expectedresults.put(1, nodePersonAtt, "nl");

		assertTrue(expectedresults.hasCorrectMapping(matches));
	}
	
	@Test
	/**
	 * Get all persons with a lang-attribute set to nl.
	 */
	public void attributePredicate() throws SAXException, IOException {
		TPENode nodeRoot      = new TPENode("people");
		TPENode nodePerson    = new TPENode("person", nodeRoot);
		TPENode nodePersonAtt = new TPENodeAttribute("lang", nodePerson);
		TPENode nodeName      = new TPENode("name", nodePerson);
		TPENode nodeLast      = new TPENode("last", nodeName);
		
		nodePersonAtt.addPredicate(new StringCompare("nl"));
		nodePersonAtt.resultvalue = true;
		nodeLast.resultvalue = true;
		
		ResultsCollectorMemory results = getResults("datasets/example-book-with-attributes.xml", nodeRoot);
		
		ArrayList<HashMap<TPENode, Match>> matches = results.getResultMatches();
		assertTrue(matches.size() == 1);
		
		TestResultsMap expectedresults = new TestResultsMap(1);
		expectedresults.put(0, nodeLast, "<last>Lang</last>");
		expectedresults.put(0, nodePersonAtt, "nl");

		assertTrue(expectedresults.hasCorrectMapping(matches));
	}
	
	@Test
	/**
	 * Get all persons with an optional lang-attribute set.
	 */
	public void attributeOptional() throws SAXException, IOException {
		TPENode nodeRoot      = new TPENode("people");
		TPENode nodePerson    = new TPENode("person", nodeRoot);
		TPENode nodePersonAtt = new TPENodeAttribute("lang", nodePerson);
		TPENode nodeName      = new TPENode("name", nodePerson);
		TPENode nodeLast      = new TPENode("last", nodeName);
		
		nodePersonAtt.resultvalue = true;
		nodePersonAtt.optional(true);
		nodeLast.resultvalue = true;
		
		ResultsCollectorMemory results = getResults("datasets/example-book-with-attributes.xml", nodeRoot);
		
		ArrayList<HashMap<TPENode, Match>> matches = results.getResultMatches();
		assertTrue(matches.size() == 3);
		
		TestResultsMap expectedresults = new TestResultsMap(3);
		expectedresults.put(0, nodeLast, "<last>Jones</last>");
		expectedresults.put(0, nodePersonAtt, "en");

		expectedresults.put(1, nodeLast, "<last>Lang</last>");
		expectedresults.put(1, nodePersonAtt, "nl");

		expectedresults.put(2, nodeLast, "<last>Hart</last>");
		expectedresults.put(2, nodePersonAtt, null);

		assertTrue(expectedresults.hasCorrectMapping(matches));
	}
	
}
