import java.util.*;
/**
 * Models a tree that stores words. Each node represents a letter of the word. 
 * The last letter of each word contains the number of times the word appeared in
 * the text. The class also has methods to check how many times a word appeared
 *  in the text.
 */
public class WordCountTree {
  private Node root = null;
  private int numberOfNodes = 0;
  private class Node {
    char character;
    int count;
    List<Node> children = new LinkedList<Node>();

    public Node(char ch, int in){
      character = ch;
      count = in;
    }

    public Node() {
      count = 0;
    }
  }
  /**
   * check if the first character of a string is present in the current level
   * of the tree. 
   * @param word a string, representing a word. The program will check if the first character of the string is present in the current level of the tree
   * @param currentNode a node of the tree where operations will be executed
   * @return the index of the place on the LinkedList were the first character of the string is stored or -1 if the character was not found
   */
  private int checkCharAtString(String word, Node currentNode){
    for(int i = 0; i < currentNode.children.size(); i++){
      char targetChar = currentNode.children.get(i).character;
      if(targetChar == word.charAt(0)){
        return i;
      }
    }
    return -1;
  } 
  /**
   * Adds the remaining characters of the string to the tree.
   * @param word a string representing a word
   * @param parentNode a node, representing the parentNode of the word which is going to be stored
   */
  private void addEntireString(String word, Node parentNode){
    if(word.length() > 1){
      Node recursiveNode = new Node(word.charAt(0), 0);
      numberOfNodes++;
      addEntireString(word.substring(1), recursiveNode);
      parentNode.children.add(recursiveNode);
    } else {
      Node recursiveNode = new Node(word.charAt(0), 1);
      numberOfNodes++;
      parentNode.children.add(recursiveNode);
    }
  } 
/**
 * Constructs the class.
 */         
public WordCountTree(){
  
}
 
/**
 * Adds 1 to the existing count for word, or adds word to the WordCountTree
 * with a count of 1 if it was not found. 
 * @param word the word to be added, or having its count incremented
 */
public void incrementCount(String word){
  if(root == null){
    root = new Node(Character.MIN_VALUE, 0);
    numberOfNodes++;
  }
  incrementCount(word, root);
}
/**
 * Adds 1 to the existing count for word, or adds word to the WordCountTree
 * with a count of 1 if it was not found.
 * @param word the word to be added, or having its count incremented
 * @param currentNode the current node, which is the level of the tree where operations are being executed
 */
private void incrementCount(String word, Node currentNode){
  int i;
  if(word.length() > 1){
    i = checkCharAtString(word, currentNode);
    if(i >= 0){
      incrementCount(word.substring(1), currentNode.children.get(i));
    } else{
      addEntireString(word, currentNode);
    }   
  } else {
    i = checkCharAtString(word, currentNode);
    if(i >= 0){
      currentNode.children.get(i).count++;
    } else{
      addEntireString(word, currentNode);
    }
  }
}
 
/**
 * Returns true if word is stored in the
 * a count greater than 0, and false otherwise. 
 * @param word a string representing a word. The method will check whether it is present in the tree
 * @return true if word is stored in the tree with a count greater than 0, false otherwise
 */
public boolean contains(String word){
  if(root == null){
    return false;
  }
  return contains(word, root);
}
/**
 * Returns true if word is stored in the
 * a count greater than 0, and false otherwise. 
 * @param word a string representing a word. The method will check whether it is present in the tree
 * @param currentNode a node, where the current operations are being executed
 * @return true if word is stored in the tree with a count greater than 0, false otherwise
 */
private boolean contains(String word, Node currentNode){
  int i;
  if(word.length() > 1){
    i = checkCharAtString(word, currentNode);
    if(i >= 0){
      return contains(word.substring(1), currentNode.children.get(i));
    } else{
      return false;
    }   
  } else {
    i = checkCharAtString(word, currentNode);
    if(i >= 0 && (currentNode.children.get(i).count > 0)){
      return true;
    } else{
      return false;
    }
  }
}
 
/**
 * Returns the count of word, or -1 if word is not found.
 * @param word a string, representing the word which is going to be evaluated by the method
 * @return an integer repressenting the number of times word is present in tree; returns -1 if word is not in the tree
 */
public int getCount(String word){
  if(root == null){
    return -1;
  }
  return getCount(word, root);
}
/**
 * Returns the count of word, or -1 if word is not found.
 * @param word a string, representing the word which is going to be evaluated by the method
 * @param currentNode a node where all operations of the method are going to be executed
 * @return an integer repressenting the number of times word is present in tree; returns -1 if word is not in the tree
 */
private int getCount(String word, Node currentNode){
  int i;
  if(word.length() > 1){
    i = checkCharAtString(word, currentNode);
    if(i >= 0){
      return getCount(word.substring(1), currentNode.children.get(i));
    } else{
      return -1;
    }   
  } else {
    i = checkCharAtString(word, currentNode);
    if(i >= 0){
      return currentNode.children.get(i).count;
    } else{
      return -1;
    }
  }
}
 
  
/** 
 * Returns the total number of nodes in the tree. 
 * @return  a count of the total number of nodes in the tree. 
 */
 public int getNodeCount(){
   return numberOfNodes;
 } 
  
 /** 
  * Creates and sorts in decreasing order a list 
  * of WordCount objects, one per word stored in this 
  * WordCountTree.
  * @return a List of WordCount objects in decreasing order
  */
  public List<WordCount> getWordCountsByCount(){
    List<WordCount> wordCountList = new LinkedList<WordCount>();
    getWordCountsHelper(root, "", wordCountList);
    Collections.sort(wordCountList, new SortWordCount());
    return wordCountList;
  }

  private void getWordCountsHelper(Node node, String wordSoFar, List<WordCount> wordCountList) {
    if(node.children.size() == 0) {
      return;
    }
    for(Node child : node.children) {
      String nextWord = wordSoFar + child.character;

      if(child.count != 0) {
        WordCount newWord = new WordCount(nextWord, child.count);
        wordCountList.add(newWord);
      }

      getWordCountsHelper(child, nextWord, wordCountList);
    }
  }
  /**
   * Tests the methods defined in the class to check whether they are working
   * properly
   */
  public static void main(String[] args) {
    WordCountTree test = new WordCountTree();
    System.out.println(test.contains("test"));
    System.out.println(test.getCount("then"));
    test.incrementCount("test");//1
    test.incrementCount("the");//1
    test.incrementCount("then");//1
    test.incrementCount("then");//2
    test.incrementCount("then");//3
    test.incrementCount("test");//2
    test.incrementCount("number");//1
    test.incrementCount("then");//4
    test.incrementCount("the");//2
    test.incrementCount("the");//3
    test.incrementCount("the");//4
    test.incrementCount("the");//5
    test.incrementCount("the");//6
    test.incrementCount("the");//7
    test.incrementCount("the");//8
    System.out.println("test "+test.contains("test"));
    System.out.println("tes "+test.contains("tes"));
    System.out.println("tensdjabbf "+test.contains("tensdjabbf"));
    System.out.println("thee "+test.contains("thee"));
    System.out.println("then "+test.contains("then"));
    System.out.println("thenn "+test.contains("thenn"));
    System.out.println("the "+test.contains("the"));
    System.out.println("thg "+test.contains("thg"));
    System.out.println("them "+test.contains("them"));
    System.out.println("something "+test.contains("something"));
    System.out.println("the "+test.contains("the"));
    System.out.println("t "+test.contains("t"));
    System.out.println("then "+test.contains("then"));
    System.out.println("test "+test.contains("test"));
    System.out.println("wardrobe "+test.contains("wardrobe"));
    System.out.println("test "+test.getCount("test"));
    System.out.println("the "+test.getCount("the"));
    System.out.println("then "+test.getCount("then"));
    System.out.println("tes "+test.getCount("tes"));
    System.out.println("tesasdsd "+test.getCount("tesasdsd"));
    System.out.println("t "+test.getCount("t"));
    System.out.println("thenn "+test.getCount("thenn"));
    System.out.println("number "+test.getCount("number"));
    System.out.println("numbew "+test.getCount("numbew"));
    System.out.println("wardrobe "+test.getCount("wardrobe"));
    WordCountTree test2 = new WordCountTree();
    test2.incrementCount("the");
    System.out.println(test2.getNodeCount());
    test2.incrementCount("then");
    System.out.println(test2.getNodeCount());
    test2.incrementCount("test");
    System.out.println(test2.getNodeCount());
    test2.incrementCount("number");
    System.out.println(test2.getNodeCount());
  }
}
