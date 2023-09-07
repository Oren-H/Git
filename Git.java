import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
        bw.write(fileName + " : " + hash);
        bw.newLine();
        bw.close();
    }

    public void remove(String fileName) throws IOException{
        File blobFile = new File(".\\objects\\" + fileName);
        File indexFile = new File("index.txt");
        File indexTemp = new File("temp.txt");
        blobFile.delete();


        BufferedReader reader = new BufferedReader(new FileReader(indexFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(indexTemp));

        String lineToRemove = fileName + " : " + blo;
        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            // trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(lineToRemove)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close(); 
        reader.close(); 
        boolean successful = indexTemp.renameTo(indexFile);
    }
}
