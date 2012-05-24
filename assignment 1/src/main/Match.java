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
	private HashMap<TPENode, ArrayList<Match>> children = new HashMap<TPENode, ArrayList<Match>>();

	
	public TPENode tpenode;
	
	
	/**
	 * Constructor.
	 * 
	 * @param index
	 * @param parent
	 * @param s
	 */
	public Match(int prenumber, Match parent, TPENode tpenode) {
		this.state = TagState.OPEN;
		this.parent = parent;
		this.prenumber = prenumber;
		
		this.tpenode = tpenode;
		
		//inform our parent that we are one of is childs :)
		if(parent != null) parent.addChild(tpenode, this);
	}


	
	
	/**
	 * In case a match has been created, add it to the parent as a child.
	 * This method is in the parent-scope.
	 * @param s
	 * @param m
	 */
	public void addChild(TPENode s, Match m) {
		//System.out.println("I ("+st.p()+") as a parent, am informed that I have a child ("+m.st.p()+")");
		if(!children.containsKey(s)) {
			children.put(s, new ArrayList<Match>());
		}
		children.get(s).add(m);
	}
	
	public void removeChild(Match m) {	
		for(TPENode s : children.keySet()) {
			children.get(s).remove(m);
			//TODO: optimization, remove all children with length 0
		}
	}
	
	
	public void die() {
		if(parent()!=null)
			parent().removeChild(this);
	}
	

	public TagState getStatus() {
		return state;
	}
	
	public Match parent() {
		return parent;
	}
	
	public HashMap<TPENode, ArrayList<Match>> getChildren() {
		return children;
	}
	
	public void close() {
		state = TagState.CLOSED;
	}
	
	public int prenumber() {
		return prenumber;
	}



	/**
	 * Generates a String of pre-indices for each query match. 
	 * Implementation is DFS on each node class.
	 * 
	 * @param s
	 * @param pre
	 * @return
	 */
	public ArrayList<String> sringyfyResults(String pre) {
		
		pre = pre + " - " + prenumber;
		
		ArrayList<String> res = new ArrayList<String>();
		res.add(pre);
		
		for(TPENode st : children.keySet()) {
			ArrayList<String> newres = new ArrayList<String>();
			for(Match m : children.get(st)) {
				for(String x : res) newres.addAll(m.sringyfyResults(x));				
			}
			res = newres;
		}
		
		return res;
	}	
}
