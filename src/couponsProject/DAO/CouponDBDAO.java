package couponsProject.DAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;
import java.sql.Date;
import java.util.HashSet;

import couponsProject.ConnectionPool;
import couponsProject.Coupon;
import couponsProject.CouponType;
import couponsProject.StringDateConvertor;
import util.SQLQueryRequest;

public class CouponDBDAO implements CouponDAO {
	public CouponDBDAO() {

	}

	@Override
	public void createCoupon(Coupon coupon) {
		Connection connection = ConnectionPool.getInstance().getConnection();
		boolean flag = true;
		try {
			PreparedStatement st = connection.prepareStatement(SQLQueryRequest.GET_ALL_COUPON_TITLES);
			ResultSet set = st.executeQuery();

			while (set.next()) {
				if (coupon.getTitle().equals(set.getString(2))) {
					flag = false;
					ConnectionPool.getInstance().returnConnection(connection);
					System.out.println("Coupon title " + coupon.getTitle() + " is already exists");
				}
			}
			if (flag) {
				st = connection.prepareStatement(SQLQueryRequest.ADD_NEW_COUPON_TO_DB);
				st.setString(1, coupon.getTitle());
				st.setDate(2, StringDateConvertor.convert(coupon.getStartDate().toString()));
				st.setDate(3, StringDateConvertor.convert(coupon.getEndDate().toString()));
				st.setInt(4, coupon.getAmount());
				st.setString(5, coupon.getType().toString());
				st.setString(6, coupon.getMessage());
				st.setDouble(7, coupon.getPrice());
				st.setString(8, coupon.getImage());
				st.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		ConnectionPool.getInstance().returnConnection(connection);
		System.out.println("Coupon added successfull");
	}

	@Override
	public void removeCoupon(Coupon coupon) {
		Connection connection = ConnectionPool.getInstance().getConnection();

		try {
			PreparedStatement st = connection.prepareStatement(SQLQueryRequest.REMOVE_COUPON_BY_ID);
			st.setLong(1, coupon.getId());
			st.executeUpdate();
			st = connection.prepareStatement(SQLQueryRequest.REMOVE_COUPON_FROM_COMPANY_COUPON_BY_COUPON_ID);
			st.setLong(1, coupon.getId());
			st.executeUpdate();
			st = connection.prepareStatement(SQLQueryRequest.REMOVE_COUPON_FROM_CUSTOMER_COUPON_BY_COUPON_ID);
			st.setLong(1, coupon.getId());
			st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ConnectionPool.getInstance().returnConnection(connection);
	}

	@Override
	public void updateCoupon(Coupon coupon) {
		Connection connection = ConnectionPool.getInstance().getConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		try {
			PreparedStatement st = connection.prepareStatement(SQLQueryRequest.GET_COUPON_BY_ID);
			ResultSet set = st.executeQuery();
			while (set.next()) {
				if (coupon.getId() == set.getInt(1)) {
					try {
						System.out.println("enter a new Coupon title");
						st = connection.prepareStatement(SQLQueryRequest.SET_COUPON_TITLE_BY_ID);
						String newTitle = reader.readLine();
						st.setString(1, newTitle);
						st.setLong(2, coupon.getId());
						st.executeUpdate();
						coupon.setTitle(newTitle);

						System.out.println("enter a new start date (YYYY-MM-DD)");
						st = connection.prepareStatement(SQLQueryRequest.SET_COUPON_START_DATE_BY_ID);
						Date newStartDate = StringDateConvertor.convert(reader.readLine());
						st.setDate(1, newStartDate);
						st.setLong(2, coupon.getId());
						st.executeUpdate();
						coupon.setStartDate(newStartDate);

						System.out.println("enter a new end date (YYYY-MM-DD)");
						st = connection.prepareStatement(SQLQueryRequest.SET_COUPON_END_DATE_BY_ID);
						Date newEndDate = StringDateConvertor.convert(reader.readLine());
						st.setDate(1, newEndDate);
						st.setLong(2, coupon.getId());
						st.executeUpdate();
						coupon.setEndDate(newEndDate);

						System.out.println("enter a new amount");
						st = connection.prepareStatement(SQLQueryRequest.SET_COUPON_AMOUNT_BY_ID);
						int newAmount = Integer.parseInt(reader.readLine());
						st.setInt(1, newAmount);
						st.setLong(2, coupon.getId());
						st.executeUpdate();
						coupon.setAmount(newAmount);

						System.out.println("enter a new coupon type");
						st = connection.prepareStatement(SQLQueryRequest.SET_COUPON_TYPE_BY_ID);
						String newType = reader.readLine();
						st.setString(1, newType);
						st.setLong(2, coupon.getId());
						st.executeUpdate();
						coupon.setType(CouponType.valueOf(newType));

						System.out.println("enter a new message");
						st = connection.prepareStatement(SQLQueryRequest.SET_COUPON_MESSAGE_BY_ID);
						String newMessage = reader.readLine();
						st.setString(1, newMessage);
						st.setLong(2, coupon.getId());
						st.executeUpdate();
						coupon.setMessage(newMessage);

						System.out.println("enter a new price");
						st = connection.prepareStatement(SQLQueryRequest.SET_COUPON_PRICE_BY_ID);
						Double newPrice = Double.parseDouble(reader.readLine());
						st.setDouble(1, newPrice);
						st.setLong(2, coupon.getId());
						st.executeUpdate();
						coupon.setPrice(newPrice);

						System.out.println("add a new image");
						st = connection.prepareStatement(SQLQueryRequest.SET_COUPON_IMAGE_BY_ID);
						String newImage = reader.readLine();
						st.setString(1, newImage);
						st.setLong(2, coupon.getId());

						System.out.println("Coupon updated successfull");
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParseException e) {
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
	public Coupon getCoupon(long id) {
		Coupon coupon = new Coupon();
		Connection connection = ConnectionPool.getInstance().getConnection();
		PreparedStatement st;
		try {
			st = connection.prepareStatement(SQLQueryRequest.GET_COUPON_BY_ID);
			st.setLong(1, id);
			ResultSet set = st.executeQuery();

			while (set.next()) {
				coupon.setId(set.getLong(1));
				coupon.setTitle(set.getString(2));
				coupon.setStartDate(StringDateConvertor.convert(set.getString(3)));
				coupon.setEndDate(StringDateConvertor.convert(set.getString(4)));
				coupon.setAmount(set.getInt(5));
				coupon.setType(CouponType.valueOf(set.getString(6)));
				coupon.setMessage(set.getString(7));
				coupon.setPrice(set.getDouble(8));
				coupon.setImage(set.getString(9));
				ConnectionPool.getInstance().returnConnection(connection);
				return coupon;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ConnectionPool.getInstance().returnConnection(connection);
		return null;
	}

	@Override
	public HashSet<Coupon> getAllCoupon() {
		Connection connection = ConnectionPool.getInstance().getConnection();
		HashSet<Coupon> collectionCoupon = new HashSet<Coupon>();
		try {
			PreparedStatement st = connection.prepareStatement(SQLQueryRequest.GET_ALL_COUPON_ID);
			ResultSet set = st.executeQuery();
			while (set.next()) {
				collectionCoupon.add(getCoupon(set.getLong(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectionPool.getInstance().returnConnection(connection);
		return collectionCoupon;
	}

	@Override
	public HashSet<Coupon> getCouponByType(CouponType couponType) {
		Connection connection = ConnectionPool.getInstance().getConnection();
		HashSet<Coupon> collectionCoupon = new HashSet<Coupon>();
		try {
			PreparedStatement st = connection.prepareStatement(SQLQueryRequest.GET_ALL_COUPON_ID_BY_TYPE);
			ResultSet set = st.executeQuery();
			while (set.next()) {
				collectionCoupon.add(getCoupon(set.getLong(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectionPool.getInstance().returnConnection(connection);
		return collectionCoupon;
	}

}
