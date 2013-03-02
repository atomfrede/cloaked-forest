package de.atomfrede.forest.alumni.application.wicket.security;

public interface IUserSession<T> {

	T getUser();

	void setUser(T user);
}
