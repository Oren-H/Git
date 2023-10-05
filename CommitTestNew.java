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
        Utils.writeStringToFile("content 1", "file1.txt");
        Utils.writeStringToFile("content 2", "file2.txt");
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
        File objects = new File("objects");
        System.out.println(objects.listFiles()[0]);
        System.out.println(Utils.readFileToString("objects/7f0e1bc2d59e1607f21b984ce6fbfe777e6f458e"));
        assertTrue(testCommit1.exists());
    }
}
