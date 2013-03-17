package de.atomfrede.forest.alumni.service.query.filter;

public class Filter {

	public enum Type {EQ, LIKE, BETWEEN};
	
	protected String propertyName;
	protected Object value;
	protected Type type;
	
	public Filter(String propertyName, Object value, Type type){
		this.propertyName = propertyName;
		this.value = value;
		this.type = type;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Object getValue() {
		return value;
	}

	public Type getType() {
		return type;
	}
}
