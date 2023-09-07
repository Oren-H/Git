import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Git {
    public Git(){
       
    }

    public void init() throws IOException{
        File indexFile = new File("index.txt");
        File objFolder = new File("objects");
        objFolder.mkdir();
    }
    
    public void add(String fileName) throws Exception{
        Blob blob = new Blob(fileName);
        String hash = blob.getHash();
        BufferedWriter bw = new BufferedWriter(new FileWriter("index.txt"));
        bw.write(fileName + ": " + hash);
        bw.newLine();
        bw.close();
    }
}
