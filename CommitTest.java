import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CommitTest {
    @BeforeAll
    static void runBeforeAll() throws IOException{
        Git.init();
        
        //test 1 files
        Utils.writeStringToFile("content 1", "file1.txt");
        Utils.writeStringToFile("content 2", "file2.txt");

        //test 2 files
        File dir1 = new File ("directory 1");
        dir1.mkdir();
        Utils.writeStringToFile("content 3", "file3.txt");
        Utils.writeStringToFile("content 4", "./directory 1/file4.txt");

        //test 3 files
        File dir2 = new File("directory 2");
        dir2.mkdir();
        Utils.writeStringToFile("content 5", "file5.txt");
        Utils.writeStringToFile ("content 6", "./directory 2/file6.txt");
        
        //test 4 files
        Utils.writeStringToFile("content 7", "file7.txt");
        Utils.writeStringToFile("content 8", "file8.txt");
    }

    @AfterAll
    static void runAfterAll(){
        File index = new File("index");
        Utils.clearDirectory("objects");
        index.delete();
    }

    @Test
    void addFirstCommit() throws Exception{
        Git.init();
        //add file to index
        Git.addFile("file1.txt");
        Git.addFile("file2.txt");

        //create commit
        Commit commit1 = new Commit("Oren H", "This is the first commit");
        commit1.finishCommit();

        runBasicCommitTests(commit1, null, null, 2);

        testIndexWipe();
        Utils.clearDirectory("objects");
    }

    @Test
    void addSecondCommit() throws Exception{
        Git.init();

        //add file to index
        Git.addFile("file1.txt");
        Git.addFile("file2.txt");
        Commit commit1 = new Commit("Oren H", "This is the first commit");
        commit1.finishCommit();

        Git.addFile("file3.txt");
        Git.addDirectory("directory 1");
        Commit commit2 = new Commit(commit1.getShaOfCommit(), "Oren H", "This is the first commit");
        commit2.finishCommit();
        
        runBasicCommitTests(commit1, null, commit2, 2);
        runBasicCommitTests(commit2, commit1, null, 2);

        testIndexWipe();
        Utils.clearDirectory("objects");
    }

    @Test
    void addThirdAndFourthCommit() throws Exception{
        Git.init();
        
        Git.addFile("file1.txt");
        Git.addFile("file2.txt");
        Commit commit1 = new Commit("Oren H", "This is the first commit");
        commit1.finishCommit();

        Git.addFile("file3.txt");
        Git.addDirectory("directory 1");
        Commit commit2 = new Commit(commit1.getShaOfCommit(), "Oren H", "This is the first commit");
        commit2.finishCommit();

        //add files to index for commit 3
        Git.addFile("file5.txt");
        Git.addDirectory("directory 2");

        Commit commit3 = new Commit(commit2.getShaOfCommit(), "Oren H", "This is the third commit");
        commit3.finishCommit();

        //create commit 4
        Git.addFile("file7.txt");
        Git.addFile("file8.txt");

        Commit commit4 = new Commit(commit3.getShaOfCommit(), "Oren H", "This is the fourth commit");
        commit4.finishCommit();

        runBasicCommitTests(commit1, null, commit2, 2);
        runBasicCommitTests(commit2, commit1, commit3, 2);
        runBasicCommitTests(commit3, commit2, commit4, 2);
        runBasicCommitTests(commit4, commit3, null, 2);

        testIndexWipe();
        Utils.clearDirectory("objects");
    }

    public void runBasicCommitTests(Commit commit, Commit prevCommit, Commit nextCommit, int filesAdded) throws IOException{
        //test if sha is valid
        String commitSha = commit.getShaOfCommit();
        assertTrue(commitSha.length() == 40, "commit length is invalid");

        //test if file was made
        File commitFile = new File("./objects/" + commitSha);
        assertTrue(commitFile.exists());

        //test if files contains correct number of lines
        assertTrue(Utils.numOfLines(commitFile)==6);

        //test tree file
        String treeSha = commit.shaOfTreeObj;
        File treeFile = new File("./objects/" + treeSha);
        assertTrue(treeFile.exists());
        assertTrue(Utils.numOfLines(treeFile)==filesAdded);

        //test prev and next shas if they exist
        String prevSha = "";
        String nextSha = "";
        if(prevCommit != null){
            prevSha = prevCommit.getShaOfCommit();
        }
        if(nextCommit != null){
            nextSha = nextCommit.getShaOfCommit();
        }
        assertTrue(Utils.getLine(commitFile, 2).equals(prevSha));
        assertTrue(Utils.getLine(commitFile, 3).equals(nextSha));
    }

    public void testIndexWipe() throws Exception{
        String indexContents = Utils.readFileToString("index");
        assertTrue(indexContents.equals(""));
    }
}
