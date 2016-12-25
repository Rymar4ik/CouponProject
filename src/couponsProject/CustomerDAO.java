package couponsProject;

import java.util.Collection;

public interface CustomerDAO {
	public void createCustomer(Customer customer);
	public void removeCustomer(Customer customer);
	public void updateCustomer(Customer customer);
	public Customer getCustomer(long id);
	public Collection<Company> getAllCustomer();
	public Collection<Coupon> getCoupons();
	public boolean login(String custName, String password);
}
