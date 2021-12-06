package kernel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main_Memory {
	public int[] memory;
	private int next = 0;
	
	public Main_Memory() {
		this.memory = new int[125000];
	}
	
	// function to parse template or .txt file and generate a file and load each
	// opcode into an array of integers simulating  memory for executuon
	
	public void loadToMemory(File p, PCB block) throws IOException {
		File file = p;
		BufferedReader br = new BufferedReader(new FileReader(file));

		String st;
		int size;
		int index = this.next;

		
		block.programStart = main.MainMemory.getNext();
		block.pc = block.programStart;
		
		while ((st = br.readLine()) != null) {
			this.memory[index] = block.generate_opcode(st);
			index++;
		}
		size = (index - 1) - block.programStart;
		block.programSize = size;
		
		this.next = index++;
		return;
	}
	
	public int getNext() {
		return this.next;
	}
	
	public void setNext(int i) {
		this.next = i;
	}
	
	public void clearBlock(PCB block) {
		
	}
	
	public void cleanMemory() {
		for(int i = 0;i<this.memory.length;i++) {
			this.memory[i]=0;
			this.next = 0;
		}
	}

}
