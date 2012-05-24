package main;

import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParserFactory; 
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

public class BootStrap {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File f = new File("datasets/example-book.xml");

		TPENode nodeRoot   = new  TPENode("root");
		TPENode nodePerson = new TPENode("person", nodeRoot);
		TPENode nodeEmail  = new TPENode("email", nodePerson);
			    nodeEmail.optional(true);
			    
		//TPENode nodeName   = new TPENodeStar(nodePerson, "* 1");
		TPENode nodeName   = new TPENode("name", nodePerson);
		TPENode nodeLast   = //new TPENodeStar(nodeName, "* 1");
							 new TPENode("last", nodeName);
//		 - 1 - 2 - 3 - 5
//		 - 10 - 11 - 13 - 15
//		 - 10 - 12 - 13 - 15

		
		nodeRoot.addChild(nodePerson);
		nodePerson.addChild(nodeEmail);
		nodePerson.addChild(nodeName);
		nodeName.addChild(nodeLast);
		
		ResultsCollector collection = new ResultsCollector();

		BootStrap.parse(f, nodeRoot, collection);
		collection.printResults();

		

	}
	
	public static void parse(File f, TPENode root, ResultsCollector rcol) {
		SAXParserFactory factory = SAXParserFactory.newInstance();

		try {
			SAXParser saxParser = factory.newSAXParser();
			ResultsCollector results = new ResultsCollector();
			
	        saxParser.parse( f, new StackEval(root, rcol) );
	        results.printResults();

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			
		} catch (SAXException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
