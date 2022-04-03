package org.uwindsor.acc.loaddb;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
import org.uwindsor.acc.search.Search;
import org.uwindsor.acc.searchstringprocessing.RegexFilter;
import org.uwindsor.acc.searchstringprocessing.StopWords;
import org.uwindsor.acc.webcrawler.InvalidInputException;
public class LoadDB {
    
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
		} catch( Exception ex) {
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
            // Condition to check if the Array element is present the hash-map
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
    
    public static HashMap<String, String> sortByValueString(HashMap<String, String> map)
    {
    	if( map==null) {
    		return null;
    	}
    	HashMap<String, String> sorted = new HashMap<String, String>();
    	map.entrySet()
    	  .stream()
    	  .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
    	  .forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));
        return sorted;
    }
    
    
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> map)
    {
    	if( map==null) {
    		return map;
    	}
    	HashMap<String, Integer> sorted = new HashMap<String, Integer>();
    	map.entrySet()
    	  .stream()
    	  .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
    	  .forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));
        return sorted;
    }
    
    public static HashMap<String, String> first500(HashMap<String, Integer> map) {
    	int count = 500;
    	HashMap<String,String> freqMap500=new HashMap<String,String>();
    	for (Map.Entry<String, Integer> mapElement : map.entrySet()) {
    		freqMap500.put(mapElement.getKey(), Integer.toString(mapElement.getValue()));
    		count--;
    		if(count==0){
    			break;
    		}
    	}
    	return freqMap500;
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
    public static HashMap<String, String> readDBURLs(File file)
    {
    	
    	HashMap<String, String> dbURL = new HashMap<String, String>();
    	BufferedReader br = null;
    	try
    	{
    		
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

    public static void loadDataBase(final String[] urls) throws InvalidInputException {
    	String pathDBKeys = System.getProperty("user.dir")+"/writeDB.txt";
		String pathDBURLs = System.getProperty("user.dir")+"/writeURL.txt";
		
    	HashMap<String, HashMap<String, String>> dbKeys;
    	HashMap<String, String> dbURLs;
    	
    	File dataDB = new File(pathDBKeys);
        File dataURL = new File(pathDBURLs);
    	
    	
    	if(dataDB.exists())
    	{
    		dbKeys = (HashMap<String, HashMap<String, String>>) Search.readDB(dataDB);
    		
    	}
    	else
    	{
    		dbKeys = new HashMap<String, HashMap<String, String>>();
   
    	}
    		
    	if(dataURL.exists())
    	{
    		dbURLs = readDBURLs(dataURL);
    	}
    	else
    	{
    		dbURLs = new HashMap<String, String>();
    		
    	}
    	for(String url:urls)
    	{
	    	String text = convertText(url);
			
			RegexFilter rFilter = new RegexFilter();
			StopWords sw = new StopWords();
			
			ArrayList<String> searchStringAfterRegex = rFilter.StringProcessViaRegex(text);
			System.out.println("Original string: \""+text+"\"");
			System.out.println("String after Regex: "+searchStringAfterRegex.toString());
			
			String newText = null;
			try {
				newText = sw.RemoveStopWordsString(searchStringAfterRegex);
			} catch ( Exception ex) {
				continue;
			}
			
	    	dbURLs.put(url, newText);
	    	HashMap<String, Integer> freqMap = countFreq(newText);
	    	freqMap = sortByValue(freqMap);
	    	HashMap<String, String> freqMap500 = new HashMap<String, String>();
	    	freqMap500 = first500(freqMap);
	    	for(Map.Entry<String, String> keyword:freqMap500.entrySet())
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