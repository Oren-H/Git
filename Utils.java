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
}
