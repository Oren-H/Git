import static org.junit.Assert.*;

import java.io.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BlobTest {

    private static String testFile = "testfile.txt";

    //runs before every test
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        /*
         * Utils.writeStringToFile("junit_example_file_data.txt", "test file contents");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */

        File f = new File(testFile);
        f.createNewFile();

        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(testFile)));

        pw.print("foobar"); 
        pw.close();
    }

    //runs after every test
    @AfterAll
    static void tearDownAfterClass() throws Exception {
        /*
         * Utils.deleteFile("junit_example_file_data.txt");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */

         //NB: don't need to delete since nothing else will access this folder
    }

    @Test
    @DisplayName("[15] Test if adding a blob works.  5 for sha, 5 for file contents, 5 for correct location")
    void testCreateBlob() throws Exception {
        Blob b = new Blob (testFile); 

        // Check blob exists in the objects folder
        String hash = Blob.getStringHash(Utils.readFileToString(testFile));
        File file_junit1 = new File("./objects/" + hash);
        assertTrue("Blob file to add not found", file_junit1.exists());


        // Read file contents
        String indexFileContents = Utils.readFileToString("./objects/" + hash);
        String mainFileContents = Utils.readFileToString(testFile);
        assertTrue("File contents of Blob don't match file contents pre-blob creation", indexFileContents.equals(mainFileContents));
    }

    @Test
    @DisplayName ("Test if the hash functions works correctly")
    void testGetStringHash () throws Exception
    {
        String test1 = "hello world";
        String answer1 = "2aae6c35c94fcfb415dbe95f408b9ce91ee846ed";
        
        assertTrue ("SHA 1 did not match", Blob.getStringHash(test1).equals (answer1) ); 

        String test2 = "pencil";
        String answer2 = "d2fc512490a15036460b5489401439d6da5407fa";
        
        assertTrue ("SHA 1 did not match", Blob.getStringHash(test2).equals (answer2) ); 
    }
}
