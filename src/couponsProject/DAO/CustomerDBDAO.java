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
			PreparedStatement st = connection.prepareStatement(SQLQueryRequest.GET_ALL_CUSTOMER_NAMES);
			ResultSet resultNamesSet = st.executeQuery();

			while (resultNamesSet.next()) {
				if (customer.getCustName().equals(resultNamesSet.getString(2))) {
					flag = false;
					ConnectionPool.getInstance().returnConnection(connection);
					System.out.println("customer " + customer.getCustName() + " is already exists");
				}
			}
			if (flag) {
				st = connection.prepareStatement(SQLQueryRequest.ADD_NEW_CUSTOMER_TO_DB);
				st.setString(1, customer.getCustName());
				st.setString(2, customer.getPassword());
				st.executeUpdate();
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
			PreparedStatement st = connection.prepareStatement(SQLQueryRequest.GET_ALL_CUSTOMER_ID);
			ResultSet resultID = st.executeQuery();
			while (resultID.next()) {
				if (customer.getId() == resultID.getInt(1)) {
					try {
						st = connection.prepareStatement(SQLQueryRequest.DELETE_CUSTOMER_BY_ID);
						st.setLong(1, customer.getId());
						st.executeUpdate();
						st = connection
								.prepareStatement(SQLQueryRequest.REMOVE_CUSTOMER_FROM_CUSTOMER_COUPON_JOIN_TABLE);
						st.setLong(1, customer.getId());
						st.executeUpdate();
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
			PreparedStatement st = connection.prepareStatement(SQLQueryRequest.GET_ALL_CUSTOMER_ID);
			ResultSet resultSet = st.executeQuery();
			while (resultSet.next()) {
				if (customer.getId() == resultSet.getInt(1)) {
					try {
						System.out.println("enter a new Customer name");
						st = connection.prepareStatement(SQLQueryRequest.SET_NEW_CUSTOMER_NAME_BY_ID);
						String newName = reader.readLine();
						st.setString(1, newName);
						st.setLong(2, customer.getId());
						st.executeUpdate();
						System.out.println("enter a new password");
						st = connection.prepareStatement(SQLQueryRequest.SET_NEW_CUSTOMER_PASSWORD_BY_ID);
						String newPassword = reader.readLine();
						st.setString(1, newPassword);
						st.setLong(2, customer.getId());
						st.executeUpdate();
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
		PreparedStatement st;
		try {

			st = connection.prepareStatement(SQLQueryRequest.GET_ALL_CUSTOMER_INFO_BY_ID);
			st.setLong(1, id);
			ResultSet set = st.executeQuery();

			while (set.next()) {
				customer.setId(set.getInt(1));
				customer.setCustName(set.getString(2));
				customer.setPassword(set.getString(3));
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
			Statement st = connection.createStatement();
			ResultSet res = st.executeQuery(SQLQueryRequest.GET_ALL_CUSTOMER_ID);
			while (res.next()) {
				collectionCustomer.add(getCustomer(res.getLong(1)));
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
			PreparedStatement st = connection
					.prepareStatement(SQLQueryRequest.GET_COUPON_FROM_CUSTOMER_COUPON_JOIN_TABLE_BY_CUSTOMER_ID);
			st.setLong(1, customer.getId());
			ResultSet set = st.executeQuery();
			while (set.next()) {
				purchasedCoupons.add(couponDBDAO.getCoupon(set.getLong(1)));
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
		PreparedStatement st;

		try {
			st = connection.prepareStatement(SQLQueryRequest.GET_CUSTOMER_BY_NAME_AND_PASSWORD);
			st.setString(1, custName);
			st.setString(2, password);
			ResultSet set = st.executeQuery();
			while (set.next()) {
				System.out.println(set.getLong(1));
				System.out.println(set.getString(2));
				System.out.println(set.getString(3));
				customer.setId(set.getLong(1));
				customer.setCustName(set.getString(2));
				customer.setPassword(set.getString(3));
				st = connection.prepareStatement(SQLQueryRequest.ADD_CUSTOMER_TO_CUSTOMER_COUPON_JOIN_TABLE);
				st.setLong(1, customer.getId());
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
				if (coupon.getEndDate().getTime() < new Date().getTime()) {
					Connection connection = ConnectionPool.getInstance().getConnection();
					PreparedStatement st = null;
					try {
						st = connection.prepareStatement(SQLQueryRequest.SET_COUPON_AMOUNT_BY_ID);
						st.setInt(1, coupon.getAmount() - 1);
						st.setLong(2, customer.getId());
						st.executeUpdate();
						coupon.setAmount(coupon.getAmount() - 1);
						st = connection.prepareStatement(
								SQLQueryRequest.ADD_COUPON_TO_CUSTOMER_COUPON_JOIN_TABLE_BY_CUSTOMER_ID);
						st.setLong(1, coupon.getId());
						st.setLong(2, customer.getId());
						st.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					finally {
						ConnectionPool.getInstance().returnConnection(connection);
						try {
							st.close();
						} catch (SQLException e) {
						}
					}

				} else
					System.out.println("The date of this coupon are over");
			} else
				System.out.println("There are no coupons of this company to purchase");
		} else
			System.out.println("This coupon isn't exist");
	}

}
