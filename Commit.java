import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Formatter;

public class Commit 
{
    String shaOfTreeObj;
    String authorName;
    String Summary;
    String shaOfPrevCommit;
    String shaOfNextCommit;
    String date;

    public Commit(String shaOfPrevCommit, String authorName, String Summary) throws Exception
    {
        //creates a tree and saves sha
        shaOfTreeObj = createTreeFromIndex();

        //saves rest of variables
        this.shaOfPrevCommit = shaOfPrevCommit;
        this.authorName = authorName;
        this.Summary = Summary;
        date = getDate();
    }

    public Commit(String authorName, String Summary) throws Exception
    {
        //creates a tree and saves sha
        shaOfTreeObj = createTreeFromIndex();

        //saves rest of variables
        shaOfPrevCommit = "";
        this.authorName = authorName;
        this.Summary = Summary;
        date = getDate();
    }

    //creates a tree from the index file and wipes the index file
    public String createTreeFromIndex() throws Exception{
        //creates tree from index file
        Tree t = new Tree();
        BufferedReader br = new BufferedReader(new FileReader("./objects/index"));
        
        while(br.ready()){
            t.add(br.readLine());
        }
        String sha = t.writeToFile();

        //replaces index with empty file
        File indexFile = new File("./objects/index");
        indexFile.delete();

        File emptyFile = new File("./objects/index");
        emptyFile.createNewFile();

        //returns sha of tree object
        return sha;
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

    public String shaOfFileContent() throws IOException
    {
        String  dataAsString = shaOfTreeObj +  "\n" + shaOfPrevCommit + "\n" + "\n" + date+ "\n" + authorName + "\n" + Summary;

        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(dataAsString.getBytes("UTF-8"));
            sha1 = byteToHexBlob(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    //helper for above method
    public static String byteToHexBlob(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
