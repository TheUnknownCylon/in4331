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
		TPEStack nodeRoot   = new  TPEStack("root");
		TPEStack nodePerson = new TPEStack("person", nodeRoot);
		TPEStack nodeEmail  = new TPEStack("email", nodePerson);
		TPEStack nodeName   = new TPEStack("name", nodePerson);
		TPEStack nodeLast   = new TPEStack("last", nodeName);
		
		nodeRoot.addChild(nodePerson);
		nodePerson.addChild(nodeEmail);
		nodePerson.addChild(nodeName);
		nodeName.addChild(nodeLast);
		
		SAXParserFactory factory = SAXParserFactory.newInstance();

		try {
			SAXParser saxParser = factory.newSAXParser();
	        saxParser.parse( new File("datasets/example1.xml"), new StackEval(nodeRoot) );

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			
		} catch (SAXException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("--- --- --- ---");
		
		//The stacks now should only contain the Match objects that participate
		// to complete the query.
		for(Match m : nodePerson.matches) {
			for(String line : m.sringyfyResults(nodePerson, "")) {
				System.out.println(line);
			}
		}
		


	}

}
