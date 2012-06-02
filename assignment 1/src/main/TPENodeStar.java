package main;

/**
 * Implementation for the *-node.
 * It simply accepts all nodes.
 */
public class TPENodeStar extends TPENode {

	/**
	 * Constructor
	 * @param parent
	 * @param id
	 */
	public TPENodeStar(TPENode parent) {
		super("*", parent); //* is not needed actually.
	}
	
	@Override
	/**
	 * Returns always true, in order to accept all.
	 */
	public boolean canPush(String name) {
		return true;
	}

}
