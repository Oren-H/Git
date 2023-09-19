import java.util.*; 
import java.io.*; 

public class Tree {
    private StringBuilder contents;

    public Tree ()
    {
        contents = new StringBuilder();
    }    

    public String getContents ()
    {
        return contents.toString(); 
    }

    public void add (String entry) //assume its a proper entry
    {
        //check if the entry already exists
        String s = contents.toString(); 
        if (s.contains(entry))
        {
            return;
        }

        if (contents.isEmpty()) 
        {
            contents.append(entry);
        }
        else
        {
            contents.append("\n");
            contents.append(entry);
        }
    }

    public void remove (String s)
    {
        //check if it even exists first
        String sContent = contents.toString();
        if (!sContent.contains(s)) 
        {
            return;
        }

        StringBuilder newContent = new StringBuilder();

        Scanner sc = new Scanner(sContent); //use scanner to loop through string line by line

        while (sc.hasNextLine())
        {
            //only add the currentline to the stringbuilder if it doesn't contain string s
            String curLine = sc.nextLine();
            if (!curLine.contains(s))
            {
                newContent.append(curLine);
            }
        }

        sc.close(); 

        contents = newContent; 
    }

    public void writeToFile () throws Exception //will write the contents to a file 
    {
        String fileName = Blob.getStringHash(contents.toString());

        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("./objects/" + fileName)));

        pw.print(contents.toString());

        pw.close(); 
    }
}
