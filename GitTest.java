import static org.junit.Assert.*;

import java.io.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GitTest {
    private static String testFile = "testfile.txt";

    @BeforeAll
    static void runBeforeAll () throws Exception {
        //make a test file
        File f = new File(testFile);
        f.createNewFile();

        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(testFile)));

        pw.print("foobar"); 
        pw.close();

        //make objects file
        File objects = new File ("./objects");
        objects.mkdirs();

    }


    @Test
    @DisplayName ("Test if add creates new file and updates Index")
    void testAdd() throws Exception {
        Git g = new Git ();
        
        g.init();  //make sure to run test for gitInit first

        g.addFile(testFile); 

        //test if it creates a file in the object folder
        String hash = Blob.getStringHash(Utils.readFileToString(testFile)); 
        File f = new File ("./objects/" + hash);
        assertTrue ("Git add did not create objects folder", f.exists());

        //test if it added something to the index file
        String lineInIndex = Utils.readLineWhichContains("index", testFile);
        assertTrue ("Git add didn't update index file", lineInIndex.equals("blob : " + hash + " : " + testFile));
    }

    @Test
    @DisplayName ("Test if the Git init method creates objects folder and index file")
    void testInit() throws Exception {
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
    @DisplayName ("Test if remove deletes file and index entry")
    void testRemove() throws Exception {
        Git g = new Git ();

        g.init();
        g.addFile(testFile);

        FileWriter fw = new FileWriter("testfile2.txt");
        fw.write("ee r menee r");
        fw.close();

        g.addFile("testfile2.txt");
        g.removeIndexEntry("testfile2.txt");

        //check whether it's removed from index

        String hash = Blob.getStringHash(Utils.readFileToString("testfile2.txt"));

        String lineInIndex = Utils.readLineWhichContains("index", "testfile2.txt");
        // String lineInIndex2 = Utils.readLineWhichContains("index", hash);

        assertTrue ("Did not remove from the index file", lineInIndex.equals(""));

        //check whether it's removed from the objects folder
        File f = new File ("./objects/" + hash);
        assertTrue ("Did not remove entry from objects folder", f.exists());
    }
}
