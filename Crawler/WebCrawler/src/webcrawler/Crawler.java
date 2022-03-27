package webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.Jsoup;   
import org.jsoup.nodes.Document;   
import org.jsoup.nodes.Element;   
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap; 

public class Crawler {

	private static HashMap<String, PageData> urlLinks = new HashMap<String, PageData>();
	private static final int maximum_depth = 3;
	
	
	public static void crawl(String url, int depth)
	{
		if(urlLinks.size() < 150 && !urlLinks.containsKey(url) && depth < maximum_depth)
		{
			try {
				Document page = Jsoup.connect(url).get();
				
				urlLinks.put(url, new PageData(depth, page.title(), page.text()));
				Elements linksOnUrl = page.select("a[href]");
				depth++;
				System.out.println("Crawling url: "+url);
				for(Element link : linksOnUrl)
				{
					
					crawl(link.attr("abs:href"), depth);
				}
				
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		crawl("https://www.javatpoint.com/digital-electronics", 0);
		/*
		 * for(String key : urlLinks.keySet()) {
		 * System.out.println("Depth:"+urlLinks.get(key).depth
		 * +" Title: "+urlLinks.get(key).title); }
		 */
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
