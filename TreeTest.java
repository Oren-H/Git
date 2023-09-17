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
        File f = new File ("temp.txt");
        assertTrue(f.exists());
    }

    @Test
    void testWriteToFile() {

    }
}
