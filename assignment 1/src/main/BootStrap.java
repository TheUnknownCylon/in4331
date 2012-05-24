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
//		TPENode nodePerson = new TPENode("person", nodeRoot);
//		TPENode nodeEmail  = new TPENode("email", nodePerson);
//		TPENode nodeName   = new TPENode("name", nodePerson);
//		TPENode nodeLast   = new TPENode("last", nodeName);
//		
//		nodeRoot.addChild(nodePerson);
//		nodePerson.addChild(nodeEmail);
//		nodePerson.addChild(nodeName);
//		nodeName.addChild(nodeLast);
//		
		TPENode nodePerson1 = new TPENode("person", nodeRoot);
		TPENode nodePerson2 = new TPENode("person", nodePerson1);
		TPENode phone       = new TPENode("phone", nodePerson2);
		TPENode nodeName   = new TPENode("name", nodePerson1);
		TPENode nodeLast   = new TPENode("last", nodeName);

		nodeRoot.addChild(nodePerson1);
		nodePerson1.addChild(nodePerson2);
		nodePerson2.addChild(phone);
		nodePerson1.addChild(nodeName);
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

	}

}
