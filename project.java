import java.util.*;
import java.io.*;
public class project {

	Hashtable<String, Object> varValues = new Hashtable<>();

    public project() {

    }

	public void print(String var){
		if(varValues.containsKey(var))
			System.out.println(varValues.get(var));
		else
			System.out.println(var);
	}

	public void assign(String var, String next) throws IOException {
		Scanner sc = new Scanner(System.in);
		if(next.equals("input")){
			try{
				varValues.put(var,sc.nextInt());
			}catch(InputMismatchException e){
				varValues.put(var,sc.nextLine());
			}
		}
		else if(next.startsWith("readFile")){
			String[] s = next.split(" ");
			String x;
			if(varValues.containsKey(s[1])) {
				x = this.readFile((String) varValues.get(s[1]));
			}
			else {
				x = this.readFile(s[1]);
			}
			varValues.put(var, x);
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

	public void add(String var1, String var2){
		if(varValues.containsKey(var1) && varValues.containsKey(var2))
			varValues.put(var1,(int) varValues.get(var1) + (int) varValues.get(var2));
	}

	public void parser(String fileName) throws IOException {
		File file = new File(fileName);
		Scanner reader = new Scanner(file);
		while (reader.hasNextLine()){
			String[] line = reader.nextLine().split(" ");
			if(line[0].equals("assign") && !line[2].equals("readFile"))
				assign(line[1],line[2]);
			else if(line[0].equals("assign"))
				assign(line[1],line[2]+" "+line[3]);
			else if(line[0].startsWith("print"))
				print(line[1]);
			else if(line[0].equals("writeFile")) {
				if(varValues.containsKey(line[1]) && varValues.containsKey(line[2]))
					writeFile((String) varValues.get(line[1]), (String) varValues.get(line[2]));
				else if(varValues.containsKey(line[1]))
					writeFile((String) varValues.get(line[1]), line[2]);
				else if(varValues.containsKey(line[2]))
					writeFile(line[1], (String) varValues.get(line[2]));
				else
					writeFile(line[1], line[2]);
			}
			else if(line[0].equals("add"))
				add(line[1],line[2]);
		}

	}


	public static void main(String[] args) throws IOException {
		project p = new project();
		p.parser("Program 2.txt");
	}


}