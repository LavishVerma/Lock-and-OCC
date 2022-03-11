package transaction.server.account;

import java.util.ArrayList;

import transaction.server.TransactionServer;

public class AccountManager {
	
	private int numberOfAccounts;
	private int initialBalance;
	private ArrayList<Account> accounts = new ArrayList<>();
	
	public AccountManager(int numberOfAccounts , int initialBalance)
	{
		this.initialBalance =initialBalance;
		this.numberOfAccounts = numberOfAccounts;
		this.InitializeAccounts();
	}
	
	void InitializeAccounts() {
		for(int i = 0 ; i < numberOfAccounts ;i++) {
			Account account = new Account(i+1,initialBalance);
			accounts.add(account);
		}
		
	}
	
	
    public int readAccount(int accountNumber) {
		int balance = 0;
		for(Account account : accounts) {
			if(account.getAccountNumber() == accountNumber) {
				balance = account.getBalance();
			}
		}
		System.out.println("Read Account balance -> "+balance);
		return balance;
	}
	
    public void writeAccount(int accountNumber, int balance) {
		
		for(Account account : accounts) {
			if(account.getAccountNumber() == accountNumber) {
				  account.setBalance(balance);
			}
		}
		System.out.println("WriteAccount balance -> "+balance);
		System.out.println("Total Amount  -- "+getTotalAmountInBank());
		 TransactionServer.accountManager.getAccounts();
	}
    
    public ArrayList<Account> getAccounts(){
    	return this.accounts;
    }
    
    public Account getAccount(int accountNumber) {
    	
     return	this.accounts.stream().filter(each -> each.getAccountNumber() == accountNumber).findFirst().get();
    	
    }
    
    public int getTotalAmountInBank() {  
    	 int sum = this.accounts.stream().mapToInt(ob -> ob.getBalance())
    			 .reduce(0,
    			 (element1, element2) -> element1 + element2 );
    	
         return sum;
    }
	
	
}
