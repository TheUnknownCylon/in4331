package tpe.parser;

import java.io.IOException;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import tpe.parser.collectors.ResultsCollector;
import tpe.parser.nodes.TPENode;

public class Parser {
	public static void parse(String f, TPENode root, ResultsCollector resultscollector) throws SAXException, IOException {
		
		if(!root.isRootNode()) {
			throw new RuntimeException("Provided root node is not a root node!");
		}
		
		XMLReader parser = XMLReaderFactory.createXMLReader();
		ContentHandler contentHandler = new StackEval(root, resultscollector);
		parser.setContentHandler(contentHandler);
		parser.parse(f);
	}
}
