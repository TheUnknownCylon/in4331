package demo;

import java.io.IOException;

import org.xml.sax.SAXException;

import tpe.parser.Parser;
import tpe.parser.collectors.ResultsCollector;
import tpe.parser.collectors.ResultsCollectorPrinter;
import tpe.parser.nodes.TPENode;

public class D2_optional {
	public static void main(String[] args) throws SAXException, IOException {
		TPENode nodeRoot   = new TPENode("people");
		TPENode nodePerson = new TPENode("person", nodeRoot);
		TPENode nodeEmail  = new TPENode("email", nodePerson);
		TPENode nodeName   = new TPENode("name", nodePerson);
		TPENode nodeLast   = new TPENode("last", nodeName);
		
		nodeEmail.optional(true);
		
		nodeEmail.resultvalue = true;
		nodeLast.resultvalue = true;

		ResultsCollector collection = new ResultsCollectorPrinter();

		String f = new String("datasets/example-book.xml");
		Parser.parse(f, nodeRoot, collection);
	}
}
