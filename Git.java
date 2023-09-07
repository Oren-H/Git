import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Git {
    public Git(){
       
    }

    public void init() throws IOException{
        File indexFile = new File("index");
        indexFile.createNewFile();
        File objFolder = new File("objects");
        //objFolder.createNewFile();
        objFolder.mkdir();
    }
    
    public void add(String fileName) throws Exception{
        Blob blob = new Blob(fileName);
        String hash = blob.getBlobHash();
        BufferedWriter bw = new BufferedWriter(new FileWriter("index"));
        bw.write(fileName + " : " + hash);
        bw.newLine();
        bw.close();
    }

    public void remove(String fileName) throws IOException{
        File blobFile = new File("/objects/" + fileName);
        Path filePath = Path.of(fileName);
        String fileContents = Files.readString(filePath);
        File indexFile = new File("index");
        File indexTemp = new File("temp");
        blobFile.delete();


        BufferedReader reader = new BufferedReader(new FileReader(indexFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(indexTemp));

        String lineToRemove = fileName + " : " + Blob.getStringHash(fileContents);
        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            // trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(lineToRemove)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close(); 
        reader.close(); 
        indexTemp.renameTo(indexFile);
    }
}
