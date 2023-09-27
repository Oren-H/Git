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

    public String writeToFile () throws Exception //will write the contents to a file, returns the fileName
    {
        String fileName = Blob.getStringHash(contents.toString());

        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("./objects/" + fileName)));

        pw.print(contents.toString());

        pw.close(); 

        return fileName;
    }

    //adds an entire directory to the tree
    public void addDirectory(String directoryPath) throws Exception{

        File dir = new File(directoryPath);

        //traverse each file
        for(File file: dir.listFiles()){
            
            if(file.isDirectory()){
                
                //creates a child tree and recursively runs addDirectory
                Tree childTree = new Tree();
                childTree.addDirectory(file.getAbsolutePath());
                String treeSha = childTree.writeToFile();
                Blob treeBlob = new Blob("./objects/" + treeSha);

                //adds entry to file
                String entry = "tree : " + treeSha + " : " + file.getName();
                add(entry);
                System.out.println("entry has been added!");
            }

            else{

                //get the sha for the file
                String fileName = file.getAbsolutePath();

                Blob blob = new Blob (fileName);

                String sha = blob.getBlobHash();

                //create and add a string entry
                String entry = "blob : " + sha + " : " + file.getName();
            
                add(entry);
            }
        }
    }
}
