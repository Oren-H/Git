import static org.junit.Assert.*;

import java.io.*;
import org.junit.jupiter.api.BeforeAll;
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

        //prepare simple test files
        File testDir = new File("./testDirectory1");
        testDir.mkdir();

        Utils.writeStringToFile("hello", "./testDirectory/file1.txt");
        Utils.writeStringToFile("world", "./testDirectory/file2.txt");
        Utils.writeStringToFile("hello world", "./testDirectory/file3.txt");

        //prepare complicated test files
        File testDir2 = new File("./testDirectory2");
        testDir2.mkdir();

        File childDir1 = new File("./testDirectory2/childDirectory1");
        childDir1.mkdir();

        File childDir2 = new File("./testDirectory2/childDirectory2");
        childDir2.mkdir();

        Utils.writeStringToFile("hello", "./testDirectory2/file1.txt");
        Utils.writeStringToFile("world", "./testDirectory2/childDirectory1/file2.txt");
        Utils.writeStringToFile("hello world", "./testDirectory2/childDirectory1/file3.txt");
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

    @Test
    void testAddDirectory() throws Exception{

        //run code for test 1
        Tree dir1 = new Tree();
        dir1.addDirectory("./testDirectory1");
        dir1.writeToFile();

        //test if all files were blobbed
        File treeFile1 = new File("./objects/fd79fb2dbd789de35288a0504171833120028ad3");
        assertTrue(treeFile1.exists());

        String contents = Utils.readFileToString("./objects/fd79fb2dbd789de35288a0504171833120028ad3");
        
        assertEquals("blob : 7c211433f02071597741e6ff5a8ea34789abbf43 : file2.txt\nblob : 2aae6c35c94fcfb415dbe95f408b9ce91ee846ed : file3.txt\nblob : aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d : file1.txt", contents);

        //run code for test 2
        Tree dir2 = new Tree();
        dir2.addDirectory("./testDirectory2");
        dir2.writeToFile();

        //test if all files were blobbed
        File treeFile2 = new File("./objects/6a9ef65580741c32c011f8af930e056be718ea6b");
        assertTrue(treeFile2.exists());

        String contents2 = Utils.readFileToString("./objects/6a9ef65580741c32c011f8af930e056be718ea6b");

        assertEquals("blob : aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d : file1.txt\ntree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : childDirectory2\ntree : 3f77f0b5063b7179a415c07e1b4e1f67eeb2e4ed : childDirectory1", contents2);
    }
}
