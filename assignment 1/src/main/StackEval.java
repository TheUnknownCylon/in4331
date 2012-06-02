package main;

import java.util.Stack;


import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

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
	 * Keep track of all open node names.
	 */
	private Stack<String> namesOfOpenNodes = new Stack<String>();
	
	
	/**
	 * Returns the depth of open nodes.
	 * @return
	 */
	private int depth() {
		return namesOfOpenNodes.size();
	}
	
	
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
		
		for(TPENode node : TPENode.getDescendents(rootNode)) {
			TPEStack parentstack = node.parent().stack();
			
			//condition 1
			if( node.canPush(localName)) {
				
				//condition 2
				//a second condition applies in the case of stack s created for
				// a query node p having a parent in the query
				if( parentstack.top(depth()-1) == null ||						//there is no node (TODO: own idea: correct??)
					parentstack.top(depth()-1).getStatus() == TagState.OPEN		// or the node is open
					//&& bla.lastElement().equals(node.parent().name())
				) {
					//create a match satisfying the ancestor conditions of query
					// node s.p
					//System.out.println("");
					//System.out.println("* "+currentPre+"."+i+":"+node.nameid() +"  pushed on " + node.parent().nameid());
					Match m = new Match(currentPre, parentstack.top(depth()-1), node, depth(), i);

					node.stack().push(m);
					i++;
				}
			}
		}
		
		namesOfOpenNodes.push(localName);
		preOfOpenNodes.push(currentPre);
		currentPre++;			

	}
	
	
	public void endElement(String namespaceURI, String localName, String qName)
		throws SAXException {

		//we need to find out if the element ending now correspond to matches
		// in some stacks
		namesOfOpenNodes.pop();
		// first: get the pre number of the element that ends now
		int preOfLastOpen = preOfOpenNodes.pop();
				
		for( TPENode node : TPENode.getDescendents(rootNode)) {
			TPEStack nodestack = node.stack();
			if( node.canPush(localName)) {
				if ( //nodestack.top() != null && //HMMM
					 nodestack.top().getStatus() == TagState.OPEN &&
					nodestack.top().prenumber() == preOfLastOpen
				) {
					//we found the corresponding match!
					// All descendants of this match have been traversed by now.
					Match m = nodestack.pop();

					//now do the post-check:
					// has m matches for all children of its pattern node?
					// if not, the match is not valid, and should be removed from its parent
					// (note: all children are valid matches themselves here!)
					boolean died = false;
				
					for(TPENode childnode : node.getChildren()) {
						if(m.getChildren().get(childnode) == null && childnode.isOptional() == false) {
							m.die(); 						//remove m, s
							died = true;
						}
					}
				
					//If we have not died, it means that all our sub-nodes 
					// are correct. In the case that parent is our root-node,
					// this match is a solution for the query.
					if(died == false && m.tpenode.parent().name().equals("root")) {
						results.addMatch(m);
					}
				
				} //else: ignored
			}
			
		}
	}

	// do nothing methods  
	public void setDocumentLocator(Locator locator) {}
	public void startDocument() throws SAXException {}
	public void endDocument() throws SAXException {}
	public void startPrefixMapping(String prefix, String uri) throws SAXException {}
	public void endPrefixMapping(String prefix) throws SAXException {}
	public void skippedEntity(String name) throws SAXException {}  
	public void ignorableWhitespace(char[] text, int start, int length) throws SAXException {}
	public void processingInstruction(String target, String data) throws SAXException {}

	public void characters(char[] ch, int start, int length) throws SAXException {}
	
}
