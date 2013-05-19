package de.atomfrede.forest.alumni.service.sector;

import java.util.List;

import de.atomfrede.forest.alumni.domain.entity.sector.Sector;
import de.atomfrede.forest.alumni.service.EntityService;

/**
 * This interface encapsules all methods regarding {@link Sector} related operations.
 * 
 * @author fred
 *
 */
public interface SectorService extends EntityService<Sector>{

	/**
	 * Returns a list of sectors for use inside a typeahead input field.
	 * 
	 * @return
	 */
	List<String> getTypeAheadSectors();
	
	/**
	 * Creates a new sector with the given name.
	 * 
	 * @param sector name of the new sector
	 * @return
	 */
	Sector createSector(String sector);
	
	/**
	 * Checks if a sector with the given name already exists.
	 * 
	 * @param sector name of the sector to check.
	 * @return false iff no sector with the given name can be found.
	 */
	boolean alreadyExisting(String sector);
}
