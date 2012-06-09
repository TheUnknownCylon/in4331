package tpe.parser;

import java.io.IOException;

import org.xml.sax.SAXException;
import tpe.parser.collectors.ResultsCollector;
import tpe.parser.collectors.ResultsCollectorPrinter;
import tpe.parser.nodes.TPENode;
import tpe.parser.nodes.TPENodeS;


public class BootStrap {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static void main(String[] args) throws SAXException, IOException {
		String f = new String("/home/remco/documents/in4331/dblp.xml");

		
//		TPENode nodeRoot   = new TPENode("people");
//		TPENode nodePerson = new TPENode("person", nodeRoot);
//		TPENode nodePersonAtt = new TPENodeAttribute("name", nodePerson);
//				nodePersonAtt.resultvalue = true;
//		//		nodePersonAtt.optional(true);
//		
//		TPENode nodeEmail  = new TPENode("email", nodePerson);
//		TPENode nodeName   = new TPENode("name", nodePerson);
//		TPENode nodeLast   = new TPENode("last", nodeName);

		TPENode nodeRoot = new TPENodeS("article");
		TPENode nodeAuthor = new TPENode("author", nodeRoot);
		TPENode nodeTitle = new TPENode("title", nodeRoot);
		
		nodeAuthor.resultvalue = true;
		nodeTitle.resultvalue = true;
		
		//ResultsCollectorMemory collection = new ResultsCollectorMemory();
		ResultsCollector collection = new ResultsCollectorPrinter();

		Parser.parse(f, nodeRoot, collection);

	}
}
