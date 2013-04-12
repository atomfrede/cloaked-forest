package de.atomfrede.forest.alumni.domain.dao.degree;

import java.util.List;

import de.atomfrede.forest.alumni.domain.dao.DAO;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;

public interface DegreeDao extends DAO<Degree> {

	List<Degree> list(long offset, long count, String orderProperty, boolean desc);
}
