package de.atomfrede.forest.alumni.domain.dao.degree;

import org.springframework.stereotype.Repository;

import de.atomfrede.forest.alumni.domain.dao.AbstractDAO;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;

@Repository(value = "degreeDao")
public class DegreeDaoImpl extends AbstractDAO<Degree> implements DegreeDao {

	public DegreeDaoImpl() {
		super(Degree.class);
	}
}
