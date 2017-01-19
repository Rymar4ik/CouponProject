package couponsProject.DAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import couponsProject.ConnectionPool;
import couponsProject.Coupon;
import couponsProject.CouponType;
import couponsProject.Customer;
import util.SQLQueryRequest;

public class CustomerDBDAO implements CustomerDAO {

	private CouponDBDAO couponDBDAO;
	private Customer customer = new Customer();

	@Override
	public void createCustomer(Customer customer) {
		Connection connection = ConnectionPool.getInstance().getConnection();
		boolean flag = true;
		try {
			PreparedStatement statement = connection.prepareStatement(SQLQueryRequest.GET_ALL_CUSTOMER_NAMES);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (customer.getCustName().equals(resultSet.getString(2))) {
					flag = false;
					ConnectionPool.getInstance().returnConnection(connection);
					System.out.println("customer " + customer.getCustName() + " is already exists");
				}
			}
			if (flag) {
				statement = connection.prepareStatement(SQLQueryRequest.ADD_NEW_CUSTOMER_TO_DB);
				statement.setString(1, customer.getCustName());
				statement.setString(2, customer.getPassword());
				statement.executeUpdate();
				ConnectionPool.getInstance().returnConnection(connection);
				System.out.println("Customer " + customer.getCustName() + " added successfull");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void removeCustomer(Customer customer) {
		Connection connection = ConnectionPool.getInstance().getConnection();

		try {
			PreparedStatement statement = connection.prepareStatement(SQLQueryRequest.GET_ALL_CUSTOMER_ID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				if (customer.getId() == resultSet.getInt(1)) {
					try {
						statement = connection.prepareStatement(SQLQueryRequest.DELETE_CUSTOMER_BY_ID);
						statement.setLong(1, customer.getId());
						statement.executeUpdate();
						statement = connection
								.prepareStatement(SQLQueryRequest.REMOVE_CUSTOMER_FROM_CUSTOMER_COUPON_JOIN_TABLE);
						statement.setLong(1, customer.getId());
						statement.executeUpdate();
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("Customer " + customer.getCustName() + " deleted successfull");
				}
			}
			ConnectionPool.getInstance().returnConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void updateCustomer(Customer customer) {
		Connection connection = ConnectionPool.getInstance().getConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		try {
			PreparedStatement statement = connection.prepareStatement(SQLQueryRequest.GET_ALL_CUSTOMER_ID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				if (customer.getId() == resultSet.getInt(1)) {
					try {
						System.out.println("enter a new Customer name");
						statement = connection.prepareStatement(SQLQueryRequest.SET_NEW_CUSTOMER_NAME_BY_ID);
						String newName = reader.readLine();
						statement.setString(1, newName);
						statement.setLong(2, customer.getId());
						statement.executeUpdate();
						System.out.println("enter a new password");
						statement = connection.prepareStatement(SQLQueryRequest.SET_NEW_CUSTOMER_PASSWORD_BY_ID);
						String newPassword = reader.readLine();
						statement.setString(1, newPassword);
						statement.setLong(2, customer.getId());
						statement.executeUpdate();
						System.out.println("Customer updated successfull");
						ConnectionPool.getInstance().returnConnection(connection);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("This customer is not exist");
		ConnectionPool.getInstance().returnConnection(connection);
	}

	@Override
	public Customer getCustomer(long id) {
		Customer customer = new Customer();
		Connection connection = ConnectionPool.getInstance().getConnection();
		PreparedStatement statement;
		try {

			statement = connection.prepareStatement(SQLQueryRequest.GET_ALL_CUSTOMER_INFO_BY_ID);
			statement.setLong(1, id);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				customer.setId(resultSet.getInt(1));
				customer.setCustName(resultSet.getString(2));
				customer.setPassword(resultSet.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectionPool.getInstance().returnConnection(connection);
		return customer;
	}

	@Override
	public Collection<Customer> getAllCustomer() {
		Connection connection = ConnectionPool.getInstance().getConnection();
		Collection<Customer> collectionCustomer = new HashSet<Customer>();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(SQLQueryRequest.GET_ALL_CUSTOMER_ID);
			while (resultSet.next()) {
				collectionCustomer.add(getCustomer(resultSet.getLong(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectionPool.getInstance().returnConnection(connection);
		return collectionCustomer;
	}

	@Override
	public Collection<Coupon> getCoupons() {
		HashSet<Coupon> purchasedCoupons = new HashSet<>();
		Connection connection = ConnectionPool.getInstance().getConnection();
		try {
			PreparedStatement statement = connection
					.prepareStatement(SQLQueryRequest.GET_COUPON_FROM_CUSTOMER_COUPON_JOIN_TABLE_BY_CUSTOMER_ID);
			statement.setLong(1, customer.getId());
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				purchasedCoupons.add(couponDBDAO.getCoupon(resultSet.getLong(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return purchasedCoupons;

	}

	public HashSet<Coupon> getAllPurchasedCouponsByType(Customer customer, CouponType type) {
		HashSet<Coupon> allPurchasedCouponsByType = new HashSet<>();
		HashSet<Coupon> purchasedCoupons = (HashSet<Coupon>) customer.getCoupons();
		Iterator<Coupon> iterator = purchasedCoupons.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getType().equals(type)) {
				allPurchasedCouponsByType.add(iterator.next());
			}
		}
		return allPurchasedCouponsByType;

	}

	public HashSet<Coupon> getAllPurchasedCouponsByPrice(double price) {
		HashSet<Coupon> allPurchasedCouponsByPrice = new HashSet<>();
		HashSet<Coupon> purchasedCoupons = (HashSet<Coupon>) customer.getCoupons();
		Iterator<Coupon> iterator = purchasedCoupons.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getPrice() == price) {
				allPurchasedCouponsByPrice.add(iterator.next());
			}
		}
		return allPurchasedCouponsByPrice;
	}

	@Override
	public boolean login(String custName, String password) {
		Connection connection = ConnectionPool.getInstance().getConnection();
		PreparedStatement statement;

		try {
			statement = connection.prepareStatement(SQLQueryRequest.GET_CUSTOMER_BY_NAME_AND_PASSWORD);
			statement.setString(1, custName);
			statement.setString(2, password);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				System.out.println(resultSet.getLong(1));
				System.out.println(resultSet.getString(2));
				System.out.println(resultSet.getString(3));
				customer.setId(resultSet.getLong(1));
				customer.setCustName(resultSet.getString(2));
				customer.setPassword(resultSet.getString(3));
				statement = connection.prepareStatement(SQLQueryRequest.ADD_CUSTOMER_TO_CUSTOMER_COUPON_JOIN_TABLE);
				statement.setLong(1, customer.getId());
				ConnectionPool.getInstance().returnConnection(connection);
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectionPool.getInstance().returnConnection(connection);
		return false;
	}

	public void purchaseCoupon(Coupon coupon) {
		if (coupon.getId() != 0) {
			if (coupon.getAmount() > 0) {
				Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement statement = null;
				try {
					statement = connection.prepareStatement(SQLQueryRequest.SET_COUPON_AMOUNT_BY_ID);
					statement.setInt(1, coupon.getAmount() - 1);
					statement.setLong(2, customer.getId());
					statement.executeUpdate();
					coupon.setAmount(coupon.getAmount() - 1);
					statement = connection
							.prepareStatement(SQLQueryRequest.ADD_COUPON_TO_CUSTOMER_COUPON_JOIN_TABLE_BY_CUSTOMER_ID);
					statement.setLong(1, coupon.getId());
					statement.setLong(2, customer.getId());
					statement.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					ConnectionPool.getInstance().returnConnection(connection);
					try {
						statement.close();
					} catch (SQLException e) {
					}
				}
			} else
				System.out.println("There are no coupons of this company to purchase");
		} else
			System.out.println("This coupon isn't exist");
	}

}
