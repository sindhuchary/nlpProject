/** 
*   Authors:    Xisen Tian, Josh Hudson, Mohamed Kotchtali 
*   Date:       15APR15
*   Purpose:    This class runs the K-means cluster algorithm using the Tagger and Vectorizer 
*/
import java.util.List; 
import java.util.ArrayList; 
import util.Counter; 
public class Cluster{
    private List<Integer> indices; 
    private Counter<String> currentCentroid;
    private Counter<String> oldCentroid;  
    public Cluster(){
        indices=new ArrayList<Integer>();
        currentCentroid = new Counter<String>();
        oldCentroid     = new Counter<String>();
    }
    //getters functions
    public List<Integer> getIndices(){return indices;}
    public Counter<String> getCurrentCentroid(){return currentCentroid;}
    public Counter<String> getOldCentroid(){return oldCentroid;}
    
    public void updateCentroid(ArrayList<Counter<String>> vectors){
        for(int i : indices){
            Counter<String> vect=vectors.get(i);
			for(String s : vect.keySet()){
			  currentCentroid.incrementCount(s, vect.getCount(s));
			}
        }
    }

}


