package de.atomfrede.forest.alumni.service.query;

import java.util.List;

import de.atomfrede.forest.alumni.domain.entity.AbstractEntity;

public interface QueryService {

	public List<AbstractEntity> queryDatabase(Query<?> query);
}
