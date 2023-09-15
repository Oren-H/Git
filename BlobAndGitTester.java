import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BlobAndGitTester {

    private static String testFile = "testfile.txt";

    //runs before every test
    //TODO: need to make the file here
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

        File dir = new File("/objects");
        dir.mkdirs();
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
    @DisplayName("[8] Test if initialize and objects are created correctly")
    void testInitialize() throws Exception {

        // Run the person's code
        // TestHelper.runTestSuiteMethods("testInitialize");

        // check if the file exists
        File file = new File("index");
        Path path = Paths.get("objects");

        assertTrue(file.exists());
        assertTrue(Files.exists(path));
    }

    @Test
    @DisplayName("[15] Test if adding a blob works.  5 for sha, 5 for file contents, 5 for correct location")
    void testCreateBlob() throws Exception {

        // try {

        //     // Manually create the files and folders before the 'testAddFile'
        //     // MyGitProject myGitClassInstance = new MyGitProject();
        //     // myGitClassInstance.init();

        //     // TestHelper.runTestSuiteMethods("testCreateBlob", file1.getName());

        // } catch (Exception e) {
        //     System.out.println("An error occurred: " + e.getMessage());
        // }

        Blob b = new Blob (testFile);

        // Check blob exists in the objects folder
        File file_junit1 = new File("./objects/" + Blob.getStringHash(testFile));
        assertTrue("Blob file to add not found", file_junit1.exists());


        // Read file contents
        String indexFileContents = Utils.readFileToString("./objects/" + Blob.getStringHash(testFile));
        String mainFileContents = Utils.readFileToString(testFile);
        assertTrue("File contents of Blob don't match file contents pre-blob creation", indexFileContents.equals(mainFileContents));
    }
}
