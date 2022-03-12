package transaction.transaction_occ_lock.common;

import java.io.Serializable;

public class Message implements Serializable {
	
	private int messageID;
	private int accountNumber;
	private int amount;
	
	public Message(int id){
		this.messageID = id;
	}
	
	public Message(int id,int accountNumber){
		this.messageID = id;
		this.accountNumber = accountNumber;
	}
	
	public Message(int id,int accountNumber,int amount){
		this.messageID = id;
		this.accountNumber = accountNumber;
		this.amount = amount;
	}
	
	

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getMessageID() {
		return messageID;
	}

	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}

	@Override
	public String toString() {
		return "Message [messageID=" + messageID + ", accountNumber=" + accountNumber + ", amount=" + amount + "]";
	}
	
	

}
