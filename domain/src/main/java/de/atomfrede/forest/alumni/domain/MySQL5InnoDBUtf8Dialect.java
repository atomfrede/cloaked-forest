package de.atomfrede.forest.alumni.domain;

import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 * Simple custom Inno Database Dialect using default UTF-8 Charset encoding.
 * 
 * @see MySQL5InnoDBDialect
 * @author fred
 *
 */
public class MySQL5InnoDBUtf8Dialect extends MySQL5InnoDBDialect {

	@Override
	public String getTableTypeString() {
		return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
	}
}
