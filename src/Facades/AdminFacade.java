package Facades;

import java.util.Collection;

import couponsProject.ClientType;
import couponsProject.ClientTypeFactory;
import couponsProject.Company;
import couponsProject.Customer;
import couponsProject.DAO.CompanyDBDAO;
import couponsProject.DAO.CustomerDBDAO;

public class AdminFacade implements CouponClientFacade {
	private CompanyDBDAO company = new CompanyDBDAO();
	private CustomerDBDAO customer = new CustomerDBDAO();

	public AdminFacade() {
	}

	public void createCompany(Company comp) {
		company.createCompany(comp);
	}

	public void removeCompany(Company comp) {	
		company.removeCompany(comp);
	}

	public void updateCompany(Company comp) {
		company.updateCompany(comp);
	}

	public Company getCompany(long id) {
		return company.getCompany(id);

	}

	public Collection<Company> getAllCompanies() {
		return company.getAllCompanies();

	}

	public void createCustomer(Customer cust) {
		customer.createCustomer(cust);
	}

	public void removeCustomer(Customer cust) {
		customer.removeCustomer(cust);
	}

	public void updateCustomer(Customer cust) {
		customer.updateCustomer(cust);
	}

	public Customer getCustomer(long id) {
		return customer.getCustomer(id);

	}

	public Collection<Customer> getAllCustomer() {
		return customer.getAllCustomer();
				
	}

	@Override
	public CouponClientFacade login(String name, String password, ClientType client) {
		return ClientTypeFactory.login(name, password, client);
	}
}
