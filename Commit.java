import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

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

        //edit the nextCommit line of the previous Commit file
        editPreviousCommit();
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

    public void finishCommit() throws IOException
    {
        
        File f = new File("./objects/"+getShaOfCommit());
        f.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter("./objects/"+ getShaOfCommit()));
        bw.write(shaOfTreeObj + "\n");
        bw.write(shaOfPrevCommit + "\n");
        bw.write(shaOfNextCommit + "\n");
        bw.write(authorName + "\n");
        bw.write(date + "\n");
        bw.write(Summary + "\n");
        bw.close();

    }

    //creates a tree from the index file and wipes the index file
    public String createTreeFromIndex() throws Exception{

        //creates tree from index file
        Tree t = new Tree();
        BufferedReader br = new BufferedReader(new FileReader("index"));
        
        while(br.ready()){
            t.add(br.readLine());
        }
        br.close();

        String sha = t.writeToFile();

        //replaces index with empty file
        File indexFile = new File("index");
        indexFile.delete();

        File emptyFile = new File("index");
        emptyFile.createNewFile();

        //returns sha of tree object
        return sha;
    }

    //add a nextSha to previousCommit line
    public void editPreviousCommit() throws IOException{

        //access the previous commit
        String contents = "";
        String prevCommitPath = "./objects/" + shaOfPrevCommit;
        File prevCommit = new File(prevCommitPath);
        BufferedReader br = new BufferedReader(new FileReader(prevCommit));
        
        //create the new file contents of the previous commit
        int lineCounter = 0;
        String line = "";
        while((line = br.readLine())!=null){
            if(lineCounter != 2){
                contents += line;
                lineCounter++;
            }
            else{
                contents += ("\n" + shaOfTreeObj);
            }
        }
        br.close();

        //replace the previous commit with the new one
        prevCommit.delete();
        Utils.writeStringToFile(contents, prevCommitPath);
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

    public String getShaOfCommit() throws IOException
    {
        String  dataAsString = shaOfTreeObj +  "\n" + shaOfPrevCommit + "\n" + "\n" + authorName + "\n" + date + "\n" + Summary;
        return Blob.getStringHash(dataAsString);
    }

    //returns the sha of a tree based on the sha of its commit
    public static String getShaOfTree(String shaOfCommit) throws IOException{

        BufferedReader br = new BufferedReader(new FileReader("./objects/shaOfCommit"));

        String shaOfTree = br.readLine();

        br.close();

        return shaOfTree;
    }
}
