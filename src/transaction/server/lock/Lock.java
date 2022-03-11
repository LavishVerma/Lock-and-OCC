package transaction.server.lock;

import java.util.Vector;

public class Lock {

	private Object object;
	private Vector holders;
	private LockType lockType;
	
	public synchronized void acquire(Trans trans,LockType locktype) {
		
		while() {
			try {
				wait();
			} catch(InterruptedException e) {
				
			}
		}
		
		if(holders.isEmpty()) {
			holders.addElement(trans);
			lockType = aLockType;
		}
		else if ()
	}
	
	public synchronized void release(TransID trans) {
		holders.removeElement(trans);
		notifyAll();
	}
}
