package main;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import tpenodes.TPENode;
import tpenodes.TPEStack;

/**
 * SAX Stack evaluation.
 * Nodes are pushed to the stack and removed from the stack. This is done in a
 * way that only nodes are left on the stack that correspond to the query-tree.
 * 
 * Node that the class which is extended is is deprecated.
 * It is used because the assignment says it should be used.
 *
 */
public class StackEval implements ContentHandler {
	/**
	 * Root of query TCP-tree
	 */
	private TPENode rootNode;
	
	/**
	 * Pre number of the last element which has started
	 */
	private int currentPre = 0;
	
	/**
	 * Pre numbers for all elements having started but not ended yet.
	 */
	private Stack<Integer> preOfOpenNodes = new Stack<Integer>();
	
	
	/**
	 * We need to push values to all open matches. Therefore store for each open
	 * node a Match.
	 */
	private Stack<Match> allOpenMatches = new Stack<Match>();
		
	/**
	 * When the StackEval can decide whether a match was found for the TCP-tree,
	 * the match is added to a results-collector, and the information is removed
	 * from the stacks.
	 */
	private ResultsCollector results;
	
	
	/**
	 * Constructor of the StackEval.
	 * @param rootNode
	 */
	public StackEval(TPENode rootNode, ResultsCollector results) {
		super();
		this.rootNode = rootNode;
		this.results = results;
	}	
	
	
	/**
	 * When a node nd in a document d is found to satisfy the ancestor
	 * conditions related to node nt in t, a match object is created to record
	 * this information.
	 */
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
		throws SAXException {

		int i = 0;
		Match m = null;

		for(TPENode node : TPENode.getDescendents(rootNode)) {
			
			//condition 1
			if( node.canPush(localName)) {
				
				//condition 2
				//a second condition applies in the case of stack s created for
				// a query node p having a parent in the query
				TPEStack parentstack = node.isRootNode()?null:node.parent().stack();
				if( 
					node.isRootNode() && (depth() == 0 || node.isSlashSlash()) ||
					parentstack != null && parentstack.top(depth()) == null ||
					parentstack != null && parentstack.top(depth()).getStatus() == TagState.OPEN
				) {
					//create a match satisfying the ancestor conditions of query
					// node s.p
					Match toPushOn = null;
					if(!node.isRootNode()) toPushOn = parentstack.top(depth());
					if(node.isSlashSlash() && !node.isRootNode()) 
						toPushOn = allOpenMatches.peek();

					m = new Match(currentPre, toPushOn, node, depth()+1, i);
					allOpenMatches.push(m);
									
					node.stack().push(m);
					i++;
				}
			}
		}
		addTextToOpenMatches("<"+localName+">");
		
		
		preOfOpenNodes.push(currentPre);
		currentPre++;			

	}
	
	
	/**
	 * Fire end element event.
	 * Do the algorithm-stuff for this close-event.
	 */
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		addTextToOpenMatches("</"+localName+">");
		
		//we need to find out if the element ending now correspond to matches
		// in some stacks
		// first: get the pre number of the element that ends now
		int preOfLastOpen = preOfOpenNodes.pop();
				
		for( TPENode node : TPENode.getDescendents(rootNode)) {
			TPEStack nodestack = node.stack();
			if( node.canPush(localName)) {
				if ( nodestack.matches.size() > 0 &&
					 nodestack.top().getStatus() == TagState.OPEN &&
					 nodestack.top().prenumber() == preOfLastOpen
				) {
					//we found a corresponding match!
					// All descendants of this match have been traversed by now.
					Match m = nodestack.pop();
					allOpenMatches.pop();

					//now do the post-check, if it fails, the node dies and is ignored:
					// two checks have to pass:
					// 1. The match holds its node predicate
					//
					// 2. Has m matches for all children of its pattern node?
					//    if not, the match is not valid, and should be removed from its parent
					//    (note: all children are valid matches themselves here!)
					boolean died = false;
					
					if(!checkPredicate(m)) {
						died = true;
					}
					
					for(TPENode childnode : node.getChildren()) {
						if(m.getChildren().get(childnode) == null && childnode.isOptional() == false) {
							died = true;
						}
					}
				
					if(died == true) {
						m.die(); // remove m, s
					}
					
					//If we have not died, it means that all our sub-nodes 
					// are correct. In the case that parent is our root-node,
					// this match is a solution for the query.
					if(died == false && m.tpenode.isRootNode()) {
						results.addMatch(m);
					}
				
				} //else: ignored
			}
			
		}
	}
	
	
	/**
	 * The SAX parser has found characters, which we are going to add
	 * to all open matches, as the data are match content.
	 */
	public void characters(char[] text, int start, int length) throws SAXException {
	    StringBuffer buffer = new StringBuffer();
	    buffer.append(text, start, length);
	    addTextToOpenMatches(buffer.toString());
	}

	
	/**
	 * The algorithm implementation needs to be aware of the depth of the
	 * current node. This information is kept in this depth variable.
	 * @return Returns the depth of open nodes.
	 */
	private int depth() {
		return preOfOpenNodes.size();
	}
	
	/**
	 * Add text to all open matches as match-data.
	 * @param text
	 */
	private void addTextToOpenMatches(String text) {
		for(Match m : allOpenMatches) {
			m.appendText(text);
		}
	}
	
	/**
	 * Checks whether m holds it predicates.
	 * @param m
	 * @return
	 */
	private boolean checkPredicate(Match m) {
		TPENode n = m.tpenode;
		if(!n.hasPredicates()) {
			return true;
		}
		return n.getPredicate().match(m.data());
	}
	
	
	
	// The following methods are fired by the SAX reader, but we are not
	//  going to use them.
	public void setDocumentLocator(Locator locator) {}
	public void startDocument() throws SAXException {}
	public void endDocument() throws SAXException {}
	public void startPrefixMapping(String prefix, String uri) throws SAXException {}
	public void endPrefixMapping(String prefix) throws SAXException {}
	public void skippedEntity(String name) throws SAXException {}  
	public void processingInstruction(String target, String data) throws SAXException {}
	public void ignorableWhitespace(char[] text, int start, int length) throws SAXException {}
	
}
