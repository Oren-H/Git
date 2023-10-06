import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CommitTestNew {
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
    }

    @AfterAll
    static void runAfterAll(){
        File index = new File("index");
        File objects = new File("objects");
        for(File file : objects.listFiles()){
            file.delete();
        }
        index.delete();
    }

    @Test
    void addFirstCommit() throws Exception{

        //add file to index
        Git.addFile("file1.txt");
        Git.addFile("file2.txt");

        //create commit
        Commit commit1 = new Commit("Oren H", "This is the first commit");
        commit1.finishCommit();

        //test sha1s
        File testCommit1 = new File("./objects/a808d15464b36f348d51b8d296d543101dce7117");
        assertTrue(testCommit1.exists());
    }

    @Test
    void addSecondCommit() throws Exception{
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


}
