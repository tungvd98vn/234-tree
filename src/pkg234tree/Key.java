/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg234tree;
import java.io.*;

import java.lang.Integer;


class Key{
    public int k;   
    public Key(int i) { 
        k = i; 
    }
    public void printKey(){ 
        System.out.print( k + ", "); 
    }
}  
/*-------------------------------------------------------------------*/
class Node{
    
    private static final int degree = 4; // degree of b tree is 4
    private int keys;                    // number of keys 
    private Node parent;
    private Node[] children = new Node[degree];
    private Key[] keysArray = new Key[degree-1];

    // functions
    public int getKeys(){
        return keys; 
    }
    
    public Key getArray(int i)  { 
        return keysArray[i]; 
    }
    
    public Node getChild(int n){
        return children[n]; 
    }

    public Node getParent(){
        return parent; 
    }

    public boolean isLeaf(){ 
        return (children[0] == null) ? true : false; 
    }


    public boolean isFull(){ 
        return (keys == degree-1) ? true : false;
    }
    
    public void merge(int n, Node child){
        children[n] = child;
        if(child != null)
            child.parent = this;
    }

    public int insertKey(Key newk){
        keys++;
        int n = newk.k;
        for(int j=degree-2; j>=0; j--){
            if(keysArray[j] == null)   
                continue;   
            else {   
                int i = keysArray[j].k;
                if(n < i)   
                    keysArray[j+1] = keysArray[j];
                else{
                    keysArray[j+1] = newk;
                    return j+1;
                }
            }  
        }  
        keysArray[0] = newk;   
        return 0;
    }  


    public Key removeKey(){
        
        Key temp = keysArray[keys-1];  
        keysArray[keys-1] = null;
        keys--;
        return temp;   
    }
    
     public Node split(int n){
        Node temp = children[n];
        children[n] = null;
        return temp;
    }


    public void printNode(){
        
        for(int i=0; i<keys; i++)
        keysArray[i].printKey();
        System.out.println("");
    }
}  
/*-------------------------------------------------------------------*/
class Tree{
    private Node root = new Node();   
    
    public Node getNext(Node node, int i){
        
        int j;
        int keys = node.getKeys();
        for(j=0; j<keys; j++){
            if( i < node.getArray(j).k )
                return node.getChild(j);  
        }
        return node.getChild(j);
    }
    
    public void split(Node n){

        Key key1;
        Key key2;
        Node parent, c1, c2;
        int i;
        key2 = n.removeKey();   
        key1 = n.removeKey();   
        c1 = n.split(2);
        c2 = n.split(3);
        Node newKey = new Node();   
        if(n==root){
            root = new Node();   
            parent = root;   
            root.merge(0, n);
        }
        else
            parent = n.getParent();   
        i = parent.insertKey(key1);
        int m = parent.getKeys();
        for(int j=m-1; j>i; j--)   {   
            Node temp = parent.split(j);
            parent.merge(j+1, temp);   
        }

        parent.merge(i+1, newKey);
        newKey.insertKey(key2);
        newKey.merge(0, c1);
        newKey.merge(1, c2);
    }
    
    public void insert(int k){
        Node n = root;
        Key key = new Key(k);
        while(true){
            if( n.isFull() ){
                split(n);
                n = n.getParent();   
                n = getNext(n, k);
            }  
            else if( n.isLeaf())   
                break;   
            else
                n = getNext(n, k);
        }

        n.insertKey(key);
    }  

    public void display(){
        display(root, 0, 0);
    }

    private void display(Node node, int l,int e){
        System.out.println("at level:----"+l);
        System.out.print("at egde: "+e+"----");
        node.printNode();

        int keys = node.getKeys();
        for(int j=0; j<keys+1; j++){
            Node nextNode = node.getChild(j);
            if(nextNode != null)
                display(nextNode, l+1, j);
            else
                return;
        }
    }  
}
/*-------------------------------------------------------------------*/

class Main{
    public static void main(String[] args) throws IOException{
        int input;
        Tree tree = new Tree();
        while(true){
            System.out.print("Please insert to the tree: ");
            input = getInput();
            tree.insert(input);
            tree.display();
        }
    }

    public static int getInput() throws IOException{
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        return Integer.parseInt(br.readLine());
    }
}  