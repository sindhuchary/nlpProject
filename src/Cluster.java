/** 
*   Authors:    Xisen Tian, Josh Hudson, Mohamed Kotchtali 
*   Date:       15APR15
*   Purpose:    This class runs the K-means cluster algorithm using the Tagger and Vectorizer 
*/
import java.util.List; 
import java.util.ArrayList; 
import java.util.Set; 
import util.Counter; 
public class Cluster{
    private List<Integer> indices;
    private Counter<String> currentCentroid;
    private Counter<String> oldCentroid;  
    public Cluster(Counter<String> centroidArg){//constructor
        indices=new ArrayList<Integer>();
        currentCentroid = centroidArg;
        oldCentroid     = new Counter<String>();
    }	
    //getters functions----------------
    public List<Integer> getIndices(){return indices;}
    public Counter<String> getCurrentCentroid(){return currentCentroid;}
    public Counter<String> getOldCentroid(){return oldCentroid;}
    //---------------------------------
	
  /* calculates the value of a vector which is the square root of the sum 
   * of the components^2 of the vector */
  public double vectVal(Counter<String> vect){
		double retVal=0.0;
		for(String s : vect.keySet()){
		  retVal+=Math.pow(vect.getCount(s), 2);
		}
		return Math.sqrt(retVal);
	}
	
  //sim cosine of vect and currentCentroid
  public double simCosine(Counter<String> vect){
		//size=(size1<size2)? size1 : size2;
		double temp=0.0;
		for(String str : vect.keySet()){
		  if(currentCentroid.containsKey(str) ){
			temp+=(vect.getCount(str)*currentCentroid.getCount(str) );
		  }
		}
		return temp/(vectVal(vect)*vectVal(currentCentroid));
	}
  
  /* This function will get the vectors that corresponds to the indices and increment
   * the counts in currentCentroid based on the strings that forms these vectors */
  public void updateCentroid(ArrayList<Counter<String>> vectors){
      oldCentroid = currentCentroid;
      for(int i : indices){
        Counter<String> vect=vectors.get(i);
        for(String s : vect.keySet()){
          currentCentroid.incrementCount(s, vect.getCount(s));
        }
      }
      //clear the indices so they can be ready for the next update of currentCentroid
      indices.clear();
  }
	
  /* return true if both old and current centroids have same keysets, This is how we 
   * know if our centroid is similar enough so we can stop looping and updating. This
   * function wil be called in Summarize in the stopClusterizeLoop() function.  */
  public boolean areCentroidsSimilar(){
    if(oldCentroid.isEmpty() ) return false;
    Set<String> curCent = currentCentroid.keySet();
    Set<String> oldCent = oldCentroid.keySet();
    if(curCent.containsAll(oldCent) && oldCent.containsAll(curCent) ) return true;
    return false;
  }
  
  public void addIndice(int i){
    indices.add(i);
  }
  
  public String toString(){
    String retStr="<";
    retStr+=currentCentroid.toString()+" indices: ";
    retStr+=indices.toString();
    retStr+=">\n";
    return retStr;
  }
  
	public static void main( String[] args ){		
		
	}
}


