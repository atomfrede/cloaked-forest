package de.atomfrede.forest.alumni.domain;

import de.atomfrede.forest.alumni.domain.entity.AbstractEntity;

public interface JsonTransferable<T extends AbstractEntity> {
	
	public T toJsonTransferable();

}
