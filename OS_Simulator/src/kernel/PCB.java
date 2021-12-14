package kernel;

import java.io.*;

public class PCB {
	int pc;
	int pid;
	int programStart;
	int programSize;
	int state = 0;
	static int counter = 0;
	long start;
	long end;
	boolean isChild;
	int priority;

	// PCB constructor
	public PCB() {
		pid = counter;
		counter++;
		pc = 0;
		programSize = 0;
		state = 0;
		programStart = 0;
		start = 0;
		isChild = false;
		priority = (int) ((Math.random() * (20 - 1)) + 1);;
		
	}

	public void printStatus() {
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
	}



// function to generate a specifc opcode with a given string. First digit of opcode is the type of instruction (calculate, fork, I/O, Critical Section)
	public int generate_opcode(String line) {
		int code = 0;
		char ch1 = line.charAt(0);
		switch (ch1) {
		case 'c':
			code = 10000;
			code += (line.charAt(10) - '0') * 1000;
			code += (line.charAt(11) - '0') * 100;
			code += (line.charAt(13) - '0') * 10;
			code += (line.charAt(14) - '0');
			break;
		case 'I':
			code = 20000;
			break;
		case 'F':
			code = 40000;
			break;
		case 's':
			code += 50000;
			if (line.charAt(7) == '1') {
				code += 1;
			}

		}

		return code;
	}

//Function to simulate execution of a given process. Iterates through an array of opcodes. Function also returns an integer to simulate handling interrupts
//depending on the given opcode. Function takes in a time quantum to help simulate Round Robin Scheduling of CPU.
	public int execute(int time, Semaphore semaph, CPU core) throws InterruptedException {
		
		main.execution[core.threadID-1] = this;
		long start = System.currentTimeMillis();

		if (this.start == 0) {
			this.start = System.currentTimeMillis();
		}
		// ...

		int operation = 0;
		//System.out.println("Executing Process " + this.pid);

		this.state = 2; // running state

		start: for (int i = this.pc; i<this.programSize+this.programStart; i++) {
			operation = main.MainMemory.memory[i] / 10000;
			switch (operation) {
			// "calculate" Opcode
			case 1:
				Thread.sleep(10); // sleep 1 second
				break;

			// "I/O" Opcode
			case 2:
				this.state = 3;
				this.pc = i + 1;
				main.IOQueue.queueEnqueue(this);
				//System.out.println("Process with pid:" + this.pid + " WAITING\n");
				return (1);

			// Criitical section entered
			case 5:
				// System.out.println("Process: "+ this.pid+ "sh");

				switch (main.MainMemory.memory[i] % 10) {

				case 0:
					//System.out.println("Process: " + this.pid + " Entering Critical Section; ");
					// Semaphore.queue.add(this.pid);
					// this.pc = i++;
					// if(Semaphore.numUsed==Semaphore.numAllowed) {
					// Semaphore.mutexLock = true;
					// System.out.println("MAX Critical Section reached");

					break;

				case 1:
					// Semaphore.numUsed--;
					// if(Semaphore.numUsed<Semaphore.numAllowed) Semaphore.mutexLock = false;
					//System.out.println("Process: " + this.pid + " Leaving Critical Section");
					break;

				}

				// Fork opcode
			case 4:
				this.state = 3;
				this.pc = i + 1;
				main.readyQueue.queueEnqueue(this);
				return (2);
			// generate process and execute it

			}

			long finish = System.currentTimeMillis();
			long timeElapsed = finish - start;
			this.end = System.currentTimeMillis();

			// System.out.println("time elapsed:" + timeElapsed);

			// if timer runs out, save pc, change state to ready, enqueue current PCB to
			// ready queue
			if ((timeElapsed) > time) {
				this.pc++;
				this.state = 1;
				main.readyQueue.queueEnqueue(this);
				return 2;

			}
		}
		//System.out.println("Process with pid:" + this.pid + " terminated\n");

		core.turnarounds.add((int) (this.end - this.start));

		this.state = 5;

		return 0;

	}

	//Helper function for testing
	public void printMemory() {
		System.out.println("Printing Memory for PID: " + this.pid);
		for (int i = 0; main.MainMemory.memory[i] != 0; i++) {
			System.out.println(main.MainMemory.memory[i]);
		}
		return;

	}
	
}
