package com.company;

import com.sun.istack.internal.localization.NullLocalizable;
import sun.awt.image.ImageWatched;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    public static class Node {
        Node left;
        Node right;
        Node parent; //For question 4.6 only
        int data;

        public Node(int value) {
            this.data = value;
        }
    }

    public static class minHeap {
        int parentIndex;
        int smallerIndex;
        int largerIndex;
        int capacity = 10;
        int size = 0;

        int[] base = new int[capacity];

        public int peek() {
            return base[0];
        }

        public int getSize(){
            return size;
        }

        public int getParentIndex(int childIndex) {
            return (childIndex - 1) / 2;
        }

        public int getSmallerIndex(int parentIndex) {
            return 2 * parentIndex + 1;
        }

        public int getLargerIndex(int parentIndex) {
            return 2 * parentIndex + 2;
        }

        public int getParent(int index) {
            return base[getParentIndex(index)];
        }

        public int getSmaller(int index) {
            return base[getSmallerIndex(index)];
        }

        public int getLarger(int index) {
            return base[getLargerIndex(index)];
        }

        public boolean hasParent(int childIndex) {
            return getParentIndex(childIndex) < 0;
        }

        public boolean hasSmaller(int parentIndex) {
            return getSmallerIndex(parentIndex) > size;
        }

        public boolean hasLarger(int parentIndex) {
            return getLargerIndex(parentIndex) > size;
        }

        public void add(int data) {
            checkSpace();
            int index = size;
            base[index] = data;
            size++;
            heapifyIns();
        }

        public int deleteMin() {
            if(size == 0)
                throw new IllegalStateException("There is nothing in the heap to delete.");
            int index = size - 1;
            int value = base[0];
            base[0] = base[index];
            base[index] = 0;
            size--;
            heapifyDel();
            return value;
        }

        public void heapifyIns() {
            int index = size - 1;
            while(hasParent(index) && base[index] < getParent(index)) {
                int temp = base[index];
                base[index] = getParent(index);
                base[getParentIndex(index)] = temp;
                index = getParentIndex(index);
            }
        }

        public void heapifyDel() {
            int index = 0;
            while(hasSmaller(index) && base[index] > getSmaller(index)) {
                int temp = base[index];
                base[index] = getSmaller(index);
                base[getSmallerIndex(index)] = temp;
                index = getSmallerIndex(index);
            }
        }

        public void checkSpace() {
            if(size < capacity)
                return;

            capacity *= 2;
            int [] newBase = new int[capacity];
            for(int i = 0; i < base.length; i++) {
                newBase[i] = base[i];
            }
            base = newBase;
        }
    }

    public static int getDepth(Node node) {
        int depth = 0;
        Node current = node;
        while(current != null) {
            current = current.parent;
            depth++;
        }
        return depth;
    }

//4.2: Given a sorted (increasing order) array with unique
// integer elements, write an algorithm to create a binary search tree with minimal height.

    public static Node trav(Node node, int value) {
        if(node.left != null && value < node.data) {
            node = trav(node.left, value);
        }

        if(node.right != null && value > node.data) {
            node = trav(node.right, value);
        }

        return node;
    }

    public static Node arrayBST(int [] nums) {
        int mid = nums.length/2;
        Node root = new Node(nums[mid]);
        for(int i = 0; i < nums.length; i++) {
            Node newNode = trav(root, nums[i]);
            //When trav is done we should be at a leaf
            if(nums[i] < newNode.data) {
                newNode.left = new Node(nums[i]);
            }

            if(nums[i] > newNode.data) {
                newNode.right = new Node(nums[i]);
            }
        }

        return root;
    }

    public static void printTree(Node bst) {
        if(bst != null) {

            System.out.println(bst.data);
            printTree(bst.left);
            printTree(bst.right);
        }

    }

// 4.3 : Given a binary tree, design an algorithm which creates a linked list of all the nodes
//at each depth (e.g., if you have a tree with depth D, you'll have D linked lists).

    public static ArrayList<Queue> listDepth(Node root) {
        //Obviously BFS
        Queue<Node> children = new LinkedList<Node>();
        Queue<Node> parents = new LinkedList<Node>();
        ArrayList<Queue> nodeDepths = new ArrayList<Queue>();
        children.add(root);
        while(!children.isEmpty()) {
            nodeDepths.add(children);

            //children have to grow up
            parents = children;
            children = new LinkedList<Node>();

            for(Node parent : parents) {
                if(parent.left != null) {
                    children.add(parent.left);
                }

                if(parent.right != null) {
                    children.add(parent.right);
                }
            }

        }

        return nodeDepths;

    }

    //Validate BST: Implement a function to check if a binary tree is a binary search tree.

    public static void inOrderCopy(Node root, ArrayList<Integer> array) {
        if(root == null) {
            return;
        }
        inOrderCopy(root.left, array);
        array.add(root.data);
        inOrderCopy(root.right, array);
    }

    public static boolean validateBST(Node treeNode) {
            ArrayList<Integer> array = new ArrayList<>();
            inOrderCopy(treeNode, array);
            for (int i = 1; i < array.size(); i++) {
                if (array.get(i) < array.get(i - 1)) {
                    return false;
                }
            }

            return true;

        }

    //Given the root of a binary tree, return the deepest node
    public static int deepestNode(Node treeRoot) {
        ArrayList<Node> children = new ArrayList<Node>();
        ArrayList<Node> parents = new ArrayList<Node>();
        children.add(treeRoot);
        while(children.size() > 0) {
            //children grow up into parents
            parents = children;
            children = new ArrayList<Node>();
            for(Node parent : parents) {
                if(parent.left != null) {
                    children.add(parent.left);
                }

                if(parent.right != null) {
                    children.add(parent.right);
                }
            }
        }

        return(parents.get(parents.size()-1).data);



    }

    //4.6: Successor: Write an algorithm to find the "next" node (i.e., in-order successor) of a given node in a
    //binary search tree. You may assume that each node has a link to its parent.
    /*public static void successor(Node predecessor) {
        if(predecessor != null) {
            //If starting node has a right child go to that child
            if(predecessor.right != null) {
                System.out.println(successor(predecessor.right, true));
            }
            //If no right child proceed with the starting node
            System.out.println(successor(predecessor, false).data);
        }
    }

    public static Node successor(Node currentNode, boolean wentRight) {
        if(currentNode != null) {
            if (wentRight) {
                //Now in the right subtree of the starting node
                return successor(currentNode.left, wentRight);
            } else {
                //Starting node has no right subtree so I want to travel back up to the middle
                if (currentNode.parent.right == currentNode) {
                    while (currentNode.parent != null) {
                        if (currentNode.parent.data > currentNode.data) {
                            break;
                        }

                        currentNode = currentNode.parent;

                    }
                    return null;
                }
            }
            }


        return currentNode.parent;
    }*/

    /*Cases:
    * Starting node has a right child
    * Starting node has no right child
    *   -Starting node has no in-order successor or going up the tree is needed to find it*/
    public static Node successor(Node current) {
        if(current == null) {
            return null;
        }

        //Node has a right child so we traverse all the left children until we get the last one
        if(current.right != null) {
            return smallestChild(current.right);
        }

        //No right child and node is a right child, we must go up the tree through the parents
        if(current.parent != null && current.parent.right == current) {
            Node n = current;
            while(n != null && n.data > n.parent.data) {
                n = n.parent;
            }
            return n.parent;
        }

        //No right child and node is a left child the successor is the node's parent
        if(current.parent.left == current) {
            return current.parent;
        }

        return current;
    }

    //If a node has a right child this will return the smallest node in the subtree
    public static Node smallestChild(Node currentNode) {
        if(currentNode.left != null) {
            return smallestChild(currentNode.left);
        }
        return currentNode;
    }

    /*4.8 First Common Ancestor: Design an algorithm and write code to find the first common ancestor
    of two nodes in a binary tree. Avoid storing additional nodes in a data structure.*/
    public static Node ancestor(Node desc1, Node desc2) {
        if(desc1 == null || desc2 == null) {return null;}

        int dist = Math.abs(getDepth(desc1) - getDepth(desc2));
        Node higher = null;
        Node lower = null;
        if(getDepth(desc1) <= getDepth(desc2)) {
            higher = desc1;
            lower = desc2;
        } else {
            higher = desc2;
            lower = desc1;
        }

        lower = evenUp(lower, higher);
        //Now both nodes are on the same depth
        while((lower != null || higher != null) && higher.parent != lower.parent) {
            higher = higher.parent;
            lower = lower.parent;
        }

        return higher.parent;


    }

    public static Node evenUp(Node lower, Node higher) {
        if(lower == null || higher == null) return null;
        int target = getDepth(higher);
        int current = getDepth(lower);
        while(current != target) {
            lower = lower.parent;
            current--;
        }

        return lower;
    }




    public static void main(String[] args) {
	// write your code here
        int [] array = {1,2,3,5,7,11,13,17,19,23,29};
        Node one = new Node(20);
        Node two = new Node (10);
        Node three = new Node(30);
        Node four = new Node(25);
        Node five = new Node(44);
        Node six = new Node(8);

        //This is not a valid BST
        one.left = two;
        one.right = three;
        two.right = four;

        Node eleven = new Node(11);
        Node eight = new Node(8);
        Node twoThree = new Node(23);
        Node zeroFive = new Node(5);
        Node ten = new Node(10);
        Node oneFive = new Node(15);
        Node twoFive = new Node(25);
        Node seven = new Node(7);
        Node nine = new Node(9);
        Node twoZero = new Node(20);
        Node twoFour = new Node(24);

        eleven.left = eight;
        eight.parent = eleven;
        eleven.right = twoThree;
        twoThree.parent = eleven;
        eight.left = zeroFive;
        zeroFive.parent = eight;
        eight.right = ten;
        ten.parent = eight;
        twoThree.left = oneFive;
        oneFive.parent = twoThree;
        twoThree.right = twoFive;
        twoFive.parent = twoThree;
        zeroFive.right = seven;
        seven.parent = zeroFive;
        ten.left = nine;
        nine.parent = ten;
        oneFive.right = twoZero;
        twoZero.parent = oneFive;
        twoFive.left = twoFour;
        twoFour.parent = twoFive;

        //Node m = new Node();

        //printTree(arrayBST(array));
        //System.out.println(validateBST(one));
        //System.out.println(deepestNode(arrayBST(array)));
        //System.out.println(successor(twoThree).data);
        System.out.println(ancestor(eleven, twoFive).data);






    }
}
