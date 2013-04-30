package de.atomfrede.forest.alumni.domain.dao.sector;

import org.springframework.stereotype.Repository;

import de.atomfrede.forest.alumni.domain.dao.AbstractDAO;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;

@Repository(value = "sectorDao")
public class SectorDaoImpl extends AbstractDAO<Sector> implements SectorDao {

	public SectorDaoImpl() {
		super(Sector.class);
	}

}
