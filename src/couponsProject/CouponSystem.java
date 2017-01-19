package couponsProject;

import Facades.CouponClientFacade;

public class CouponSystem {
	private CouponClientFacade client;
	private static CouponSystem instance;
	private DailyCouponExpirationTask task;

	private CouponSystem() {
		task = new DailyCouponExpirationTask();
	}

	public static CouponSystem getInstance() {
		if (instance == null) {
			instance = new CouponSystem();
		}
		System.out.println("You got an instance of CouponSystem");
		return instance;
	}

	public CouponClientFacade login(String name, String password, ClientType type) {
		client = ClientTypeFactory.login(name, password, type);
		return client;
	}
}
