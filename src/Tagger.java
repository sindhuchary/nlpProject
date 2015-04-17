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
  private static HashMap<String,List<ArrayList<String>>> productMap;

  //HashMap< ProductId, ListList Sentences >
  //HashMap< ProductId, List rawSentences >

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
             
             productMap.put( newLine.split( ":" )[1].substring(1), new ArrayList<ArrayList<String>>() );

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
  public List<ArrayList<String>> vectorize( String filename ) {
    List<String> vectors = new ArrayList<String>();

    //Parse the file for the product id and review text
    parse( filename );
      
    //Call the tag function to tag each sentence with POS tags
    vectors = tag( filename );

    //Call the removeNoise function to get rid of non-informative words
    List<ArrayList<String>> finalVectors = removeNoise( vectors );

    return finalVectors;
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

    return tagged;
  }

  //RemoveNoise takes the tagged sentences and returns a list of vectors containing words essential to the review sentence
  private List<ArrayList<String>> removeNoise( List<String> tReviews ) {
    List<ArrayList<String>> noNoise = new ArrayList<ArrayList<String>>();
    int count = 0;

    String[] temp, temp2;//holds temporary string arrays to split the tags from the words
    String[] keepTags = { "NN", "NNS", "NNP", "NNPS", "RB", "RBR", "RBS", "JJ", "JJR", "JJS", "FW" };//tags we want to keep

    //Trace through each sentence and pull out the words we want
    for( String t : tReviews ) {
      noNoise.add( new ArrayList<String>() );
  
      temp = t.split(" ");//split each sentence into word/tag pairs

      //Split each word/tag pair and add the ones matching our keepTags list to the final vector
      //Java contains? String contains from array? TODO****
      for( int i = 0; i < temp.length; i++ ) {
        temp2 = temp[i].split("/");

        for( int j = 0; j < keepTags.length; j++ ) {

          if( temp2[1].equals(keepTags[j]) )
            noNoise.get(count).add(temp2[0]);//add word to its sentence's vector

        }   
      }

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
    productMap = new HashMap<String,List<ArrayList<String>>>();

    //Vectorize the Strings in the input file
    List<ArrayList<String>> vectors = newTagger.vectorize( args[0] );
    
    //Print out the vectors
    for( List<String> s : vectors )
      System.out.println( s );

  }

}


