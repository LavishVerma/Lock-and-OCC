package transaction.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import transaction.server.account.Account;
import transaction.server.account.AccountManager;
import transaction.server.lock.LockManager;
import transaction.server.transaction.Transaction;
import transaction.server.transaction.TransactionManager;
import transaction.util.PropertyHandler;

public class TransactionServer implements Runnable {

	public static AccountManager accountManager = null;
	public static TransactionManager transactionManager = null;
	public static LockManager lockManager = null;

	public static ServerSocket serverSocket = null;

	static boolean keepgoing = true;

	static boolean isLockingEnabled = true;

	static int messageCount = 0;

	TransactionServer() {
		Properties properties = null;

		int numberAccounts;
		int initialBalance;

		properties = PropertyHandler.readProperties();

		//Reading from Configuration...
		isLockingEnabled = Boolean.valueOf(properties.getProperty("LOCKING_ENABLED"));
		TransactionServer.transactionManager = new TransactionManager();

		numberAccounts = Integer.parseInt(properties.getProperty("NUMBER_ACCOUNTS"));
		initialBalance = Integer.parseInt(properties.getProperty("INITIAL_BALANCE"));
		
		TransactionServer.lockManager = new LockManager(isLockingEnabled);
		TransactionServer.accountManager = new AccountManager(numberAccounts, initialBalance);

		try {
			serverSocket = new ServerSocket(Integer.parseInt(properties.getProperty("PORT")));
			System.out.println("Server started");
			System.out.println("Waiting for a client ...");
		} catch (IOException ex) {

			System.out.println("Could not create Connection");
			System.exit(1);
		}
	}

	public static synchronized int getMessageCount() {
		return ++messageCount;
	}

	public static void shutdown() {
		try {
			keepgoing = false;
			serverSocket.close();
		} catch (IOException ex) {
			Logger.getLogger(TransactionServer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void printOutSummary() {
		//Printing Out Accounts and Balances in them.
		//Also the total amount in the bank.
	}

	@Override
	public void run() {

	//CAlling Transaction Manager as the request.
	}

	public static void main(String[] args) {
		
			new TransactionServer().run();
		
	}

}
