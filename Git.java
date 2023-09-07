import java.io.File;

public class Git {
    public Git(){
       
    }

    public void init(){
        File indexFile = new File("index.txt");
        File objFolder = new File("objects");
        objFolder.mkdir();
    }
}
