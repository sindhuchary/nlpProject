/** 
*   Authors:    Xisen Tian, Josh Hudson, Mohamed Kotchtali 
*   Date:       15APR15
*   Purpose:    This class runs the K-means cluster algorithm using the Tagger and Vectorizer 
*/
//run "ant" to build
//run "./summarizer.sh filename kValue" to execute, for example: "./summarizer.sh smallData/Electronics_10kLines.aa 8"
//kValue how many clusters we will have
/*This program will provide a summary for all product even for products that didn't have a lot of reviews, but the
 summary will be very short.*/
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import util.*;
public class Summarize{
  //BufferedReader reader; 
  private int k ;
  private HashMap<String, String> rawSummary;//will be filled in retrieveBackSentences(), stores the final summaries
  private HashMap<String, ArrayList<List<String>> > allProds ;//gets filled in main() using setAllProds()
  private HashMap<String, ArrayList<Counter<String>>> allProdsVects;//gets filled in main after calling setAllProdsVectsx()
  private HashMap<String, ArrayList<Cluster>> allProdsClusters ;//gets filled in iniateClusters() and clusterize()
  
  public Summarize(int kArg){
    allProds = new HashMap<String, ArrayList<List<String>> >(); 
    allProdsVects = new HashMap<String, ArrayList<Counter<String>>>();
    allProdsClusters= new HashMap<String, ArrayList<Cluster>>();
    rawSummary = new HashMap<String, String>();
    k=kArg;
  }
  public HashMap<String, ArrayList<List<String>>> getAllProds(){return allProds;}
  void setAllProds(HashMap<String, ArrayList<List<String>>> argHM){allProds=argHM;}
  
  public HashMap<String, ArrayList<Counter<String>>> getAllProdsVects(){return allProdsVects;}
  void setAllProdsVects(HashMap<String, ArrayList<Counter<String>>> argHM){allProdsVects=argHM;}
  public HashMap<String, ArrayList<Cluster>> getAllProdsClusters(){return allProdsClusters;}
	
  
  
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
          //System.out.println("\nprod:"+prod+" indexOfVect:"+curProdVects.indexOf(vect)+" vect"+vect+" ------------------------------------------------");
          for(Cluster clus : curProdClusters){//loop clusters
            tempSim = clus.simCosine(vect);
            if(tempSim>=maxSim) {maxSim=tempSim; maxClus=clus; maxVect=vect;}
            //System.out.print("tempSim:"+tempSim+" clus:"+clus);
          }
          maxClus.addIndice(curProdVects.indexOf(maxVect));
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
    /*System.out.println("*************************************************************************************");
    System.out.println("*************************************************************************************");
    System.out.println("*************************************************************************************");
    */
    for(String prod : allProdsClusters.keySet()){
      String rawSentOutput="", bulletedOutput="";
      //System.out.println("=====================================================================================");
      //System.out.println("prod: "+prod);
      ArrayList<Cluster> curProdClusters = allProdsClusters.get(prod);
      for(Cluster clus : curProdClusters){
        ArrayList<Counter<String>> curProdVects = allProdsVects.get(prod);
        double temp=0.0,max=0.0; Counter<String>  maxVect=null;int maxIndex=0, index=0;
        for(Counter<String> vect : curProdVects){//loop products
          temp=clus.simCosine(vect);
          if(temp>=max) {max=temp; maxVect=vect;maxIndex=index;}
          index++;
        }
        //System.out.print("For cluster: "+clus+"Sent vect: "+maxVect+"\nsent: "+Tagger.getRawSentencesMap().get(prod).get(maxIndex)+"\n");
        //Preparing strings to be output to the screen****************************
        bulletedOutput+="* ";
        for(String word : allProds.get(prod).get(maxIndex) ){
          bulletedOutput+=word+" ";
        }
        bulletedOutput+="\n";
        for(String word : Tagger.getRawSentencesMap().get(prod).get(maxIndex) ){
          rawSentOutput+=word+" ";
        }
        rawSentOutput+="\n";
        //*************************************************************************
      }
      rawSummary.put(prod, rawSentOutput);
      //System.out.println("B U L L E T S   O U T P U T :"+"\n"+bulletedOutput);
      //System.out.println("R A W   S E N T E N C E S   O U T P U T :\n"+rawSentOutput);
      //System.out.println("=====================================================================================");
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
      System.out.println(prod+" => "+allProdsVects.get(prod).size()+" vectors divided between "+k+" clusters"+"\n"+allProdsClusters.get(prod) );
    }
  }
  public void printAnyHashMap(HashMap<String, ArrayList<List<String>>> hm){
    System.out.println("/n------------------------------------the hashMap: ----------------------------------");
    for(String prod : hm.keySet() ){
      System.out.println(prod+" => "+hm.get(prod) );
    }
  }
    
  public static void main(String[] args) throws Exception{
    if(args.length!=2){
      System.out.println("Usage: java Summarize <fileName> k"); System.exit(0);
    }
    //check if the file exists or not
    File f = new File(args[0]);
    if( !f.exists() || f.isDirectory() ) {
      System.out.println("File does not exist!!!!!!!"); System.exit(0);
    }
    //run python parser ------------------------------
    String[] cmd = {
        "/bin/bash",
        "-c",
        "smallData/parser.py "+args[0]+" > parsedInput.txt"
    };
    Runtime.getRuntime().exec(cmd);
    //------------------------------------------------
    Summarize program = new Summarize(Integer.parseInt(args[1]) );
    Vectorizer v =new Vectorizer();
    Tagger t=new Tagger();
    //program.parse();
    //program.printAllProds();
    //System.out.println("Vectorizing");
    program.setAllProds(v.cleanHM(t.vectorize("parsedInput.txt")) );    
    /*System.out.println("---------------------allProds-----------------------");
    program.printAllProds();
    System.out.println("--------------------- END -----------------------");System.out.println("\n");
    */
    HashMap<String, ArrayList<Counter<String>>> myCounters = v.makeVectors( program.getAllProds() ); 
    
    /*System.out.println("---------------------myCounters-----------------------");
    System.out.println(myCounters);
    System.out.println("--------------------- END -----------------------");System.out.println("\n");
    */
    program.setAllProdsVects(  myCounters);
    /*TODO: run the k-means cluster Algorithm. Double Loop here: loop through products and loop til
     * clusters centroid dont change that much anymore */
    program.initiateClusters();
    //program.printAllProdsClusters();
    //System.out.println("\n\n=====================================================================================CLUSTERIZE==================");
    program.clusterize();
    //program.printAllProdsClusters();
    /*System.out.println("\n\n============================= RAW SENTENCES ========================");
    program.printAnyHashMap(t.getRawSentencesMap());
    System.out.println("\n\n================================== END ============================");System.out.println("\n\n");
    */
    program.retrieveBackSentences(); 
    //read in ID and turn back review-----------------------------------------
    try {
      InputStreamReader in= new InputStreamReader(System.in);
      BufferedReader input = new BufferedReader(in);
      String str="";
      while (true){
        System.out.print("Hit q or quit to leave. Enter product ID: ");
        if((str = input.readLine()) == null ) break;
        if(str.equals("quit") || str.equals("q") ) break;
        String retStr = program.rawSummary.get(str);
        if(retStr != null ) System.out.println(retStr);
        else System.out.println("No review for this id you entered !!!!!!");
      }
    }catch (IOException io) {
      io.printStackTrace();
    }
    //------------------------------------------------------------------------
  }
}
