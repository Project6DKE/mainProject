package botFolder;

public class Node {
    public String value;
    public Node left, right;

    public Node(String val){
        this.value = val;
        this.left = this.right = null;
    }
}