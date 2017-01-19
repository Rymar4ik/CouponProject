package couponsProject.DAO;

import java.util.Collection;

import couponsProject.Company;
import couponsProject.Coupon;

public interface CompanyDAO {
	public void createCompany(Company company);

	public void updateCompany(Company company);
	
	public void removeCompany(Company company);

	public Company getCompany(long id);

	public Collection<Company> getAllCompanies();

	public Collection<Coupon> getAllCoupons();

	public boolean login(String name, String password);
}
