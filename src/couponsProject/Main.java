package couponsProject;

import java.util.Date;

import Facades.CustomerFacade;
import couponsProject.DAO.CompanyDBDAO;
import couponsProject.DAO.CouponDBDAO;
import couponsProject.DAO.CustomerDBDAO;

public class Main {

	public static void main(String[] args) throws Exception{
		CouponSystem cs = CouponSystem.getInstance();
//		Coupon coup = new Coupon("title", StringDateConvertor.convert("2017-01-16"), StringDateConvertor.convert("2017-02-16"), 3, 
//				CouponType.FOOD, "message", 15.0, "D:\\img.png");
		CouponDBDAO coupon = new CouponDBDAO();
		Coupon coup = coupon.getCoupon(6);
		CompanyDBDAO companyDBDAO = new CompanyDBDAO();
		coupon.createCoupon(coup);
		Company company = new Company("Electronica", "electronica", "el@gmail.com");
		companyDBDAO.createCompany(company);
		CustomerDBDAO customerDBDAO = new CustomerDBDAO();
		customerDBDAO.login("Or", "password1");
		System.out.println(company);
		companyDBDAO.createCoupon(coup, company);
		CustomerFacade fac = (CustomerFacade) cs.login("Or", "password1", ClientType.Customer);
		fac.purchaseCoupon(coup);
		
	}
}
