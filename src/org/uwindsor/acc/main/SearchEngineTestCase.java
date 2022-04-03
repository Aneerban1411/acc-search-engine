package org.uwindsor.acc.main;

import static org.junit.Assert.*;

import org.uwindsor.acc.cache.CacheModule;
import org.uwindsor.acc.cache.CacheModuleLDE;
import org.uwindsor.acc.search.Search;
import org.uwindsor.acc.search.SearchCacheLDE;
import org.uwindsor.acc.spelllcheck.SpellCheck;

import org.junit.Test;

public class SearchEngineTestCase {

	@Test
	public void testSpellCheck() {

		SpellCheck.loadDictionary();
		
		String searchTerm = "prepar";
		long startTime = System.currentTimeMillis();
		String suggestionWord = SpellCheck.getSimilarWord(searchTerm);
		long endTime = System.currentTimeMillis();
		
		System.out.println("SpellCheck took :"+(endTime-startTime)+" nanoseconds");
		assertTrue( suggestionWord.equalsIgnoreCase("prepare"));
	
	}
	
	
	@Test
	public void cacheHashMapSearch() {
		
		long startTime = System.currentTimeMillis();
		Search.performSearch(new String[] {"prepare"});
		long endTime = System.currentTimeMillis();
		CacheModule cacheModule = CacheModule.getInstance();
		
		long startTime1 = System.currentTimeMillis();
		Search.performSearch(new String[] {"prepare"});
		long endTime1 = System.currentTimeMillis();
		
		System.out.println("Search without cache took :"+(endTime-startTime)+" nanoseconds");
		System.out.println("HashMap cache took :"+(endTime1-startTime1)+" nanoseconds");
	}
	
	
	@Test
	public void cacheLDESearch() {
		
		long startTime = System.currentTimeMillis();
		SearchCacheLDE.performSearch(new String[] {"prepare"});
		long endTime = System.currentTimeMillis();
		
		CacheModuleLDE cacheModule = CacheModuleLDE.getInstance();
		
		long startTime1 = System.currentTimeMillis();
		SearchCacheLDE.performSearch(new String[] {"prepare"});
		long endTime1 = System.currentTimeMillis();
		
		System.out.println("Search without cache took :"+(endTime-startTime)+" nanoseconds");
		System.out.println("Array cache took :"+(endTime1-startTime1)+" nanoseconds");
		
	}

}
