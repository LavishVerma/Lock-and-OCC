package transaction.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import transaction.common.Message;
import transaction.common.MessageTypes;
import transaction.server.TransactionServer;
import transaction.util.PropertyHandler;

public class TransactionClient extends Thread {

	public static int numberTransaction = 0;
	public static int numberAccounts;
	public static int initialBalance;

	public static String host;
	public static int port;

	public ArrayList<Thread> threads = new ArrayList<>();
	public static boolean restartTransactions = true;

	public TransactionClient() {

		Properties properties = null;

		try {
			properties = PropertyHandler.readProperties();
			host = properties.getProperty("HOST");
			port = Integer.parseInt(properties.getProperty("PORT"));
			numberAccounts = Integer.parseInt(properties.getProperty("NUMBER_ACCOUNTS"));
			initialBalance = Integer.parseInt(properties.getProperty("INITIAL_BALANCE"));
			numberTransaction = Integer.parseInt(properties.getProperty("NUMBER_TRANSACTIONS"));
			restartTransactions = Boolean.valueOf(properties.getProperty("RESTART_TRANSACTIONS"));
		
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void run() {

		int transactionCounter;
		Thread currentThread;

		Socket dbConnection;
		ObjectOutputStream writeToNet;

		for (transactionCounter = 0; transactionCounter < numberTransaction; transactionCounter++) {
			currentThread = new TransactionThread();
			threads.add(currentThread);
		}

		Iterator<Thread> threadIterator = threads.iterator();

		while (threadIterator.hasNext()) {
			threadIterator.next().start();;
		}
		

//		try {
//			System.out.println("Socket is about to shut down at client");
//			dbConnection = new Socket(host, port);
//			writeToNet = new ObjectOutputStream(dbConnection.getOutputStream());
//			writeToNet.writeObject(new Message(MessageTypes.SHUTDOWN));
//			dbConnection.close();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}

	}

	public class TransactionThread extends Thread {

		int numberTransaction=0;
		@Override
        public void run() {
			
            int transactionID;
            int priorTransactionID = -1;

            int accountFrom;
            int accountTo;

            int amount;
            int balance;

            int returnStatus;
            
//(Math.random() * (max-min)) + min 
            accountFrom = (int) (Math.random() * (numberAccounts -1) + 1);
            accountTo = (int) (Math.random() * (numberAccounts - 1) +1);
            amount = (int) (Math.random() * (initialBalance - 1) + 1);
        
        
            try{
                  Thread.sleep((int) Math.floor(Math.random() * 1000));
               }
               catch( InterruptedException ex){
                  Logger.getLogger(TransactionClient.class.getName()).log(Level.SEVERE, null, ex);
               }


     do{
        TransactionServerProxy transaction = new TransactionServerProxy(host , port);
        transactionID = transaction.openTransaction();
        
         System.out.println("Transaction started");
       
          
    
           balance = transaction.readTransaction(accountFrom);
           System.out.println("Balance -- " + balance);
           System.out.println("Amount -- " + amount);
           transaction.writeTransaction(accountFrom,balance - amount);
     
           try{
             Thread.sleep((int) Math.floor(Math.random() * 1000));
              }
               catch( InterruptedException ex){
                  Logger.getLogger(TransactionClient.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 
                  balance = transaction.readTransaction(accountTo);
                  transaction.writeTransaction(accountFrom,balance + amount);
                  
                  returnStatus = transaction.closeTransaction();
     
                  numberTransaction++;
     }while(numberTransaction < this.numberTransaction);
     
	}
	}
	
	public static void main(String[] args) {
		
		new TransactionClient().run();
	
}
}
