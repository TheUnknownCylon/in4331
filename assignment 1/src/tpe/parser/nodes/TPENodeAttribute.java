package tpe.parser.nodes;

public class TPENodeAttribute extends TPENode {

	public TPENodeAttribute(String name, TPENode parent) {
		super(name, parent);
	}

	@Override
	public boolean canPush(String name, boolean isattribute) {
		return isattribute == true && name.equals(ownname());
	}
	
}
