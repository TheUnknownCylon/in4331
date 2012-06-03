package main;

import java.io.IOException;

import org.xml.sax.SAXException;

import resultcollectors.ResultsCollectorMemory;
import tpenodes.TPENode;

public abstract class TestFunctionality {
	
	protected static ResultsCollectorMemory getResults(String filename, TPENode rootnode)
			throws SAXException, IOException {
		
		ResultsCollectorMemory resultscollection = new ResultsCollectorMemory();
		BootStrap.parse(filename, rootnode, resultscollection);
		return resultscollection;
	}

}
