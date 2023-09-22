import java.io.IOException;

public class tester 
{
    public static void main(String[] args) throws IOException
    {
        Commit c = new Commit("f924e482dd33576fd0de90b6376f1671b08b5f52", "Bob", "committest");
        System.out.println(c.commitwithoutprevLine());
        System.out.println("SHAAAAA:" + c.shaOfFileContent());
        System.out.println(c.getDate());
    }
    
}
