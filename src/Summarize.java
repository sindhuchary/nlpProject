/** 
*   Authors:    Xisen Tian, Josh Hudson, Mohamed Kotchtali 
*   Date:       15APR15
*   Purpose:    This class runs the K-means cluster algorithm using the Tagger and Vectorizer 
*/

//import Vectorizer.java

public class Summarize{

    public Summarize(){
        
    }
    public static void main(String[] args) throws Exception{
        System.out.println("In Summarizer"); 
       //get file input from stdin
        if(args.length != 1){
            System.err.println("Please provide a single datafile"); 
            System.exit(0); 
        }
        String filename = args[0]; 

       //TODO: run tagger on file
       
        //Create an instance of a Tagger()
        Tagger newTagger = new Tagger();

        //Vectorize the Strings in the input file
        
        //Print out the vectors
        
        //TODO: run vectorize --> HashMap<Ids, ArrayList<Counter>> 

       //TODO: run the k-means cluster Algorithm


   // HashMap<String, ArrayList<Counter<String>> >
       //double Loop here: loop through products and loop til clusters centroid dont change that much anymore
       
    }

}
