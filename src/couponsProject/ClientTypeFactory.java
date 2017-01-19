package couponsProject;

import Facades.AdminFacade;
import Facades.CompanyFacade;
import Facades.CouponClientFacade;
import Facades.CustomerFacade;
import couponsProject.DAO.CompanyDBDAO;
import couponsProject.DAO.CustomerDBDAO;

public class ClientTypeFactory {
	public static CouponClientFacade login(String name, String password, ClientType client) {
		CompanyDBDAO comp = new CompanyDBDAO();
		CustomerDBDAO cust = new CustomerDBDAO();
		
		if (comp.login(name, password) && client.equals(ClientType.Company)) {
			System.out.println("You got a Company facade");
			return new CompanyFacade();
		} 
		else if (cust.login(name, password) && client.equals(ClientType.Customer)) {
			System.out.println("You got a Customer facade");
			return new CustomerFacade();
		}
		else if (name.equals("admin") && password.equals("1234") && client.equals(ClientType.Admin)) {
			System.out.println("You got a Admin facade");
			return new AdminFacade();
		}
		else {
			System.out.println("Invalid login or password");
		return null;}

	}

}
