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

    public static int numOfLines(File file) throws IOException{
        int lines = 0;
        BufferedReader br = new BufferedReader(new FileReader(file));
        while(br.readLine()!=null){
            lines++;
        }
        br.close();
        return lines;
    }

    public static String getLine(File file, int lineNum) throws IOException{
        String currString = "";
        BufferedReader br = new BufferedReader(new FileReader(file));
        for(int i = 0; i<lineNum; i++){
            currString = br.readLine();
        }
        br.close();
        return currString;
    }
}
