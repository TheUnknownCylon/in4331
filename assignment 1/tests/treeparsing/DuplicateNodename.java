package treeparsing;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;

import main.BootStrap;
import main.Match;
import main.ResultsCollector;
import main.StackEval;
import main.TPENode;

import org.junit.Before;
import org.junit.Test;

public class DuplicateNodename {

	
	private ResultsCollector collection;
	private ArrayList<String> expectedresults;

	
	@Before
	public void setUp() throws Exception {
		collection = new ResultsCollector();
		expectedresults = new ArrayList<String>();		
	}

	
	/**
	 * Note: expected results is altered by this operation!
	 * This is no problem for this test suite.
	 */
	private void checkResults() {
		System.out.println("---");
		collection.printResults();

		for(Match m : collection.getResults()) {
			ArrayList<String> idstrings = m.sringyfyResults("");
			for(String idstring : idstrings) {
				assertTrue(expectedresults.contains(idstring));
				expectedresults.remove(idstring);
			}
		}
		assertTrue(expectedresults.size()==0);
	}

	
	
	@Test
	public void testDuplicateKeyname() {
		File f = new File("datasets/example1.xml");
		expectedresults.add(" - 1 - 4 - 5 - 2 - 3");
		
		TPENode nodeRoot   = new  TPENode("root");
		TPENode nodePerson1 = new TPENode("person", nodeRoot, "rootperson");
		TPENode nodePerson2 = new TPENode("person", nodePerson1, "phoneperson");
		TPENode phone       = new TPENode("phone", nodePerson2);
		TPENode nodeName   = new TPENode("name", nodePerson1);
		TPENode nodeLast   = new TPENode("last", nodeName);

		nodeRoot.addChild(nodePerson1);
		nodePerson1.addChild(nodePerson2);
		nodePerson2.addChild(phone);
		nodePerson1.addChild(nodeName);
		nodeName.addChild(nodeLast);

		
		BootStrap.parse(f, nodeRoot, collection);
		checkResults();
	}
	
	@Test
	public void testBookQuery1() {
		File f = new File("datasets/example-book.xml");
		expectedresults.add(" - 1 - 2 - 3 - 5");
		expectedresults.add(" - 10 - 11 - 13 - 15");
		expectedresults.add(" - 10 - 12 - 13 - 15");
		
		TPENode nodeRoot   = new  TPENode("root");
		TPENode nodePerson = new TPENode("person", nodeRoot);
		TPENode nodeEmail  = new TPENode("email", nodePerson);
		TPENode nodeName   = new TPENode("name", nodePerson);
		TPENode nodeLast   = new TPENode("last", nodeName);
		
		nodeRoot.addChild(nodePerson);
		nodePerson.addChild(nodeEmail);
		nodePerson.addChild(nodeName);
		nodeName.addChild(nodeLast);
		
		BootStrap.parse(f, nodeRoot, collection);
		checkResults();
	}
	
	@Test
	public void testBookQuery1a() {
		File f = new File("datasets/example-book-namenotnested.xml");

		TPENode nodeRoot   = new  TPENode("root");
		TPENode nodePerson = new TPENode("person", nodeRoot);
		TPENode nodeEmail  = new TPENode("email", nodePerson);
		TPENode nodeName   = new TPENode("name", nodePerson);
		TPENode nodeLast   = new TPENode("last", nodeName);
		
		nodeRoot.addChild(nodePerson);
		nodePerson.addChild(nodeEmail);
		nodePerson.addChild(nodeName);
		nodeName.addChild(nodeLast);
		
		BootStrap.parse(f, nodeRoot, collection);
		checkResults();
	}
		
}
