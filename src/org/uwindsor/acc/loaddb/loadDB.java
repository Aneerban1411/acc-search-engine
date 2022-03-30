package org.uwindsor.acc.loaddb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;

import javax.swing.text.Document;

import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
public class loadDB {
    
    // Function to count frequency of
    // words in the given string
	public static String convertText(String url)
	{
		
		try {
			org.jsoup.nodes.Document doc = Jsoup.connect(url).data("query", "Java").userAgent("Mozilla").cookie("auth", "token").timeout(3000).post();
			String txt = doc.text();
			return txt;
		}
		catch(IOException e) {
			return "";
		}
		
	}
    public static HashMap<String, Integer> countFreq(String str)
    {
    	HashMap<String,Integer> freqMap=new HashMap<>();
 
        // Array of words
        String words[]=str.split(" ");
 
        // Iterating over each word
        for(int i=0;i<words.length;i++)
        {
            // Condition to check if the
            // Array element is present
            // the hash-map
            if(freqMap.containsKey(words[i]))
            {
            	freqMap.put(words[i], freqMap.get(words[i])+1);
            }
            else
            {
            	freqMap.put(words[i],1);
            }
        }
        return freqMap;
    }
    
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> map)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list
            = new LinkedList<Map.Entry<String, Integer> >(
                map.entrySet());
 
        // Sort the list using lambda expression
        Collections.sort(
            list,
            (i1,
             i2) -> i1.getValue().compareTo(i2.getValue()));
 
        // put data from sorted list to hashmap
        HashMap<String, Integer> temp
            = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
    
    public static HashMap<String, String> first100(HashMap<String, Integer> map) {
    	int count = 100;
    	HashMap<String,String> freqMap100=new HashMap<String,String>();
    	for (Map.Entry<String, Integer> mapElement : map.entrySet()) {
    		freqMap100.put(mapElement.getKey(), Integer.toString(mapElement.getValue()));
    		count--;
    		if(count==0){
    			break;
    		}
    	}
    	return freqMap100;
    }
    public static String removeStopWords(String text) {
    	// Driver function for prasham's remove stop words function
    	return text;
    }
    public static HashMap<String, HashMap<String, String>> readDB(String path)
    {
    	// Driver function that rakshana created
    	HashMap<String, HashMap<String, String>> DB = new HashMap<String, HashMap<String, String>>();
    	return DB;
    }
    public static void writeDB(HashMap<String, HashMap<String, String>> map, String path)
    {
    	File file = new File(path);
  
        BufferedWriter bf = null;
  
        try {
  
            // create new BufferedWriter for the output file
            bf = new BufferedWriter(new FileWriter(file));
  
            // iterate map entries
            for (Map.Entry<String, HashMap<String, String>> entry : map.entrySet()) {
            	String str = entry.getKey()+" ";
            	
            	for (Map.Entry<String,String> i:entry.getValue().entrySet()) {
            		// put key and value separated by a colon
                    str += i.getKey() + " " + i.getValue() + " ";
               
            	}
            	 bf.write(str);
            	 bf.newLine();
            }
  
            bf.flush();
        }
        catch (IOException e) {
            e.getMessage();
        }
        finally {
  
            try {
  
                // always close the writer
                bf.close();
            }
            catch (Exception e) {
            }
        }
    }
    public static HashMap<String, String> readDBURLs(String path)
    {
    	
    	HashMap<String, String> dbURL = new HashMap<String, String>();
    	BufferedReader br = null;
    	try
    	{
    		File file = new File(path);
    		br = new BufferedReader(new FileReader(file));
    		String str = null;
    		while((str = br.readLine())!=null)
    		{
    			String[] element = str.split(" ");
    			dbURL.put(element[0].trim(), element[1].trim());
    		}	
    	}
    	catch(IOException e)
    	{
    		;
    	}
    	finally
    	{
    		if(br!=null)
    		{
    			try
    			{
    				br.close();
    			}
    			catch(Exception ex)
    			{
    				;
    			}
    		}
    	}
    	return dbURL;
    }
    
    public static void writeDBURLs(HashMap<String, String> dbURLs, String path)
    {
    	
    	File fileURL = new File(path);
    	BufferedWriter bf = null;
    	String str = "";
    	try
    	{
    		bf = new BufferedWriter(new FileWriter(fileURL));
    		for(Map.Entry<String, String> mapElement: dbURLs.entrySet())
        	{
        		str = mapElement.getKey() + " " + mapElement.getValue();
    			bf.write(str);
        		bf.newLine();
        	}
    		bf.flush();
    	}
    	catch(IOException e)
    	{
    		;
    	}
    	finally
    	{
    		if(bf!=null)
    		{
    			try
    			{
    				bf.close();
    			}
    			catch(Exception ex)
    			{
    				;
    			}
    		}
    	}
    	
    }

    public static void loadDataBase(final String[] urls, String pathDBKeys, String pathDBURLs) {
    	HashMap<String, HashMap<String, String>> dbKeys;
    	HashMap<String, String> dbURLs;
    	if(pathDBKeys == "")
    	{
    		dbKeys = new HashMap<String, HashMap<String, String>>();
    		System.out.println("Enter path to write New DB for keys");
    		Scanner sc = new Scanner(System.in);
    		pathDBKeys = sc.nextLine();
    	}
    	else
    	{
    		dbKeys = readDB(pathDBKeys);
    		
    	if(pathDBURLs == "")
    	{
    		dbURLs = new HashMap<String, String>();
    		System.out.println("Enter path to write New DB for URLs");
    		Scanner sc = new Scanner(System.in);
    		pathDBURLs = sc.nextLine();
    	}
    	else
    	{
    		dbURLs = readDBURLs(pathDBURLs);
    	}
    	for(String url:urls)
    	{
	    	String text = convertText(url);
	    	String newText = removeStopWords(text);
	    	dbURLs.put(url, newText);
	    	HashMap<String, Integer> freqMap = countFreq(newText);
	    	freqMap = sortByValue(freqMap);
	    	HashMap<String, String> freqMap100 = new HashMap<String, String>();
	    	freqMap100 = first100(freqMap);
	    	for(Map.Entry<String, String> keyword:freqMap100.entrySet())
	    	{
	    		if(dbKeys.containsKey(keyword.getKey()))
	    		{
	    			dbKeys.get(keyword.getKey()).put(url, keyword.getValue());
	    		}
	    		else
	    		{
	    			HashMap<String, String> dbValue = new HashMap<String, String>();
	    			dbValue.put(url, keyword.getValue());
	    			dbKeys.put(keyword.getKey(), dbValue);
	    		}
	    	}
    	}
    	writeDB(dbKeys, pathDBKeys);
    	writeDBURLs(dbURLs, pathDBURLs);
    }
    }
}