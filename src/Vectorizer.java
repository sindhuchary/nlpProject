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
     HashMap<String, ArrayList<List<String>> > hh = new HashMap<String, ArrayList<List<String>> >();
	 String Id="1";String s1="loving " ;String s2="life";
	 List<String> l0 = new ArrayList<String>(); l0.add(s1);l0.add(s2);
	 ArrayList<List<String>> li0=new ArrayList<List<String>>();
	 li0.add(l0);
	 hh.put(Id,li0);
	 	 
	 Id="2";s1="having" ;s2="dreams";
	 List<String> ll = new ArrayList<String>(); ll.add(s1);ll.add(s2);
	 ArrayList<List<String>> lii=new ArrayList<List<String>>();
	 lii.add(ll);
	 hh.put(Id,lii);
	 
	 HashMap<String, ArrayList<Counter<String>> > hm =v.makeVectors(hh);
	 
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
     
  }
}


