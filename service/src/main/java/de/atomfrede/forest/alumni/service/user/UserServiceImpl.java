package de.atomfrede.forest.alumni.service.user;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.atomfrede.forest.alumni.domain.dao.user.UserDao;
import de.atomfrede.forest.alumni.domain.entity.user.User;

@Service(value = "userService")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

	private final Log log = LogFactory.getLog(UserServiceImpl.class);

	@Autowired
	private UserDao userDao;

	@Override
	public User getByUsername(String username) {
		return userDao.getByUserName(username);
	}

	@Override
	public List<User> list(long offset, long count) {
		return userDao.list(offset, count);
	}

	@Override
	public List<User> findAll() {
		return userDao.findAll();
	}

	@Override
	public User findById(Long id) {
		return userDao.findById(id);
	}

	@Override
	public User findByProperty(String propertyName, Object propertyValue) {
		return userDao.findByProperty(propertyName, propertyValue);
	}

	@Override
	public void remove(User entity) {
		userDao.remove(entity);

	}

	@Override
	public void persist(User entity) {
		userDao.persist(entity);

	}

	@Override
	public long count() {
		return userDao.count();
	}

	@Override
	public User createUser(String username, String firstname, String lastname,
			String email, String password) throws UsernameAlreadyTakenException {
		User possibleUser = userDao.findByProperty("username", username);
		if (possibleUser != null) {
			log.error("Username already take. Can't create user with user name "
					+ username);
			throw new UsernameAlreadyTakenException();

		}
		User user = new User();
		user.setUsername(username);
		user.setFirstname(firstname);
		user.setLastname(lastname);
		user.setPassword(password);
		user.setEmail(email);

		userDao.persist(user);
		return user;
	}

	@Override
	public boolean canCreateUser(String username) {
		User user = userDao.getByUserName(username);
		return user == null;
	}

}
