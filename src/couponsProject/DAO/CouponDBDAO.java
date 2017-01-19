package couponsProject.DAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.sql.Date;
import java.util.HashSet;

import couponsProject.ConnectionPool;
import couponsProject.Coupon;
import couponsProject.CouponType;
import util.SQLQueryRequest;
import util.StringDateConvertor;

public class CouponDBDAO implements CouponDAO {
	public CouponDBDAO() {

	}

	@Override
	public void createCoupon(Coupon coupon) {
		Connection connection = ConnectionPool.getInstance().getConnection();
		boolean flag = true;
		try {
			PreparedStatement statement = connection.prepareStatement(SQLQueryRequest.GET_ALL_COUPON_TITLES);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (coupon.getTitle().equals(resultSet.getString(2))) {
					flag = false;
					ConnectionPool.getInstance().returnConnection(connection);
					System.out.println("Coupon title " + coupon.getTitle() + " is already exists");
				}
			}
			if (flag) {
				statement = connection.prepareStatement(SQLQueryRequest.ADD_NEW_COUPON_TO_DB);
				statement.setString(1, coupon.getTitle());
				statement.setDate(2, StringDateConvertor.convert(coupon.getStartDate().toString()));
				statement.setDate(3, StringDateConvertor.convert(coupon.getEndDate().toString()));
				statement.setInt(4, coupon.getAmount());
				statement.setString(5, coupon.getType().toString());
				statement.setString(6, coupon.getMessage());
				statement.setDouble(7, coupon.getPrice());
				statement.setString(8, coupon.getImage());
				statement.executeUpdate();
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
			PreparedStatement statement = connection.prepareStatement(SQLQueryRequest.REMOVE_COUPON_BY_ID);
			statement.setLong(1, coupon.getId());
			statement.executeUpdate();
			statement = connection.prepareStatement(SQLQueryRequest.REMOVE_COUPON_FROM_COMPANY_COUPON_BY_COUPON_ID);
			statement.setLong(1, coupon.getId());
			statement.executeUpdate();
			statement = connection.prepareStatement(SQLQueryRequest.REMOVE_COUPON_FROM_CUSTOMER_COUPON_BY_COUPON_ID);
			statement.setLong(1, coupon.getId());
			statement.executeUpdate();
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
			PreparedStatement statement = connection.prepareStatement(SQLQueryRequest.GET_COUPON_BY_ID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				if (coupon.getId() == resultSet.getInt(1)) {
					try {
						System.out.println("enter a new Coupon title");
						statement = connection.prepareStatement(SQLQueryRequest.SET_COUPON_TITLE_BY_ID);
						String newTitle = reader.readLine();
						statement.setString(1, newTitle);
						statement.setLong(2, coupon.getId());
						statement.executeUpdate();
						coupon.setTitle(newTitle);

						System.out.println("enter a new start date (YYYY-MM-DD)");
						statement = connection.prepareStatement(SQLQueryRequest.SET_COUPON_START_DATE_BY_ID);
						Date newStartDate = StringDateConvertor.convert(reader.readLine());
						statement.setDate(1, newStartDate);
						statement.setLong(2, coupon.getId());
						statement.executeUpdate();
						coupon.setStartDate(newStartDate);

						System.out.println("enter a new end date (YYYY-MM-DD)");
						statement = connection.prepareStatement(SQLQueryRequest.SET_COUPON_END_DATE_BY_ID);
						Date newEndDate = StringDateConvertor.convert(reader.readLine());
						statement.setDate(1, newEndDate);
						statement.setLong(2, coupon.getId());
						statement.executeUpdate();
						coupon.setEndDate(newEndDate);

						System.out.println("enter a new amount");
						statement = connection.prepareStatement(SQLQueryRequest.SET_COUPON_AMOUNT_BY_ID);
						int newAmount = Integer.parseInt(reader.readLine());
						statement.setInt(1, newAmount);
						statement.setLong(2, coupon.getId());
						statement.executeUpdate();
						coupon.setAmount(newAmount);

						System.out.println("enter a new coupon type");
						statement = connection.prepareStatement(SQLQueryRequest.SET_COUPON_TYPE_BY_ID);
						String newType = reader.readLine();
						statement.setString(1, newType);
						statement.setLong(2, coupon.getId());
						statement.executeUpdate();
						coupon.setType(CouponType.valueOf(newType));

						System.out.println("enter a new message");
						statement = connection.prepareStatement(SQLQueryRequest.SET_COUPON_MESSAGE_BY_ID);
						String newMessage = reader.readLine();
						statement.setString(1, newMessage);
						statement.setLong(2, coupon.getId());
						statement.executeUpdate();
						coupon.setMessage(newMessage);

						System.out.println("enter a new price");
						statement = connection.prepareStatement(SQLQueryRequest.SET_COUPON_PRICE_BY_ID);
						Double newPrice = Double.parseDouble(reader.readLine());
						statement.setDouble(1, newPrice);
						statement.setLong(2, coupon.getId());
						statement.executeUpdate();
						coupon.setPrice(newPrice);

						System.out.println("add a new image");
						statement = connection.prepareStatement(SQLQueryRequest.SET_COUPON_IMAGE_BY_ID);
						String newImage = reader.readLine();
						statement.setString(1, newImage);
						statement.setLong(2, coupon.getId());

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
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(SQLQueryRequest.GET_COUPON_BY_ID);
			statement.setLong(1, id);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				coupon.setId(resultSet.getLong(1));
				coupon.setTitle(resultSet.getString(2));
				coupon.setStartDate(StringDateConvertor.convert(resultSet.getString(3)));
				coupon.setEndDate(StringDateConvertor.convert(resultSet.getString(4)));
				coupon.setAmount(resultSet.getInt(5));
				coupon.setType(CouponType.valueOf(resultSet.getString(6)));
				coupon.setMessage(resultSet.getString(7));
				coupon.setPrice(resultSet.getDouble(8));
				coupon.setImage(resultSet.getString(9));
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
			PreparedStatement statement = connection.prepareStatement(SQLQueryRequest.GET_ALL_COUPON_ID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				collectionCoupon.add(getCoupon(resultSet.getLong(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionPool.getInstance().returnConnection(connection);
		}
		
		return collectionCoupon;
	}

	@Override
	public HashSet<Coupon> getCouponByType(CouponType couponType) {
		Connection connection = ConnectionPool.getInstance().getConnection();
		HashSet<Coupon> collectionCoupon = new HashSet<Coupon>();
		try {
			PreparedStatement statement = connection.prepareStatement(SQLQueryRequest.GET_ALL_COUPON_ID_BY_TYPE);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				collectionCoupon.add(getCoupon(resultSet.getLong(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectionPool.getInstance().returnConnection(connection);
		return collectionCoupon;
	}

}
