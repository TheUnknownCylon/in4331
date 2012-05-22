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
	//TODO
	//private TreePattern q;
	
	/**
	 * Stack for the root of q
	 */
	private TPEStack rootStack;
	
	/**
	 * Pre number of the last element which has started
	 */
	private int currentPre = 0;
	
	/**
	 * Pre numbers for all elements having started but not ended yet.
	 */
	private Stack<Integer> preOfOpenNodes = new Stack<Integer>();
	
	
	
	/**
	 * Constructor of the StackEval.
	 * @param rootStack
	 */
	public StackEval(TPEStack rootStack) {
		super();
		this.rootStack = rootStack;
	}
	
	
	
	@Override
	/**
	 * When a node nd in a document d is found to satisfy the ancestor
	 * conditions related to node nt in t, a match object is created to record
	 * this information.
	 */
	public void startElement(String localName, AttributeList attributes)
			throws SAXException {
		
		//System.out.println("Open: "+localName);

		for(TPEStack s : rootStack.getDescendentStacks()) {
			
			//condition 1
			if(	s.p().equals(localName)) {
				
				//condition 2
				//a second condition applies in the case of stack s created for
				// a query node p having a parent in the query
				if( s.parent().top() == null ||							//there is no node (TODO: own idea: correct??)
					s.parent().top().getStatus() == TagState.OPEN		// or the node is open
				) {
					//create a match satisfying the ancestor conditions of query
					// node s.p
					//System.out.println(">> New Match (1): " + localName);
					Match m = new Match(currentPre, s.parent().top(), s);
					s.push(m);
				}
			}
		}
		
		preOfOpenNodes.push(currentPre);
		currentPre++;			

	}
	
	
	@Override
	public void endElement(String localName) throws SAXException {
		//System.out.println(">> Close: "+localName);
		
		//we need to find out if the element ending now correspond to matches
		// in some stacks

		// first: get the pre number of the element that ends now
		int preOfLastOpen = preOfOpenNodes.pop();
		
		for( TPEStack s : rootStack.getDescendentStacks()) {
			if( s.p().equals(localName) &&
					s.top().getStatus() == TagState.OPEN &&
					s.top().prenumber() == preOfLastOpen
			) {
				//we found the corresponding match!
				// All descendents of this match have been traversed by now.
				Match m = s.top();
				
				//now do the post-check:
				// has m matches for all children of its pattern node?
				//boolean mislukt = false;
				for(TPEStack child : s.getChildren()) {
					if(m.getChildren().get(child) == null) {		
						//remove m from s
						s.pop();
					}
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