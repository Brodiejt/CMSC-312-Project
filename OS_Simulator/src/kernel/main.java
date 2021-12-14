package kernel;

import java.util.*;
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class main {

	
	//Command Line Interface Thread
	public static CLI CL = new CLI();
	
	//PCB queues
	public static Queue jobQueue = new Queue(999);
	public static Queue readyQueue = new Queue(999);
	public static Queue IOQueue = new Queue(999);
	
	
	//custom PCB comparator for priority queue
	public static Comparator<PCB> customComparator = new Comparator<PCB>() {
        @Override
        public int compare(PCB s1, PCB s2) {
            return s1.priority - s2.priority;
        }
    };
	
	//priority Ready Queue
	public static PriorityQueue<PCB> pReadyQueue = new PriorityQueue<PCB>(customComparator);
	
	//Time quantum for RR execution
	public static int TIME_QUANTUM = 5;
	
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

		int counter = 0;

		// Open Program folder and parse txt files
		final File programs = new File("src\\kernel\\Programs");




		for (File fileEntry : programs.listFiles()) {
			PCB block = new PCB();

			// System.out.println("Opening file for process: "+ block.pid);
			MainMemory.loadToMemory(fileEntry, block);
			block.state = 2;
			
			
			//Stores half of the programs into priority ready queue
			if(counter%2==0) {
			readyQueue.queueEnqueue(block);
			}
			else {
				pReadyQueue.add(block);
			}
			counter++;
			
			continue;
		}
		
		//Initialize Command Line Interface
		if(!CL.isAlive()) {
		CL.start();
		}

		//Create CPU core Threads to simulate multiprocessing
		for(int i=1;i<=4;i++) {
			(new RRCPU(i)).start();
			(new PCPU(i+4)).start();
		}
	}

}
