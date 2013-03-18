package de.atomfrede.forest.alumni.service.query;

import de.atomfrede.forest.alumni.domain.entity.AbstractEntity;

public class SubQuery<T extends AbstractEntity> extends Query<T> {

	public enum Conjunction {
		AND, OR
	}

	private Conjunction conjunction;

	public SubQuery(Class<T> clazz, Conjunction conjunction) {
		super(clazz);
		this.conjunction = conjunction;
	}
}
