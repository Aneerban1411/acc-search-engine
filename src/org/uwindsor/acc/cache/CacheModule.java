package org.uwindsor.acc.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author rohanaswani
 * 
 * Singleton class as Cache can only have one instance
 *
 */
public class CacheModule {

	private static CacheModule cacheModule = null;
	private static Map<String, List<Integer>> keyToPages = null;
	private static Map<String, Integer> hitCount = null;
	
	/**
	 * Gives the maximum size of the cache
	 */
	private int MAX_SIZE = 20;
	private CacheModule() {
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static CacheModule getInstance() {
		
		if( cacheModule == null ) {
			cacheModule = new CacheModule();
			keyToPages = new HashMap<>();
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
		
		Set<String> keys = keyToPages.keySet();
		
		for( String keySing: keys){
			if( key.equals(keySing))
				return true;
		}
		return false;
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
	public List<Integer> getCacheEntry(String key){
		
		if(!isCacheEntryPresent(key)) {
			return new ArrayList<>();
		}
		
		hitCount.put(key, hitCount.get(key)+1);
		return keyToPages.get(key);	
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
		keyToPages.remove(key);
		hitCount.remove(key);
		return;
	}
	
	/**
	 * returns if the cache is full or not
	 * @return
	 */
	public boolean isCacheFull() {
		
		if(keyToPages.size()<MAX_SIZE) {
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
	public void insertCacheEntry(String key, List<Integer> pageIds) {
		boolean isCacheFull = isCacheFull();
		
		if(isCacheFull) {
			String keyToRemove = getCacheEntryWithLowestHit();
			removeCacheEntry(keyToRemove);
		}
		
		keyToPages.put(key, pageIds);
		hitCount.put(key, 0);
	}
}
