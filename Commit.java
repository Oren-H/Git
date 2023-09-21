public class Commit 
{
    String shaOfTreeObj;
    String authorName;
    String Summary;

    CommitNode head;
    CommitNode tail;
    public Commit(String shaOfTreeObj, String authorName, String Summary)
    {
        shaOfTreeObj = this.shaOfTreeObj;
        authorName = this.authorName;
        Summary = this.Summary;
        head = null;
        tail = null;
    }

    public void newCommit()
    {
        
    }

    
}
