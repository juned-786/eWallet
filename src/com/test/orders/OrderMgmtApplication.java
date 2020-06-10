package com.test.orders;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderMgmtApplication {

	public static void main(String[] args) throws Exception {

		Scanner sc = new Scanner(System.in);
		System.out.println("1. Sign Up (Y/N)");

		String name = null;
		String accountNo = null;
		String mobileNo = null;
		String govtId = null;
		String opt = null;
		boolean flag1 = true;
		boolean flag = true;
		boolean insuff = false;

		String option = sc.nextLine();
		switch (option) {
		case "Y":
			System.out.println("Enter the name");
			name = sc.nextLine();
			while (flag1) {

				System.out.println("Enter the account number");
				accountNo = sc.nextLine();
				if (accountNo.length() < 10)
					System.out.println("Not a valid account number");
				else
					flag1 = false;

			}

			while (flag) {

				System.out.println("Enter Mobile No");
				mobileNo = sc.nextLine();
				flag = isValid(mobileNo);
				System.out.println(flag);
				if (!flag) {
					System.out.println("Not a valid Mobile No");
					flag = true;
				}

				else
					flag = false;

			}

			System.out.println("Enter Your Govt Id");
			govtId = sc.nextLine();
			System.out.println("Please Enter the OTP");
			Wallet wallet = new Wallet();
			wallet.setName(name);
			wallet.setGovtId(govtId);
			wallet.setMobileNo(mobileNo);

			DBConfig.dbConnetcion("insert", wallet);

			opt = sc.nextLine();
			System.out.println("Wallet Cretaed Successfully");
			break;
		default:
			break;
		}

		System.out.println("2. Add Money to the wallet (Y/N)");
		String option1 = sc.nextLine();
		switch (option1) {
		case "Y":
			System.out.println("Enter the Amount (Not more than 200000)");
			int amount = sc.nextInt();
			Wallet wallet = new Wallet();
			wallet.setWalletAmt(amount);
			wallet.setName(name);
			String totalAmount = DBConfig.dbConnetcion("update", wallet);
			System.out.println("Total Balance {} " + totalAmount);

			break;
		case "N":
			break;

		default:
			break;
		}

		System.out.println("3. P2M/P2P Fund Transfer (Y/N)");

		String option2 = sc.nextLine();
		switch (option2) {
		case "Y":
			System.out.println("Enter the Amount ");
			int amount = sc.nextInt();
			System.out.println("Enter Consumer Mobile Number");
			String mobNo = sc.nextLine();

			Wallet wallet = new Wallet();
			wallet.setName(name);
			wallet.setMobileNo(mobNo);
			wallet.setWalletAmt(amount);
			String result = DBConfig.dbConnetcion("update", wallet);
			if (!result.equals("insufficent funds")) {
				System.out.println("Total Balance {} " + result);
				insuff = true;
			}
			break;
		case "N":
			break;

		default:
			sc.close();
			if (insuff)

				throw new Exception("Insufficient Amount");
		}

	}

	public static boolean isValid(String s) {

		Pattern p = Pattern.compile("(0/91)?[7-9][0-9]{9}");

		Matcher m = p.matcher(s);
		return (m.find() && m.group().equals(s));
	}

}
