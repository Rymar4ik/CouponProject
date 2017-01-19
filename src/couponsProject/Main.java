package couponsProject;


import Facades.AdminFacade;
import Facades.CustomerFacade;
import couponsProject.DAO.CompanyDBDAO;
import couponsProject.DAO.CouponDBDAO;
import couponsProject.DAO.CustomerDBDAO;

public class Main {

	public static void main(String[] args) throws Exception{
		CouponSystem cs = CouponSystem.getInstance();
		AdminFacade admin = (AdminFacade) cs.login("admin", "1234", ClientType.Admin);

		
		
	}
}
