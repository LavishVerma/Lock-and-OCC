package transaction.server.transaction;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import transaction.common.Message;
import transaction.common.MessageTypes;
import transaction.server.TransactionServer;

public class TransactionManager {
	
	private static int transactionIdCounter = 0 ;
	private ArrayList<Transaction> Abortedtransactions = new ArrayList<>();
	private ArrayList<Transaction> Runningtransactions = new ArrayList<>();
	private HashMap<Integer, Transaction> committedTransaction = new HashMap<>();
     private Lock lock = new ReentrantLock();
	public boolean validateTransaction(Transaction transaction) {
		return true;
	}

	public void writeTransaction(Transaction transaction) {
	}
	public ArrayList<Transaction> getAbortedtransactions() {
		return Abortedtransactions;
	}


	public int getTransactionIdCounter() {
		return transactionIdCounter;
	}
	public void runTransaction(Socket client) {
		
		Thread currentThread;
		
		currentThread = new TransactionManagerWorker(client);
		currentThread.start();
		
		

	}
	
	public class TransactionManagerWorker extends Thread {
		
		Socket client;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		Transaction transaction = null;
		
		public TransactionManagerWorker(Socket client) {
			this.client = client;
		}
		@Override
        public void run() {
			 lock.lock();
			// Read/Write from server TransactionID
			try {
				oos = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
				ois = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		    
		    
			//Keeps ruuning until we receive the close request.
			while(true) {


				try {
					
					Message message = (Message) ois.readObject();
					
					if (message != null) {
						if (message.getMessageID() == MessageTypes.OPEN_TRANSACTION) {
							System.out.println("OPEN_TRANSACTION  " + message);
							transaction = new Transaction();
						
							oos.writeObject(transaction.getTransactionID());
							oos.flush();
							System.out.println("transaction.getTransactionID()--"+transaction.getTransactionID());

						} else if (message.getMessageID() == MessageTypes.READ_REQUEST) {
							transaction = new Transaction();
							System.out.println("READ_REQUEST --"+ message);
							oos.writeObject(transaction.read(message.getAccountNumber()));
							oos.flush();
						} else if (message.getMessageID() == MessageTypes.WRITE_REQUEST) {
							 System.out.println("WRITE REQUEST " + message); 
		                      transaction.write( message.getAccountNumber(),  message.getAmount());
		        
						}
						else if (message.getMessageID() == MessageTypes.CLOSE_TRANSACTION) {
							System.out.println("CLOSE REQUEST + " + message); 
							
							oos.close();
							ois.close();
							client.close();
							break;
							
							
						}
					}

				} catch (ClassNotFoundException | IOException e) {
					
					e.printStackTrace();
				}
				
				
			
			}
			
			lock.unlock();
			
	 }
	}

}
