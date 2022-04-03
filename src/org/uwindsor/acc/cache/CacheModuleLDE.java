package org.uwindsor.acc.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author rohanaswani
 * 
 * Singleton class as Cache can only have one instance
 *
 */
public class CacheModuleLDE {

	private static CacheModuleLDE cacheModule = null;
	private static List<String> keys = null;
	private static List<HashMap<String,String>> pages = null;
	private static HashMap<String, Integer> hitCount = null;
	
	/**
	 * Gives the maximum size of the cache
	 */
	private int MAX_SIZE = 20;
	private CacheModuleLDE() {
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static CacheModuleLDE getInstance() {
		
		if( cacheModule == null ) {
			cacheModule = new CacheModuleLDE();
			keys = new ArrayList<>();
			pages = new ArrayList<>();
			hitCount = new HashMap<>();
		}
		
		return cacheModule;
	}

	/**
	 * checks whether a key is present in cache
	 * @param key
	 * @return
	 */
	public boolean isCacheEntryPresent(String key) {
		
		if(!isValidKey(key)) {
			return false;
		}
		
		for( String keySing: keys){
			if( key.equals(keySing))
				return true;
		}
		return false;
	}
	
	/**
	 * Get Cache Index 
	 * @param key
	 * @return
	 */
	
	public int getCacheIndex(String key) {
		
		int low = 0;
		int high = keys.size();
		
		while(low <= high) {
			
			int mid = low + (high - low)/2;
			
			if( keys.get(mid).equalsIgnoreCase(key) ) {
				return mid;
			}
			else if( keys.get(mid).compareTo(key) < 0 ) {
				low = mid + 1;
			}
			else if( keys.get(mid).compareTo(key) > 0 ) {
				high = mid -1;
			}
		}
		
		return -1;
	}
	
	

	/**
	 * checks whether the key is valid or not
	 * @param key
	 * @return boolean
	 */
	public boolean isValidKey(String key) {
		if(key == null || key.isBlank() || key.isEmpty() ) {
			return false;
		}
		return true;	
	}

	/**
	 * gets the cache entry and increases the count by one
	 * @param key
	 * @return boolean
	 */
	public HashMap<String, String> getCacheEntry(String key){
		
		if(!isCacheEntryPresent(key)) {
			return new HashMap<>();
		}
		
		hitCount.put(key, hitCount.get(key)+1);
		return pages.get(this.getCacheIndex(key));	
	}
	
	/**
	 * removes the cache entry present in the cache
	 * @param key
	 */
	public void removeCacheEntry(String key) {
		if(!isCacheEntryPresent(key)) {
			System.out.println("Tried removing a key not present in the map");
			return;
		}
		keys.remove(key);
		hitCount.remove(key);
		return;
	}
	
	/**
	 * returns if the cache is full or not
	 * @return
	 */
	public boolean isCacheFull() {
		
		if(keys.size()<MAX_SIZE) {
			return false;
		}
		return true;
	}
	
	/**
	 * returns the cache entry for the lowest hit page 
	 */
	public String getCacheEntryWithLowestHit() {
		
		String key = "";
		Integer lowestCount = 0;
		for(Map.Entry<String, Integer> entry : hitCount.entrySet()) {
			
			if(key.equals("")) {
				key = entry.getKey();
				lowestCount = entry.getValue();
			}
			else {
				if( lowestCount > entry.getValue() ) {
					key = entry.getKey();
					lowestCount = entry.getValue();
				}
			}
		}
		return key;
	}
	
	
	/**
	 * inserts an entry into the cache
	 * 
	 * 2 Cases
	 * 
	 * 1. When the cache is full and the entry is supposed to happen
	 * 
	 * 2. When the entry is not present in cache
	 */
	public void insertCacheEntry(String key, HashMap<String,String> pageIds) {
		boolean isCacheFull = isCacheFull();
		
		if(isCacheFull) {
			String keyToRemove = getCacheEntryWithLowestHit();
			removeCacheEntry(keyToRemove);
		}
		
		keys.add(key);
		pages.add(pageIds);
		hitCount.put(key, 0);
	}
	
	
	/**
	 * 
	 */
	public void updateCount(String key) {
		hitCount.put(key, hitCount.get(key)+1);
	}
}
