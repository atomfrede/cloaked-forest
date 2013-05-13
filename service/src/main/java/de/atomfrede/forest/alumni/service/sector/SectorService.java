package de.atomfrede.forest.alumni.service.sector;

import java.util.List;

import de.atomfrede.forest.alumni.domain.entity.sector.Sector;

public interface SectorService {

	/**
	 * Returns a list of sectors for use inside a typeahead input field.
	 * @return
	 */
	List<String> getTypeAheadSectors();
	
	Sector createSector(String sector);
	
	boolean alreadyExisting(String sector);
}
