package de.atomfrede.forest.alumni.domain.dao.contact;

import org.springframework.stereotype.Repository;

import de.atomfrede.forest.alumni.domain.dao.AbstractDAO;
import de.atomfrede.forest.alumni.domain.entity.contact.ContactData;

@Repository(value = "contactDao")
public class ContactDaoImpl extends AbstractDAO<ContactData> implements
		ContactDataDao {

	public ContactDaoImpl() {
		super(ContactData.class);
	}

	

}
