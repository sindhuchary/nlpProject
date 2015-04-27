//ReviewSummarizer
//Tagger.java
//Tian, Khochtali, Hudson
//NLP/Chambers

//Imports
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.NoSuchElementException;

//Stanford POS Tagger imports
//--Taken from: nlp.stanford.edu/software/tagger.shtml
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

//Tagger class reads in a text file of Strings and returns a vectors of Strings in vector form
public class Tagger {

  //Declarations
  private static HashMap<String, ArrayList<List<String>>> productMap;
  private static ArrayList<String> productIDLogger;

  public Tagger(){
    this.productMap = new HashMap<String,ArrayList<List<String>>>();
    this.productIDLogger = new ArrayList<String>();
  }

  private void parse( String filename ) {

    try{

      //Create a reader to read in the file provided
      Scanner scan = new Scanner( new File( filename ) );

      try {
      
        //Temp string to hold new lines
        String newLine;

        //Trace over reviews to parse out product IDs
        while( scan.hasNextLine() ) {

           //Store the line
           newLine = scan.nextLine();

           if( newLine.contains( "productId" ) ) {
             
             productMap.put( newLine.split( ":" )[1].substring(1), new ArrayList<List<String>>() );

           }  

           //Write productId to a file?
           //Write subsequent reviews beneath?
	   //--Basically, until you see a new productId, skip over the junk and add the reviews to the file with 
	   //--new lines in between
        }

        //Close the file stream
        scan.close();

      } catch( NoSuchElementException nsee ) {
          System.err.println( "Problem in the file input stream." );
          return;
      }

    } catch( FileNotFoundException fnfe ) {
	System.err.println( "File open error.  Please check that your file is correct." );
        System.exit(13);
    }

  }

  //Vectorize takes in a filename and returns a vectorized form of the Strings with only the key information
  public HashMap<String, ArrayList<List<String>>> vectorize( String filename ) {
    List<String> vectors = new ArrayList<String>();

    //Parse the file for the product id and review text
    //parse( filename );
      
    //Call the tag function to tag each sentence with POS tags
    vectors = tag( filename );

    //Call the removeNoise function to get rid of non-informative words
    ArrayList<List<String>> finalVectors = removeNoise( vectors );

    return productMap;//finalVectors
  }

  //Tag takes the filename, reads in the Strings, tags them with Penn Treebank tags, and returns it in a List of Strings
  private List<String> tag( String filename ) {
    List<String> tagged = new ArrayList<String>();

    //Loads the proper Stanford POS Tagger trained on English words
    MaxentTagger tagger = new MaxentTagger("lib/english-left3words-distsim.tagger");

    try {
      //Creates a tokenizer based on a Buffered reader that reads in the provided filename
      List<List<HasWord>> sentences = MaxentTagger.tokenizeText( new BufferedReader( new FileReader( filename )));

      //Tags the words and adds them to the List
      for( List<HasWord> sentence : sentences ) {
        List<TaggedWord> tSentence = tagger.tagSentence( sentence );
        tagged.add( Sentence.listToString( tSentence, false ) );
      }

    } catch( FileNotFoundException fnfe ) {
      fnfe.printStackTrace();
    }  

    return tagged;//List<String> and every string is a sentence with this structure: "Cats/NN scratch/vb walls/NN"
  }

  //RemoveNoise takes the tagged sentences and returns a list of vectors containing words essential to the review sentence
  private ArrayList<List<String>> removeNoise( List<String> tReviews ) {
    ArrayList<List<String>> noNoise = new ArrayList<List<String>>();
    int count = 0;
    int IDcount = 0;

    String[] temp, temp2;//holds temporary string arrays to split the tags from the words
    String[] keepTags = { "NN", "NNS", "NNP", "NNPS", "RB", "RBR", "RBS", "JJ", "JJR", "JJS"/*, "FW"*/ };//tags we want to keep

    //Trace through each sentence and pull out the words we want
    for( String t : tReviews ) {


      //*************************TO DO**********************************
      if( t.contains( "UNKQQQ" ) && (t.length() < 26)) {
        t = t.substring( 0, t.length() - 13 );//remove the ending tag of ProductID
        if( !productMap.containsKey( t ) ) {
          productMap.put( t, new ArrayList<List<String>>() );//add the productID to the HashMap
          productIDLogger.add(t);//add the productID to the ArrayList to give it an index
          IDcount++;//increase the ID count so that reviews can be matched back to specific ID
        }
	continue;
      }

      //Initialize a new ArrayList in each noNoise entry
      noNoise.add( new ArrayList<String>() );
  
      temp = t.split(" ");//split each sentence into word/tag pairs

      //Split each word/tag pair and add the ones matching our keepTags list to the final vector
      //Java contains? String contains from array? TODO****
      for( int i = 0; i < temp.length; i++ ) {
        temp2 = temp[i].split("/");

        for( int j = 0; j < keepTags.length; j++ ) {

          if( temp2[1].equals(keepTags[j]) ) {
            noNoise.get(count).add(temp2[0]);//add word to its sentence's vector
          }

        }   
      }

      //Add each vector to it's corresponding productID entry in the HashMap
      productMap.get(productIDLogger.get(IDcount-1)).add(noNoise.get(count));

      count++;//increase count to move on to another sentence

    }

    return noNoise;
  }

  public static void main( String[] args ) throws Exception {
    
    //Usage statement
    if( args.length != 1 ) {
      System.err.println( "Usage: java Tagger [fileToTag]" );
      return;
    }

    //Create an instance of a Tagger()
    Tagger newTagger = new Tagger();

    //Initialize productMap
    productMap = new HashMap<String,ArrayList<List<String>>>();
    productIDLogger = new ArrayList<String>();

    //Vectorize the Strings in the input file
    HashMap<String, ArrayList<List<String>>> vectors = newTagger.vectorize( args[0] );
   
    //Useful help statements 
    System.out.println( productIDLogger.get(1) );
    System.out.println( productMap.get( productIDLogger.get(1) ) );

  }

}


