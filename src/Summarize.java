/** 
*   Authors:    Xisen Tian, Josh Hudson, Mohamed Kotchtali 
*   Date:       15APR15
*   Purpose:    This class runs the K-means cluster algorithm using the Tagger and Vectorizer 
*/

//import Vectorizer.java

import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import util.*;

public class Summarize{
  private int k =4;
  private HashMap<String, ArrayList<List<String>> > allProds ;//gets filled in parse()
  private HashMap<String, ArrayList<Counter<String>>> allProdsVects;//gets filled in main after calling makeVectors()
  private HashMap<String, ArrayList<Cluster>> allProdsClusters ;//gets filled in iniateClusters() and clusterize()
  public Summarize(){
    allProds = new HashMap<String, ArrayList<List<String>> >(); 
    allProdsVects = new HashMap<String, ArrayList<Counter<String>>>();
    allProdsClusters= new HashMap<String, ArrayList<Cluster>>();
  }
  //getters and setters functions ------------------------------------------------------------
  public HashMap<String, ArrayList<List<String>>> getAllProds(){return allProds;}
  public HashMap<String, ArrayList<Counter<String>>> getAllProdsVects(){return allProdsVects;}
  void setAllProds(HashMap<String, ArrayList<List<String>>> argHM){allProds=argHM;}

  void setAllProdsVects(HashMap<String, ArrayList<Counter<String>>> argHM){allProdsVects=argHM;}
  public HashMap<String, ArrayList<Cluster>> getAllProdsClusters(){return allProdsClusters;}
  //------------------------------------------------------------------------------------------
  
  
  /* This function will be called by clusterize(). It determines when is are the clusters not changing enough
  * so we can stop looping and updating.  */
  public boolean stopClusterizeLoop(ArrayList<Cluster> curProdClusters){
    for(Cluster clus : curProdClusters){      
      if(!clus.areCentroidsSimilar()) return false;
    }
    return true;
  }
  
  /* This function will go through the products and make k clusters for each one. Notice that the 
  * constructor for the class Cluster takes a vector (which is just a Counter<String>) as an 
  * argument and makes it the currentCentroid for that Cluster. So the vector we pick to initialize
  * the n_th Cluster would be the vector of index (number_of_vectors/k)*n_th. Also notice we need 
  * to do this process for all products we have.  */
  public void initiateClusters(){
    for(String prod : allProdsVects.keySet()){//loop products
      ArrayList<Counter<String>> curProdVects = allProdsVects.get(prod);      
      int jump = curProdVects.size()/k;
      
      ArrayList<Cluster> tempArrClusters=new ArrayList<Cluster>();
      for(int i=0; i<k; i++){
        if(i>0 && jump==0){break;}//not enough vectors
        Cluster tempC = new Cluster( curProdVects.get(i*jump) );
        tempArrClusters.add(tempC);
      }
      allProdsClusters.put(prod, tempArrClusters);
    }
  }  
  
  /* This function will just loop vectors for every products, assign those vectors to their correspondant 
  * clusters, and then call updateCluster() function which will update the currentCentroid in that cluster.
  * There is a while loop that keep repeating the process until the clusters are not changing that much 
  * anymore at which time we stop.  */
  public void clusterize(){
    for(String prod : allProdsVects.keySet()){//loop products
      ArrayList<Counter<String>> curProdVects = allProdsVects.get(prod);       
      ArrayList<Cluster> curProdClusters = allProdsClusters.get(prod);
      while(!stopClusterizeLoop(curProdClusters)){//while the clusters are still changing
        for(Counter<String> vect : curProdVects){//loop vectors of prod
          double maxSim=0.0, tempSim=0.0; Cluster maxClus=null; Counter<String> maxVect=null;
          //loop clusters of prod to be able to classify vectors
          if(curProdClusters.isEmpty()){
            System.out.println("!!!!!!!!!!!! ---- EMPTY curProdClusters ---- !!!");  
          }
          else{
            System.out.println("#####    NOT EMPTY ! Size :  " + curProdClusters.size()); 
          }

          for(Cluster clus : curProdClusters){//loop clusters
            tempSim = clus.simCosine(vect);
            if(tempSim>=maxSim) {maxSim=tempSim; maxClus=clus; maxVect=vect;}
          }
          if(curProdVects.indexOf(maxVect) < 0 ){
            System.out.println("maxVect"+maxVect); 
            System.out.println("^^^^^^maxVect is null!!!****"); 
          }
          else{
            maxClus.addIndice(curProdVects.indexOf(maxVect));
            }
        }
        //loop and update all clusters
        for(Cluster clus : curProdClusters){clus.updateCentroid(curProdVects);}
      }//End of while loop
    }//end of loop products
  }
  
  /* This function will loop all the vectors, find out which vector is the most similar to the currentCentroid,
  * get the index for that vector and retrieve the original sentence from allProds (allProds has all the 
  * original sentences) . */
  public void retrieveBackSentences(){
    System.out.println("*************************************************************************************");
    System.out.println("*************************************************************************************");
    System.out.println("*************************************************************************************");
    for(String prod : allProdsClusters.keySet()){
      System.out.println("prod: " + prod); 
      ArrayList<Cluster> curProdClusters = allProdsClusters.get(prod);
      for(Cluster clus : curProdClusters){      
        ArrayList<Counter<String>> curProdVects = allProdsVects.get(prod);
        double temp=0.0,max=0.0; Counter<String>  maxVect=null;int maxIndex=0, index=0;
        for(Counter<String> vect : curProdVects){//loop products
          temp=clus.simCosine(vect);
          if(temp>=max) {max=temp; maxVect=vect;maxIndex=index;}
          index++;
        }
        System.out.print(
                        "For cluster: "+clus+
                      //  +"Sent vect: "+maxVect+
                        "\nsent: "+allProds.get(prod).get(maxIndex)); 
        System.out.println("\n=====================================================================================\n\n");
      }
    }
  }
    
  public void printAllProds(){
    System.out.println("allProds: ");
    // Get a set of the entries
    Set set = allProds.entrySet();
    // Get an iterator
    Iterator i = set.iterator();
    // Display elements
    while(i.hasNext()) {
       Map.Entry me = (Map.Entry)i.next();
       System.out.print(me.getKey() + ": ");
       System.out.println(me.getValue());
    }
  }  
  public void printAllProdsClusters(){
    System.out.println("allProdsClusters: ");
    for(String prod : allProdsClusters.keySet() ){
      System.out.println(prod+" => "+allProdsVects.get(prod).size()+"vectors divided between "+k+" clusters"+"\n"+allProdsClusters.get(prod) );
    }
  }
  
  
  public static void main(String[] args) throws Exception{
    Summarize program = new Summarize();
    
    //get file input from stdin
    if(args.length != 1){
        System.err.println("Please provide a single datafile"); 
        System.exit(0); 
    }
    String filename = args[0]; 

    //run tagger on file//run Vectorizer-----------------------------------------
    Tagger tagger = new Tagger();
    program.setAllProds(tagger.vectorize(filename)); 
    
   //System.out.println("@@@@@@@VECTORS@@@@@@@@@@"+ vectors.toString()); 

    Vectorizer vectorizer =new Vectorizer();
    /*TO FIX: make vectorize() turn back "HashMap<String, ArrayList< List<String>>>"
     * uncomment the next line after we get the fix */    

    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"); 
    HashMap<String, ArrayList<Counter<String>>> myCounters = vectorizer.makeVectors(program.getAllProds() ) ;  

    System.out.println("----------------------MY COUNTERS --------------"); 
   // System.out.println(myCounters); 
    program.setAllProdsVects(myCounters );
    System.out.println("*******AFTER setAllProdsVects*****" ); 
   
    //---------------------------------------------------------------------------

    //run the k-means cluster Algorithm       
    program.initiateClusters();
    program.clusterize();
    program.printAllProdsClusters();
    program.retrieveBackSentences();

  }


}
