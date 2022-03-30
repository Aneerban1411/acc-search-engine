package SearchStringProcessing;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* Implements an Alphanumeric Regular expression pattern to remove
* symbols from a given search string
* 
* @author  Prasham Thawani
* @StudentId 110065917
*/
public class RegexFilter {

	static Pattern alphaNumericRegex;	
	static Matcher matcher;
	
	//Constructor
	public RegexFilter()
	{
		alphaNumericRegex = Pattern.compile("[0-9a-zA-Z]+");		
	}
	
	/**
    * Filters a given string via and Alpha-number RegEx pattern.
    * @param searchString Input a search string.
    * @return A string ArrayList of words.
    */
	public ArrayList<String> StringProcessViaRegex(String searchString)
	{
		ArrayList<String> searchStringAfterRegex = new ArrayList<String>();
		String searchStringLC = searchString.toLowerCase();
		
		matcher = alphaNumericRegex.matcher(searchStringLC);
		
		while(matcher.find())
		{
			searchStringAfterRegex.add(matcher.group());
		}
		return searchStringAfterRegex;
	}

}
