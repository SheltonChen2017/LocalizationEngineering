package leetcode;

public class ConstructBinaryTreeFromString {

    /**
     * You need to construct a binary tree from a string consisting of parenthesis and integers.
     * <p>
     * The whole input represents a binary tree. It contains an integer followed by zero, one or two pairs of parenthesis. The integer represents the root's value and a pair of parenthesis contains a child binary tree with the same structure.
     * <p>
     * You always start to construct the left child node of the parent first if it exists.
     */


    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }


    private int index;
    public TreeNode str2tree(String s) {

        index = 0;

        return BuildTree(s);
    }


    public int getValue(String s){

        StringBuilder sb = new StringBuilder();

        while(index<s.length() &&s.charAt(index)!=')' && s.charAt(index)!='(') {
            sb.append(s.charAt(index++));
        }

        return Integer.parseInt(sb.toString());
    }

    public TreeNode BuildTree(String str){

        System.out.println("current process is "+ str.charAt(index));

        if(index>=str.length()) return null;

        if(str.charAt(index)==')') {
            index ++;
            return null;
        }

        int val = this.getValue(str);

        TreeNode node = new TreeNode(val);

        if(index<str.length() && str.charAt(index)=='(') {
            index ++;
            node.left = this.BuildTree(str);
        }

        if(index<str.length() && str.charAt(index)=='(') {
            index ++;
            node.right = this.BuildTree(str);
        }

        index++;

        return node;
    }

}
