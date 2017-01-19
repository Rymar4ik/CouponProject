package couponsProject.DAO;

import java.util.Collection;

import couponsProject.Coupon;
import couponsProject.Customer;

public interface CustomerDAO {
	public void createCustomer(Customer customer);
	public void removeCustomer(Customer customer);
	public void updateCustomer(Customer customer);
	public Customer getCustomer(long id);
	public Collection<Customer> getAllCustomer();
	public Collection<Coupon> getCoupons();
	public boolean login(String custName, String password);
}
