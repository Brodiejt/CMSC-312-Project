package kernel;

import java.util.ArrayList;

//Simulation of CPU core using round-Robin execution
public class RRCPU extends CPU {
	
	
	public RRCPU(int id) {
		super(id);
	}
	

	
	@Override
	public void run() {
		
		Semaphore semaph = new Semaphore(2);

		System.out.println("Round Robin Execution: (Core: "+threadID+")");

		long RRStart = System.currentTimeMillis();
		
		boolean waited = false;

		// Iterate through read queue and execute programs unitl empty (RR executions)
		finish: while (main.readyQueue.getSize() != 0) {
			
			long start = System.currentTimeMillis();
			int status = 0;
			
			
			try {
				try {
					status = main.readyQueue.queueDequeue().execute(10, semaph, this);
					continue;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (NullPointerException e1) {
				// TODO Auto-generated catch block
				continue;
			}

			switch (status) {
			case 0:
				if(main.readyQueue.getSize() == 0) {

					//wait 15 seconds for user to enter another process, check status' or to see if ready queue re fills
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else
				
				continue;

			case 1:
				long finish = System.currentTimeMillis();
				long timeElapsed = finish - start;
				if (main.readyQueue.getSize() > 0) {
					while (status != 1 || timeElapsed < main.IO_WAIT) {

						try {
							status = main.readyQueue.queueDequeue().execute(main.TIME_QUANTUM,  semaph, this);
							continue;
						} catch (NullPointerException e) {
							break finish;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				System.out.println("Continuing Process with Pid: " + main.readyQueue.queue[0].pid);
				try {
					status = main.IOQueue.queueDequeue().execute(main.TIME_QUANTUM, semaph, this);
					continue;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2:
				//System.out.println("Process ^ FORKED\n");
				try {
					main.generator.forkPCB().execute(main.TIME_QUANTUM,  semaph, this);
					continue;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			
			
		}
		
		long RRFinish = System.currentTimeMillis();
		int tavg = 0;

		try {
		for(long time : this.turnarounds)
			tavg += (int)time;
		}
		catch(ArithmeticException e) {
			System.out.println("No Procesess ran on Core: "+this.threadID);
		}
		
		//(main.CL.isAlive())
			//main.CL.stop();
			
		System.out.println("\nRound Robin Executuon Stats:-----------\n" +"CPU Core: "+ this.threadID +"\nTime Complemeted: "
				+ (RRFinish - RRStart) + " milliseconds\n" + "Average Turnaround Time: "+tavg+" milliseconds");
		
		//main.CL.start();


	}
	public int getThreadID() {
		return this.threadID;
	}

}
