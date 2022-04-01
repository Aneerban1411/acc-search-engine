package org.uwindsor.acc.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.uwindsor.acc.cache.CacheModule;
import org.uwindsor.acc.loaddb.LoadDB;
public class Search
{
	
	static CacheModule cacheModule = CacheModule.getInstance();
	// Function 1 : Reading Hash Map
	public static Map<String, HashMap<String, String>> readDB(File data)
	{
		Map<String, HashMap<String, String>> hashMapDB = new HashMap<String, HashMap<String, String>>();
		BufferedReader br = null;
		try
		{
			
			br = new BufferedReader(new FileReader(data));
			String line = null;
			while ((line = br.readLine()) != null)
			{
				String[] sep = line.split( " ");
				String key = sep[0].trim();
				HashMap<String, String> value = new HashMap<String, String>();
				for(int i=1;i<sep.length;i+=2)
				{
					value.put(sep[i], sep[i+1]);
				}
				hashMapDB.put(key, value);
			}
		}
		catch (Exception exp)
		{
	           exp.getMessage();
	    }
	    finally
	    {
	    	if (br != null)
	    	{
                try
                {
                    br.close();
                }
                catch (Exception exp)
                {
                	exp.getMessage();
                };
            }
	    }
		return hashMapDB;
	}
	
	// Function 2 : Searching
	public static HashMap<String, String> searching(String key, Map<String, HashMap<String, String>> hashMapDB, Map<String, String> hashMapURL)
	{
		HashMap<String, String> result = new HashMap<String, String>();
		result = hashMapDB.get(key);
		if( result == null)
			return result;
		if(result!= null && result.size()!=0)
		{
			return result;
		}
		else
		{
			for(HashMap.Entry<String, String> url: hashMapURL.entrySet())
			{
				String txt = url.getValue();
				KMP kmp = new KMP(key);
				int offset = kmp.search(txt);
				if(offset != -1)
				{
					result.put(url.getKey(), "1");
				}
			}
			return result;
		}
	}
	
	public static int indexOfSmallest(int[] array)
	{
	    int index = 0;
	    int min = array[index];

	    for (int i = 1; i < array.length; i++)
	    {
	        if (array[i] <= min)
	        {
	        min = array[i];
	        index = i;
	        }
	    }
	    return index;
	}

	
	// Intersection
	public static HashMap<String, String> findIntersection(List<HashMap<String, String>> arrList)
	{
		// get Iterator for looping through AL
        Iterator<HashMap<String, String>> iterator = arrList.iterator();
        int[] lengthArr = new int[arrList.size()];
        int count = 0;
        // iterate AL using while-loop
        while(iterator.hasNext())
        {
        	HashMap<String, String> element = iterator.next();
        	if( element!=null ) {
        		lengthArr[count] = element.size();
        	
        		count++;
        	}
        }
        // Finding the smallest HashTable
        int indexSmallest = indexOfSmallest(lengthArr);
        HashMap<String, String> smallest = arrList.get(indexSmallest);
        if( smallest!= null ) 
        {
	        arrList.remove(indexSmallest);
        }
	    iterator = arrList.iterator();
	    while(iterator.hasNext())
	    {
	    	HashMap<String, String> element = iterator.next();
	       	for(String key : element.keySet())
	        {
	           	if(smallest.containsKey(key))
	           	{
	           		String newFreq = Integer.toString(Integer.parseInt(smallest.get(key)) + Integer.parseInt(element.get(key)));
	           		smallest.put(key, newFreq);
	           	}
	           	else
	           	{
	           		smallest.remove(key);
	           	}
	        }
	    }
	    return smallest;
        
	}
	
	// Union
	public static HashMap<String, String> findUnion(List<HashMap<String, String>> arrList)
	{
		// get Iterator for looping through AL
        Iterator<HashMap<String, String>> iterator = arrList.iterator();
        int[] lengthArr = new int[arrList.size()];
        int count = 0;
        // iterate AL using while-loop
        while(iterator.hasNext())
        {
        	HashMap<String, String> element = iterator.next();
        	lengthArr[count] = element.size();
        	count++;
        }
        // Finding the smallest HashTable
        int indexSmallest = indexOfSmallest(lengthArr);
        HashMap<String, String> smallest = arrList.get(indexSmallest);
        if(smallest!=null)
        {
	        //removing the smallest from the list
	        arrList.remove(indexSmallest);
        }
        iterator = arrList.iterator();
        while(iterator.hasNext())
        {
        	HashMap<String, String> element = iterator.next();
        	for(String key : element.keySet())
            {
            	if(smallest.containsKey(key))
            	{
            		String newFreq = Integer.toString(Integer.parseInt(smallest.get(key)) + Integer.parseInt(element.get(key)));
            		smallest.put(key, newFreq);
            	}
            	else
            	{
            		smallest.put(key, element.get(key));
            	}
            }
        }
        return smallest;
        
	}
	
	
	
	public static void performSearch(String[] searchTerms) {
		
		String pathDB = System.getProperty("user.dir")+"/writeDB.txt";
		String pathURL = System.getProperty("user.dir")+"/writeURL.txt";
		
		// Reading HashMap
		File dataDB = new File(pathDB);
		File dataURL = new File(pathURL);
		Map<String, HashMap<String, String>> hashTable = readDB(dataDB);
		Map<String, String> dbURL = LoadDB.readDBURLs(dataURL);
		List<HashMap<String, String>> arrList = new ArrayList<HashMap<String, String>>();
		for( String searchTerm: searchTerms ) {
			// Cache Module integration
			if( !cacheModule.isCacheEntryPresent(searchTerm) ) {
				HashMap<String, String> searchResult = searching(searchTerm, hashTable, dbURL);
				arrList.add(searchResult);
				if( cacheModule.isCacheFull())
					cacheModule.removeCacheEntry(cacheModule.getCacheEntryWithLowestHit());
				cacheModule.insertCacheEntry(searchTerm, searchResult);
				cacheModule.updateCount(searchTerm);
			}
			else {
				arrList.add(cacheModule.getCacheEntry(searchTerm));
				cacheModule.updateCount(searchTerm);
			}
		}
		
		// Find Intersection
		HashMap<String, String> result = findIntersection(arrList);
		
		if(result!=null && result.size()==0)
		{
			result = findUnion(arrList);
		}
		HashMap<String, String> finalResult = LoadDB.sortByValueString(result);
	    // Iterate over HashMap entries
		
		if( finalResult== null ) {
			return ;
		}
		for (Map.Entry<String, String> entry : finalResult.entrySet())
        {
        	// Put key and value separated
			System.out.println(entry.getKey());    
        }
	}
	// Main
	public static void main(String[] Args)
	{
		String pathDB = System.getProperty("user.dir")+"/writeDB.txt";
		String pathURL = System.getProperty("user.dir")+"/writeURL.txt";
		
		// Reading HashMap
		File dataDB = new File(pathDB);
		File dataURL = new File(pathURL);
		Map<String, HashMap<String, String>> hashTable = readDB(dataDB);
		Map<String, String> dbURL = LoadDB.readDBURLs(dataURL);
		List<HashMap<String, String>> arrList = new ArrayList<HashMap<String, String>>();
		String[] searchTerms = {"chole", "Bhature"};
		for(String searchTerm: searchTerms)
		{
			HashMap<String, String> searchResult = searching(searchTerm, hashTable, dbURL);
			arrList.add(searchResult);
		}
		
		// Find Intersection
		HashMap<String, String> result = findIntersection(arrList);
		
		if(result.size()==0)
		{
			result = findUnion(arrList);
		}
	    // Iterate over HashMap entries
		
		for (Map.Entry<String, String> entry : result.entrySet())
        {
        	// Put key and value separated by a colon
			System.out.println(entry.getKey() + " " + entry.getValue() + " ");    
        }
	}
}