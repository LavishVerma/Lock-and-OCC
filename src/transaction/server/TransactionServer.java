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
import transaction.server.transaction.Transaction;
import transaction.server.transaction.TransactionManager;
import transaction.util.PropertyHandler;

public class TransactionServer implements Runnable {

	public static AccountManager accountManager = null;
	public static TransactionManager transactionManager = null;

	public static ServerSocket serverSocket = null;

	static boolean keepgoing = true;

	static boolean transactionView = true;

	static int messageCount = 0;

	TransactionServer() {
		Properties properties = null;

		int numberAccounts;
		int initialBalance;

		properties = PropertyHandler.readProperties();

		transactionView = Boolean.valueOf(properties.getProperty("TRANSACTION_VIEW"));
		TransactionServer.transactionManager = new TransactionManager();

		numberAccounts = Integer.parseInt(properties.getProperty("NUMBER_ACCOUNTS"));
		initialBalance = Integer.parseInt(properties.getProperty("INITIAL_BALANCE"));

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
		System.out.println("In printout summary");
		StringBuffer abortedTransactionLogs = new StringBuffer();

		Iterator<Transaction> abortedTransactionIterator = TransactionServer.transactionManager.getAbortedtransactions().iterator();
		Transaction abortedTransaction;

		while (abortedTransactionIterator.hasNext()) {
			abortedTransaction = abortedTransactionIterator.next();
			abortedTransactionLogs.append(abortedTransaction.getLogs()).append("\n");

		}
		System.out.println(abortedTransactionLogs);

		ArrayList<Account> accounts = TransactionServer.accountManager.getAccounts();
		Iterator<Account> accountIterator = accounts.iterator();
		Account account;
		int total = 0;
		while (accountIterator.hasNext()) {
			account = accountIterator.next();
			total += TransactionServer.accountManager.readAccount(account.getAccountNumber());

		}

		System.out.println("Total ----" + total + "\n\n");
	}

	@Override
	public void run() {

		while (keepgoing) {
			try {
				transactionManager.runTransaction(serverSocket.accept());
System.out.println("Work is done for one thread!!!!!!!!!!!!!!!!!!!!!!!!!");

			} catch (SocketException e) {
				System.out.println("SocketException");
			} catch (IOException e) {
				System.out.println("IOException");
			}
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {

		}

		printOutSummary();
	}

	public static void main(String[] args) {
		
			new TransactionServer().run();
		
	}

}
