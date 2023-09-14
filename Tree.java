import java.util.*; 

public class Tree {
    private StringBuilder contents;

    public Tree ()
    {

    }    

    public void add (String entry) //assume its a proper entry
    {
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

    }
}
