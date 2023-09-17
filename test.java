import java.util.*;
import java.io.*; 

public class test {
   public static void main (String []args) throws Exception
   {
      String testFile = "file1.txt";
        Git g = new Git ();
        
        g.init();  //make sure to run test for gitInit first

        g.add(testFile); 

        //test if it creates a file in the object folder
        String hash = Blob.getStringHash(Utils.readFileToString(testFile)); 
        File f = new File ("./objects/" + hash);
        System.out.println(f.exists());

        //test if it added something to the index file
        String lineInIndex = Utils.readLineWhichContains("index", testFile);
        System.out.println(testFile + " : " + hash);
        System.out.println("________________________");
        System.out.println(lineInIndex);
   }
}
