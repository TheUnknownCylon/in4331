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
		//Creating a tree matching the query
		// This code be improved much :)
		TPENode nodeRoot   = new  TPENode("root");
		TPENode nodePerson = new TPENode("person", nodeRoot);
		TPENode nodeEmail  = new TPENode("email", nodePerson);
		TPENode nodeName   = new TPENode("name", nodePerson);
		TPENode nodeLast   = new TPENode("last", nodeName);
		
		nodeRoot.addChild(nodePerson);
		nodePerson.addChild(nodeEmail);
		nodePerson.addChild(nodeName);
		nodeName.addChild(nodeLast);
		
		

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
