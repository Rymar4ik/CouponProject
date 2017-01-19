package couponsProject;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import couponsProject.DAO.*;

public class DailyCouponExpirationTask implements Runnable {
	private CouponDBDAO couponDBDAO;
	private boolean quit = false;
	 Thread thread;

	public DailyCouponExpirationTask() {
		thread = new Thread(this, "DailyCouponExpirationTask");
		thread.start();
	}
	@Override
	public void run() {
		while (!quit) {
			HashSet<Coupon> allCoupons = couponDBDAO.getAllCoupon();
			Iterator<Coupon> iterator = allCoupons.iterator();
			while (iterator.hasNext()) {
				Coupon tmp = iterator.next();
				if (tmp.getEndDate().before(new Date())) {
					couponDBDAO.removeCoupon(tmp);
				}
			}
		}
	}

	public void stopTask() {
		quit = true;
	}

}
