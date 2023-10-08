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

        ArrayList<String> deleted = new ArrayList<String>();
        ArrayList<String> edited = new ArrayList<String>();

        //creates tree from index file
        Tree t = new Tree();
        BufferedReader br = new BufferedReader(new FileReader("index"));

        t.add("tree : " + getShaOfTree(shaOfPrevCommit));
        
        String line = "";
        while((line = br.readLine())!=null){
            if(line.charAt(0)!='*'){
                t.add(line);
            }
            else if (line.charAt(1) == 'd'){
                deleted.add(line);
            }
            else{
                edited.add(line);
            }
        }
        br.close();

        if(deletedOrEdited.size()>0){
            
        }

        String sha = t.writeToFile();

        //replaces index with empty file
        File indexFile = new File("index");
        indexFile.delete();

        File emptyFile = new File("index");
        emptyFile.createNewFile();

        //returns sha of tree object
        return sha;
    }

    public void checkPrevTreeFiles(ArrayList<String> deleted, ArrayList<String> edited){

        String shaOfPrevTree = getShaOfTree(shaOfPrevCommit);

        File prevTree = new File("./objects/" + shaOfPrevTree);

        //initialize arrayList of file names
        ArrayList<String> deletedFileNames = new ArrayList<String>();
        ArrayList<String> editedFileNames = new ArrayList<String>();
        
        for(String deletedFile : deleted){
            deletedFileNames.add(deletedFile.substring(10));
        }

        for(String editedFile : edited){
            editedFileNames.add(editedFile.substring(9));
        }

        ArrayList<String> deletedOrEdited = new ArrayList<String>(); //combined list of file names
        deletedOrEdited.addAll(deletedFileNames);
        deletedOrEdited.addAll(editedFileNames);

        //loop through deleted or edited file
        for(String file : deletedOrEdited){

            String line = Utils.readLineWhichContains("./objects/" + shaOfPrevTree, file);

            

        }
        
        
    }

    public String pointToAllExcept(ArrayList<String> deletedOrEdited, Tree t) throws IOException{

        //create an ArrayList of files to delete and edit
        ArrayList<String> deletedFileNames = new ArrayList<String>();
        ArrayList<String> editedFileNames = new ArrayList<String>();
        ArrayList<String> fileNames = new ArrayList<String>();

        for(String entry : deletedOrEdited){

            //parse through string
            String[] entrySegments = entry.split(" : ");
            String type = entrySegments[0];
            String fileName = entrySegments[2];

            //sort into deleted and edited
            if(type.equals("*deleted* blob")){
                deletedFileNames.add(fileName);
            }
            else{
                editedFileNames.add(fileName);
            }
            fileNames.add(fileName);
        }
        
        //get the sha of the previous commit's tree:
        String shaOfPrevTree = getShaOfTree(shaOfPrevCommit);
        for(String fileName : fileNames){
             String lineContainingFile = Utils.readLineWhichContains("./objects/" + shaOfPrevTree, fileName);
             if(lineContainingFile.equals("")){
                t.add(lineContainingFile);
             }
        }
        String lineContainingFile = Utils.readLineWhichContains("./objects/" + shaOfPrevTree, )

        //check if tree contains any file in deletedOrEdited

    }

    public void pointToFiles()

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

        BufferedReader br = new BufferedReader(new FileReader("./objects/shaOfCommit"));

        String shaOfTree = br.readLine();

        br.close();

        return shaOfTree;
    }
}
