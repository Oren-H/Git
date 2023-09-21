public class CommitNode 
{
    String value;
    String nextCommitName;
    String lastCommitName;

    public CommitNode(String value, String nextCommitName, String lastCommitName)
    {
        value = this.value;
        nextCommitName = this.nextCommitName;
        lastCommitName = this.lastCommitName;
    }

    public void setValue(String newV)
    {
        value = newV;
    }

    public void setNext(String nextV)
    {
        nextCommitName = nextV;
    }

    public void setLast(String lastV)
    {
        lastCommitName = lastV;
    }

    public String getValue()
    {
        return value;
    }

    public String getNext()
    {
        return nextCommitName;
    }

    public String getLast()
    {
        return lastCommitName;
    }
}
