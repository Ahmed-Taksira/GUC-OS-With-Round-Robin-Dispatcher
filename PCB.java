public class PCB {

    int processID;
    boolean processState;
    int programCounter;
    int[] boundaries;

    public PCB(int id){
        processID = id;
        processState = true;
        programCounter = 0;
        boundaries = new int[2];
    }

}
