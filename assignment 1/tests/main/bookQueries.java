package main;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import main.BootStrap;
import main.ResultsCollector;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

@SuppressWarnings("unused")
public class bookQueries {

	private String filename = "datasets/example-book.xml";	
	
	@Before
	public void setUp() throws Exception {
	}

	private ResultsCollector getResults(String filename, TPENode rootnode)
			throws SAXException, IOException {
		
		ResultsCollector resultscollection = new ResultsCollector();
		BootStrap.parse(filename, rootnode, resultscollection);
		return resultscollection;
	}

	
	
	@Test
	/**
	 * First test query example from the book.
	 * No optional values, no null values.
	 * @throws SAXException
	 * @throws IOException
	 */
	public void bookExample() throws SAXException, IOException {
		TPENode nodeRoot   = new TPENode("root");
		TPENode nodePerson = new TPENode("person", nodeRoot);
		TPENode nodeEmail  = new TPENode("email", nodePerson);
		TPENode nodeName   = new TPENode("name", nodePerson);
		TPENode nodeLast   = new TPENode("last", nodeName);
		
		nodeEmail.resultvalue = true;
		nodeLast.resultvalue = true;

		ResultsCollector results = getResults(filename, nodeRoot);

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
	/**
	 * Test query from the book. Email is an optional value.
	 * @throws SAXException
	 * @throws IOException
	 */
	public void bookExampleOptional() throws SAXException, IOException {
		TPENode nodeRoot   = new TPENode("root");
		TPENode nodePerson = new TPENode("person", nodeRoot);
		TPENode nodeEmail  = new TPENode("email", nodePerson);
		TPENode nodeName   = new TPENode("name", nodePerson);
		TPENode nodeLast   = new TPENode("last", nodeName);
		
		nodeEmail.resultvalue = true;
		nodeEmail.optional(true);

		nodeLast.resultvalue = true;

		ResultsCollector results = getResults(filename, nodeRoot);

		ArrayList<HashMap<TPENode, Match>> matches = results.getResultMatches();
		assertTrue(matches.size() == 4);
		
		TestResultsMap expectedresults = new TestResultsMap(4);
		expectedresults.put(0, nodeEmail, "<email>m@home</email>");
		expectedresults.put(0, nodeLast, "<last>Jones</last>");
		
		expectedresults.put(1, nodeEmail, "<email>a@home</email>");
		expectedresults.put(1, nodeLast, "<last>Hart</last>");

		expectedresults.put(2, nodeEmail, "<email>a@work</email>");
		expectedresults.put(2, nodeLast, "<last>Hart</last>");
		
		expectedresults.put(3, nodeEmail, null);
		expectedresults.put(3, nodeLast, "<last>Lang</last>");


		assertTrue(expectedresults.hasCorrectMapping(matches));
	}

	@Test
	/**
	 * Test Query from the book. person[email] , get all name/*
	 * @throws SAXException
	 * @throws IOException
	 */
	public void bookExampleStar() throws SAXException, IOException {
		TPENode nodeRoot   = new TPENode("root");
		TPENode nodePerson = new TPENode("person", nodeRoot);
		TPENode nodeEmail  = new TPENode("email", nodePerson);
		TPENode nodeName   = new TPENode("name", nodePerson);
		TPENode nodeStar   = new TPENodeStar(nodeName);

		nodeStar.resultvalue = true;

		ResultsCollector results = getResults(filename, nodeRoot);

		ArrayList<HashMap<TPENode, Match>> matches = results.getResultMatches();
		assertTrue(matches.size() == 4);
		
		TestResultsMap expectedresults = new TestResultsMap(4);
		expectedresults.put(0, nodeStar, "<first>Mary</first>");
		expectedresults.put(1, nodeStar, "<last>Jones</last>");
		expectedresults.put(2, nodeStar, "<first>Al</first>");
		expectedresults.put(3, nodeStar, "<last>Hart</last>");

		assertTrue(expectedresults.hasCorrectMapping(matches));
	}
	
	@Test
	/**
	 * First test query example from the book.
	 * No optional values, no null values.
	 * @throws SAXException
	 * @throws IOException
	 */
	public void innerStartQuery() throws SAXException, IOException {
		TPENode nodeRoot   = new TPENode("root");
		TPENode nodePerson = new TPENode("person", nodeRoot);
		TPENode nodeEmail  = new TPENode("email", nodePerson);
		TPENode nodeName   = new TPENodeStar(nodePerson);
		TPENode nodeLast   = new TPENode("last", nodeName);
		
		nodeEmail.resultvalue = true;
		nodeLast.resultvalue = true;

		ResultsCollector results = getResults(filename, nodeRoot);

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
