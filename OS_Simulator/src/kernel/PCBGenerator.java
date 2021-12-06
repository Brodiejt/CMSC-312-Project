package kernel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.*;
import java.util.Random;

public class PCBGenerator {
	
	//generates PCB object to simulate a child process
	public PCB forkPCB() {
		PCB forkedPCB = new PCB();
		forkedPCB.isChild = true;

		int Num_of_Instructions = (int) ((Math.random() * (30 - 10)) + 10);
		int sectionStart = (int) ((Math.random() * ((Num_of_Instructions - 3) - 1)) + 1);
		int sectionEnd = sectionStart + 3;

		String calculate = "calculate";
		String io = "IO";
		String fork = "Fork";

		String program = "";

		String[] instructions = { calculate, io, fork };

		for (int i = main.MainMemory.getNext();  i < Num_of_Instructions; i++) {

			if (i == sectionStart) {
				program = program + "section0\n";

			} else if (i == sectionEnd) {
				program = program + "section1\n";

			} else {

				int min_burst = (int) ((Math.random() * (30 - 10)) + 10);
				int max_burst = (int) ((Math.random() * (99 - 31)) + 31);
				int code = (int) ((Math.random() * (2 - 0)) + 0);
				program = instructions[code] + " " + min_burst + " " + max_burst + "\n";

				main.MainMemory.memory[i] = forkedPCB.generate_opcode(program);
			}
			
		}
		forkedPCB.programStart = main.MainMemory.getNext();
		forkedPCB.pc = main.MainMemory.getNext(); //set pc to next available address
		forkedPCB.programSize = Num_of_Instructions; 
		main.MainMemory.setNext(main.MainMemory.getNext()+Num_of_Instructions+1); //change next available addr is memory

		return forkedPCB;
	}
	
//generates template
	public void generateTXT() {
		String fileID = "F";

		String calculate = "calculate";
		String io = "IO";
		String fork = "Fork";

		String path = "src\\kernel\\Programs\\";

		int Num_of_Instructions = (int) ((Math.random() * (100 - 10)) + 10);

		// generate random location for critical section
		int sectionStart = (int) ((Math.random() * ((Num_of_Instructions - 3) - 1)) + 1);
		int sectionEnd = sectionStart + 3;
		
		int forkLoc = (int) ((Math.random() * ((Num_of_Instructions - 3) - 1)) + 1);

		Random rd = new Random();
		// boolean hasFork = rd.nextBoolean();
		boolean hasFork = true;

		int forkLimit = 0;

		String program = "";

		String[] instructions = { calculate, io, fork };

		for (int i = 0; i < Num_of_Instructions; i++) {

			if (i == sectionStart) {
				program = program + "section0\n";

			} else if (i == sectionEnd) {
				program = program + "section1\n";
				

			} else if(i == forkLoc) {
				int min_burst = (int) ((Math.random() * (30 - 10)) + 10);
				int max_burst = (int) ((Math.random() * (99 - 31)) + 31);

				int code = (int) ((Math.random() * (1 - 0)) + 0);
				if (hasFork) {
					code = 2;
					forkLimit++;
					if (forkLimit > 1)
						code = (int) ((Math.random() * (1 - 0)) + 0);
				
			}
			else {
				 min_burst = (int) ((Math.random() * (30 - 10)) + 10);
				 max_burst = (int) ((Math.random() * (99 - 31)) + 31);
				 code = (int) ((Math.random() * (1 - 0)) + 0);
				


				

				}

				program = program + instructions[code] + " " + min_burst + " " + max_burst + "\n";
			}
		}
		int id = (int) ((Math.random() * (999 - 1)) + 1);
		fileID = "F" + id;
		path = path + fileID;

		createFile(path + ".txt", program);

	}

	public void createFile(String path, String program) {

		try {
			File myObj = new File(path);
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		try {
			FileWriter myWriter = new FileWriter(path);

			myWriter.write(program);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return;
	}

	public void cleanFolder() {
		File programs = new File("src\\kernel\\Programs");
		for (File fileEntry : programs.listFiles()) {
			if (fileEntry.getName().charAt(0) == 'F') {
				fileEntry.delete();
				System.out.println("Deleted: " + fileEntry.getName());
			}

		}

	}
}
