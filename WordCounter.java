import java.util.*;
import java.io.*;
/**
* The class generates an html document containing a wordcloud
* based on an external file.
*/
public class WordCounter{
  WordCountTree myTree;
  List<String> stopWords = new ArrayList<String>();


  /**
   * Constructor the class.
   * @param inputFileName the name of the file with words to generate the word cloud
   * @param stopwordsName the name of the file with stop words that shouldn't be counted
   */
  public WordCounter(String inputFileName, String stopwordsName) {
    myTree = new WordCountTree();
    loadFiles(inputFileName, stopwordsName);
  }

  /**
   * Prints the HTML for the word cloud on the console.
   * @param int the number of words of the word cloud
   */
  public void printWordCloudHTML(int numWords) {
    List<WordCount> wordCounts = myTree.getWordCountsByCount();
    if(numWords > wordCounts.size()){
      System.out.println("There are not these many words in the text, please type a smaller value");
    } else if(numWords <= 0){
      System.out.println("The number of words on a wordcloud must be positive");
    } else{
      String html = WordCloudMaker.getWordCloudHTML("Word Counts", wordCounts.subList(0,numWords));
      createWordCloudWebPage(numWords);
      System.out.println(html);
    }
  }
  /**
   * Creates a html file with the containing
   * a wordcloud with numWords words.
   * @param numWords the number of words on the word cloud
   */
  private void createWordCloudWebPage(int numWords){
    List<WordCount> wordCounts = myTree.getWordCountsByCount();
    String html = WordCloudMaker.getWordCloudHTML("Word Counts", wordCounts.subList(0,numWords));
    try {
      File fileToCreate = new File("wordcloud.html");
      fileToCreate.createNewFile();
    } catch (IOException e) {
      System.out.println("");
    }
    try {
      FileWriter writeOutput = new FileWriter("wordcloud.html");
      writeOutput.write(html);
      writeOutput.close();
    } catch (IOException e) {
      System.out.println("");
    }
  }
  /**
   * Adds the word to the WordCountTree.
   * @param word the word to be added to the WordCountTree
   */
  public void addWord(String word) {
    myTree.incrementCount(word);
  }
/**
 * Loads the inputFile and the stopWords files,  and processes their data. 
 */
  
  private void loadFiles(String inputFileName, String stopwordsName) {
    File stopWordsFile = new File(stopwordsName);
    Scanner scanner1 = null;
    try {
      scanner1 = new Scanner(stopWordsFile);
    } catch (FileNotFoundException e) {
      System.err.println(e);
      System.exit(1);
    }
    
    while(scanner1.hasNextLine()) {
      String next = scanner1.nextLine();
      stopWords.add(next);
    }
    File inputFile = new File(inputFileName);
    Scanner scanner = null;
      try {
          scanner = new Scanner(inputFile);
      } catch (FileNotFoundException e) {
          System.err.println("Sorry, it was not possible to find your file, try running the code again with the correct name of  your file");
          System.exit(1);
      }

      while(scanner.hasNext()){
        String next = scanner.next();
        String[] splitnext = next.split("--");
        for (int i=0; i<splitnext.length; i++) {
          splitnext[i] = splitnext[i].replaceAll("\\s*\\p{Punct}+\\s*$", "").toLowerCase();
          splitnext[i] = splitnext[i].replaceAll("^\\s*\\p{Punct}+\\s*", "");
          if(!stopWords.contains(splitnext[i]) && !splitnext[i].equals("")) {
            addWord(splitnext[i]);
          } 
        }
        
      }


  }
/**
 * Generates a wordcloud with the file specified by the
 * user with the number of number of words specified by the user
 */
  public static void main(String[] args) { 
    Scanner userInput = new Scanner(System.in);
    String inputFileName;
    String stopwordsName;
    int numWords;
    System.out.println("Type the name of your file with your text, to make your wordcloud");
    inputFileName = userInput.nextLine();
    WordCounter wordCount = new WordCounter(inputFileName, "StopWords.txt");
    System.out.println("Type the number of words you want on your tree");
    numWords = userInput.nextInt();
    wordCount.printWordCloudHTML(numWords);
  }
}
