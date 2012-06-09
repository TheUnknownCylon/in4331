package tpe.parser.nodes;

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
	 * We accept all but attributes.
	 */
	public boolean canPush(String name, boolean isattribute) {
		return !isattribute;
	}

}
