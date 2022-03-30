package org.uwindsor.acc.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;   
import org.jsoup.nodes.Element;   
import org.jsoup.select.Elements;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap; 

/**
* This code is used to implement the Web Crawler using JSoup
*
* @author  Prasham Thawani
* @StudentId 110065917
*/
public class Crawler {

	private static HashMap<String, PageData> urlLinks = new HashMap<String, PageData>();
	private static final int maximum_depth = 3;
	
	/**
    * Inserts string URL and the depth until which Crawler will crawl
    * @param url string URL
    * @param depth the depth until which crawler will crawl
    */
	public static void crawl(String url, int depth)
	{
		//Max number of URLs to crawl and condition to limit depth
		if(urlLinks.size() < 200 && !urlLinks.containsKey(url) && depth < maximum_depth)
		{
			try 
			{
				//get document using Jsoup
				Document page = Jsoup.connect(url).ignoreContentType(true).get();
				
				//add url and page text into the List
				urlLinks.put(url, new PageData(depth, page.title(), page.text()));
				//Get links on present url page
				Elements linksOnUrl = page.select("a[href]");
				depth++;
				System.out.println("Crawling url: "+url);
				//Call crawl recursively
				for(Element link : linksOnUrl)
					crawl(link.attr("abs:href"), depth);				
			} 
			catch (Exception e) {
				//e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		
		crawl("https://regex101.com/", 0);
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
	}

}
