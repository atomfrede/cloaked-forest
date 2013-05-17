package de.atomfrede.forest.alumni.domain.converter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import au.com.bytecode.opencsv.CSVReader;
import de.atomfrede.forest.alumni.domain.dao.activity.ActivityDao;
import de.atomfrede.forest.alumni.domain.dao.company.CompanyDao;
import de.atomfrede.forest.alumni.domain.dao.contact.ContactDataDao;
import de.atomfrede.forest.alumni.domain.dao.degree.DegreeDao;
import de.atomfrede.forest.alumni.domain.dao.department.DepartmentDao;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.dao.sector.SectorDao;
import de.atomfrede.forest.alumni.domain.entity.activity.Activity;
import de.atomfrede.forest.alumni.domain.entity.company.Company;
import de.atomfrede.forest.alumni.domain.entity.contact.ContactData;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.domain.entity.department.Department;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;

public class ConverterExecutor {

	private final Log log = LogFactory.getLog(ConverterExecutor.class);

	@Autowired
	private DegreeDao degreeDao;
	@Autowired
	private SectorDao sectorDao;
	@Autowired
	private ActivityDao activityDao;
	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private ContactDataDao contactDao;

	private Map<Long, Degree> degreeId_degree = new HashMap<Long, Degree>();

	private Map<Long, Activity> activityId_activity = new HashMap<Long, Activity>();

	private Map<Long, Member> memberId_member = new HashMap<Long, Member>();

	private Map<Long, Sector> sectorId_sector = new HashMap<Long, Sector>();

	private Map<Long, Department> departmentId_department = new HashMap<Long, Department>();

	private Map<Long, Company> companyId_company = new HashMap<>();

	public ConverterExecutor() {
		URL resource = ConverterExecutor.class
				.getResource("../../../../../../domain-context.xml");
		System.out.println("URL " + resource);
		new ApplicationContextLoader().load(this, resource.toString());
	}

	public void startConvert() {
		log.info("Starting Conver...");
		readtblabschluss();
		readtblbranche();
		readtbltaetigkeit();
		readtblfirma();
		readtblabteilung();
		readtblmitglied();
		readtblrelmitgliedtaetigkeit();
		readtblrelmitgliedbranche();
		readtbladressen();
	}

	private void readtblabschluss() {
		try {
			CSVReader reader = new CSVReader(new InputStreamReader(
					ConverterExecutor.class
							.getResourceAsStream("tblabschluss.csv")));
			List<String[]> input = reader.readAll();

			for (String[] values : input) {
				String id = values[0];
				String longname = values[1];
				String shortname = values[2];

				Degree deg = new Degree();
				deg.setId(Long.parseLong(id));
				deg.setShortForm(shortname);
				deg.setTitle(longname);
				degreeDao.persist(deg);

				degreeId_degree.put(deg.getId(), deg);
			}

			reader.close();

		} catch (RuntimeException e) {
			throw e;
		} catch (IOException ioe) {

		}
		// CSVReader reader = new CSVReader(new )

	}

	private void readtblbranche() {
		try {
			CSVReader reader = new CSVReader(new InputStreamReader(
					ConverterExecutor.class
							.getResourceAsStream("tblbranche.csv")));
			List<String[]> input = reader.readAll();

			for (String[] values : input) {
				String id = values[0];
				String branche = values[1];

				Sector sec = new Sector();
				sec.setId(Long.parseLong(id));
				sec.setSector(branche);
				sectorDao.persist(sec);

				sectorId_sector.put(sec.getId(), sec);
			}

			reader.close();
		} catch (RuntimeException e) {

		} catch (IOException ioe) {

		}
	}

	private void readtbltaetigkeit() {
		try {
			CSVReader reader = new CSVReader(new InputStreamReader(
					ConverterExecutor.class
							.getResourceAsStream("tbltaetigkeit.csv")));
			List<String[]> input = reader.readAll();

			for (String[] values : input) {
				String id = values[0];
				String taetigkeit = values[1];

				Activity act = new Activity();
				act.setId(Long.parseLong(id));
				act.setActivity(taetigkeit);
				activityDao.persist(act);

				activityId_activity.put(act.getId(), act);
			}

			reader.close();
		} catch (RuntimeException e) {

		} catch (IOException ioe) {

		}
	}

	private void readtblfirma() {
		try {
			CSVReader reader = new CSVReader(
					new InputStreamReader(
							ConverterExecutor.class
									.getResourceAsStream("tblfirma.csv")));
			List<String[]> input = reader.readAll();

			for (String[] values : input) {
				String id = values[0];
				String firma = values[1];
				String brancheId = values[2];
				String size = values[3];

				Company comp = new Company();
				comp.setId(Long.parseLong(id));
				comp.setCompany(firma);
				comp.setSize(size);

				Sector sec = sectorDao.findById(Long.parseLong(brancheId));
				comp.setSector(sec);

				companyDao.persist(comp);

				companyId_company.put(comp.getId(), comp);

			}

			reader.close();
		} catch (RuntimeException e) {

		} catch (IOException ioe) {

		}
	}

	private void readtblabteilung() {
		try {
			CSVReader reader = new CSVReader(new InputStreamReader(
					ConverterExecutor.class
							.getResourceAsStream("tblabteilung.csv")));
			List<String[]> input = reader.readAll();

			Map<Long, List<Department>> comp_departments = new HashMap<>();

			for (String[] values : input) {
				String id = values[0];
				String abteilung = values[1];
				String companyId = values[2];
				String strasse = values[3];
				String hausnr = values[4];
				String zusatz = values[5];
				String plz = values[6];
				String ort = values[7];
				String land = values[8];
				String internet = values[9];

				Long companyIdLong = Long.parseLong(companyId);
				// Company comp =
				// companyDao.findById(Long.parseLong(companyId));

				Department dep = new Department();
				dep.setId(Long.parseLong(id));
				dep.setDepartment(abteilung);
				dep.setAddon(zusatz);
				dep.setCountry(land);
				dep.setInternet(internet);
				dep.setNumber(hausnr);
				dep.setPostCode(plz);
				dep.setStreet(strasse);
				dep.setTown(ort);

				departmentDao.persist(dep);

				departmentId_department.put(dep.getId(), dep);

				if (comp_departments.get(companyIdLong) == null) {
					ArrayList<Department> departments = new ArrayList<Department>();
					departments.add(dep);
					comp_departments.put(companyIdLong, departments);
				} else {
					comp_departments.get(companyIdLong).add(dep);
				}
			}

			reader.close();

			for (Long compId : comp_departments.keySet()) {
				Company company = companyDao.findById(compId);
				company.setDepartments(comp_departments.get(compId));
				companyDao.persist(company);

				for (Department dep : comp_departments.get(compId)) {
					dep.setCompany(company);
					departmentDao.persist(dep);
				}
			}
		} catch (RuntimeException e) {

		} catch (IOException ioe) {

		}
	}

	private void readtblmitglied() {
		try {
			CSVReader reader = new CSVReader(new InputStreamReader(
					ConverterExecutor.class
							.getResourceAsStream("tblmitglied.csv")));
			List<String[]> input = reader.readAll();
			List<Member> members = new ArrayList<Member>();

			int line = 0;
			for (String[] values : input) {
				line++;
				String id = values[0];
				String anrede = values[1];
				String title = values[2];
				String nachname = values[3];
				String vorname = values[4];
				String abschlussId = values[5];
				String abschlussjahr0 = values[6];
				String abschlussfach = values[7];
				String abschlussjahr = values[8];
				String beitrittsdatum = values[9];

				// 2012-09-13 00:00:0
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:s");
				Date entryDate = null;
				if (beitrittsdatum != null) {
					entryDate = format.parse(beitrittsdatum);
				}

				Degree degree = null;
				if (abschlussId != null) {
					try {
						degree = degreeId_degree.get(Long
								.parseLong(abschlussId));
					} catch (NumberFormatException nfe) {
						System.out.println("NFE");
					}
				}

				Member member = new Member();
				member.setId(Long.parseLong(id));
				member.setDegree(degree);
				member.setEntryDate(entryDate);
				member.setFirstname(vorname);
				member.setLastname(nachname);
				member.setProfession(abschlussfach);
				member.setSalutation(anrede);
				member.setTitle(title);
				member.setYearOfGraduation(abschlussjahr);
				members.add(member);

				memberId_member.put(member.getId(), member);
			}

			memberDao.persistAll(members);
			reader.close();

		} catch (RuntimeException e) {
			e.printStackTrace();
			System.out.println("Error " + e);
			throw e;
		} catch (IOException ioe) {

		}
	}

	private void readtblrelmitgliedtaetigkeit() {
		Map<Long, Set<Activity>> memberId_activites = new HashMap<Long, Set<Activity>>();
		List<Member> members = new ArrayList<Member>();
		try {
			CSVReader reader = new CSVReader(
					new InputStreamReader(
							ConverterExecutor.class
									.getResourceAsStream("tblrelmitgliedtaetigkeit.csv")));
			List<String[]> input = reader.readAll();
			int line = 0;
			for (String[] values : input) {
				System.out.println("Reading line " + line);
				line++;
				String mitgliedId = values[0];
				String taetigkeitId = values[1];

				System.out.println("MitgliedId " + mitgliedId);
				System.out.println("TätigkeitId " + taetigkeitId);

				if (memberId_activites.get(Long.parseLong(mitgliedId)) == null) {
					Set<Activity> activites = new HashSet<Activity>();
					activites.add(activityId_activity.get(Long
							.parseLong(taetigkeitId)));
					memberId_activites.put(Long.parseLong(mitgliedId),
							activites);
				} else {
					memberId_activites.get(Long.parseLong(mitgliedId)).add(
							activityId_activity.get(Long
									.parseLong(taetigkeitId)));
				}

			}

			// memberDao.persistAll(members);
			reader.close();
			System.out.println("Before Persiting");
			for (Long memberId : memberId_activites.keySet()) {
				Member member = memberId_member.get(memberId);
				member.setActivities(memberId_activites.get(memberId));
				members.add(member);
			}

			memberDao.persistAll(members);

		} catch (RuntimeException e) {
			e.printStackTrace();
			System.out.println("Error " + e);
			throw e;
		} catch (IOException ioe) {

		}
	}

	private void readtblrelmitgliedbranche() {
		List<Member> membersDb = memberDao.findAll();
		Map<Long, Member> cMemberId_member = new HashMap<Long, Member>();

		for (Member mem : membersDb) {
			cMemberId_member.put(mem.getId(), mem);
		}

		try {
			membersDb.clear();
			CSVReader reader = new CSVReader(new InputStreamReader(
					ConverterExecutor.class
							.getResourceAsStream("tblrelmitgliedbranche.csv")));
			List<String[]> input = reader.readAll();
			for (String[] values : input) {
				String mitgliedId = values[0];
				String brancheId = values[1];
				String abteilungId = values[2];

				Member mem = cMemberId_member.get(Long.parseLong(mitgliedId));
				mem.setSector(sectorId_sector.get(Long.parseLong(brancheId)));
				try {
					mem.setDepartment(departmentId_department.get(Long
							.parseLong(abteilungId)));
				} catch (NumberFormatException nfe) {
					System.out.println("NFE");
				}

				membersDb.add(mem);

			}

			reader.close();
			System.out.println("Ready with Branche und Abteilung.");

			memberDao.persistAll(membersDb);

		} catch (RuntimeException e) {
			e.printStackTrace();
			System.out.println("Error " + e);
			throw e;
		} catch (IOException ioe) {

		}
	}

	private void readtbladressen() {
		List<Member> membersDb = memberDao.findAll();
		List<Department> departmentsDb = departmentDao.findAll();
		List<ContactData> contactData = new ArrayList<ContactData>();

		Map<Long, Member> cMemberId_member = new HashMap<Long, Member>();
		Map<Long, Department> cDepartmentId_department = new HashMap<Long, Department>();

		for (Member mem : membersDb) {
			cMemberId_member.put(mem.getId(), mem);
		}

		for (Department dep : departmentsDb) {
			cDepartmentId_department.put(dep.getId(), dep);
		}

		try {
			membersDb.clear();
			CSVReader reader = new CSVReader(new InputStreamReader(
					ConverterExecutor.class
							.getResourceAsStream("tbladressen.csv")));
			List<String[]> input = reader.readAll();
			for (String[] values : input) {
				String mitgliedId = values[0];
				String strasse = values[1];
				String hausNr = values[2];
				String zusatz = values[3];
				String plz = values[4];
				String ort = values[5];
				String land = values[6];
				String telefon = values[7];
				String fax = values[8];
				String mobile = values[9];
				String email = values[10];
				String internet = values[11];
				String abteilungsId = values[12];
				String telefonD = values[13];
				String faxD = values[14];
				String mobileD = values[15];
				String emailD = values[16];
				String internetD = values[17];

				ContactData contact = new ContactData();
				contact.setAddon(zusatz);
				contact.setCountry(land);
				try {
					Department dep = cDepartmentId_department.get(Long
							.parseLong(abteilungsId));
					if (dep != null) {
						System.out.println("Setting Department with ID "
								+ dep.getId() + " into contact data...");
					}
					contact.setDepartment(dep);
				} catch (NumberFormatException nfe) {
					System.out.println("NFE");
				}
				contact.setEmail(email);
				contact.setEmailD(emailD);
				contact.setFax(fax);
				contact.setFaxD(faxD);
				contact.setInternet(internet);
				contact.setInternetD(internetD);
				contact.setMobile(mobile);
				contact.setMobileD(mobileD);
				contact.setPhone(telefon);
				contact.setPhoneD(telefonD);
				contact.setPostCode(plz);
				contact.setStreet(strasse);
				contact.setTown(ort);
				contact.setNumber(hausNr);

				contactDao.persist(contact);

				Member mem = cMemberId_member.get(Long.parseLong(mitgliedId));
				mem.setContactData(contact);
				if (contact.getDepartment() != null) {
					ContactData data = contactDao.findById(contact.getId());
					mem.setDepartment(contact.getDepartment());
					companyDao.findById(contact.getDepartment().getCompany()
							.getId());
					System.out.println("Company "
							+ contact.getDepartment().getCompany().getId());

					mem.setCompany(companyId_company.get(contact
							.getDepartment().getCompany().getId()));
				}
				contactData.add(contact);
				membersDb.add(mem);

				memberDao.persist(mem);

			}

			reader.close();
			System.out.println("Ready with Adressen");

			// memberDao.persistAll(membersDb);

		} catch (RuntimeException e) {
			e.printStackTrace();
			System.out.println("Error " + e);
		} catch (IOException ioe) {
			// TODO: handle exception
		}
	}
}
