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
			PreparedStatement st = connection.prepareStatement(SQLQueryRequest.GET_ALL_COMPANY_NAMES);
			ResultSet set = st.executeQuery();
			
			
			while (set.next()) {
				if (company.getCompName().equals(set.getString(2))) {
					flag = false;
					System.out.println("company name " + company.getCompName() + " is already exists");
				}
			}
			st = connection.prepareStatement(SQLQueryRequest.GET_ALL_COMPANY_EMAILS);
			set = st.executeQuery();
			while (set.next()) {
				if (company.getEmail().equals(set.getString(4))) {
					flag = false;
					System.out.println("Company with email " + company.getEmail() + " already exists");
				}
			}
			if (flag) {
				st = connection.prepareStatement(SQLQueryRequest.ADD_NEW_COMPANY_TO_DB);
				st.setString(1, company.getCompName());
				st.setString(2, company.getPassword());
				st.setString(3, company.getEmail());
				st.executeUpdate();	
				st = connection.prepareStatement(SQLQueryRequest.ADD_COMPANY_TO_COMPANY_COUPON_JOIN_TABLE);
				st.setLong(1, company.getId());
				st.executeUpdate();
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
			PreparedStatement st = connection.prepareStatement(SQLQueryRequest.GET_ALL_COMPANY_ID);
			ResultSet resultSet = st.executeQuery();
			while (resultSet.next()) {
				if (company.getId() == resultSet.getInt(1)) {
					try {
						System.out.println("enter a new Company name");
						st = connection.prepareStatement(SQLQueryRequest.SET_NEW_COMPANY_NAME_BY_ID);
						String newName = reader.readLine();
						st.setString(1, newName);
						st.setLong(2, company.getId());
						st.executeUpdate();
						System.out.println("enter a new password");
						st = connection.prepareStatement(SQLQueryRequest.SET_NEW_COMPANY_PASSWORD_BY_ID);
						String newPassword = reader.readLine();
						st.setString(1, newPassword);
						st.setLong(2, company.getId());
						st.executeUpdate();
						st = connection.prepareStatement(SQLQueryRequest.SET_NEW_COMPANY_EMAIL_BY_ID);
						String newEmail = reader.readLine();
						st.setString(1, newEmail);
						st.setLong(2, company.getId());
						st.executeUpdate();
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
		PreparedStatement st;
		try {
			st = connection.prepareStatement(SQLQueryRequest.GET_COMPANY_BY_ID);
			st.setLong(1, id);
			ResultSet set = st.executeQuery();
			while (set.next()) {
				company.setId(set.getLong(1));
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
			PreparedStatement st = connection.prepareStatement(SQLQueryRequest.GET_ALL_COMPANY_ID);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				collectionCompany
						.add(getCompany(res.getLong(1)));
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
			PreparedStatement st = connection
					.prepareStatement(SQLQueryRequest.GET_COUPON_FROM_COMPANY_COUPON_JOIN_TABLE_BY_COMPANY_ID);
			st.setLong(1, company.getId());
			ResultSet set = st.executeQuery();
			while (set.next()) {
				allCompanyCoupons.add(couponDBDAO.getCoupon(set.getLong(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allCompanyCoupons;
	}

	@Override
	public boolean login(String name, String password) {
		Connection connection = ConnectionPool.getInstance().getConnection();
		PreparedStatement st;
		try {
			st = connection.prepareStatement(SQLQueryRequest.GET_COMPANY_BY_NAME_AND_PASSWORD);
			st.setString(1, name);
			st.setString(2, password);
			ResultSet set = st.executeQuery();
			while (set.next()) {
				company.setId(set.getLong(1));
				company.setCompName(set.getString(2));
				company.setPassword(set.getString(3));
				company.setEmail(set.getString(4));
				st = connection.prepareStatement(SQLQueryRequest.ADD_COMPANY_TO_COMPANY_COUPON_JOIN_TABLE);
				st.setLong(1, company.getId());
				st.executeUpdate();
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
			PreparedStatement st = connection.prepareStatement(SQLQueryRequest.GET_ALL_COMPANY_ID);
			ResultSet resultID = st.executeQuery();
			while (resultID.next()) {
				if (company.getId() == resultID.getInt(1)) {
					try {
						st = connection.prepareStatement(SQLQueryRequest.DELETE_COMPANY_BY_ID);
						st.setLong(1, company.getId());
						st.executeUpdate();
						st = connection
								.prepareStatement(SQLQueryRequest.REMOVE_COMPANY_FROM_COMPANY_COUPON_JOIN_TABLE);
						st.setLong(1, company.getId());
						st.executeUpdate();
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
			PreparedStatement st = connection.prepareStatement(SQLQueryRequest.ADD_COUPON_TO_COMPANY_COUPON_JOIN_TABLE_BY_COMPANY_ID);
			st.setLong(1, coupon.getId());
			st.setLong(2, company.getId());
			st.executeUpdate();
			couponDBDAO.createCoupon(coupon);
			ConnectionPool.getInstance().returnConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
