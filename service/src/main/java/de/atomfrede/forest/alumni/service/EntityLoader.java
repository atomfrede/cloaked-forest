package de.atomfrede.forest.alumni.service;

import java.io.Serializable;

public interface EntityLoader {

	<T> T load(Class<T> clazz, Serializable id);
}
