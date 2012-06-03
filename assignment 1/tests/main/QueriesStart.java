package main;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.xml.sax.SAXException;

import tpenodes.TPENode;
import tpenodes.TPENodeS;

/**
 * There are different ways to start a query.
 * For example a matching node from root:
 * 1. /people/person/
 * 2. //person
 * 3. /person    <-- no results since the document starts with /people/
 * @author remco
 *
 */
public class QueriesStart extends TestFunctionality {

	@Test
	public void normalStart() throws SAXException, IOException {
		TPENode nodeRoot   = new TPENode("people");
		TPENode nodePerson = new TPENode("person", nodeRoot);
		TPENode nodeEmail  = new TPENode("email", nodePerson);
		TPENode nodeName   = new TPENode("name", nodePerson);
		TPENode nodeLast   = new TPENode("last", nodeName);
		
		nodeEmail.resultvalue = true;
		nodeLast.resultvalue = true;
		
		ResultsCollector results = getResults("datasets/example-book.xml", nodeRoot);
		
		
		ArrayList<HashMap<TPENode, Match>> matches = results.getResultMatches();
		assertTrue(matches.size() == 3);
		
		TestResultsMap expectedresults = new TestResultsMap(3);
		expectedresults.put(0, nodeEmail, "<email>m@home</email>");
		expectedresults.put(0, nodeLast, "<last>Jones</last>");
		
		expectedresults.put(1, nodeEmail, "<email>a@home</email>");
		expectedresults.put(1, nodeLast, "<last>Hart</last>");

		expectedresults.put(2, nodeEmail, "<email>a@work</email>");
		expectedresults.put(2, nodeLast, "<last>Hart</last>");
		
		assertTrue(expectedresults.hasCorrectMapping(matches));
	}
		
	@Test
	public void startWithoutCorrectRoot() throws SAXException, IOException {
		TPENode nodePerson = new TPENode("person");
		TPENode nodeEmail  = new TPENode("email", nodePerson);
		TPENode nodeName   = new TPENode("name", nodePerson);
		TPENode nodeLast   = new TPENode("last", nodeName);
		
		nodeEmail.resultvalue = true;
		nodeLast.resultvalue = true;
		
		ResultsCollector results = getResults("datasets/example-book.xml", nodePerson);
			
		ArrayList<HashMap<TPENode, Match>> matches = results.getResultMatches();
		assertTrue(matches.size() == 0);

	}
	
	
	@Test
	public void startWithoutRootButWithSlashSlash() throws SAXException, IOException {
		TPENode nodePerson = new TPENodeS("person");
		TPENode nodeEmail  = new TPENode("email", nodePerson);
		TPENode nodeName   = new TPENode("name", nodePerson);
		TPENode nodeLast   = new TPENode("last", nodeName);
		
		nodeEmail.resultvalue = true;
		nodeLast.resultvalue = true;
		
		ResultsCollector results = getResults("datasets/example-book.xml", nodePerson);
		
		
		ArrayList<HashMap<TPENode, Match>> matches = results.getResultMatches();
		assertTrue(matches.size() == 3);
		
		TestResultsMap expectedresults = new TestResultsMap(3);
		expectedresults.put(0, nodeEmail, "<email>m@home</email>");
		expectedresults.put(0, nodeLast, "<last>Jones</last>");
		
		expectedresults.put(1, nodeEmail, "<email>a@home</email>");
		expectedresults.put(1, nodeLast, "<last>Hart</last>");

		expectedresults.put(2, nodeEmail, "<email>a@work</email>");
		expectedresults.put(2, nodeLast, "<last>Hart</last>");
		
		assertTrue(expectedresults.hasCorrectMapping(matches));
	}	
	
}
