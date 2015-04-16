//ReviewSummarizer
//Tagger.java
//Tian, Khochtali, Hudson
//NLP/Chambers

//Imports
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import util.Counter;

public class Vectorizer {

  public Vectorizer( ) {//constructor
  }
 
  //input is hashmap of ID's and list of sentences
  public HashMap<String, ArrayList<Counter<String>> > makeVectors(HashMap<String, ArrayList< List<String> > > hm) {    
    HashMap<String, ArrayList<Counter<String>> > retHM = new HashMap<String, ArrayList<Counter<String>> >();
    for(String ID : hm.keySet() ){
        ArrayList<Counter<String>> tempVectList=new ArrayList<Counter<String>>();
        //hm.get(ID) returns back List< List<String> > 
        for(List<String> sentence : hm.get(ID) ){//loop through the list of sentences for that specific ID 
          Counter tempVect = new Counter<String>();
          for(String word : sentence){//loop through the sentence
            tempVect.incrementCount(word,1.0);
          }
          tempVectList.add(tempVect);
        } 
        retHM.put(ID,tempVectList);
    }
    return retHM;
  }

  //RemoveNoise takes the tagged sentences and returns a list of vectors containing words essential to the review sentence
  
  
  public static void main( String[] args ) throws Exception {
     
     
  }
}


