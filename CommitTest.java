import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
        runBasicCommitTests(commit2, commit1, null, 3);

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
        runBasicCommitTests(commit2, commit1, commit3, 3);
        runBasicCommitTests(commit3, commit2, commit4, 3);
        runBasicCommitTests(commit4, commit3, null, 3);

        testIndexWipe();
        Utils.clearDirectory("objects");
    }

    @Test
    void addAndDeleteOneFile() throws Exception{
        Git.init();
        Git.addFile("file1.txt");
        Commit commit1 = new Commit("Oren H", "Added file");
        commit1.finishCommit();

        Git.deleteFile("file1.txt");
        Commit commit2 = new Commit(commit1.getShaOfCommit(), "Oren H", "Deleted file");
        commit2.finishCommit();

        runBasicCommitTests(commit1, null, commit2, 1);
        runBasicCommitTests(commit2, commit1, null, 0);

        testIndexWipe();
        Utils.clearDirectory("objects");
    }

    @Test
    void addAndEditOneFile() throws Exception{
        Git.init();
        Git.addFile("file1.txt");
        Commit commit1 = new Commit("Oren H", "Added file");
        commit1.finishCommit();

        File file1 = new File("./objects/file1.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file1));
        bw.write("hello");
        bw.close();

        Git.editFile("file1.txt");
        Commit commit2 = new Commit(commit1.getShaOfCommit(), "Oren H", "Edited file");
        commit2.finishCommit();

        runBasicCommitTests(commit1, null, commit2, 1);
        runBasicCommitTests(commit2, commit1, null, 1);

        testIndexWipe();
        Utils.clearDirectory("objects");
    }

    @Test
    void deleteAndEditAFile() throws Exception{
        Git.init();

        //add file
        Git.addFile("file1.txt");
        Git.addFile("file2.txt");
        Commit commit1 = new Commit("Oren H", "Added file");
        commit1.finishCommit();

        //edit file
        File file1 = new File("./objects/file1.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file1));
        bw.write("hello");
        bw.close();

        Git.editFile("file1.txt");
        Commit commit2 = new Commit(commit1.getShaOfCommit(), "Oren H", "Edited file");
        commit2.finishCommit();

        //delete file
        Git.deleteFile("file2.txt");
        Commit commit3 = new Commit(commit2.getShaOfCommit(), "Oren H", "Deleted file");
        commit3.finishCommit();

        runBasicCommitTests(commit1, null, commit2, 2);
        runBasicCommitTests(commit2, commit1, commit3, 2);
        runBasicCommitTests(commit3, commit2, null, 1);
    }

    @Test
    void deleteAndEditMultipleFiles() throws Exception{
        Git.init();

        //add file
        Git.addFile("file1.txt");
        Git.addFile("file2.txt");
        Commit commit1 = new Commit("Oren H", "Added file");
        commit1.finishCommit();

        //edit file
        File file1 = new File("./objects/file1.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file1));
        bw.write("hello");
        bw.close();

        Git.editFile("file1.txt");
        Commit commit2 = new Commit(commit1.getShaOfCommit(), "Oren H", "Edited file");
        commit2.finishCommit();

        //delete file
        Git.deleteFile("file2.txt");
        Commit commit3 = new Commit(commit2.getShaOfCommit(), "Oren H", "Deleted file");
        commit3.finishCommit();

        //add another file
        Git.addFile("file3.txt");
        Commit commit4 = new Commit(commit3.getShaOfCommit(), "Oren H", "Added file");
        commit4.finishCommit();

        //delete two files
        Git.deleteFile("file1.txt");
        Git.deleteFile("file3.txt");
        Commit commit5 = new Commit(commit4.getShaOfCommit(), "Oren H", "Deleted two files");
        commit5.finishCommit();

        runBasicCommitTests(commit1, null, commit2, 2);
        runBasicCommitTests(commit2, commit1, commit3, 2);
        runBasicCommitTests(commit3, commit2, commit4, 1);
        runBasicCommitTests(commit4, commit3, commit5, 2);
        runBasicCommitTests(commit5, commit4, null, 0);
    }

    public void runBasicCommitTests(Commit commit, Commit prevCommit, Commit nextCommit, int filesInTree) throws Exception{
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
        System.out.println(Utils.readFileToString("./objects/" + treeSha)+ "\n");
        assertTrue(treeFile.exists());
        System.out.println(Utils.numOfLines(treeFile));
        assertTrue(Utils.numOfLines(treeFile)==filesInTree);

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
