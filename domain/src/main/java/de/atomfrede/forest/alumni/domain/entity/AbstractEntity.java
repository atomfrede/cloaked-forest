package de.atomfrede.forest.alumni.domain.entity;

@SuppressWarnings("serial")
public abstract class AbstractEntity implements IEntity{

	public boolean isNew() {
        return getId() == null;
    }

    public boolean isPersisted() {
        return !isNew();
    }
    
}
