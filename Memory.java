import java.io.File;
import java.io.IOException;

public class Memory {

    Block[] memoryBlocks;

    public Memory() throws IOException {
        memoryBlocks = new Block[getProgramCount()];
        for (int i =0; i<memoryBlocks.length; i++)
            memoryBlocks[i] = new Block(new PCB(i+1));
    }

    public int getProgramCount(){
        File file;
        for(int i = 1; ;i++) {
            file = new File("Program " + i + ".txt");
            if(!file.exists())
                return --i;
        }
    }

    public static void main(String[] args) {

    }
}
