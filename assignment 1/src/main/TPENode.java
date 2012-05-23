package main;

import java.util.ArrayList;

public class TPENode {	
	/**
	 * Tag name for the node. 
	 */
	private String name;
	
	/**
	 * Stores the parent node. If there is no parent (root),
	 * then this value is null.
	 */
	private TPENode parent;
	
	/**
	 * Keep a list of all children nodes.
	 */
	private ArrayList<TPENode> children = new ArrayList<TPENode>();
	
	
	/**
	 * Keep a stack on which all matches for this node are stored.
	 */
	private TPEStack stack = new TPEStack();
	
	/**
	 * Constructor.
	 * @param name
	 */
	public TPENode(String name) {
		this.name = name;
	}
	
	
	/**
	 * Constructor, which allows to set a parent.
	 * @param name
	 * @param parent
	 */
	public TPENode(String name, TPENode parent) {
		this(name);
		this.parent = parent;
	}
	
	
	/**
	 * Returns the node name.
	 * @return
	 */
	public String name() {
		return name;
	}
	
	/**
	 * Returns the stack of matches for this node.
	 * @return
	 */
	public TPEStack stack() {
		return stack;
	}
	
	/**
	 * Returns the parent node.
	 * @return
	 */
	public TPENode parent() {
		return parent;
	}
	
	/**
	 * In case this node has a child, inform it.
	 * @param childstack
	 */
	public void addChild(TPENode childstack) {
		children.add(childstack);
	}
	
	/**
	 * Get a list of all children.
	 * @return
	 */
	public ArrayList<TPENode> getChildren() {
		return children;
	}
	
	/**
	 * Gets the stacks for all descendants of p.
	 * @TODO In case only the root children are needed, adjust this code!!
	 * @return
	 */
	public static ArrayList<TPENode> getDescendents(TPENode node) {
		
		ArrayList<TPENode> alldescendants  = new ArrayList<TPENode>();
		for(TPENode n : node.getChildren()) {
			alldescendants.add(n);
			alldescendants.addAll(getDescendents(n));
		}
		
		return alldescendants;
	}

}