package de.atomfrede.forest.alumni.domain.dao.user;

import de.atomfrede.forest.alumni.domain.dao.DAO;
import de.atomfrede.forest.alumni.domain.entity.user.User;

public interface UserDao extends DAO<User> {

	User getByUserName(String userName);
}
