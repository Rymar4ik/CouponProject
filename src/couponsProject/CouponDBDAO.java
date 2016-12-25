package couponsProject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

public class CouponDBDAO implements CouponDAO {
	public CouponDBDAO(){
		
	}

	@Override
	public void createCoupon(Coupon coupon) {
		Connection connection = ConnectionPool.getInstance().getConnection();
		boolean flag = true;
		try {
			Statement st = connection.createStatement();
			ResultSet resultTitle = st.executeQuery("SELECT TITLE FROM COUPONSYSTEM.COUPON");
			
			while (resultTitle.next()) {
				if (coupon.getTitle().equals(resultTitle.getString("TITLE"))) {
					flag = false;
					System.out.println("Coupon title " + coupon.getTitle() + " is already exists");
				}
			}
			if (flag) {
				st.executeUpdate(
						"INSERT INTO COUPONSYSTEM.COUPON(TITLE, START_DATE, END_DATE, AMOUNT, TYPE, MESSAGE, PRICE, IMAGE) VALUES(\"" + coupon.getTitle()
								+ "\",\"" + coupon.getStartDate() + "\",\"" + coupon.getEndDate() + "\",\"" + coupon.getAmount() + "\",\"" + coupon.getType() 
								+ "\",\"" + coupon.getMessage() + "\",\"" + coupon.getPrice() + "\",\"" + coupon.getImage() + "\")");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ConnectionPool.getInstance().returnConnection(connection);
		
	}

	@Override
	public void removeCoupon(Coupon coupon) {
		
	}

	@Override
	public void updateCoupon(Coupon coupon) {
		
	}

	@Override
	public Coupon getCoupon(long id) {
		return null;
	}

	@Override
	public Collection<Coupon> getAllCoupon() {
		return null;
	}

	@Override
	public Collection<Coupon> getCouponByType(CouponType couponType) {
		return null;
	}
	
	
}
