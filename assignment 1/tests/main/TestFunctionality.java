package main;

import java.io.IOException;

import org.xml.sax.SAXException;

import tpenodes.TPENode;

public abstract class TestFunctionality {
	
	protected ResultsCollector getResults(String filename, TPENode rootnode)
			throws SAXException, IOException {
		
		ResultsCollector resultscollection = new ResultsCollector();
		BootStrap.parse(filename, rootnode, resultscollection);
		return resultscollection;
	}

}
