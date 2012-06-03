package predicates;

public class StringCompare implements SimplePredicate {

	private String toCompare;
	public StringCompare(String toCompare) {
		this.toCompare = toCompare;
	}
	
	@Override
	public boolean match(String value) {
		return toCompare.equals(value); 
	}

}
