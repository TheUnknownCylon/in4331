import java.io.IOException;
import org.xml.sax.SAXException;

import tpe.parser.Parser;
import tpe.parser.collectors.ResultsCollectorPrinter;
import tpe.parser.nodes.TPENode;
import tpe.parser.nodes.TPENodeS;
import tpe.parser.predicates.SimplePredicate;
import tpe.parser.predicates.StringCompare;

/**
 * Demonstration for a 'large' file.
 *   http://cheats.gbatemp.net/files/26_03_11_All.zip
 *
 */
public class Demo {
	public static void main(String[] args) throws SAXException, IOException {
		String f = "datasets/ds-cheats.xml";
		
		TPENode nodeRoot = //gameNames();
						   codesForSpecificGame();
		
		ResultsCollectorPrinter collection = new ResultsCollectorPrinter();

		collection.printOpen();
		Parser.parse(f, nodeRoot, collection);
		collection.printClose();

	}
	
	@SuppressWarnings("unused")
	private static TPENode gameNames() {
		TPENode nodeRoot   = new TPENodeS("name");
		nodeRoot.addPredicate(StringCompare("101 in 1 Sports Megamix (U)"));

		nodeRoot.resultvalue = true;
		return nodeRoot;
	}
	
	
	
	
	private static TPENode codesForSpecificGame() {
		TPENode nodeRoot   = new TPENodeS("game");
		TPENode nodename   = new TPENode ("name", nodeRoot);
		TPENode nodeCodes  = new TPENodeS("cheat", nodeRoot); 
		
		nodename.resultvalue = true;
		nodename.addPredicate(new StringCompare("101 in 1 Sports Megamix (U)"));
		
		nodeCodes.resultvalue = true;
		
		return nodeRoot;
	}

	private static SimplePredicate StringCompare(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
