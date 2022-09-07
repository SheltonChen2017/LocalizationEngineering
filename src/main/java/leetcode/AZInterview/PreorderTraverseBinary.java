package leetcode.AZInterview;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class PreorderTraverseBinary {

    class TreeNode{

        int val;
        TreeNode left;
        TreeNode right;

    }

    public List<Integer> preorderTraverse(TreeNode root){

        ArrayList<Integer> res = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode temp = root;

        while(temp!=null ||!stack.isEmpty()){

            if(temp!=null){

                res.add(temp.val);
                stack.push(temp);
                temp=temp.left;
            } else {

                TreeNode node = stack.pop();

                temp = node.right;

            }

        }

        return res;
    }

    public List<Integer> inorderTraverse(TreeNode root){

        ArrayList<Integer> list = new ArrayList<>();

        Stack<TreeNode> stack = new Stack<>();

        TreeNode temp = root;

        while(!stack.isEmpty()||temp!=null){
    if(temp!=null){

        stack.push(temp);
        temp=temp.left;

    } else {
        temp=stack.pop();
        list.add(temp.val);
        temp = temp.right;

    }
        }

        return list;
    }

    public List<Integer> postorderTraverse(TreeNode root){
        LinkedList<Integer> res = new LinkedList<>();

        Stack<TreeNode> stack = new Stack<>();

        TreeNode temp = root;

        while(!stack.isEmpty()||temp!=null){

            if(temp!=null){
               res.addFirst(temp.val);
               stack.push(temp);
            } else {

                TreeNode node = stack.pop();
                temp = node.left;


            }

        }

        return null;
    }

}
