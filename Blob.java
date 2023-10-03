import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blob{

    private String hash;

    public Blob(String fileName) throws Exception{
        String fileContents = Utils.readFileToString(fileName);

        hash = getStringHash(fileContents);

        //create objects folder
        File dir = new File("./objects");
        dir.mkdirs();

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