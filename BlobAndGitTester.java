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
        Git g = new Git ();
        g.init(); 

        // check if the file exists
        
        File file = new File("index");
        Path path = Paths.get("objects");

        assertTrue(file.exists());
        assertTrue(Files.exists(path));
    }

    //              BLOB TESTS 

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
        String hash = Blob.getStringHash(Utils.readFileToString(testFile));
        File file_junit1 = new File("./objects/" + hash);
        assertTrue("Blob file to add not found", file_junit1.exists());


        // Read file contents
        String indexFileContents = Utils.readFileToString("./objects/" + hash);
        String mainFileContents = Utils.readFileToString(testFile);
        assertTrue("File contents of Blob don't match file contents pre-blob creation", indexFileContents.equals(mainFileContents));
    }

    @Test
    @DisplayName ("Test if Git Init method works")
    void testGitInit () throws Exception 
    {
        Git g = new Git ();    
        g.init();

        //check if objects folder is created
        File objectsFolder = new File ("./objects");
        assertTrue ("NO OBJECST FOLDER FOUND", objectsFolder.isDirectory());

        //check if index file is created 
        File indexFile = new File ("./index");
        assertTrue("NO INDEX FILE FOUND", indexFile.isFile());
    }
    

    @Test
    @DisplayName ("Test if Git Add method works")
    void testGitAdd () throws Exception
    {
        Git g = new Git ();
        
        g.init();  //make sure to run test for gitInit first

        g.add(testFile); 

        //test if it creates a file in the object folder
        String hash = Blob.getStringHash(Utils.readFileToString(testFile)); 
        File f = new File ("./objects/" + hash);
        assertTrue ("Git add did not create objects folder", f.exists());

        //test if it added something to the index file
        String lineInIndex = Utils.readLineWhichContains("index", testFile);
        assertTrue ("Git add didn't update index file", lineInIndex.equals(testFile + " : " + hash));
    }

    @Test
    @DisplayName ("Test if Git Remove method works")
    void testGitRemove () throws Exception
    {
        Git g = new Git ();

        g.init();
        g.add(testFile);

        FileWriter fw = new FileWriter("testfile2.txt");
        fw.write("ee r menee r");
        fw.close();

        g.add("testfile2.txt");
        g.remove("testfile2.txt");

        //check whether it's removed from index

        String hash = Blob.getStringHash(Utils.readFileToString("testfile2.txt"));

        String lineInIndex = Utils.readLineWhichContains("index", "testfile2.txt");
        String lineInIndex2 = Utils.readLineWhichContains("index", hash);

        assertTrue ("Did not remove from the index file", lineInIndex.equals("") && lineInIndex2.equals(""));

        //check whether it's removed from the objects folder
        File f = new File ("./objects/" + hash);
        assertTrue ("Did not remove entry from objects folder", f.exists());
    }
}
