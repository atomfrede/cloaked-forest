package de.atomfrede.forest.alumni.service.user;

import de.atomfrede.forest.alumni.domain.entity.user.User;
import de.atomfrede.forest.alumni.service.EntityService;

public interface UserService extends EntityService<User> {

	User getByUsername(String username);

	/**
	 * Creates a new user and tries to write it into database.
	 * 
	 * @param username
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param password
	 * @return
	 */
	User createUser(String username, String firstname, String lastname,
			String email, String password) throws UsernameAlreadyTakenException;

	boolean canCreateUser(String username);

}
