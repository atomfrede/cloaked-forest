package de.atomfrede.forest.alumni.service.sector;

import java.util.List;

public interface SectorService {

	/**
	 * Returns a list of sectors for use inside a typeahead input field.
	 * @return
	 */
	List<String> getTypeAheadSectors();
}
