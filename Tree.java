import java.util.*; 

public class Tree {
    private StringBuilder contents;

    public Tree ()
    {
        contents = new StringBuilder();
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

        contents = newContent; 
    }
}
