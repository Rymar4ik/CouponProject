package couponsProject;

import java.util.Collection;

public interface CompanyDAO {
	public void createCompany(Company company);

	public void updateCompany(Company company);

	public Company getCompany(long id);

	public Collection<Company> getAllCompanies();

	public Collection<Coupon> getAllCoupons();

	public boolean login(String name, String password);
}
