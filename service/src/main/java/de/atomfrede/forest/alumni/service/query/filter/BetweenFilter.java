package de.atomfrede.forest.alumni.service.query.filter;

public class BetweenFilter extends Filter {

	protected Object value2;
	
	public BetweenFilter(String propertyName, Object value, Object value2, Type type){
		super(propertyName, value, type);
		this.value2 = value2;
	}

	public Object getValue2() {
		return value2;
	}
}
