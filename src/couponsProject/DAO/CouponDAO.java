package couponsProject.DAO;

import java.util.Collection;

import couponsProject.Coupon;
import couponsProject.CouponType;

public interface CouponDAO {
	public void createCoupon(Coupon coupon);
	public void removeCoupon(Coupon coupon);
	public void updateCoupon(Coupon coupon);
	public Coupon getCoupon(long id);
	public Collection<Coupon> getAllCoupon();
	public Collection<Coupon> getCouponByType(CouponType couponType);
}
