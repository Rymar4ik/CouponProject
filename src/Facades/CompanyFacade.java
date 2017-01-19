package Facades;

import java.util.Collection;

import couponsProject.ClientType;
import couponsProject.ClientTypeFactory;
import couponsProject.Coupon;
import couponsProject.CouponType;
import couponsProject.DAO.CompanyDBDAO;
import couponsProject.DAO.CouponDBDAO;



public class CompanyFacade implements CouponClientFacade {
	private CouponDBDAO coupon;
	private CompanyDBDAO company;
	public CompanyFacade() {

	}

	public void createCoupon(Coupon coup) {
		company.createCoupon(coup);
	}

	public void removeCoupon(Coupon coup) {
		coupon.removeCoupon(coup);
	}

	public void updateCoupon(Coupon coup) {
		coupon.updateCoupon(coup);
	}

	public Coupon getCoupon(long id) {
		return coupon.getCoupon(id);
	}

	public Collection<Coupon> getAllCoupon() {
		return coupon.getAllCoupon();
	}

	public Collection<Coupon> getCouponByType(CouponType couponType) {
		return coupon.getCouponByType(couponType);
	}

	@Override
	public CouponClientFacade login(String name, String password, ClientType client) {
		return ClientTypeFactory.login(name, password, client);
	}

}
