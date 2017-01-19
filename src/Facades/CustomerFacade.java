package Facades;

import java.util.Collection;

import couponsProject.ClientType;
import couponsProject.ClientTypeFactory;
import couponsProject.Coupon;
import couponsProject.CouponType;
import couponsProject.Customer;
import couponsProject.DAO.CustomerDBDAO;

public class CustomerFacade implements CouponClientFacade {
	private Customer customer = new Customer();
	private CustomerDBDAO customerDBDAO = new CustomerDBDAO();

	public CustomerFacade() {
	 
	}

	public void purchaseCoupon(Coupon coupon)  {
		customerDBDAO.purchaseCoupon(coupon);
	}

	public Collection<Coupon> getAllPurchasedCoupons() {
		return customerDBDAO.getCoupons();
	}
	
	public Collection<Coupon> getAllPurchasedCouponsByType(CouponType type) {
		return customerDBDAO.getAllPurchasedCouponsByType(customer, type);
	}
	
	public Collection<Coupon> getAllPurchasedCouponsByPrice(double price) {
		return customerDBDAO.getAllPurchasedCouponsByPrice(price);
	}

	@Override
	public CouponClientFacade login(String name, String password, ClientType client) {
		return ClientTypeFactory.login(name, password, client);

	}

}
