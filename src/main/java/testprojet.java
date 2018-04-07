
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;
import org.json.*;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import it.stilo.g.structures.WeightedDirectedGraph;
import it.stilo.g.util.NodesMapper;
import java.util.Arrays;


import it.stilo.g.algo.ConnectedComponents;
import it.stilo.g.algo.SubGraphByEdgesWeight;
import it.stilo.g.structures.WeightedDirectedGraph;
import it.stilo.g.structures.WeightedUndirectedGraph;
import it.stilo.g.util.MemInfo;
import it.stilo.g.util.WeightedRandomGenerator;
import java.util.Arrays;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import it.stilo.g.util.GraphReader; 




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Simon
 */
public class testprojet {
        private static final Logger logger = LogManager.getLogger(testprojet.class);

    public static void main(String[] args) throws IOException {

    String tweet = "twitter";
            
    File folder = new File(tweet);
    File[] listOfFiles = folder.listFiles();
    for (File file : listOfFiles) {
        if (file.isFile()) {
        System.out.println(file.getName());
        FileInputStream fstream = new FileInputStream(file);
        GZIPInputStream gzStream = new GZIPInputStream(fstream);
        InputStreamReader isr = new InputStreamReader(gzStream);
        BufferedReader br = new BufferedReader(isr);
        String line;
        Hashtable table = new Hashtable();
        StringTokenizer st;
        String mot;
        int nbOcc;

        
        //Read File Line By Line
        ArrayList<ArrayList<String>> matrix = new ArrayList<ArrayList<String>>();
        int k =0;
        
        while ((line = br.readLine()) != null) {
            // We get the Json part of the file
            String json_part=line.substring(44,line.length()-11);
            JSONObject text = new JSONObject(json_part);
            //System.out.println(tasse.getString("text")); 
            //System.out.println(json_part);
            ArrayList line_word =new ArrayList();

            st = new StringTokenizer(text.getString("text"), " ,.;:_-+*/\\.;\n\"'{}()=><\t!?");
	   while(st.hasMoreTokens())
	    {
	      mot = st.nextToken();
              line_word.add(mot);
	      if (table.containsKey(mot))
		{
		  nbOcc = ((Integer)table.get(mot));
		  nbOcc++;
		}
	      else nbOcc = 1;
	      table.put(mot, nbOcc);
	    }
           matrix.add(line_word);
           //System.out.println(matrix.get(k));
           k++;
	}

      Enumeration lesMots = table.keys();
      ArrayList<Integer> occ =new ArrayList<Integer>();
      ArrayList word =new ArrayList();
      
      
        while (lesMots.hasMoreElements())
	{
	  mot = (String)lesMots.nextElement();
	  nbOcc = ((Integer)table.get(mot));
          occ.add(nbOcc);
          word.add(mot);
          
          
	  //System.out.println("Le mot " + mot + " figure " +nbOcc + " fois");
	}
      
      
      ArrayList sorted_occ =new ArrayList();
      ArrayList sorted_word =new ArrayList();

 
      //we take the top 1000 words by occurencies
      
      for (int j=0; j<1000; j++){
          
        int nbOccmax=0;
        int rank=0;
        int rank_max=0;
        //String word_number_max=0;
        
        sorted_word.add(0);
        sorted_occ.add(0);

        
           for(int i = 0 ; i < occ.size(); i++){
            

            if (occ.get(i) >nbOccmax){
                nbOccmax=occ.get(i);
                sorted_word.remove(j);
                sorted_occ.remove(j);
                sorted_word.add(j,word.get(i));
                sorted_occ.add(j,occ.get(i));
                rank_max=rank;
            }
            //System.out.println("Le mot " + mot + " figure " +nbOcc + " fois");
            rank++;
          }
           occ.set(rank_max,0); //on met le nombre d'occurrence max à 0 
            //System.out.println(sorted_word.get(j));
            //System.out.println(sorted_occ.get(j));
           
      }
      
      //System.out.println(matrix.size());
        Table<String, String, Integer> coOccurrence = HashBasedTable.create();
        for (int l=0;l<matrix.size()-1;l++){
            for (int idx = 0; idx<matrix.get(l).size()-1; idx++) {
                String token = matrix.get(l).get(idx);
                //System.out.println(token);

                Map<String, Integer> tokenRow = coOccurrence.row(token);

                for (int otherIdx = 0; otherIdx <= matrix.get(l).size()-1 ; otherIdx++) {
                    if (otherIdx < 0 || otherIdx >= matrix.size()) {
                        continue;
                    }
                    if (otherIdx == idx) {
                        continue;
                    }
                    String other = matrix.get(l).get(otherIdx);
                    //System.out.println(other);

                }
            }
        }

        int size_graph=matrix.size();

        System.out.println(size_graph);
        
        
        
        int worker = (int) (Runtime.getRuntime().availableProcessors());

        logger.info("\t----\t" + size_graph + "@" + 4 + ":" + worker + "\t---");
        WeightedDirectedGraph g = new WeightedDirectedGraph(size_graph);
        NodesMapper<String> mapper=new NodesMapper<String>();

        for (int l=0;l<matrix.size()-1;l++){
        for (int idx = 0; idx<matrix.get(l).size()-1; idx++) {
            String token = matrix.get(l).get(idx);
            //System.out.println(token);


            for (int otherIdx = 0; otherIdx <= matrix.get(l).size()-1 ; otherIdx++) {
                if (otherIdx < 0 || otherIdx >= matrix.size()) {
                    continue;
                }
                if (otherIdx == idx) {
                    continue;
                }
                if (coOccurrence.get(matrix.get(l).get(otherIdx),matrix.get(l).get(idx)) != null) {
                    //System.out.println(matrix.get(l).get(idx));
                //System.out.println(matrix.get(l).get(otherIdx));
                coOccurrence.get(matrix.get(l).get(otherIdx),matrix.get(l).get(idx));
                

                g.testAndAdd(mapper.getId(matrix.get(l).get(idx)), mapper.getId(matrix.get(l).get(otherIdx)), coOccurrence.get(matrix.get(l).get(idx), matrix.get(l).get(otherIdx)));

                }
                //System.out.println(matrix.get(l).get(idx));
                //System.out.println(matrix.get(l).get(otherIdx));
                coOccurrence.get(matrix.get(l).get(otherIdx),matrix.get(l).get(idx));
                



            }

        }
    }        
        logger.info(Arrays.deepToString(g.out));
        logger.info(Arrays.deepToString(g.in));
        logger.info(Arrays.deepToString(g.weights));

        g = SubGraphByEdgesWeight.extract(g, 3.0, 1);
        
        logger.info(Arrays.deepToString(g.out));
        logger.info(Arrays.deepToString(g.in));
        logger.info(Arrays.deepToString(g.weights));
                MemInfo.info();


                //GraphReader.readGraph(g,true);

                //g = SubGraphByEdgesWeight.extract(g, 3.0, 1);


        br.close();
        
        
            // DO SOMETING;
        }
    }    
    
    

    
}}
