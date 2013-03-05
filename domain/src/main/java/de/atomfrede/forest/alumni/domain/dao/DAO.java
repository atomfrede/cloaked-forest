package de.atomfrede.forest.alumni.domain.dao;

import java.util.List;

import de.atomfrede.forest.alumni.domain.entity.IEntity;

public interface DAO<EntityClass extends IEntity> {

	List<EntityClass> list(long offset, long count);

	List<EntityClass> findAll();

	EntityClass findById(Long id);

	EntityClass findByProperty(String propertyName, Object propertyValue);

	List<EntityClass> findAllByProperty(String propertName, Object propertyValue);
	
	void remove(EntityClass entity);
	
	void remove(Long id);

	void persistAll(List<EntityClass> entities);
	
	void persist(EntityClass entity);

	long count();

	Class<EntityClass> getClazz();

}
