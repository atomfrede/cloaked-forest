package de.atomfrede.forest.alumni.domain.dao.activity;

import org.springframework.stereotype.Repository;

import de.atomfrede.forest.alumni.domain.dao.AbstractDAO;
import de.atomfrede.forest.alumni.domain.entity.activity.Activity;

@Repository(value = "activityDao")
public class ActivityDaoImpl extends AbstractDAO<Activity> implements
		ActivityDao {

	public ActivityDaoImpl() {
		super(Activity.class);
	}
}
