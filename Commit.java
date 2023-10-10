import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
        this.shaOfNextCommit = "";
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
        this.shaOfNextCommit = "";
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
        ArrayList<String> deletedOrEdited = new ArrayList<String>(); //contains all deleted or edited file names

        //look through index file and add non-edited/deleted files
        Tree t = new Tree();

        BufferedReader br = new BufferedReader(new FileReader("index"));

        String line = "";
        while((line = br.readLine())!=null){
            if(line.charAt(0)!='*'){ //if line is not deleted or edited, add it to tree
                t.add(line);
            }
            else if (line.charAt(1) == 'e'){ //if edited

                //add the edited file to deletedOrEdited arraylist
                String editedFileName = line.substring(9);
                deletedOrEdited.add(editedFileName);

                //blob and add the edited file to tree
                Blob editedBlob = new Blob(editedFileName);
                String editedEntry = "blob : " + editedBlob.getBlobHash() + " : " + editedFileName;
                t.add(editedEntry);
            }
            else{

                //add the deleted file to deletedOrEdited arraylist
                String deletedFileName = line.substring(10);
                deletedOrEdited.add(deletedFileName);
            }
        }
        br.close();

        //recursive call
        if(shaOfPrevCommit!=null){
            checkPrevTreeFiles(shaOfPrevCommit, deletedOrEdited, t);
        }

        //create tree
        String sha = t.writeToFile();

        wipeIndexFile();

        //returns sha of tree object
        return sha;
    }

    //recursive method to go into a previous tree and point to all files that aren't deleted/edited
    public void checkPrevTreeFiles(String currCommitSha, ArrayList<String> deletedOrEdited, Tree t) throws IOException{

        //initialize tree file of commit
        String shaOfTree = getShaOfTree(currCommitSha);
        File treeFile = new File("./objects/" + shaOfTree);

        BufferedReader br = new BufferedReader(new FileReader(treeFile));

        //loop through lines in tree
        String treeLine = "";
        while((treeLine = br.readLine())!=null){
            boolean isDeletedOrEdited = false; //if the treeLine is of a deleted or edited file

            //loop through files in deletedOrEdited
            for(String fileName : deletedOrEdited){
                if(treeLine.contains(fileName)){
                    deletedOrEdited.remove(fileName);
                    isDeletedOrEdited = true;
                }
            }

            //add the line to the master tree
            if(!isDeletedOrEdited){
                t.add(treeLine);
            }
        }
        br.close();

        //get the sha of the previous commit
        File commitFile = new File("./objects/" + currCommitSha);
        BufferedReader tempBr = new BufferedReader(new FileReader(commitFile));
        tempBr.readLine();
        String prevCommitSha = tempBr.readLine();
        tempBr.close();

        if(prevCommitSha.equals("")){
            return;
        }
        else if (deletedOrEdited.size() == 0){ 
            //point to the previous tree
            String treeSha = getShaOfTree(prevCommitSha);
            t.add("tree : " + treeSha);
        }
        else{
            //recursive call
            checkPrevTreeFiles(prevCommitSha, deletedOrEdited, t);
        }
    }
    
    //replaces index with empty file
    public void wipeIndexFile() throws IOException{
        File indexFile = new File("index");
        indexFile.delete();

        File emptyFile = new File("index");
        emptyFile.createNewFile();
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
                contents += "\n" + line;
                lineCounter++;
            }
            else{
                contents += ("\n" + shaOfTreeObj);
                lineCounter++;
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

        BufferedReader br = new BufferedReader(new FileReader("./objects/" + shaOfCommit));

        String shaOfTree = br.readLine();

        br.close();

        return shaOfTree;
    }
}
