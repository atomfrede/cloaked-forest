package de.atomfrede.forest.alumni.domain.dao.filter;

public class FilterElement {

	String propertyName;
	String filter;

	public FilterElement propertyName(String propertyName) {
		this.propertyName = propertyName;
		return this;
	}

	public FilterElement filter(String filter) {
		this.filter = filter;
		return this;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public String getFilter() {
		return filter;
	}

}
