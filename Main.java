import java.util.*;
import java.io.*;
public class Main {

	Memory memory;

    public Main() throws IOException {
		memory = new Memory();
    }
	// In our implementation, we assumed that the order in the memory is always PCB->lines of code->variables.

    public void scheduler() throws IOException {
    	int time = 0;
    	int processesFinished = 0;
		for(int i = 0; i<memory.memoryBlocks.length && processesFinished < memory.memoryBlocks.length;i++){
			if(!memory.memoryBlocks[i].finished && memory.memoryBlocks[i].pcb.processState){
				for(int j = 0; j<2; j++){
					System.out.println("Time: " + time);
					String s = memory.memoryBlocks[i].unParsedCode[memory.memoryBlocks[i].pcb.programCounter];
					System.out.println("Process " + memory.memoryBlocks[i].pcb.processID + ": " + s);
					System.out.println("State: "+memory.memoryBlocks[i].pcb.processState);
					System.out.println("Process Index in Memory: "+(memory.memoryBlocks[i].pcb.boundaries[0] + memory.memoryBlocks[i].pcb.programCounter + 4));
					System.out.println("Program Counter: "+(memory.memoryBlocks[i].pcb.programCounter+1));
					parser(s, memory.memoryBlocks[i]);
					time++;
					memory.memoryBlocks[i].pcb.programCounter++;

					if(memory.memoryBlocks[i].pcb.programCounter == memory.memoryBlocks[i].unParsedCode.length) {
						memory.memoryBlocks[i].finished = true;
						processesFinished++;
						memory.memoryBlocks[i].quanta++;
						System.out.println("Process " + memory.memoryBlocks[i].pcb.processID + " has finished in " + memory.memoryBlocks[i].quanta+" quanta.");
						System.out.println("--------------------------------------");
						break;
					}
					System.out.println("--------------------------------------");
				}
				memory.memoryBlocks[i].quanta++;
				memory.memoryBlocks[i].pcb.processState = false;
			}
			if(i == memory.memoryBlocks.length-1) {
				i = -1;
				memory.memoryBlocks[0].pcb.processState = true;
			}
			else
				memory.memoryBlocks[i+1].pcb.processState = true;
		}
	}

	public void print(String var, Block block){
		if(includes(block.variableNames, var))
			System.out.println(block.variableNames[getIndex(block.variableNames, var)]+" = "+block.variables[getIndex(block.variableNames, var)]);
		else
			System.out.println(var);
	}

	public void assign(String var, String next, Block block) throws IOException {
		Scanner sc = new Scanner(System.in);
		if(next.equals("input")){
			try{
				put(block,var,sc.nextInt());
			}catch(InputMismatchException e){
				put(block,var,sc.nextLine());
			}
		}
		else if(next.startsWith("readFile")){
			String[] s = next.split(" ");
			String x;
			if(includes(block.variableNames,s[1])) {
				x = this.readFile((String) block.variables[getIndex(block.variableNames, s[1])]);
			}
			else {
				x = this.readFile(s[1]);
			}
			try {
				int z = Integer.parseInt(x);
				put(block,var,z);
			}
			catch (NumberFormatException e) {
				put(block,var,x);
			}
		}
	}

	private void put(Block block, String var, Object data) {
		if(includes(block.variableNames, var)){
			int i = getIndex(block.variableNames, var);
			block.variables[i] = data;
		}
		else {
			for(int i = 0; i<block.variables.length; i++){
				if(block.variables[i] == null){
					block.variableNames[i] = var;
					block.variables[i] = data;
					break;
				}
			}
		}
	}

	public String readFile(String fileName) throws IOException{
		File file = new File(fileName);
		Scanner reader = new Scanner(file);
		if(reader.hasNextLine())
			return reader.nextLine();
		return null;
	}

	public void writeFile(String fileName, String data) throws IOException {
		FileWriter myWriter = new FileWriter(fileName);
		myWriter.write(data);
		myWriter.close();
	}

	public void add(String var1, String var2, Block block){
		if(includes(block.variableNames, var1) && includes(block.variableNames, var2))
			block.variables[getIndex(block.variableNames, var1)] = (int) block.variables[getIndex(block.variableNames, var1)] + (int) block.variables[getIndex(block.variableNames, var2)];
	}

	public void parser(String instruction, Block block) throws IOException {
    	String[] line = instruction.split(" ");
    	if(line[0].equals("assign") && !line[2].equals("readFile"))
    		assign(line[1],line[2], block);
    	else if(line[0].equals("assign"))
    		assign(line[1],line[2]+" "+line[3],block);
    	else if(line[0].startsWith("print"))
    		print(line[1], block);
    	else if(line[0].equals("writeFile")) {
    		if(includes(block.variableNames, line[1]) && includes(block.variableNames, line[2]))
    			writeFile((String) block.variables[getIndex(block.variableNames, line[1])], block.variables[getIndex(block.variableNames, line[2])].toString());
    		else if(includes(block.variableNames, line[1]))
    			writeFile((String) block.variables[getIndex(block.variableNames, line[1])], line[2]);
    		else if(includes(block.variableNames, line[2]))
    			writeFile(line[1], (String) block.variables[getIndex(block.variableNames, line[1])]);
    		else
    			writeFile(line[1], line[2]);
			}
    	else if(line[0].equals("add"))
    		add(line[1],line[2], block);
	}

	public boolean includes(Object[] array, Object obj){
		boolean found = false;

		for(Object x : array){
			if(x != null && x.equals(obj)){
				found = true;
				break;
			}
		}

		return  found;
	}

	public int getIndex(Object[] array, Object obj){

		for(int i = 0; i<array.length; i++){
			if(array[i].equals(obj))
				return i;
		}

		return 0;

	}


	public static void main(String[] args) throws IOException {
		Main main = new Main();
		main.scheduler();
	}


}