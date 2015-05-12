package phero.calculator.calctree;

/**
 * Created by Jason on 11/05/2015.
 */
public class TreeNode {

  private String value = "";
  private TreeNode parent = null;

  private TreeNode right = null;
  private TreeNode left = null;

  public TreeNode(String value){
    this.value = value;
    this.parent = null;
  }

  public TreeNode(String value, TreeNode parent){
    this.value = value;
    this.parent = parent;
  }

  public String getValue() {
    return value;
  }
  public TreeNode getParent() {
    return parent;
  }
  public void setParent(TreeNode parent) {
    this.parent = parent;
  }
  public TreeNode getRight() {
    return right;
  }
  public void setRight(TreeNode right) {
    this.right = right;
  }
  public TreeNode getLeft() {
    return left;
  }
  public void setLeft(TreeNode left) {
    this.left = left;
  }
}
