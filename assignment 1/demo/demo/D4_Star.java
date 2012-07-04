package demo;

import java.io.IOException;

import org.xml.sax.SAXException;

import tpe.parser.Parser;
import tpe.parser.collectors.ResultsCollector;
import tpe.parser.collectors.ResultsCollectorPrinter;
import tpe.parser.nodes.TPENode;
import tpe.parser.nodes.TPENodeS;
import tpe.parser.nodes.TPENodeStar;

public class D4_Star {
	public static void main(String[] args) throws SAXException, IOException {
		TPENode nodePerson = new TPENodeS("person");
		TPENode nodeEmail  = new TPENode("email", nodePerson);
		TPENode nodeName   = new TPENode("name", nodePerson);
		TPENode nodeNameV  = new TPENodeStar(nodeName);
		
		nodeEmail.optional(true);
		
		nodeNameV.resultvalue = true;

		ResultsCollector collection = new ResultsCollectorPrinter();

		String f = new String("datasets/example-book.xml");
		Parser.parse(f, nodePerson, collection);
	}
}
