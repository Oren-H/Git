import java.util.*;
import java.io.*; 

public class test {
   public static void main (String []args) throws Exception
   {
   //  Blob b = new Blob("file1.txt");
        String fileName = "testfile.txt";
        File f = new File(fileName);
        f.createNewFile();

        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));

        pw.print("foobar"); 
        pw.close();

        Blob b = new Blob ("testfile.txt"); 
        System.out.println(Utils.readFileToString("testfile.txt"));
   }
}
