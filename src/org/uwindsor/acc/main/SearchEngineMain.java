package org.uwindsor.acc.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;


import org.uwindsor.acc.loaddb.LoadDB;
import org.uwindsor.acc.search.Search;
import org.uwindsor.acc.searchstringprocessing.RegexFilter;
import org.uwindsor.acc.searchstringprocessing.StopWords;
import org.uwindsor.acc.spelllcheck.SpellCheck;
import org.uwindsor.acc.webcrawler.Crawler;
import org.uwindsor.acc.webcrawler.InvalidInputException;
import org.uwindsor.acc.webcrawler.PageData;

public class SearchEngineMain {
	
	private static HashMap<String, PageData> urlLinks = new HashMap<String, PageData>();
	private static final int maximum_depth = 10;

	public static void main(String[] args) throws IOException, InvalidInputException {
		
		while(true) {
		Scanner in = new Scanner(System.in);
		System.out.println("****************************************");
		System.out.println("1. Crawler");
		System.out.println("2. Load/Create DB");
		System.out.println("3. Perform Search");
		System.out.println("Enter Menu Item:");
		Integer menu = Integer.parseInt( in.nextLine() );
		System.out.println("****************************************");
		
		switch( menu ) {
			
		case 1:
			String urlToCrawl = in.nextLine();
			Crawler.crawl(urlToCrawl, 0, urlLinks);
			//crawl("https://www.javatpoint.com/digital-electronics", 0);
			/*
			 * for(String key : urlLinks.keySet()) {
			 * System.out.println("Depth:"+urlLinks.get(key).depth
			 * +" Title: "+urlLinks.get(key).title); }
			 */
			
			//Write urls to a csv file
			try (PrintWriter writer = new PrintWriter("test.csv")) {

			      StringBuilder sb = new StringBuilder();
			      sb.append("URL");
			      sb.append(',');
			      sb.append("Title");
			      sb.append('\n');

			      for(String key : urlLinks.keySet())
					{
						sb.append(key);
						sb.append(',');
						sb.append(urlLinks.get(key).title);
						sb.append('\n');		    	  
					}

			      writer.write(sb.toString());

			      System.out.println("Output in CSV file.");

			    } catch (FileNotFoundException e) {
			      System.out.println(e.getMessage());
			    }
			System.out.println();
			break;
			
		case 2:
			List<List<String>> records = new ArrayList<>();
			try (BufferedReader br = new BufferedReader(new FileReader("test.csv"))) {
			    String line;
			    while ((line = br.readLine()) != null) {
			        String[] values = line.split(",");
			        records.add(Arrays.asList(values));
			    }
			}
			String[] urls = new String[records.size()];
			Integer i =0;
			for( List<String> lstOfString : records) {
				urls[i++] = lstOfString.get(0);
			}
			try
			{
				LoadDB.loadDataBase(urls);
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			System.out.println("do nothing");
			break;
		case 3:
			String searchString = in.nextLine();
			RegexFilter rFilter = new RegexFilter();
			StopWords sw = new StopWords();
			
			ArrayList<String> searchStringAfterRegex = rFilter.StringProcessViaRegex(searchString);
			System.out.println("Original string: \""+searchString+"\"");
			System.out.println("String after Regex: "+searchStringAfterRegex.toString());
			
			ArrayList<String> finalSearchString = sw.RemoveStopWords(searchStringAfterRegex);
			
			System.out.println("List after removing Stop Words: "+finalSearchString.toString());
			System.out.println("String after removing Stop Words: \""+sw.RemoveStopWordsString(searchStringAfterRegex)+"\"");

			String[] parseStrings= new String[finalSearchString.size()];
			Map<String,String> updatedWords = new HashMap<>();
			for(int j=0;j<finalSearchString.size();j++) {

				SpellCheck.loadDictionary();
				
				String suggestedWord = SpellCheck.getSimilarWord(finalSearchString.get(j));
				
				if(!suggestedWord.equalsIgnoreCase(finalSearchString.get(j))) {
					updatedWords.put(finalSearchString.get(j), suggestedWord);
					
				}
				parseStrings[j] = suggestedWord;
			}
			Search.performSearch(parseStrings);
			
			
			for(Entry<String,String> entry:updatedWords.entrySet()) {
				System.out.println("Did you mean ?");
				System.out.println(entry.getKey()+" - "+entry.getValue());
			}
			break;
		}
		
		
	}
	}
}
