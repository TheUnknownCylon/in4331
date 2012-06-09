package tpe.parser;

import java.io.IOException;


import org.xml.sax.SAXException;

import tpe.parser.collectors.ResultsCollectorMemory;
import tpe.parser.nodes.TPENode;

public abstract class TestFunctionality {
	
	protected static ResultsCollectorMemory getResults(String filename, TPENode rootnode)
			throws SAXException, IOException {
		
		ResultsCollectorMemory resultscollection = new ResultsCollectorMemory();
		Parser.parse(filename, rootnode, resultscollection);
		return resultscollection;
	}

}
