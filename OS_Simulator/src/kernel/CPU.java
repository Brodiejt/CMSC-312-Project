package kernel;

import java.util.ArrayList;

public class CPU extends Thread {
	
	// List of turnaround times
	public ArrayList<Integer> turnarounds = new ArrayList<Integer>();

			// list of wait times
	public ArrayList<Integer> waits = new ArrayList<Integer>();

 public int threadID;

	
	public CPU(int id) {
		this.threadID = id;

}
	
}
