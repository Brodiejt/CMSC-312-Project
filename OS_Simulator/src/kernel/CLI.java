package kernel;

import java.io.IOException;
import java.util.Scanner;
public class CLI extends Thread {
	
	//Thread to simulate Command Line Interface that has basic commands
	
	@Override
	public void run() {
		
		String prompt = "OS_Command_Prompt>";
		String command;
		Scanner scan = new Scanner(System.in);
		/*
		 * Commands -------------------
		 * status: Prints the status of all process in execution
		 * 
		 * quit: exits the program
		 * 
		 * g: generates a program by calling fork
		 * 
		 * gtxt: generates a program file template
		 * 
		 * run: re-executes all programs
		 * 
		 * clean: deletes all txt files from "Progams" folder.
		 * 
		 */
		
		while(true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.print(prompt);
			command  = scan.nextLine();
			switch(command) {
			case "status":
				String[] states =  {"NEW", "READY", "RUNNING", "WAITING", "", "TERMINATED"};
				System.out.println("Printing current Processes in execution");
				//System.out.println("--------------Processors--------------");
				for(int i=0; i<8;i++) {
					PCB curr = main.execution[i];
					
					System.out.println("-------Processor: " + (i+1) + " -----------\n"+
									   "Process ID: " + curr.pid +"\n"
									   		+ "Process State: " + states[curr.state]+ "\n"
									   				+ "Memory Allocated: Mem["+curr.programStart+"] to Mem["+(curr.programStart
									   				+curr.programSize)+"]\n"
									   						+ "");
				}
				break;
			case "quit":
				System.exit(0);
				return;
				
			case "g":
				System.out.println("Creating new Process");
				main.readyQueue.queueEnqueue(main.generator.forkPCB());
				break;
				
			case "run":
				main.MainMemory.cleanMemory();
				try {
					main.bootSystem();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			case "gtxt":
				int number;
				System.out.println("Enter the number of programs you would like to generate");
				number = scan.nextInt();

				System.out.println("Generating files..");
				for (int i = 0; i < number; i++) {
					main.generator.generateTXT();
				}
				
				break;
			
			case "clean":
				main.generator.cleanFolder();
				break;
			default:
				System.out.println("\nThat is not a command sorry :(");
			}
			
		}
		
	}


}
