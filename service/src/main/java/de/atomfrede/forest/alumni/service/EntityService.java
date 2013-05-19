package de.atomfrede.forest.alumni.service;

import java.util.List;

import de.atomfrede.forest.alumni.domain.dao.DAO;
import de.atomfrede.forest.alumni.domain.entity.IEntity;

/**
 * Providing common operations useful for any service interacting with entity classes.
 * 
 * Most of the time these methods only call the corresponding method inside the {@link DAO}.
 * 
 * @author fred
 *
 * @param <EntityClass>
 */
public interface EntityService<EntityClass extends IEntity> {

	List<EntityClass> list(long offset, long count);

	/**
	 * Returns a list with all elements of EntityClass
	 * @return
	 */
	List<EntityClass> findAll();

	/**
	 * Returns the element with the given id or null if it doesn't exist.
	 * 
	 * @param id
	 * @return
	 */
	EntityClass findById(Long id);

	EntityClass findByProperty(String propertyName, Object propertyValue);

	void remove(EntityClass entity);

	void persist(EntityClass entity);

	long count();

}
