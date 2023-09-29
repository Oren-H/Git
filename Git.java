import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Git {
    public Git(){
    }

    public void init() throws IOException{
        File indexFile = new File("index");
        if(!indexFile.exists()){
             indexFile.createNewFile();
        }
        File objFolder = new File("objects");
        if(!objFolder.exists()){
            objFolder.mkdir();
        }
    }
    
    //blobs a file and adds it to index
    public void addFile(String fileName) throws Exception{
        
        Blob blob = new Blob(fileName);

        String hash = blob.getBlobHash();

        BufferedWriter bw = new BufferedWriter(new FileWriter("index", true));

        bw.write("blob : " + hash + " : " + fileName);
        bw.newLine();
        bw.close();
    }

    //blobs an entire directory as a tree and adds to index
    public void addDirectory(String directoryName) throws Exception{
       
        //create tree from directory
        Tree dir = new Tree();

        dir.addDirectory(directoryName);

        String hash = dir.writeToFile();

        //add to index
        BufferedWriter bw = new BufferedWriter(new FileWriter("index", true));

        bw.write("tree : " + hash + " : " + directoryName);
        bw.newLine();
        bw.close();
    }

    public void remove(String fileName) throws IOException{
        Path filePath = Path.of(fileName);
        String fileContents = Files.readString(filePath);
        File indexFile = new File("index");
        File indexTemp = new File("temp");

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

    public boolean isEqual(Path firstFile, Path secondFile)
    {
        try {
            if (Files.size(firstFile) != Files.size(secondFile)) {
                return false;
            }
 
            byte[] first = Files.readAllBytes(firstFile);
            byte[] second = Files.readAllBytes(secondFile);
            return Arrays.equals(first, second);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
