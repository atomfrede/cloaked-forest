package de.atomfrede.forest.alumni.domain.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import de.atomfrede.forest.alumni.domain.entity.AbstractEntity;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = {
		"username", "email" }))
public class User extends AbstractEntity {

	private static final long serialVersionUID = -8695856794737512171L;

	public static String cryptPass(String password) {
		// easy encrypt
		return DigestUtils.sha256Hex(password);
	}

	@GenericGenerator(name = "UserIdGenerator", strategy = "org.hibernate.id.MultipleHiLoPerTableGenerator", parameters = {
			@Parameter(name = "table", value = "IdentityGenerator"),
			@Parameter(name = "primary_key_column", value = "sequence_name"),
			@Parameter(name = "primary_key_value", value = "User"),
			@Parameter(name = "value_column", value = "next_hi_value"),
			@Parameter(name = "primary_key_length", value = "100"),
			@Parameter(name = "max_lo", value = "1000") })
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "UserIdGenerator")
	protected Long id;

	@Column(name = "firstname")
	protected String firstname;

	@Column(name = "lastname")
	protected String lastname;

	@Column(name = "username")
	protected String username;

	@Column(name = "email")
	protected String email;

	@Column(name = "password")
	protected String password;

	@Override
	public Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = User.cryptPass(password);
	}

	public boolean isPassword(String plainText) {
		if (StringUtils.isEmpty(plainText)) {
			return false;
		}
		// don't encrypt password because after
		// Model.setPassword in form already encrypted
		return password.equals(plainText);
	}

	public boolean isPasswordPlain(String plainPassword) {
		if (StringUtils.isEmpty(plainPassword)) {
			return false;
		}

		return password.equals(cryptPass(plainPassword));
	}

	// @Override
	// public User toJsonTransferable() {
	// User clone = new User();
	// clone.setFirstname(firstname);
	// clone.setLastname(lastname);
	// clone.setUsername(username);
	// clone.setEmail(email);
	// clone.setId(getId());
	// return clone;
	// }

}
