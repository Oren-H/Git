import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Blob{

    private String hash;

    public Blob(String fileName) throws Exception{
        Path filePath = Paths.get(fileName);
        String fileContents = Files.readString(filePath);
        hash = getStringHash(fileContents);
        File blobFile = new File("./objects/" + hash);
        blobFile.createNewFile();
        FileWriter fw = new FileWriter(blobFile);
        fw.write(fileContents);
        fw.close();
    }

    //hashes an array of bytes to a string using the Sha1 hash function
    public static String getStringHash(String contents) {
        byte[] b = contents.getBytes();
        String result = "";
        for (int i=0; i < b.length; i++) {
          result +=
                Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

    public String getBlobHash(){
        return hash;
    }
}