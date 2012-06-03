package main;

import java.util.ArrayList;
import java.util.HashMap;

import tpenodes.TPENode;

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
	public int depth;
	
	private int id;
	private StringBuffer value = new StringBuffer();
		
	/**
	 * Constructor.
	 * 
	 * @param index
	 * @param parent
	 * @param s
	 */
	public Match(int prenumber, Match parent, TPENode tpenode, int depth) {
		this.state = TagState.OPEN;
		this.parent = parent;
		this.prenumber = prenumber;
		
		this.tpenode = tpenode;
		
		this.depth = depth;
		
		//inform our parent that we are one of is childs :)
		if(parent != null) {
			parent.addChild(tpenode, this);
			//System.out.println("  > "+parent.name() + " knows us under " + tpenode.nameid());	
		}
	}


	
	
	/**
	 * In case a match has been created, add it to the parent as a child.
	 * This method is in the parent-scope.
	 * @param s
	 * @param m
	 */
	public void addChild(TPENode s, Match m) {
		//System.out.println("I ("+st.p()+") as a parent, am informed that I have a child ("+m.st.p()+")");
		
		//System.out.println("$ "+prenumber+" adding child: "+m.prenumber);
		
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

	public String name() {
		return prenumber+"."+id;
	}


	/**
	 * Generates a String of pre-indices for each query match. 
	 * Implementation is DFS on each node class.
	 * 
	 * @param s
	 * @param pre
	 * @return
	 */
	public ArrayList<String> allPrestoString(String pre) {
		
		pre = pre + " - " + prenumber;
		
		ArrayList<String> res = new ArrayList<String>();
		res.add(pre);
		
		for(TPENode st : children.keySet()) {
			ArrayList<String> newres = new ArrayList<String>();
			for(Match m : children.get(st)) {
				for(String x : res) newres.addAll(m.allPrestoString(x));				
			}
			res = newres;
		}
		
		return res;
	}
	
	/**
	 * 
	 * @param s
	 * @param pre
	 * @return
	 */
	public ArrayList<HashMap<TPENode, Match>> mapResults(HashMap<TPENode, Match> pre) {
		
		if(tpenode.resultvalue == true) {
			pre.put(tpenode, this);
		}
		
		ArrayList<HashMap<TPENode, Match>> res = new ArrayList<HashMap<TPENode, Match>>();
		res.add(pre);
		
		for(TPENode st : children.keySet()) {
			ArrayList<HashMap<TPENode, Match>> newres = new ArrayList<HashMap<TPENode, Match>>();
			for(Match m : children.get(st)) {
				for(HashMap<TPENode, Match> x : res) {
					//clone, otherwise results will be overwritten
					@SuppressWarnings("unchecked")
					HashMap<TPENode, Match> clone = (HashMap<TPENode, Match>) x.clone();
					newres.addAll(m.mapResults(clone));				
				}
			}
			res = newres;
		}
		
		return res;
	}


	/**
	 * Some text has to be added to the string value for this match.
	 * Note that text will only be appended in case the node content must be stored
	 * for printing later (result) or when there is a predicate to check.
	 * @param text
	 */
	public void appendText(String text) {
		if(tpenode.resultvalue || tpenode.hasPredicates())
			value.append(text);
	}
	
	
	/**
	 * Returns the data contained in this Match. This is (at least in my implemenatation):
	 *  all text within the node, without the nodes open and close tags.
	 * @return
	 */
	public String data() {
		String v = value.toString();
		return v.substring(v.indexOf(">")+1, v.lastIndexOf("<"));
	}
	
	/**
	 * Returns the String value of the match.
	 */
	public String toString() {
		return value.toString();
	}

	

	
}
