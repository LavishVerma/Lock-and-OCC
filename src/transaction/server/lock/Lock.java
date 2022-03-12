package transaction.server.lock;



public class Lock {
int lockHoldCount;
   	
   	//Id of thread which is currently holding the lock.
   	long IdOfThreadCurrentlyHoldingLock;
   	
   	/**
   	 * Creates an instance of ReentrantLock.
   	 * Initially lock hold count is 0.
   	 */
   	Lock(){
          	lockHoldCount=0;
   	}
   	
   	/**
   	 * Acquires the lock if it is not held by another thread.
   	 * And sets lock hold count to 1.
   	 * If current thread already holds lock then lock hold
   	 * count is increased by 1.
   	 * If the lock is held by another thread then the current
   	 * thread waits for another thread to release lock.
   	 */
   	public synchronized void lock() {
          	
          	//Acquires the lock if it is not held by another thread.
          	// And sets lock hold count to 1.
          	if(lockHoldCount==0){
                 	lockHoldCount++;
                 	IdOfThreadCurrentlyHoldingLock=Thread.currentThread().getId();
          	}
          	//If current thread already holds lock then lock hold
          	// count is increased by 1.
          	else if(lockHoldCount>0
                       	&& IdOfThreadCurrentlyHoldingLock==Thread.currentThread().getId()){
                 	lockHoldCount++;
          	}
          	//If the lock is held by another thread then the current
          	// thread waits for another thread to release lock.
          	else{
                 	try {
                       	wait();
                       	lockHoldCount++;
                       	IdOfThreadCurrentlyHoldingLock=Thread.currentThread().getId();
                 	} catch (InterruptedException e) {
                       	e.printStackTrace();
                 	}
          	}
   	}
 
   	/**
   	 * If the current thread is the holding the lock then the lock hold
   	 * count is decremented by 1. If the lock hold count has reached 0,
   	 * then the lock is released.
   	 * If lock hold count is still greater than 0 then lock is not released.
   	 * If the current thread is not holding the lock then
   	 * IllegalMonitorStateException is thrown.
 
   	 */
   	public synchronized void unlock() {
          	//current thread is not holding the lock, throw IllegalMonitorStateException.
          	if(lockHoldCount==0)
                 	throw new IllegalMonitorStateException();
          	
          	lockHoldCount--; //decrement lock hold count by 1
          	
          	//if lockHoldCount is 0, lock is released, and
          	//one waiting thread is notified.
   	      if(lockHoldCount==0)
          	  notify();

   	}
   	
   	/**
   	 * Acquires the lock if it is not held by another thread and returns
   	 * true. And sets lock hold count to 1.
   	 * If current thread already holds lock then method
   	 * returns true. And increments lock hold count by 1.
   	 * If lock is held by another thread then method return false.
   	 */
   	public synchronized boolean tryLock(){
          	//Acquires the lock if it is not held by another thread and
          	//returns true
          	if(lockHoldCount==0){
                 	lock();
                 	return true;
          	}
          	//If lock is held by another thread then method return false.
          	else
                 	return false;
   	}
	}



//private Object object;
//private Vector holders;
//private LockType lockType;
//
//public synchronized void acquire(Trans trans,LockType locktype) {
//	
//	while() {
//		try {
//			wait();
//		} catch(InterruptedException e) {
//			
//		}
//	}
//	
//	if(holders.isEmpty()) {
//		holders.addElement(trans);
//		lockType = aLockType;
//	}
//	else if ()
//}
//
//public synchronized void release(TransID trans) {
//	holders.removeElement(trans);
//	notifyAll();
//}
