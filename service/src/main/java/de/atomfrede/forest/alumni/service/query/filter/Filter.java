package de.atomfrede.forest.alumni.service.query.filter;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Filter implements Serializable {

	public enum Type {
		EQ, LIKE, BETWEEN
	};

	protected String propertyName;
	protected Object value;
	protected Type type;

	public Filter(String propertyName, Object value, Type type) {
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

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(convertPropertyName()+" ");
		sb.append(convertOperator()+" ");
		sb.append(value.toString());
		
		return sb.toString();
	}

	private String convertOperator() {
		switch (type) {
		case EQ:
			return "=";
		case LIKE:
			return "~";
		case BETWEEN:
			return "zwischen";
		default:
			break;
		}

		return type.toString();
	}

	private String convertPropertyName() {
		switch (propertyName) {
		case "degree":
			return "Abschluss";
		case "profession":
			return "Fach";
		case "company":
			return "Firma";
		case "sector":
			return "Branche";
		default:
			break;
		}

		return propertyName;
	}
}
