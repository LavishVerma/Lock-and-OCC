package transaction.server.lock;


public class LockManager {
	
	private boolean isLockingEnabled =false;
	
	public LockManager(boolean flag){
		this.isLockingEnabled = flag;
	}
	
	
	
	Lock lock = new Lock();
	
	public void setLock() {
		if(this.isLockingEnabled)
		lock.acquire(); 
	}
	
	public void setUnlock() {
		if(this.isLockingEnabled)
		lock.release();
	}

}
