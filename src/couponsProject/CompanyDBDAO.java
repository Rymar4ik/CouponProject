package couponsProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;

public class CompanyDBDAO implements CompanyDAO {

	public CompanyDBDAO() {

	}

	@Override
	public void createCompany(Company company) {
		Connection connection = ConnectionPool.getInstance().getConnection();
		boolean flag = true;
		try {
			Statement st = connection.createStatement();
			ResultSet resultNames = st.executeQuery("SELECT COMP_NAME FROM COUPONSYSTEM.COMPANY");
			Statement st1 = connection.createStatement();
			ResultSet resultEmails = st1.executeQuery("SELECT EMAIL FROM COUPONSYSTEM.COMPANY");
			while (resultEmails.next()) {
				if (company.getEmail().equals(resultEmails.getString("EMAIL"))) {
					flag = false;
					System.out.println("Company with email " + company.getEmail() + " already exists");
				}
			}
			while (resultNames.next()) {
				if (company.getCompName().equals(resultNames.getString("COMP_NAME"))) {
					flag = false;
					System.out.println("company name " + company.getCompName() + " is already exists");
				}
			}
			if (flag) {
				st.executeUpdate(
						"INSERT INTO COUPONSYSTEM.COMPANY(COMP_NAME, PASSWORD, EMAIL) VALUES(\"" + company.getCompName()
								+ "\",\"" + company.getPassword() + "\",\"" + company.getEmail() + "\")");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ConnectionPool.getInstance().returnConnection(connection);
	}

	@Override
	public void updateCompany(Company company) {
		Connection connection = ConnectionPool.getInstance().getConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		try {
			Statement st4id = connection.createStatement();
			ResultSet resultID = st4id.executeQuery("SELECT ID FROM COUPONSYSTEM.COMPANY");
			while (resultID.next()) {
				if (company.getId() == resultID.getInt(1)) {
					try {
						System.out.println("enter a new Company name");
						Statement st4name = connection.createStatement();
						String newName = reader.readLine();
						st4name.executeUpdate(
								"UPDATE COMPANY SET COMP_NAME = \"" + newName + "\" WHERE ID = " + company.getId());
						company.setCompName(newName);
						System.out.println("enter a new password");
						Statement st4password = connection.createStatement();
						String newPassword = reader.readLine();
						st4password.executeUpdate(
								"UPDATE COMPANY SET PASSWORD = \"" + newPassword + "\" WHERE ID = " + company.getId());
						company.setPassword(newPassword);
						System.out.println("enter a new email");
						Statement st4email = connection.createStatement();
						String newEmail = reader.readLine();
						st4email.executeUpdate(
								"UPDATE COMPANY SET EMAIL = \"" + newEmail + "\" WHERE ID = " + company.getId());
						company.setEmail(newEmail);
						System.out.println("Company updatet successfull");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ConnectionPool.getInstance().returnConnection(connection);
	}

	@Override
	public Company getCompany(long id) {
		Company company = new Company();
		Connection connection = ConnectionPool.getInstance().getConnection();
		Statement st;
		try {
			st = connection.createStatement();
			ResultSet set = st.executeQuery("SELECT * FROM COMPANY WHERE ID = \"" + id + "\"");
			while (set.next()) {
				company.setId(set.getInt(1));
				company.setCompName(set.getString(2));
				company.setPassword(set.getString(3));
				company.setEmail(set.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectionPool.getInstance().returnConnection(connection);
		return company;
	}

	@Override
	public Collection<Company> getAllCompanies() {
		Connection connection = ConnectionPool.getInstance().getConnection();
		Collection<Company> collectionCompany = new HashSet<Company>();
		try {
			Statement st = connection.createStatement();
			ResultSet res = st.executeQuery("SELECT * FROM COMPANY");
			while (res.next()) {
				collectionCompany
						.add(new Company(res.getLong(1), res.getString(2), res.getString(3), res.getString(4)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectionPool.getInstance().returnConnection(connection);
		return collectionCompany;

	}

	@Override
	public Collection<Coupon> getAllCoupons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean login(String name, String password) {
		Connection connection = ConnectionPool.getInstance().getConnection();
		Statement st;
		try {
			st = connection.createStatement();
			ResultSet set = st.executeQuery(
					"SELECT * FROM COMPANY WHERE COMP_NAME = \"" + name + "\" && PASSWORD = \"" + password + "\"");
			while (set.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectionPool.getInstance().returnConnection(connection);
		return false;

	}

}
