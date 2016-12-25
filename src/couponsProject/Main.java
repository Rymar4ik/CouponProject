package couponsProject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

	public static void main(String[] args) throws ParseException {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-mm-dd");
		Coupon coupon = new Coupon();
		Date d1 = new Date();
		Date d = new Date();
		coupon.setStartDate(d1);
		coupon.setEndDate(d);
		coupon.setTitle("TestCoupon");
		coupon.setAmount(22);
		coupon.setMessage("Test Message");
		coupon.setPrice(244);
		coupon.setType(CouponType.FOOD);
		CouponDBDAO c = new CouponDBDAO();
		c.createCoupon(coupon);
	}

}
