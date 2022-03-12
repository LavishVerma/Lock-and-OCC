package transaction.server.transaction;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import transaction.client.TransactionClient.TransactionThread;
import transaction.common.Message;
import transaction.common.MessageTypes;
import transaction.server.TransactionServer;

public class TransactionManager {

	private static int transactionIdCounter = 1;
	

	public int getTransactionIdCounter() {
		return transactionIdCounter;
	}

	public void runTransaction(Socket client) {

		// Creating a new Thread on receiving a new Socket Request.
		Thread currentThread;

		currentThread = new TransactionManagerWorker(client);
		currentThread.start();

	}

	public class TransactionManagerWorker extends Thread {

		Socket client;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		Transaction transaction = null;
		private boolean isShutdown = false;

		public boolean getIsShutdown() {
			return isShutdown;
		}

		public TransactionManagerWorker(Socket client) {
			this.client = client;
		}

		@Override
		public void run() {

			++transactionIdCounter;

			// Read/Write from server TransactionID
			try {
				
				//Setting a lock
				TransactionServer.lockManager.setLock();
				
				oos = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
				ois = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
			} catch (IOException e1) {

				e1.printStackTrace();
			}

			// Keeps ruuning until we receive the close request.
			while (true) {

				try {

					Message message = (Message) ois.readObject();

					if (message != null) {

						if (message.getMessageID() == MessageTypes.OPEN_TRANSACTION) {
							System.err.println("OPEN_TRANSACTION");
							transaction = new Transaction();
							
							oos.writeObject(transaction.getTransactionID());
							oos.flush();

						} else if (message.getMessageID() == MessageTypes.READ_REQUEST) {
							transaction = new Transaction();
							System.err.println("READ_REQUEST");
							oos.writeObject(transaction.read(message.getAccountNumber()));
							oos.flush();
						} else if (message.getMessageID() == MessageTypes.WRITE_REQUEST) {
							System.err.println("WRITE REQUEST");
							transaction.write(message.getAccountNumber(), message.getAmount());

						} else if (message.getMessageID() == MessageTypes.CLOSE_TRANSACTION) {
							System.err.println("CLOSE REQUEST");

							oos.flush();
							oos.close();
							ois.close();
							client.close();
							break;

						} else if (message.getMessageID() == MessageTypes.SHUTDOWN) {

							System.err.println("SHUTTING IT DOWN");
							TransactionServer.shutdown();
							isShutdown = true;
							break;
						}
					}

				} catch (ClassNotFoundException | IOException e) {

					e.printStackTrace();
				} finally {

				}

			}
			// Unsetting a Lock
			TransactionServer.lockManager.setUnlock();

		}
	}

}
