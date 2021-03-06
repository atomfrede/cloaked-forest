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

	/**
	 * Returns the specific subset of all elements.
	 * 
	 * @param offset starting element.
	 * @param count number of elements to retrieve. 
	 * @return elements starting at 0 + offset until offset + count number of elements.
	 */
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

	/**
	 * Returns the element that has the given value for the requested property.
	 * 
	 * @param propertyName Name of the property to check.
	 * @param propertyValue value the property must have.
	 * @return element with e.propertyName == propertyValue or NULL if such an element can't be found.
	 */
	EntityClass findByProperty(String propertyName, Object propertyValue);
	
	void remove(EntityClass entity);

	void persist(EntityClass entity);

	long count();

}
