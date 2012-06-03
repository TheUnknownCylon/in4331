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
	
	@Override
	public void addChild(TPENode s, Match m) {
		throw new RuntimeException("Can not add an child node to an attribute!");		
	}
	
	public String data() {
		return this.toString();
	}
}
