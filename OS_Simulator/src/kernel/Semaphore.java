package kernel;

import java.util.ArrayList;

public class Semaphore {
	public static int numAllowed;
	public static boolean mutexLock;
	public static int numUsed;
	public static ArrayList<Integer> queue;
	
	public Semaphore(int num) {
		this.numAllowed = num;
		mutexLock = false;
		numUsed =0 ;
		queue = new ArrayList<Integer>();
	
	}

}
