package de.atomfrede.forest.alumni.domain.dao.user;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import de.atomfrede.forest.alumni.domain.dao.AbstractDAO;
import de.atomfrede.forest.alumni.domain.entity.user.User;

@Repository(value = "userDao")
public class UserDaoImpl extends AbstractDAO<User> implements UserDao {

	public UserDaoImpl() {
		super(User.class);
	}

	@Override
	@Transactional(readOnly = true)
	public User getByUserName(String userName) {
		return findByProperty("username", userName);
	}
}
