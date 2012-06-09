package tpe.parser;

import java.io.IOException;
import java.io.StringReader;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import tpe.parser.collectors.ResultsCollector;
import tpe.parser.collectors.ResultsCollectorMemory;
import tpe.parser.nodes.TPENode;

public class Parser {
	public static void parse(String f, TPENode root, ResultsCollector c) throws SAXException, IOException {
		getParser(root, c).parse(f);
	}

	public static void parseText(String xmlstring, TPENode root, ResultsCollectorMemory c) throws IOException, SAXException {
		InputSource input = new InputSource(new StringReader(xmlstring));
		getParser(root, c).parse(input);
	}
	
	private static XMLReader getParser(TPENode root, ResultsCollector r) throws SAXException {
		if(!root.isRootNode()) {
			throw new RuntimeException("Provided root node is not a root node!");
		}
		
		XMLReader parser = XMLReaderFactory.createXMLReader();
		ContentHandler contentHandler = new StackEval(root, r);
		parser.setContentHandler(contentHandler);
		
		return parser;
	}
}
