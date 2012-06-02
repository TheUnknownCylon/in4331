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
	@SuppressWarnings("unused")
	public static void main(String[] args) throws SAXException, IOException {
		String f = new String("datasets/example-book.xml");

		TPENode nodeRoot   = new  TPENode("root");
		TPENode nodePerson = new TPENode("person", nodeRoot);
				nodePerson.resultvalue = true;
		TPENode nodeEmail  = new TPENode("email", nodePerson);
			    nodeEmail.optional(true);
			    //nodeEmail.resultvalue = true;
			    
		//TPENode nodeName   = new TPENodeStar(nodePerson, "* 1");
		TPENode nodeName   = new TPENode("name", nodePerson);
				//nodeName.resultvalue = true;
		TPENode nodeLast   = //new TPENodeStar(nodeName, "* 1");
							 new TPENode("last", nodeName);
				//nodeLast.resultvalue = true;
		
		//TPENode nodeName   = new TPENodeStar(nodePerson, "* 1");
		//TPENode nodeName2   = new TPENode("name", nodePerson);
		//TPENode nodeLast2   = //new TPENodeStar(nodeName, "* 1");
		//					 new TPENode("last", nodeName2);

		
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
