package transaction.server.transaction;

import java.util.ArrayList;
import java.util.HashMap;

import transaction.server.TransactionServer;

public class Transaction {
	int transactionID;
	int transactionNumber = 0;
	int lastCommittedTransactionNumber;

	private ArrayList<Integer> readSet = new ArrayList<>(); //Account Numbers
	private HashMap<Integer, Integer> writeSet = new HashMap<>();  // <Account_Number , Balance>

	private StringBuffer log = new StringBuffer("");

	
	public Transaction() {

	}

	Transaction(int transactionID, int lastCommittedTransactionNumber) {
		this.lastCommittedTransactionNumber = lastCommittedTransactionNumber;
		this.transactionID = transactionID;
	}

	public int read(int accountNumber) {
		log.append("Reading account Number -> "+ accountNumber);
		Integer balance;
		balance = writeSet.get(accountNumber);

		if (balance == null) {
			balance = TransactionServer.accountManager.readAccount(accountNumber);
		}
		if (!readSet.contains(accountNumber)) {
			readSet.add(accountNumber);
		}
		System.out.println("Balance of Account Number " + accountNumber + " is " + balance);
		return balance;
	}

	public int write(int accountNumber, int balance) {
		log.append("Writing "+ balance+" into Account Number -> "+ accountNumber);
		int priorBalance = read(accountNumber);

		if (!writeSet.containsKey(accountNumber)) {

		}
		TransactionServer.accountManager.writeAccount(accountNumber, balance);
		writeSet.put(accountNumber, balance);
		System.out.println("Writing "+ balance+" into Account Number -> "+ accountNumber);
		
		return accountNumber;
	}
	
	public String getLogs() {
		return log.toString();
	}

	public int getTransactionID() {
		return (int) Math.floor(Math.random() * 100000);
	}


	public int getTransactionNumber() {
		 ++transactionNumber;
		return transactionNumber;
	}

	

	public ArrayList<Integer> getReadSet() {
		return readSet;
	}

	public HashMap<Integer, Integer> getWriteSet() {
		return writeSet;
	}
	

}
