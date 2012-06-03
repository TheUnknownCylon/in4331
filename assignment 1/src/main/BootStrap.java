package main;

import java.io.IOException;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


public class BootStrap {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static void main(String[] args) throws SAXException, IOException {
		String f = new String("datasets/example-book-copy.xml");

		TPENode nodeRoot = new TPENode("root");
		TPENode nodePerson = new TPENode("person", nodeRoot);
		TPENode nodeLast = new TPENodeS("last", nodePerson);
				nodeLast.optional(true);
				nodeLast.resultvalue = true;
		TPENode nodeFirst = new TPENodeS("first", nodePerson);
				nodeFirst.resultvalue = true;
		new TPENode("email", nodePerson);
				
		ResultsCollector collection = new ResultsCollector();

		BootStrap.parse(f, nodeRoot, collection);
		collection.printResultsPre();		System.out.println("=============");
		collection.printResultsPres();      System.out.println("=============");
		collection.printResultsStrings();

	}
	
	public static void parse(String f, TPENode root, ResultsCollector resultscollector) throws SAXException, IOException {		
		XMLReader parser = XMLReaderFactory.createXMLReader();
		ContentHandler contentHandler = new StackEval(root, resultscollector);
		parser.setContentHandler(contentHandler);
		parser.parse(f);
	}

}
