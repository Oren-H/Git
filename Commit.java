import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

public class Commit 
{
    String shaOfTreeObj;
    String authorName;
    String Summary;
    String shaOfPrevCommit;
    String shaOfNextCommit;
    String date;

    public Commit(String shaOfPrevCommit, String authorName, String Summary)
    {
        Tree t = new Tree();
        shaOfTreeObj = shaOfTree();
        shaOfPrevCommit = this.shaOfPrevCommit;
        authorName = this.authorName;
        Summary = this.Summary;
        date = getDate();
    }

    public Commit(String authorName, String Summary)
    {
        Tree t = new Tree();
        shaOfTreeObj = shaOfTree();
        shaOfPrevCommit = "";
        authorName = this.authorName;
        Summary = this.Summary;
        date = getDate();
    }

    public void finishCommit() throws IOException
    {
        
        File f = new File("./objects/"+shaOfFileContent());
        f.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter("./objects/"+ shaOfFileContent()));
        bw.write(shaOfTreeObj + "\n");
        bw.write(shaOfPrevCommit + "\n");
        bw.write(shaOfNextCommit + "\n");
        bw.write(date + "\n");
        bw.write(authorName + "\n");
        bw.write(Summary + "\n");
        bw.close();

    }

    private String shaOfTree()
    {
        //online this is the sha of an empty file
        return "da39a3ee5e6b4b0d3255bfef95601890afd80709";
    }

    public void addNextCommitVal(String nextSha) throws IOException
    {
        shaOfNextCommit = nextSha;
        finishCommit();
    }

    public String getDate()
    {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String s = currentDateTime.toString();
        s = s.substring(0,10);
        return s;
    }

    private String commitwithoutprevLine()
    {
        return (shaOfTreeObj +  "\n" + shaOfPrevCommit + "\n" +
         date+ "\n" + authorName + "\n" + Summary);
    }

    public String shaOfFileContent()
    {
        
         try {
            // getInstance() method is called with algorithm SHA-1 
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(commitwithoutprevLine().getBytes());
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


    
}
