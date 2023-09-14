import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blob{

    private String hash;

    public Blob(String fileName) throws Exception{
        Path filePath = Paths.get(fileName);
        String fileContents = Files.readString(filePath);

        hash = getStringHash(fileContents);

        //FIXME: this does not create the objects folder if it doesn't exists
        File blobFile = new File("./objects/" + hash);
        blobFile.createNewFile();

        FileWriter fw = new FileWriter(blobFile);
        fw.write(fileContents);
        fw.close();
    }

    //hashes an array of bytes to a string using the Sha1 hash function
    public static String getStringHash(String input)
    {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getBlobHash(){
        return hash;
    }
}