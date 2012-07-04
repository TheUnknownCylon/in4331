package demo;

import java.io.IOException;

import org.xml.sax.SAXException;

import tpe.parser.Parser;
import tpe.parser.collectors.ResultsCollector;
import tpe.parser.collectors.ResultsCollectorPrinterFormat;
import tpe.parser.collectors.ResultsXMLFormat;
import tpe.parser.nodes.TPENode;
import tpe.parser.nodes.TPENodeS;
import tpe.parser.predicates.IntLargerThan;

public class LargeFileDBLP {
	/**
	 * @param args
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static void main(String[] args) throws SAXException, IOException {

		TPENode nodeRoot = new TPENodeS("article");
		TPENode nodeJournal = new TPENode("journal", nodeRoot);
		TPENode nodeTitle = new TPENode("title", nodeRoot);
		TPENode nodeYear = new TPENode("year", nodeRoot);
		
		nodeJournal.resultvalue = true;
		nodeTitle.resultvalue = true;
		nodeYear.resultvalue = true;
		
		nodeYear.addPredicate(new IntLargerThan(2008));
		
		
		/**
		 * Specify a custom output format for each result.
		 * It should be possible to build this up from the result of an XQuery.
		 */
		ResultsXMLFormat format = new ResultsXMLFormat();
		format.append("<article");
		format.append(" year=\"");
		format.appendContentOf(nodeYear);
		format.append("\">");
		
		format.append(nodeJournal);
		format.append(nodeTitle);
		format.append("</article>");
		
		/**
		 * Create the collection printer, with the specified output
		 */
		ResultsCollector collection = new ResultsCollectorPrinterFormat(format);

		String f = new String("datasets/dblp/dblp.xml");
		Parser.parse(f, nodeRoot, collection);
	}
}
