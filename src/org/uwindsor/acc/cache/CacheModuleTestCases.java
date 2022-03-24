package org.uwindsor.acc.cache;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class CacheModuleTestCases {
	
	/**
	 * 
	 */
	@Test
	public void getInstanceTestCase() {
		
		CacheModule cacheModule = CacheModule.getInstance();
		assertNotNull(cacheModule);
	}
	
	/**
	 * 
	 */
	@Test
	public void isCacheEntryPresentTrueTestCase() {
		CacheModule cacheModule = CacheModule.getInstance();
		cacheModule.insertCacheEntry("adam", Arrays.asList(1,2));
		assertTrue(cacheModule.isCacheEntryPresent("adam"));
		
	}
	
	/**
	 * 
	 */
	@Test
	public void isCacheEntryPresentFalseTestCase() {
		CacheModule cacheModule = CacheModule.getInstance();
		cacheModule.insertCacheEntry("adam", Arrays.asList(1,2));
		assertFalse(cacheModule.isCacheEntryPresent("ray"));
	}
	
	/**
	 * 
	 */
	public void isValidKeyPositiveTestCase() {
		
	}
	
	/**
	 * 
	 */
	public void isValidKeyNegativeTestCase() {
		
	}
	
	/**
	 * 
	 */
	public void getCacheEntryPositiveTestCase() {
		
	}
	
	/**
	 * 
	 */
	public void getCacheEntryNegativeTestCase() {
		
	}
	
	/**
	 * 
	 */
	public void removeCacheEntryTestCase() {
		
	}
	
	/**
	 * 
	 */
	public void isCacheFullTestCase() {
		
	}

}
