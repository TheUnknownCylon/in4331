package main;

import tpenodes.TPENode;

/**
 * Special match class, for attributes
 */
public class MatchAttribute extends Match{

	public MatchAttribute(int prenumber, Match parent, TPENode tpenode, int depth, String value) {
		super(prenumber, parent, tpenode, depth);
		super.appendText(value);
	}

	@Override
	public void appendText(String text) {}
	
}
