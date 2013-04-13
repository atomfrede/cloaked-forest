package de.atomfrede.forest.alumni.service.degree;

import java.util.List;

import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.service.EntityService;

/**
 * Service methods interacting with a degree.
 * 
 * @author fred
 *
 */
public interface DegreeService extends EntityService<Degree> {

	/**
	 * Creates a degree with the given title and shortform.
	 * 
	 * @param title of the new degree
	 * @param shortform of the new degree
	 * @return
	 */
	Degree createDegree(String title, String shortform);

	/**
	 * Deletes the given degree from database.
	 * 
	 * @param degree
	 * @return
	 */
	boolean deleteDegree(Degree degree);

	/**
	 * Deletes the degree wit the given ID from database.
	 * 
	 * @param id
	 * @return
	 */
	boolean deleteDegree(long id);

	/**
	 * Utility method for use with paging dataviews.
	 * 
	 * @param offset
	 * @param count
	 * @param orderProperty property to order the result set
	 * @param desc flag indicating the order direction
	 * @return
	 */
	List<Degree> list(long offset, long count, String orderProperty,
			boolean desc);
}
