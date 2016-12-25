package couponsProject;

import java.util.Collection;

public interface CouponDAO {
	public void createCoupon(Coupon coupon);
	public void removeCoupon(Coupon coupon);
	public void updateCoupon(Coupon coupon);
	public Coupon getCoupon(long id);
	public Collection<Coupon> getAllCoupon();
	public Collection<Coupon> getCouponByType(CouponType couponType);
}
