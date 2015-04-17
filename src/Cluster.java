/** 
*   Authors:    Xisen Tian, Josh Hudson, Mohamed Kotchtali 
*   Date:       15APR15
*   Purpose:    This class runs the K-means cluster algorithm using the Tagger and Vectorizer 
*/
import util.Counter; 
import java.util.List; 
import java.util.ArrayList; 
public class Cluster{
    private List<Integer> indices; 
    private Counter<String> currentCentroid;
    private Counter<String> oldCentroid;  
    public Cluster(){
        indices=new List<Integer>();
        currentCentroid = new Counter<String>();
        oldCentroid     = new Counter<String>();
    }
    //getters functions
    public List<Integer> getIndices(){return indices;}
    public Counter<String> getCurrentCentroid(){return currentCentroid;}
    public Counter<String> getOldCentroid(){return oldCentroid;}
    
    public updateCentroid(ArrayList<Counter<String>> vectors){
        for(int i : indices){
            
        }
    }

}


