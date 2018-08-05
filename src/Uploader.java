package gos;

public class Uploader implements Runnable {

    //private String data;

    // Doesnt have to be a separate class but in case I need to pass in
    // state data, I'm leaving it for now.
    public Uploader() { //String strData) {
        //this.data = strData;
    }

    public void run() {
        // code in the other thread, can reference "var" variable
        FileTransfer.uploadData();
    }
}
