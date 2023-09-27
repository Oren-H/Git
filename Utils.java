import java.util.*;
import java.io.*; 

public class Utils {
    public static String readFileToString (String fileName) throws Exception
    {
        BufferedReader br = new BufferedReader (new FileReader(fileName));

        StringBuilder sb = new StringBuilder("");

        while (br.ready())
        {
            sb.append((char) br.read());
        }

        br.close();

        return sb.toString();
    }

    public static String readLineWhichContains (String fileName, String substring)  throws Exception
    {
        Scanner sc = new Scanner (new File (fileName));

        while (sc.hasNextLine())
        {
            String nextLine = sc.nextLine();
            if (nextLine.contains(substring))
            {
                return nextLine;
            }
        }


        sc.close();

        return "";
    }

    public static void writeStringToFile(String str, String path) throws FileNotFoundException{
        PrintWriter pw = new PrintWriter(path);
        pw.print(str); 
        pw.close();
    }
}
