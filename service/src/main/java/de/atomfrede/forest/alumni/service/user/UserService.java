package de.atomfrede.forest.alumni.service.user;

import de.atomfrede.forest.alumni.domain.entity.user.User;
import de.atomfrede.forest.alumni.service.EntityService;

/**
 * Service methods interacting with a user object.
 * 
 * @author fred
 *
 */
public interface UserService extends EntityService<User> {

	/**
	 * Deletes the user with the given id.
	 * 
	 * @param id
	 * @return
	 */
	boolean deleteUser(long id);

	/**
	 * Returns the user with the given username.
	 * 
	 * @param username
	 * @return
	 */
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

	/**
	 * Creates a user with the given parameters.
	 * 
	 * The email is set to the empty string.
	 * 
	 * @param username
	 * @param firstname
	 * @param lastname
	 * @param password
	 * @return
	 * @throws UsernameAlreadyTakenException
	 */
	User createUser(String username, String firstname, String lastname,
			String password) throws UsernameAlreadyTakenException;

	/**
	 * Checks if a user with the given username can be created.
	 * 
	 * In particular, this method returns true iff there is no user already with
	 * the given username.
	 * 
	 * @param username
	 * @return
	 */
	boolean canCreateUser(String username);

}
