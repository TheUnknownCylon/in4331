package main;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * For each node p in the tree-pattern, a TPEStack is created, on which the
 * matches corresponding to this pattern node are pushed as they are created.
 */
public class TPEStack {

	
	/**
	 * 
	 */
	//private PatternNode p;
	private String p;
	
	/**
	 * 
	 */
	public Stack<Match> matches = new Stack<Match>();
	
	/**
	 * Corresponding to the parent of p, if p is not the pattern root.
	 */
	private TPEStack sParent;
	
	/**
	 * A set of the TPEStacks, corresponding to the children of p, if any.
	 * Empty list otherwise.
	 */
	private ArrayList<TPEStack> children = new ArrayList<TPEStack>();
	
	
	
	
	
	/**
	 * Constructor for root node.
	 * @param name
	 * @param children
	 */
	public TPEStack(String name) {
		//this.p = new PatternNode(name);
		this.p = name;
	}

	/**
	 * Constructor for non-root nodes.
	 * @param name
	 * @param sParent
	 * @param children
	 */
	public TPEStack(String name, TPEStack sParent) {
		this(name);
		this.sParent = sParent;
	}
	
	public void addChild(TPEStack childstack) {
		children.add(childstack);
	}
	

	
	public ArrayList<TPEStack> getChildren() {
		return children;
	}
	
	
	/**
	 * Gets the stacks for all descendants of p.
	 * @TODO In case only the root children are needed, adjust this code!!
	 * @return
	 */
	public ArrayList<TPEStack> getDescendentStacks() {
		
		ArrayList<TPEStack> alldescendants  = new ArrayList<TPEStack>();
		alldescendants.addAll(getChildren());
		
		for(TPEStack child : children) {
			alldescendants.addAll(child.getDescendentStacks());
		}
		return alldescendants;
	}
	
	/**
	 * A match has been found, and is pushed to our stack.
	 * @param m
	 */
	public void push(Match  m) {
		matches.push(m);
	}
	
	/**
	 * TODO: Verify
	 * @return
	 */
	public Match top() {
		try {
			return matches.lastElement(); //TODO: verify that this is meant by top
			
		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	public Match pop() {
		return matches.pop();
	}
	
	public TPEStack parent() {
		return sParent;
	}
	
//	public PatternNode p() {
//		return p;
//	}
	
	public String p() {
		return p;
	}
}
