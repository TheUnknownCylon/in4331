package tpe.parser.predicates;

public class IntLargerThan implements SimplePredicate {
	private int toCompare;
	
	public IntLargerThan(int toCompare) {
		this.toCompare = toCompare;
	}
	
	@Override
	public boolean match(String value) {
		try {
			return Integer.parseInt(value) > toCompare;	
		} catch(Exception e) {
			return false;
		} 
	}

}
