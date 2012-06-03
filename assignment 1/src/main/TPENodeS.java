package main;

public class TPENodeS extends TPENode {

	public TPENodeS(String name) {
		super(name);
	}
	
	public TPENodeS(String name, TPENode parent) {
		super(name, parent);
	}

	@Override
	public boolean isSlashSlash() {
		return true;
	}
	
}
