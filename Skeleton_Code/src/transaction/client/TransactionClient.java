package transaction.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

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
			
			System.out.println("Total accounts -> " + numberAccounts);
			System.out.println("Initial Balance -> " + initialBalance);
			System.out.println("Total Transactions -> " + numberTransaction);
			
			
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void run() {

		int transactionCounter;
		Thread currentThread;

		Socket dbConnection=null;
		ObjectOutputStream writeToNet;

		for (transactionCounter = 0; transactionCounter < numberTransaction; transactionCounter++) {
			currentThread = new TransactionThread();
			threads.add(currentThread);
		}

		Iterator<Thread> threadIterator = threads.iterator();

//		while (threadIterator.hasNext()) {
//			threadIterator.next().start();
//
//		}
//		while (threadIterator.hasNext()) {
//			try {
//				threadIterator.next().join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
		
		//Doing this to make the threads wait
		CountDownLatch latch = new CountDownLatch(numberTransaction);
	    ExecutorService executor = Executors.newCachedThreadPool();
	    IntStream.range(0, numberTransaction).forEach(item -> executor.execute(new TransactionThread(latch)));
	    executor.shutdown();

	    try {
	        latch.await();
	    } catch(InterruptedException ex) {
	        System.out.println(ex);
	    }
	

		try {
			System.out.println(" Closing Socket....");
			dbConnection = new Socket(host, port);
			writeToNet = new ObjectOutputStream(dbConnection.getOutputStream());
			writeToNet.writeObject(new Message(MessageTypes.SHUTDOWN));
			writeToNet.flush();
			dbConnection.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public class TransactionThread extends Thread {
		private CountDownLatch latch;
		int numberTransaction = 0;
		
		TransactionThread(){}
		TransactionThread(CountDownLatch latch){
			this.latch = latch;
		}

		@Override
		public void run() {

			int transactionID;

			int accountFrom;
			int accountTo;

			int amount;
			int balance;

			

//(Math.random() * (max-min)) + min 
			accountFrom = (int) (Math.random() * (numberAccounts - 1) + 1);
			accountTo = (int) (Math.random() * (numberAccounts - 1) + 1);
			amount = (int) (Math.random() * (initialBalance - 1) + 1);

			
			System.out.println("\nAccount FROM -> " + accountFrom);
			System.out.println("Account TO -> " + accountTo );
			System.out.println("Amount -> " + amount);
			try {
				Thread.sleep((int) Math.floor(Math.random() * 1000));
			} catch (InterruptedException ex) {
				Logger.getLogger(TransactionClient.class.getName()).log(Level.SEVERE, null, ex);
			}

			do {
				TransactionServerProxy transaction = new TransactionServerProxy(host, port);
				transactionID = transaction.openTransaction();

				balance = transaction.readTransaction(accountFrom);
				transaction.writeTransaction(accountFrom, balance - amount);

				balance = transaction.readTransaction(accountTo);
				transaction.writeTransaction(accountTo, balance + amount);

				// Making a thread wait for sometime after one transaction.
				try {
					Thread.sleep((int) Math.floor(Math.random() * 1000));
				} catch (InterruptedException ex) {
					Logger.getLogger(TransactionClient.class.getName()).log(Level.SEVERE, null, ex);
				}

				  transaction.closeTransaction();

				numberTransaction++;
			} while (numberTransaction < this.numberTransaction);
					

	        this.latch.countDown();
		}
	}

	public static void main(String[] args) {

		new TransactionClient().run();

	}
}
