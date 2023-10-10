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

        //test if commit is valid and file exists
        String commitSha = commit1.getShaOfCommit();
        assertTrue(commitSha.length() == 40, "commit length is invalid");

        //test if file was made
        File commitFile = new File("./objects/" + commitSha);
        assertTrue(commitFile.exists());

        //test if files contains correct number of lines
        assertTrue(Utils.numOfLines(commitFile)==6);

        //test tree file
        String treeSha = commit1.shaOfTreeObj;
        File treeFile = new File("./objects/" + treeSha);
        assertTrue(treeFile.exists());
        assertTrue(Utils.numOfLines(treeFile)==2);

        //test prev and next shas
        assertTrue(Utils.getLine(commitFile, 2).equals(""));
        System.out.println(Utils.getLine(commitFile, 3));
        System.out.println(Utils.readFileToString("./objects/"+commitSha));
        assertTrue(Utils.getLine(commitFile, 3).equals(""));

        //test if index file was wiped
        String indexContents = Utils.readFileToString("index");
        assertTrue(indexContents.equals(""));

        Utils.clearDirectory("objects");
    }

    @Test
    void addSecondCommit() throws Exception{
        addFirstCommit();

        //add file to index
        Git.addFile("file3.txt");
        Git.addDirectory("directory 1");

        //create commit
        Commit commit2 = new Commit("a808d15464b36f348d51b8d296d543101dce7117", "Oren H", "This is the second commit");
        commit2.finishCommit();

        //test sha1s
        File testCommit2 = new File("./objects/9ed765d5bc8dda682ad5598fd281fc465d7063c7");
        assertTrue(testCommit2.exists());
    }

    @Test
    void addThirdAndFourthCommit() throws Exception{
        Git.init();
        addSecondCommit();

        //add files to index for commit 3
        Git.addFile("file5.txt");
        Git.addDirectory("directory 2");

        //create commit 3
        Commit commit3 = new Commit("9ed765d5bc8dda682ad5598fd281fc465d7063c7", "Oren H", "This is the third commit");
        commit3.finishCommit();

        //test commit 3 sha1s
        File testCommit3 = new File("./objects/27f47e2a347cab8de5ca570ede0d88d11a3261d0");
        assertTrue(testCommit3.exists());

        //create commit 4
        Git.addFile("file7.txt");
        Git.addFile("file8.txt");

        Commit commit4 = new Commit("27f47e2a347cab8de5ca570ede0d88d11a3261d0", "Oren H", "This is the fourth commit");
        commit4.finishCommit();

        //test commit 4
        File testCommit4 = new File("./objects/8496d551aba45091178a405547f0cd08bd2c0f6b");
        assertTrue(testCommit4.exists());
    }

    public void runBasicCommitTests(Commit commit, String prevSha, String nextSha) throws IOException{
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

        //test prev and next shas
        assertTrue(Utils.getLine(commitFile, 2).equals(prevSha));
        assertTrue(Utils.getLine(commitFile, 3).equals(nextSha));



    }
}
