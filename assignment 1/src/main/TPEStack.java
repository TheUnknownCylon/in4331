package main;

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
	public Stack<Match> matches = new Stack<Match>();

	/**
	 * A match has been found, and is pushed to our stack.
	 * @param m
	 */
	public void push(Match  m) {
		matches.push(m);
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
	

	public Match top(int depth) {
		
		if(matches.size()>0) for(int i = matches.size()-1; i >= 0; i--) {
			Match m = matches.get(i);
			if(m.depth == depth) {
				return m;
			}
		}
		
		return null;
	}
	
	public Match pop() {
		return matches.pop();
	}
	

}
