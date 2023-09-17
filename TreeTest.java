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



public class TreeTest {

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
    void testAdd() throws Exception {
        Tree tree = new Tree();

        String entry1 = "tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b";
        String entry2 = "blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt";

        tree.add(entry1);
        tree.add(entry2);

        assertTrue ("Did not add the entries to the tree", tree.getContents().contains(entry1) && tree.getContents().contains(entry2));
    }

    @Test
    void testRemove() {

        Tree tree = new Tree();
        
        String entry1 = "tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b";
        String entry2 = "blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt";

        tree.add(entry1);
        tree.add(entry2);

        tree.remove("file1.txt");
        assertFalse ("Did not remove file correctly", tree.getContents().contains (entry2));

        tree.remove("bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        assertFalse ("Did not remove tree correctly", tree.getContents().contains (entry1));
    }

    @Test
    void testWriteToFile() throws Exception {
        Tree tree = new Tree();
        

        tree.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        tree.add("blob : 01d82591292494afd1602d175e165f94992f6f5f : file2.txt");
        tree.add("blob : f1d82236ab908c86ed095023b1d2e6ddf78a6d83 : file3.txt");
        tree.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        tree.add("tree : e7d79898d3342fd15daf6ec36f4cb095b52fd976");

        tree.writeToFile();

        //test if the file is created in objects
        String hash = Blob.getStringHash(tree.getContents());
        File treeFile = new File ("./objects/" + hash);
        assertTrue ("Tree does not write to correct SHA file", treeFile.exists());

        //then check to see if all the information is copied correctly
        assertTrue ("Tree Sha file does not contain the correct information", Utils.readFileToString("./objects/" + hash).equals(tree.getContents()));
    }
}
