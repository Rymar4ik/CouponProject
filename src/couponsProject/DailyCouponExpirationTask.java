package couponsProject;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import couponsProject.DAO.*;

public class DailyCouponExpirationTask implements Runnable {
	private CouponDBDAO couponDBDAO = new CouponDBDAO();
	private boolean quit = false;
	private Thread thread;

	public DailyCouponExpirationTask() {
		thread = new Thread(this, "DailyCouponExpirationTask");
		thread.start();
	}
//îäèí ðàç ïðîâåðÿåò íåò ëè ïðîñðî÷åííûõ êóïîíîâ ïðè çàïóñêå ïðîãðàììû, åñëè åñòü - óäàëÿåò èõ
	//ïîñëå ýòîãî, ïîêà ïðîãðàììà ðàáîòàåò, â 00.00.00 êàæäîãî äíÿ ïðîâåðÿåò è óäàëÿåò ïðîñðî÷åííûå êóïîíû
	@Override
	public void run() {
		HashSet<Coupon> allCoupons = couponDBDAO.getAllCoupon();
		Iterator<Coupon> iterator = allCoupons.iterator();
		while (iterator.hasNext()) {
			Coupon tmp = iterator.next();
			if (tmp.getEndDate().before(new Date())) {
				couponDBDAO.removeCoupon(tmp);
			}
		}
		while (!quit) {
			if (new Date().getTime() % 86400000 == 0) {
				while (iterator.hasNext()) {
					Coupon tmp = iterator.next();
					if (tmp.getEndDate().before(new Date())) {
						couponDBDAO.removeCoupon(tmp);
					}
				}
			}
		}
	}

	public void stopTask() {
		quit = true;
	}

}
