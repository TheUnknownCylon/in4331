package main;

import java.util.ArrayList;

import predicates.SimplePredicate;

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
	 * Set true if this node is optional
	 */
	private boolean optinal = false;
	
	
	/**
	 * We can have predicates, for which each match should follow this.
	 * For now we have only one simple predicate: data() == VALUE.
	 * Each stored predicate is in the list and operates as an AND.
	 * 
	 * TODO: AND makes no sense for String values here, but other kinds can
	 * 		 be added later, such as numeric checks etc.
	 */
	private ArrayList<SimplePredicate> predicates = new ArrayList<SimplePredicate>();
	
	/**
	 * Set to true if the value of this node is in the result list.
	 */
	public boolean resultvalue = false;
	
	
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
		parent.addChild(this);
	}

	
	/**
	 * Returns the node name.
	 * @return
	 */
	public String name() {
		if(parent != null) {
			return parent.name() + "/" + name;
		} else {
			return name;
		}
	}
	
	
	/**
	 * Returns only the node name, without parents appended to it.
	 * @return
	 */
	public String ownname() {
		return name;
	}
	
	
	/**
	 * Returns true iff the node should hold the node.
	 * @param name
	 * @return
	 */
	public boolean canPush(String name) {
		return name.equals(this.name);
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
	 * Returns true iff the node is optional, false otherwise.
	 * @return
	 */
	public boolean isOptional() {
		return optinal;
	}
	
	/**
	 * Allows the node to be set as optional.
	 * (Note: not in constructor because too munch parameters will go there.)
	 * @param optinal
	 */
	public void optional(boolean optinal) {
		this.optinal = optinal;
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
	 * Returns true iff a match should be compared agains some predicates.
	 * @return
	 */
	public boolean hasPredicates() {
		return predicates.size() > 0;
	}
	
	/**
	 * Returns the first predicate string.
	 * @return
	 */
	public SimplePredicate getPredicate() {
		return predicates.get(0);
	}
	
	
	/**
	 * Add a predicate to this node.
	 */
	public void addPredicate(SimplePredicate p) {
		predicates.add(p);
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
	
	
	@Override
	public String toString() {
		return name();
	}
}
