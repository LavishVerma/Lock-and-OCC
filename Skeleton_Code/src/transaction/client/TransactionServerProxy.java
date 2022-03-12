package transaction.client;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



public class TransactionServerProxy {
	Socket socket = null;
	ObjectOutputStream oos = null;
	ObjectInputStream ois = null;

	private int port;
	private String host;

	TransactionServerProxy(String host, int port) {
		this.port = port;
		this.host = host;
	}

	public int openTransaction() {
		//Code for Opening a connection with server.
		return 0;
	}

	public int closeTransaction() {
		//Code for Closing a connection with server.
		return 0;

	}

	

	public int readTransaction(int accountNumber) {
		// read Account Balance from server
		
		return 0;

	}

	public void writeTransaction(int account, int amount) {
//		Writing Amount in Account.
	}

}
