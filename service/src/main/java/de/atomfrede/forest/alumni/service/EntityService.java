package de.atomfrede.forest.alumni.service;

import java.util.List;

import de.atomfrede.forest.alumni.domain.entity.IEntity;

public interface EntityService<EntityClass extends IEntity> {

	List<EntityClass> list(long offset, long count);

	List<EntityClass> findAll();

	EntityClass findById(Long id);

	EntityClass findByProperty(String propertyName, Object propertyValue);

	void remove(EntityClass entity);

	void persist(EntityClass entity);

	long count();

}
