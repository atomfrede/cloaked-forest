package de.atomfrede.forest.alumni.service.degree;

import java.util.List;

import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.service.EntityService;

public interface DegreeService extends EntityService<Degree>{

	Degree createDegree(String title, String shortform);

	boolean deleteDegree(Degree degree);

	boolean deleteDegree(long id);
	
	List<Degree> list(long offset, long count, String orderProperty,
			boolean desc);
}
