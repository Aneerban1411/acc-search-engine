package SearchStringProcessing;

import java.util.ArrayList;

import webcrawler.InvalidInputException;

/**
* This code is used to implement the RegexFilter and StopWords classes.
* It return the output of a sample search input string
*
* @author  Prasham Thawani
* @StudentId 110065917
*/
public class StringProcessing {

	public static void main(String[] args) throws InvalidInputException {	
		
		String searchString = "This@is-a (precarious) situation!";	
		
		RegexFilter rFilter = new RegexFilter();
		StopWords sw = new StopWords();
		
		ArrayList<String> searchStringAfterRegex = rFilter.StringProcessViaRegex(searchString);
		System.out.println("Original string: \""+searchString+"\"");
		System.out.println("String after Regex: "+searchStringAfterRegex.toString());
		
		ArrayList<String> finalSearchString = sw.RemoveStopWords(searchStringAfterRegex);
		
		System.out.println("List after removing Stop Words: "+finalSearchString.toString());
		System.out.println("String after removing Stop Words: \""+sw.RemoveStopWordsString(searchStringAfterRegex)+"\"");
		
	}

}
