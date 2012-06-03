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

import predicates.StringCompare;

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
	 * Same test as previous, only now we set a predicate on the email value.
	 * @throws SAXException
	 * @throws IOException
	 */
	public void bookExamplePredicate() throws SAXException, IOException {
		TPENode nodeRoot   = new TPENode("root");
		TPENode nodePerson = new TPENode("person", nodeRoot);
		TPENode nodeEmail  = new TPENode("email", nodePerson);
		TPENode nodeName   = new TPENode("name", nodePerson);
		TPENode nodeLast   = new TPENode("last", nodeName);
		
		nodeEmail.resultvalue = true;
		nodeEmail.addPredicate(new StringCompare("m@home"));
		nodeLast.resultvalue = true;

		ResultsCollector results = getResults(filename, nodeRoot);

		ArrayList<HashMap<TPENode, Match>> matches = results.getResultMatches();
		assertTrue(matches.size() == 1);
		
		TestResultsMap expectedresults = new TestResultsMap(1);
		expectedresults.put(0, nodeEmail, "<email>m@home</email>");
		expectedresults.put(0, nodeLast, "<last>Jones</last>");

		assertTrue(expectedresults.hasCorrectMapping(matches));
	}
	
	@Test
	/**
	 * Same test as previous, only now we set a predicate on the last name value.
	 * Multiple email address are in this sub node, check if we get them all.
	 * @throws SAXException
	 * @throws IOException
	 */
	public void bookExamplePredicate2() throws SAXException, IOException {
		TPENode nodeRoot   = new TPENode("root");
		TPENode nodePerson = new TPENode("person", nodeRoot);
		TPENode nodeEmail  = new TPENode("email", nodePerson);
		TPENode nodeName   = new TPENode("name", nodePerson);
		TPENode nodeLast   = new TPENode("last", nodeName);
		
		nodeEmail.resultvalue = true;
		nodeLast.resultvalue = true;
		nodeLast.addPredicate(new StringCompare("Hart"));

		ResultsCollector results = getResults(filename, nodeRoot);

		ArrayList<HashMap<TPENode, Match>> matches = results.getResultMatches();
		assertTrue(matches.size() == 2);
		
		TestResultsMap expectedresults = new TestResultsMap(2);
		expectedresults.put(0, nodeEmail, "<email>a@home</email>");
		expectedresults.put(0, nodeLast, "<last>Hart</last>");

		expectedresults.put(1, nodeEmail, "<email>a@work</email>");
		expectedresults.put(1, nodeLast, "<last>Hart</last>");

		assertTrue(expectedresults.hasCorrectMapping(matches));
	}
	
	
	@Test
	/**
	 * First test query example from the book.
	 * No optional values, no null values.
	 * 
	 * Using an example file where the name is not encapsulated in 
	 *   <name>  </name>
	 * Therefore no results are expected to be returned.
	 * 
	 * @throws SAXException
	 * @throws IOException
	 */
	public void bookExampleNameNotNested() throws SAXException, IOException {
		TPENode nodeRoot   = new TPENode("root");
		TPENode nodePerson = new TPENode("person", nodeRoot);
		TPENode nodeEmail  = new TPENode("email", nodePerson);
		TPENode nodeName   = new TPENode("name", nodePerson);
		TPENode nodeLast   = new TPENode("last", nodeName);
		
		nodeEmail.resultvalue = true;
		nodeLast.resultvalue = true;

		ResultsCollector results = getResults(filename = "datasets/example-book-namenotnested.xml", nodeRoot);

		ArrayList<HashMap<TPENode, Match>> matches = results.getResultMatches();
		assertTrue(matches.size() == 0);
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

	
	@Test
	public void matchSameAsStar() throws SAXException, IOException {
		TPENode nodeRoot   = new TPENode("root");
		TPENode nodePerson = new TPENode("person", nodeRoot);
		TPENode nodeEmail  = new TPENode("email", nodePerson);
		TPENode nodeStar   = new TPENodeStar(nodePerson);
		
		nodeEmail.resultvalue = true;

		ResultsCollector results = getResults(filename, nodeRoot);
		
		ArrayList<HashMap<TPENode, Match>> matches = results.getResultMatches();
		assertTrue(matches.size() == 3);
		
		TestResultsMap expectedresults = new TestResultsMap(3);
		expectedresults.put(0, nodeEmail, "<email>m@home</email>");
		expectedresults.put(1, nodeEmail, "<email>a@home</email>");
		expectedresults.put(2, nodeEmail, "<email>a@work</email>");

		assertTrue(expectedresults.hasCorrectMapping(matches));
	}
	
	
	
	@Test
	public void x1() throws SAXException, IOException {
		TPENode nodeRoot   = new  TPENode("root");

		TPENode nodePerson1 = new TPENode("person", nodeRoot);
		TPENode nodePerson2 = new TPENode("person", nodePerson1);
		TPENode phone       = new TPENode("phone", nodePerson2);
		TPENode nodeName   = new TPENode("name", nodePerson1);
		TPENode nodeLast   = new TPENode("last", nodeName);

		phone.resultvalue = true;
		nodeLast.resultvalue = true;

		ResultsCollector results = getResults("datasets/example-nestedperson.xml", nodeRoot);
		
		ArrayList<HashMap<TPENode, Match>> matches = results.getResultMatches();
		assertTrue(matches.size() == 1);
		
		TestResultsMap expectedresults = new TestResultsMap(1);
		expectedresults.put(0, nodeLast, "<last>a</last>");
		expectedresults.put(0, phone, "<phone>112</phone>");

		assertTrue(expectedresults.hasCorrectMapping(matches));
	}
	
	@Test
	/**
	 * Test Query from book.
	 * 
	 * @throws SAXException
	 * @throws IOException
	 */
	public void x2() throws SAXException, IOException {
		TPENode nodeRoot = new TPENode("root");
		TPENode nodePerson = new TPENode("person", nodeRoot);
		TPENode nodeLast = new TPENodeS("last", nodePerson);
				nodeLast.optional(true);
				nodeLast.resultvalue = true;
		TPENode nodeFirst = new TPENodeS("first", nodePerson);
				nodeFirst.resultvalue = true;
		TPENode nodeEmail = new TPENode("email", nodePerson);
				nodeEmail.addPredicate(new StringCompare("a@home"));

		ResultsCollector results = getResults("datasets/example-book.xml", nodeRoot);
		
		ArrayList<HashMap<TPENode, Match>> matches = results.getResultMatches();

		assertTrue(matches.size() == 1);
		
		TestResultsMap expectedresults = new TestResultsMap(1);
		expectedresults.put(0, nodeFirst, "<first>Al</first>");
		expectedresults.put(0, nodeLast, "<last>Hart</last>");

		assertTrue(expectedresults.hasCorrectMapping(matches));
	}
		
	
	
}
