package main;

import java.util.Stack;

import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.SAXException;

/**
 * SAX Stack evaluation.
 * Nodes are pushed to the stack and removed from the stack. This is done in a
 * way that only nodes are left on the stack that correspond to the query-tree.
 * 
 * Node that the class which is extended is is deprecated.
 * It is used because the assignment says it should be used.
 * 
 * TODO Ask what is preferred: using this deprecated stuff or refactor it.
 *
 */
@SuppressWarnings("deprecation")
public class StackEval extends HandlerBase {
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
	
	private Stack<String> bla = new Stack<String>();
	
	private int depth = 0;
	
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
	
	@Override
	/**
	 * When a node nd in a document d is found to satisfy the ancestor
	 * conditions related to node nt in t, a match object is created to record
	 * this information.
	 */
	public void startElement(String localName, AttributeList attributes)
			throws SAXException {
		
		depth ++;
		
		int i = 0;
		//System.out.println("\nOpen: "+localName);
		for(TPENode node : TPENode.getDescendents(rootNode)) {
			TPEStack parentstack = node.parent().stack();
			
			//condition 1
			if(	node.name().equals(localName)) {
				
				//condition 2
				//a second condition applies in the case of stack s created for
				// a query node p having a parent in the query
				if( parentstack.top(depth-1) == null ||						//there is no node (TODO: own idea: correct??)
					parentstack.top(depth-1).getStatus() == TagState.OPEN		// or the node is open
					&& bla.lastElement().equals(node.parent().name())
				) {
					//create a match satisfying the ancestor conditions of query
					// node s.p
					//System.out.println("");
					//System.out.println("* "+currentPre+"."+i+":"+node.nameid() +"  pushed on " + node.parent().nameid());
					Match m = new Match(currentPre, parentstack.top(depth-1), node, depth, i);

					node.stack().push(m);
					i++;
				}
			}
		}
		
		bla.push(localName);
		preOfOpenNodes.push(currentPre);
		currentPre++;			

	}
	
	
	@Override
	public void endElement(String localName) throws SAXException {
		depth --;
		//System.out.println(">> Close: "+localName);
		
		//we need to find out if the element ending now correspond to matches
		// in some stacks
		bla.pop();
		// first: get the pre number of the element that ends now
		int preOfLastOpen = preOfOpenNodes.pop();
		
		//System.out.println("GOING TO CLOSE "+preOfLastOpen);
		
		for( TPENode node : TPENode.getDescendents(rootNode)) {
			TPEStack nodestack = node.stack();
			if( node.name().equals(localName) &&
					//nodestack.top() != null && //HMMM
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
					if(m.getChildren().get(childnode) == null) {
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

////similarly look for query nodes possibly matched by the attributes
//// of the currently started element
//for(int aindex = 0; aindex < attributes.getLength(); aindex++) {
//	System.out.println(".");
//	String aname = attributes.getName(aindex);
//	
//	for(TPEStack s : rootStack.getDescendentStacks()) {
//		if(aname.equals(s.p().name) && s.spar().top().getStatus() == TagState.OPEN) {
//			System.out.println("New Match (2)!");
//			Match ma = new Match(currentPre, s.spar().top(), s);
//			s.push(ma);
//		}
//	}
//	
//	currentPre++;
//}