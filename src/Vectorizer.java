//ReviewSummarizer
//Tagger.java
//Tian, Khochtali, Hudson
//NLP/Chambers

//Imports
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.util.Collections;
import util.Counter;

public class Vectorizer {

  public Vectorizer( ) {//constructor
  }
  
  //this will delete all empty strings from the hashmap
  public HashMap<String, ArrayList< List<String> >> cleanHM(HashMap<String, ArrayList< List<String> >> hm){
    for(String ID : hm.keySet() ){
      ArrayList<List<String>> curIDSentences = hm.get(ID);
      List<String> tempL= new ArrayList<String>();
      curIDSentences.removeAll(Collections.singleton(tempL));
      //for(List<String> sentence : curIDSentences ){        
        //if(sentence.isEmpty()) {System.out.println("yachaaaaaaaaaaaaaaaaaaaaaaaaaaaaawm");curIDSentences.remove(sentence);}
      //}
    }
    return hm;
  }
  
  //input is hashmap of ID's and list of sentences
  public HashMap<String, ArrayList<Counter<String>> > makeVectors(HashMap<String, ArrayList< List<String> > > hm) {    
    HashMap<String, ArrayList<Counter<String>> > retHM = new HashMap<String, ArrayList<Counter<String>> >();
    for(String ID : hm.keySet() ){
        ArrayList<Counter<String>> tempVectList=new ArrayList<Counter<String>>();
        //hm.get(ID) returns back List< List<String> > 
        for(List<String> sentence : hm.get(ID) ){//loop through the list of sentences for that specific ID 
          Counter<String> tempVect = new Counter<String>(); 
          for(String word : sentence){//loop through the sentence
            tempVect.incrementCount(word,1.0);
          }
          tempVectList.add(tempVect);
        } 
        retHM.put(ID,tempVectList);
    }
    return retHM;
  }
  
  
  public static void main( String[] args ) throws Exception {
	 Vectorizer v =new Vectorizer();
	 //Make a hashmap of arraylist to test makeVectors() function
     HashMap<String, ArrayList<List<String>> > hh = new HashMap<String, ArrayList<List<String>> >();
	 String Id="1";String s1="loving " ;String s2="life";
	 List<String> l1 = new ArrayList<String>(); l1.add(s1);l1.add(s2);
	 Id="3";s1="street" ;s2="washington";
	 List<String> l3 = new ArrayList<String>(); l3.add(s1);l3.add(s2);
	 ArrayList<List<String>> al1=new ArrayList<List<String>>();
	 al1.add(l1);al1.add(l3);
	 hh.put(Id,al1);
	 	 
	 Id="2";s1="having" ;s2="dreams";
	 List<String> l2 = new ArrayList<String>(); l2.add(s1);l2.add(s2);
	 ArrayList<List<String>> al2=new ArrayList<List<String>>();
	 al2.add(l2);
	 hh.put(Id,al2);
	 
	 HashMap<String, ArrayList<Counter<String>> > hm =v.makeVectors(hh);
    {//Print the hashmap-----------------------------
      // Get a set of the entries
      Set set = hm.entrySet();
      // Get an iterator
      Iterator i = set.iterator();
      // Display elements
      while(i.hasNext()) {
         Map.Entry me = (Map.Entry)i.next();
         System.out.print(me.getKey() + ": ");
         System.out.println(me.getValue());
      }
    }//End of the print section---------------------- 
  }
}


