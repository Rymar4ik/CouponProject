package couponsProject.DAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import couponsProject.Company;
import couponsProject.ConnectionPool;
import couponsProject.Coupon;
import util.SQLQueryRequest;

public class CompanyDBDAO implements CompanyDAO {
	Company company = new Company();
	CouponDBDAO couponDBDAO;
	public CompanyDBDAO() {

	}

	@Override
	public void createCompany(Company company) {
		Connection connection = ConnectionPool.getInstance().getConnection();
		boolean flag = true;
		try {
			PreparedStatement statement = connection.prepareStatement(SQLQueryRequest.GET_ALL_COMPANY_NAMES);
			ResultSet resultSet = statement.executeQuery();
			
			
			while (resultSet.next()) {
				if (company.getCompName().equals(resultSet.getString(1))) {
					flag = false;
					System.out.println("company name " + company.getCompName() + " is already exists");
				}
			}
			statement = connection.prepareStatement(SQLQueryRequest.GET_ALL_COMPANY_EMAILS);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				if (company.getEmail().equals(resultSet.getString(1))) {
					flag = false;
					System.out.println("Company with email " + company.getEmail() + " already exists");
				}
			}
			if (flag) {
				statement = connection.prepareStatement(SQLQueryRequest.ADD_NEW_COMPANY_TO_DB);
				statement.setString(1, company.getCompName());
				statement.setString(2, company.getPassword());
				statement.setString(3, company.getEmail());
				statement.executeUpdate();	
				statement = connection.prepareStatement(SQLQueryRequest.GET_COMPANY_ID_BY_NAME);
				statement.setString(1, company.getCompName());
				ResultSet id = statement.executeQuery();
				while (id.next()) {
				company.setId(Long.parseLong(id.getString(1)));
				}
				statement = connection.prepareStatement(SQLQueryRequest.ADD_COMPANY_TO_COMPANY_COUPON_JOIN_TABLE);
				statement.setLong(1, company.getId());
				statement.executeUpdate();
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
			PreparedStatement statement = connection.prepareStatement(SQLQueryRequest.GET_ALL_COMPANY_ID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				if (company.getId() == resultSet.getInt(1)) {
					try {
						System.out.println("enter a new Company name");
						statement = connection.prepareStatement(SQLQueryRequest.SET_NEW_COMPANY_NAME_BY_ID);
						String newName = reader.readLine();
						statement.setString(1, newName);
						statement.setLong(2, company.getId());
						statement.executeUpdate();
						System.out.println("enter a new password");
						statement = connection.prepareStatement(SQLQueryRequest.SET_NEW_COMPANY_PASSWORD_BY_ID);
						String newPassword = reader.readLine();
						statement.setString(1, newPassword);
						statement.setLong(2, company.getId());
						statement.executeUpdate();
						statement = connection.prepareStatement(SQLQueryRequest.SET_NEW_COMPANY_EMAIL_BY_ID);
						String newEmail = reader.readLine();
						statement.setString(1, newEmail);
						statement.setLong(2, company.getId());
						statement.executeUpdate();
						System.out.println("Company updated successfull");
						ConnectionPool.getInstance().returnConnection(connection);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("This company is not exist");
		ConnectionPool.getInstance().returnConnection(connection);
	}

	@Override
	public Company getCompany(long id) {
		Company company = new Company();
		Connection connection = ConnectionPool.getInstance().getConnection();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(SQLQueryRequest.GET_COMPANY_BY_ID);
			statement.setLong(1, id);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				company.setId(resultSet.getLong(1));
				company.setCompName(resultSet.getString(2));
				company.setPassword(resultSet.getString(3));
				company.setEmail(resultSet.getString(4));
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
			PreparedStatement statement = connection.prepareStatement(SQLQueryRequest.GET_ALL_COMPANY_ID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				collectionCompany
						.add(getCompany(resultSet.getLong(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectionPool.getInstance().returnConnection(connection);
		return collectionCompany;

	}

	@Override
	public Collection<Coupon> getAllCoupons() {
		HashSet<Coupon> allCompanyCoupons = new HashSet<>();
		Connection connection = ConnectionPool.getInstance().getConnection();
		try {
			PreparedStatement statement = connection
					.prepareStatement(SQLQueryRequest.GET_COUPON_FROM_COMPANY_COUPON_JOIN_TABLE_BY_COMPANY_ID);
			statement.setLong(1, company.getId());
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				allCompanyCoupons.add(couponDBDAO.getCoupon(resultSet.getLong(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allCompanyCoupons;
	}

	@Override
	public boolean login(String name, String password) {
		Connection connection = ConnectionPool.getInstance().getConnection();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(SQLQueryRequest.GET_COMPANY_BY_NAME_AND_PASSWORD);
			statement.setString(1, name);
			statement.setString(2, password);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				company.setId(resultSet.getLong(1));
				company.setCompName(resultSet.getString(2));
				company.setPassword(resultSet.getString(3));
				company.setEmail(resultSet.getString(4));
				statement = connection.prepareStatement(SQLQueryRequest.ADD_COMPANY_TO_COMPANY_COUPON_JOIN_TABLE);
				statement.setLong(1, company.getId());
				statement.executeUpdate();
				ConnectionPool.getInstance().returnConnection(connection);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectionPool.getInstance().returnConnection(connection);
		return false;

	}

	@Override
	public void removeCompany(Company company) {
		Connection connection = ConnectionPool.getInstance().getConnection();

		try {
			PreparedStatement statement = connection.prepareStatement(SQLQueryRequest.GET_ALL_COMPANY_ID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				if (company.getId() == resultSet.getInt(1)) {
					try {
						statement = connection.prepareStatement(SQLQueryRequest.DELETE_COMPANY_BY_ID);
						statement.setLong(1, company.getId());
						statement.executeUpdate();
						statement = connection
								.prepareStatement(SQLQueryRequest.REMOVE_COMPANY_FROM_COMPANY_COUPON_JOIN_TABLE);
						statement.setLong(1, company.getId());
						statement.executeUpdate();
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("Company " + company.getCompName() + " deleted successfull");
				}
			}
			ConnectionPool.getInstance().returnConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createCoupon(Coupon coupon) {
		Connection connection = ConnectionPool.getInstance().getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(SQLQueryRequest.ADD_COUPON_TO_COMPANY_COUPON_JOIN_TABLE_BY_COMPANY_ID);
			statement.setLong(1, coupon.getId());
			statement.setLong(2, company.getId());
			statement.executeUpdate();
			couponDBDAO.createCoupon(coupon);
			ConnectionPool.getInstance().returnConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
