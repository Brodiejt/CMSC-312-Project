package kernel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class main {

	// List of turnaround times
	public static ArrayList<Integer> turnarounds = new ArrayList<Integer>();

	// list of wait times
	public static ArrayList<Integer> waits = new ArrayList<Integer>();

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		// System.out.println(System.getProperty("user.dir"));

		System.out.println("Welcome to my Operating System");
		System.out.println("Booting System...");

		int TIME_QUANTUM = 10;
		PCBGenerator generator = new PCBGenerator();
		String[] commands = { "generate", "run", "clean", "stop" };
		String command;
		boolean run = false;
		// Scanner scan = new Scanner(System.in);
		int number;

		while (true) {
			Scanner scan = new Scanner(System.in);
			System.out.println("Enter a command");
			command = scan.nextLine();

			switch (command.charAt(0)) {
			case 'g':
				System.out.println("Enter the number of programs you would like to generate");
				number = scan.nextInt();

				System.out.println("Generating files..");
				for (int i = 0; i < number; i++) {
					generator.generateTXT();
				}
				break;

			case 'r':
				do {

					PCB currentlyRunning;

					int pCounter = 0;

					long IO_WAIT = 10;

					// Open Program folder and parse txt files
					final File programs = new File("src\\kernel\\Programs");

					Queue jobQueue = new Queue(999);
					Queue readyQueue = new Queue(999);
					Queue IOQueue = new Queue(999);

					for (File fileEntry : programs.listFiles()) {
						PCB block = new PCB();

						// System.out.println("Opening file for process: "+ block.pid);
						block.loadToMemory(fileEntry);
						block.state = 2;
						// block.printMemory();
						readyQueue.queueEnqueue(block);
						pCounter++;
						continue;
					}

					// System.out.println("Printing Job Queue");
					// jobQueue.queueDisplay();

					// System.out.println("Printing Ready Queue");
					// readyQueue.queueDisplay();

					Semaphore semaph = new Semaphore(2);

					System.out.println("Round Robin Execution");

					long RRStart = System.currentTimeMillis();

					// Iterate through read queue and execute programs unitl empty (RR executions)
					finish: while (readyQueue.getSize() != 0) {
						long start = System.currentTimeMillis();
						int status = readyQueue.queueDequeue().execute(10, jobQueue, readyQueue, IOQueue, semaph);

						switch (status) {
						case 0:

							continue;

						case 1:
							long finish = System.currentTimeMillis();
							long timeElapsed = finish - start;
							if (readyQueue.getSize() > 0) {
								while (status != 1 || timeElapsed < IO_WAIT) {

									try {
										status = readyQueue.queueDequeue().execute(TIME_QUANTUM, jobQueue, readyQueue,
												IOQueue, semaph);
									} catch (NullPointerException e) {
										break finish;
									}
								}
							}
							System.out.println("Continuing Process with Pid: " + readyQueue.queue[0].pid);
							status = IOQueue.queueDequeue().execute(TIME_QUANTUM, jobQueue, readyQueue, IOQueue,
									semaph);
							break;
						case 2:
							System.out.println("Process ^ FORKED\n");
							generator.forkPCB().execute(TIME_QUANTUM, jobQueue, readyQueue, IOQueue, semaph);
							continue;
						}
					}
					int tavg = 0;

					tavg /= main.turnarounds.size();
					long RRFinish = System.currentTimeMillis();
					System.out.println("Round Robin Executuon Stats:-----------\n" + "Time Complemeted: "
							+ (RRFinish - RRStart) + "\n" + "Average Turnaround Time: \n" + "Average Wait Time: \n");

					// System.out.println("Printing IO Queue");

					// IOQueue.queueDisplay();

					System.out.println("would you like to run again? Y/N");
					if (scan.next().charAt(0) == 'y')
						run = true;
					else
						run = false;

				} while (run);
				System.out.println("Finishing Execution...");
				break;

			case 'c':
				generator.cleanFolder();
				break;

			case 's':
				System.out.println("Closing Program");
				return;

			default:
				System.out.println("Thats not a command sorry :(\n");

			}

		}
	}

}
