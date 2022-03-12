package transaction.server.account;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import transaction.server.TransactionServer;

public class AccountManager {

	private int numberOfAccounts;
	private int initialBalance;
	private ArrayList<Account> accounts = new ArrayList<>();
//	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
//	private final Lock writeLock = readWriteLock.writeLock();
//	private final Lock readLock = readWriteLock.readLock();

	public AccountManager(int numberOfAccounts, int initialBalance) {
		this.initialBalance = initialBalance;
		this.numberOfAccounts = numberOfAccounts;
		this.InitializeAccounts();
	}

	void InitializeAccounts() {
		for (int i = 0; i < numberOfAccounts; i++) {
			Account account = new Account(i + 1, initialBalance);
			accounts.add(account);
		}

	}

	public int readAccount(int accountNumber) {
//		readLock.lock();
		int balance = 0;
		for (Account account : accounts) {
			if (account.getAccountNumber() == accountNumber) {
				balance = account.getBalance();
			}
		}
		System.out.println("Read Account balance -> " + balance);
//		readLock.unlock();
		return balance;
	}

	public void writeAccount(int accountNumber, int balance) {
//		writeLock.lock();
		for (Account account : accounts) {
			if (account.getAccountNumber() == accountNumber) {
				account.setBalance(balance);
			}
		}
		System.out.println("WriteAccount balance -> " + balance);
		System.out.println("Total Amount in the Bank  -- " + getTotalAmountInBank());
		TransactionServer.accountManager.getAccounts();

//		writeLock.unlock();
	}

	public ArrayList<Account> getAccounts() {
		return this.accounts;
	}

	public Account getAccount(int accountNumber) {

		return this.accounts.stream().filter(each -> each.getAccountNumber() == accountNumber).findFirst().get();

	}

	public int getTotalAmountInBank() {
		int sum = this.accounts.stream().mapToInt(ob -> ob.getBalance()).reduce(0,
				(element1, element2) -> element1 + element2);

		return sum;
	}

}
