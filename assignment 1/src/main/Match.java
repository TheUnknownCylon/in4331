package main;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 */
public class Match {
	/**
	 * Start number of node nd
	 */
	private int prenumber;
	
	/**
	 * Is the match open or closed?
	 */
	private TagState state;
	
	/**
	 * Ancestors condition:
	 *   The Match parent that corresponds to a match between the parent of nd,
	 *   and the parent of nt in the tree pattern query.
	 *   
	 *   If the edge above nt is a parent edge, then parent can only correspond
	 *   to the parent of nd. Otherwise, parent may be built from the parent of
	 *   another ancestor of nd.
	 */
	private Match parent;
	
	/**
	 * 
	 */
	private HashMap<TPEStack, ArrayList<Match>> children = new HashMap<TPEStack, ArrayList<Match>>();
	
	/**
	 * A pointer to TPEStack: points to the stack associated to the pattern node
	 * nt.
	 * TODO
	 */
	private TPEStack st;
	
	
	/**
	 * Constructor.
	 * 
	 * @param index
	 * @param parent
	 * @param s
	 */
	public Match(int prenumber, Match parent, TPEStack s) {
		this.state = TagState.OPEN;
		this.parent = parent;
		this.prenumber = prenumber;
		this.st = s;
		
		//inform our parent that we are one of is childs :)
		if(parent != null) parent.addChild(s, this);
	}


	
	
	/**
	 * In case a match has been created, add it to the parent as a child.
	 * This method is in the parent-scope.
	 * @param s
	 * @param m
	 */
	public void addChild(TPEStack s, Match m) {
		//System.out.println("I ("+st.p()+") as a parent, am informed that I have a child ("+m.st.p()+")");
		if(!children.containsKey(s)) {
			children.put(s, new ArrayList<Match>());
		}
		children.get(s).add(m);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public TagState getStatus() {
		return state;
	}
	
	public Match parent() {
		return parent;
	}
	
	public HashMap<TPEStack, ArrayList<Match>> getChildren() {
		return children;
	}
	
	public void close() {
		state = TagState.CLOSED;
	}
	
	public int prenumber() {
		return prenumber;
	}
	

	
}
