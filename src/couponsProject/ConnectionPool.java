package couponsProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;

public class ConnectionPool {

	private static ConnectionPool instance;
	private static String conURL = "jdbc:mysql://localhost/CouponSystem";
	private static String user = "root";
	private static String password = "0000";
	private int maxConnectionCount = 5;
	HashSet<Connection> connections = new HashSet<>();
	Object key = new Object();

	// private constructor because we don't need create an instance of this
	// class from any other class
	// creation of 5 connections
	private ConnectionPool() {
		for (int i = 0; i < maxConnectionCount; i++) {
			try {
				try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				connections.add(DriverManager.getConnection(conURL, user, password));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Connected ");
	}

	// realization of singleton
	public static synchronized ConnectionPool getInstance() {
		if (instance == null) {
			instance = new ConnectionPool();
		}
		return instance;
	}

	// takes one connection from connection pool for doing something with
	// DataBase
	public Connection getConnection() {
		synchronized (key) {
			Iterator<Connection> iterator = connections.iterator();

			if (connections.isEmpty()) {
				try {
					System.out.println("All connections are buisy, please wait");
					key.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Connection tmp = iterator.next();
			iterator.remove();
			System.out.println("now you get a connection");
			return tmp;

		}
	}

	// returns connection back to connection pool after operation(s) with
	// DataBase
	public void returnConnection(Connection connection) {
		synchronized (key) {
			if (connections.size() < maxConnectionCount) {
				try {
					connections.add(DriverManager.getConnection(conURL, user, password));
					key.notify();
					System.out.println("connection returned!");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else
				System.out.println("There are no connections to return");
		}
	}

	// closing all connections
	public void closeAllConnections() {
		Iterator<Connection> iterator = connections.iterator();
		while (iterator.hasNext()) {
			try {
				iterator.next().close();
				iterator.remove();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println(connections.size() + " connections are open now");
	}

}
