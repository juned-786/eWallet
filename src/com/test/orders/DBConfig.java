package com.test.orders;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class DBConfig {

	public static String dbConnetcion(String operation, Wallet wallet) {
		String result = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "root", "root");

			if (operation.equals("insert"))
				insert(con, wallet);
			else if (operation.equals("update"))
				result = update(con, wallet);
			else
				result = transfer(con, wallet);

		} catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}

	public static void insert(Connection con, Wallet wallet) {

		try {

			String query = "insert into wallet values(?,?,?,?,?)";
			PreparedStatement p = con.prepareStatement(query);
			p.setString(1, wallet.getName());
			p.setString(2, wallet.getMobileNo());
			p.setString(3, wallet.getGovtId());
			p.execute();

			con.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public static String update(Connection con, Wallet wallet) {
		long amount = 0;
		try {

			String query1 = "select * from  wallet where name=" + wallet.getName();
			Statement st = con.createStatement();
			ResultSet r = st.executeQuery(query1);
			while (r.next()) {
				amount = r.getInt("amount");
			}

			String query = "update wallet set amount=? where name=?";
			PreparedStatement p = con.prepareStatement(query);
			p.setLong(1, wallet.getWalletAmt() + amount);
			p.setString(2, wallet.getName());

			con.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return String.valueOf(amount + wallet.getWalletAmt());

	}

	public static String transfer(Connection con, Wallet wallet) {
		long amount = 0;
		String result = null;
		try {

			// updating user wallet

			String query = "select * from  wallet where name=" + wallet.getName();
			Statement st = con.createStatement();
			ResultSet r = st.executeQuery(query);
			while (r.next()) {
				amount = r.getInt("amount");
			}
			if (amount < wallet.getWalletAmt()) {
				result = "insufficent funds";
			} else {

				String query1 = "update wallet set amount=? where name=?";
				PreparedStatement p1 = con.prepareStatement(query1);
				p1.setLong(1, amount - wallet.getWalletAmt());
				p1.setString(2, wallet.getName());

				result = String.valueOf(amount - wallet.getWalletAmt());
				// updating consumer wallet

				String query2 = "select * from  wallet where mobileNumber=" + wallet.getWalletAmt();
				Statement st1 = con.createStatement();
				ResultSet r1 = st1.executeQuery(query2);
				while (r1.next()) {
					amount = r.getInt("amount");
				}

				String query3 = "update wallet set amount=? where mobileNumber=?";
				PreparedStatement p = con.prepareStatement(query3);
				p.setLong(1, wallet.getWalletAmt() + amount);
				p.setString(2, wallet.getMobileNo());
			}
			con.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return result;

	}

}
