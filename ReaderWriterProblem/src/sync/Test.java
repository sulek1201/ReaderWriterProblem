package sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


public class Test {
	public static void main(String [] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		ReadWriteLock RW = new ReadWriteLock();
		
		
		executorService.execute(new Writer(RW));
		executorService.execute(new Writer(RW));
		executorService.execute(new Writer(RW));
		executorService.execute(new Writer(RW));
		
		executorService.execute(new Reader(RW));
		executorService.execute(new Reader(RW));
		executorService.execute(new Reader(RW));
		executorService.execute(new Reader(RW));
		
		
	}
}


class ReadWriteLock{
	static int readerCount = 0;
    static Semaphore smhpr1 = new Semaphore(1);
    static Semaphore smhpr2 = new Semaphore(1);
	
	public void readLock() {
		 try {
			smhpr1.acquire();
	         readerCount++;
	         if (readerCount == 1) smhpr2.acquire();
	         smhpr1.release();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
	}
	public void writeLock() {
		try {
			smhpr2.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void readUnLock() {
		try {
			smhpr1.acquire();
		     readerCount--;
		        if (readerCount == 0) smhpr2.release();
		        Thread.sleep(1000);
		        smhpr1.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			smhpr2.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		smhpr2.release();
   
	
		
		
	}
	public void writeUnLock() {
		 
		
		
		smhpr2.release();
		 
		 try {
			Thread.sleep(1005);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		smhpr1.release();
		try {
			smhpr1.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}




class Writer implements Runnable
{
   private ReadWriteLock RW_lock;
   

    public Writer(ReadWriteLock rw) {
    	RW_lock = rw;
   }

    public void run() {
      while (true){
    	  RW_lock.writeLock();
    	  
    	  System.out.println("Thread "+Thread.currentThread().getName() + " is WRITING");
//          try {
//			Thread.sleep(2500);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
          System.out.println("Thread "+Thread.currentThread().getName() + " has finished WRITING");
    	
    	  RW_lock.writeUnLock();
       
      }
   }


}



class Reader implements Runnable
{
   private ReadWriteLock RW_lock;
   

   public Reader(ReadWriteLock rw) {
    	RW_lock = rw;
   }
    public void run() {
     while (true){ 	    	  
    	  RW_lock.readLock();
    	  System.out.println("Thread "+Thread.currentThread().getName() + " is READING");
//          try {
//			Thread.sleep(1500);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
          System.out.println("Thread "+Thread.currentThread().getName() + " has FINISHED READING");
          
    	  
    	  RW_lock.readUnLock();
       
      }
   }


}