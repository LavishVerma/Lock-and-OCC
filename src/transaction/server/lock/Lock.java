package transaction.server.lock;

public class Lock {
	int lockHoldersCount;

	long threadIDHoldingLock;

	Lock() {
		lockHoldersCount = 0;
	}

	public synchronized void acquire() {

		if (lockHoldersCount == 0) {
			lockHoldersCount++;
			threadIDHoldingLock = Thread.currentThread().getId();
		}

		else if (lockHoldersCount > 0 && threadIDHoldingLock == Thread.currentThread().getId()) {
			lockHoldersCount++;
		}

		else {
			try {
				wait();
				lockHoldersCount++;
				threadIDHoldingLock = Thread.currentThread().getId();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void release() {

		if (lockHoldersCount == 0)
			throw new IllegalMonitorStateException();

		lockHoldersCount--;

		if (lockHoldersCount == 0)
			notify();

	}

	public synchronized boolean tryLock() {

		if (lockHoldersCount == 0) {
			acquire();
			return true;
		}

		else
			return false;
	}
}
