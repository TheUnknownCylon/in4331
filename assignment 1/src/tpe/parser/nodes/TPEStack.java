package tpe.parser.nodes;

import java.util.NoSuchElementException;
import java.util.Stack;

import tpe.parser.Match;


/**
 * For each node p in the tree-pattern, a TPEStack is created, on which the
 * matches corresponding to this pattern node are pushed as they are created.
 */
public class TPEStack {

	/**
	 * Hold a stack of all matches.
	 */
	public Stack<Match> matches = new Stack<Match>();

	/**
	 * A match has been found, and is pushed to our stack.
	 * @param m
	 */
	public void push(Match  m) {
		matches.push(m);
	}
	
	
	/**
	 * Pops the top item of the list.
	 * @return
	 */
	public Match pop() {
		return matches.pop();
	}
	
	
	/**
	 * Returns the last element on the stack, without popping it.
	 */
	public Match top() {
		try {
			return matches.lastElement();
			
		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	
	/**
	 * Returns the first element on the stack for a given depth.
	 * This functionality is required in case nested nodes are in the query-tree,
	 * for example one wants to match:
	 *   <person><person><email>a@b.c</email></person></person>
	 * @param depth
	 * @return
	 */
	public Match top(int depth) {
		
		if(matches.size()>0) for(int i = matches.size()-1; i >= 0; i--) {
			Match m = matches.get(i);
			if(m.depth == depth) {
				return m;
			}
		}
		
		return null;
	}

}
