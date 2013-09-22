package de.atomfrede.forest.alumni.application.wicket.model;

import java.io.Serializable;

import javax.persistence.EntityNotFoundException;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.io.IClusterable;

import de.atomfrede.forest.alumni.domain.entity.AbstractEntity;
import de.atomfrede.forest.alumni.service.EntityLoader;

public class AbstractEntityModel<T extends AbstractEntity> implements
		IModel<T>, IClusterable, Serializable {

	private static final long serialVersionUID = -1926446968284768121L;

	@SpringBean
	protected EntityLoader mEntityLoader;

	protected Serializable id;

	protected T entity;

	protected Class<? extends AbstractEntity> clazz;

	public AbstractEntityModel(T entity) {
		this.clazz = entity.getClass();
		id = entity.getId();
		this.entity = entity;

		Injector.get().inject(this);

	}

	public AbstractEntityModel(Class<? extends AbstractEntity> clazz,
			Serializable id) {
		this.clazz = clazz;
		this.id = id;
		Injector.get().inject(this);
	}

	public T getObject() {
		try {
			// FIXME CHeck why this throws an exception
			entity.getId();
		} catch (Exception e) {
			entity = null;
		}
		if (entity == null) {
			if (id != null) {
				entity = load(id);
				if (entity == null) {
					throw new EntityNotFoundException("Entity of type " + clazz
							+ " with id " + id + " could not be found.");
				}
			}
		}
		return entity;
	}

	public void detach() {
		try {
			// FIXME CHeck why this throws an exception
			entity.getId();
		} catch (Exception e) {
			entity = null;
		}
		if (entity != null) {
			if (entity.getId() != null) {
				id = entity.getId();
				entity = null;
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected T load(Serializable id) {
		return (T) mEntityLoader.load(clazz, id);
	}

	public void setObject(T object) {
		throw new UnsupportedOperationException(getClass()
				+ " does not support #setObject(T entity)");
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if(obj instanceof AbstractEntityModel){
			if(((AbstractEntityModel<?>)obj).id.equals(id) && ((AbstractEntityModel<?>)obj).entity.equals(entity)){
				return true;
			}
		}
		return false;
	}

}
