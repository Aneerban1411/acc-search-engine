package webcrawler;

public class PageData{
	int depth;
	//String url;
	String title;
	String content;
	public PageData()
	{
		depth = Integer.MIN_VALUE;
		title = "-";
		content = "-";
	}
	public PageData(int d, String t, String c)
	{
		depth = d;
		title = t;
		content = c;
	}
}
