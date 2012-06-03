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
		String f = new String("datasets/example-book.xml");

		TPENode nodeRoot   = new TPENode("root");
		TPENode nodePerson = new TPENode("person", nodeRoot);
		TPENode nodeEmail  = new TPENode("email", nodePerson);
		TPENode nodeFirst   = new TPENode("first", nodePerson);
		TPENode nodeLast   = new TPENode("last", nodePerson);
		
		nodeEmail.resultvalue = true;
		nodeLast.resultvalue  = true;
		nodeFirst.resultvalue = true;
		
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
