import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Block {

    PCB pcb;
    Object[] variables;
    Object[] variableNames;
    String[] unParsedCode;
    boolean finished;
    protected int startFrom = 0;
    int quanta;

    public Block(PCB pcb) throws IOException {

        finished = false;

        quanta = 0;

        this.pcb = pcb;

        ArrayList<String> vars = new ArrayList<>();

        unParsedCode = new String[(int) Files.lines(Paths.get("Program "+pcb.processID+".txt")).count()];
        File file = new File("Program "+pcb.processID+".txt");
        Scanner reader = new Scanner(file);
        for(int i = 0; i< unParsedCode.length; i++) {
            unParsedCode[i] = reader.nextLine();
            String[] s = unParsedCode[i].split(" ");
            if(s[0].equals("assign") && !vars.contains(s[1]))
                vars.add(s[1]);
        }

        variables = new Object[vars.size()];
        variableNames = new Object[vars.size()];

        pcb.boundaries[0] = startFrom;
        pcb.boundaries[1] = startFrom += vars.size() + unParsedCode.length + 4;
        startFrom++;

    }

    public static void main(String[] args) {

    }

}
