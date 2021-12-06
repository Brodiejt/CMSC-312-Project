package kernel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class main {

	
	
	//PCB queues
	public static Queue jobQueue = new Queue(999);
	public static Queue readyQueue = new Queue(999);
	public static Queue IOQueue = new Queue(999);
	
	//Time quantum for RR execution
	public static int TIME_QUANTUM = 30;
	
	//IO-Random event wait
	public static long IO_WAIT = 60;
	
	//Array of all process currently in execution
	public static PCB[] execution = new PCB[8];
	
	
	public static PCBGenerator generator = new PCBGenerator();
	
	//Main Memory object to simulate physical memory
	public static Main_Memory MainMemory = new Main_Memory();

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		// System.out.println(System.getProperty("user.dir"));

		
		System.out.println("Welcome to my Operating System");
		System.out.println("Booting System...");

	
		String[] commands = { "generate", "run", "clean", "stop" };
		String command;
		boolean run = false;
		// Scanner scan = new Scanner(System.in);
		int number;

		
		Scanner scan = new Scanner(System.in);
		System.out.println("To start off, would you like to generate program files? (Y/N)");
		command = scan.nextLine();

		switch (command.charAt(0)) {
		case 'y'|'Y':
			System.out.println("Enter the number of programs you would like to generate");
			number = scan.nextInt();

			System.out.println("Generating files..");
			for (int i = 0; i < number; i++) {
				generator.generateTXT();
			}
			break;

		case 'n'|'N':
			break;

		default:
			System.out.println("Thats not a valid response sorry :(\n");

		}
		
		bootSystem();
		
		
		
	}
	
	public static void bootSystem() throws IOException {
		System.out.println("Executing Processes..");
		PCB currentlyRunning;

		int pCounter = 0;

		// Open Program folder and parse txt files
		final File programs = new File("src\\kernel\\Programs");




		for (File fileEntry : programs.listFiles()) {
			PCB block = new PCB();

			// System.out.println("Opening file for process: "+ block.pid);
			MainMemory.loadToMemory(fileEntry, block);
			block.state = 2;
			// block.printMemory();
			readyQueue.queueEnqueue(block);
			pCounter++;
			continue;
		}
		
		//Initialize Command Line Interface
		(new CLI()).start();

		//Create CPU core Threads to simulate multiprocessing
		for(int i=1;i<=8;i++) {
			(new RRCPU(i)).start();
		}
	}

}
