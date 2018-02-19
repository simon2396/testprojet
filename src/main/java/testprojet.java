
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

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
        //Read File Line By Line
        while ((line = br.readLine()) != null) {
        // Print the content on the console
        System.out.println(line);
        }
        br.close();
            // DO SOMETHING;
        }
    }    
    
    

    
}}
