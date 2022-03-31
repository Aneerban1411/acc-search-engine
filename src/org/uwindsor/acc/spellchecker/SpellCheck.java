package acc_porj;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SpellCheck {
	
	private static ArrayList dictWords;
	static HashMap<Integer, String> map = new HashMap<>();

	
	public SpellCheck()
	{
		dictWords = new ArrayList();
	}
	
	public static void loadDictionary()
	{
		try
		{
			File files = new File("C:/Users/mnkgr/eclipse-workspace/Acc_project/src/words2.txt");
			Scanner myReader = new Scanner(files);
			
			while (myReader.hasNextLine())
			{
				String data = myReader.nextLine();
				//System.out.println(data);
				//dictWords.add(data);
				map.put(count,data);
				count++;
			}
			myReader.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public static String getSimilarWord(String inputWord)
	{
		int holder, smallestWord = -1, smallestDistance = 100;
		//System.out.println(dictWords.size());
		for(int i=0;i<map.size();i++)
		{
			//System.out.println(dictWords.get(i).toString());
			holder = editDistance(inputWord,map.get(i));
			
			if(holder < smallestDistance)
			{
				smallestDistance = holder;
				smallestWord = i;
			}
		}
		
		if(smallestDistance == 0)
		{
			return "The word is spelled Correctly";
		}
		else
		{
			return map.get(smallestWord).toString();
		}
	}
	public static int editDistance(String word1, String word2)
	{
		int x = word1.length();
		int y = word2.length();
		
		int dp[][] = new int[x+1][y+1];
		
		for(int i=0;i<=x;i++)
		{
			for(int j=0;j<=y;j++)
			{
				if(i == 0)
				{
					dp[i][j] = j;
				}
				
				else if(j == 0)
				{
					dp[i][j] = i;
				}
				
				else if(word1.charAt(i-1) == word2.charAt(j-1))
				{
					dp[i][j] = dp[i-1][j-1];
				}
				
				else
				{
					dp[i][j] = 1 + minimum(dp[i][j-1], dp[i-1][j], dp[i-1][j-1]);
				}
			}
		}
		return dp[x][y];
	}
	
	public static int minimum(int insert, int remove, int replace)
	{
		if(insert <= remove && insert <= replace)
		{
			return insert;
		}
		
		if(remove <= insert && remove<= replace)
		{
			return remove;
		}
		else
		{
			return replace;
		}
	}
	
	public static void main(String args[]) throws IOException
	{
//		String word1 = "man";
//		String word2 = "van";
//		
//		System.out.println(editDistance(word1,word2,word1.length(),word2.length()));
		SpellCheck chk = new SpellCheck();

		loadDictionary();
		String suggestionWord;
		
		Scanner userInput = new Scanner(System.in);
		System.out.println("Enter the search term");
		
		String searchTerm = userInput.nextLine().toLowerCase();
		
		suggestionWord = chk.getSimilarWord(searchTerm);
		
		if(suggestionWord.equals("The word is spelled Correctly"))
		{
			System.out.println("The word is correctly spelled");
		}
		else
		{
			System.out.println("Did you mean "+suggestionWord+"?");
		}
		
		
	//	loadDictionary();
	}
	
}
