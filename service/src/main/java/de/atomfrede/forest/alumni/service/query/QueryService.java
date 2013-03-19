package de.atomfrede.forest.alumni.service.query;

import java.util.List;

public interface QueryService {

	List<?> queryDatabase(Query<?> query);
}
