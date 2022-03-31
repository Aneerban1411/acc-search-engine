package org.uwindsor.acc.webcrawler;

/**
* This class is the strcture used to save a url link
*
* @author  Prasham Thawani
* @StudentId 110065917
*/
public class PageData{
	int depth;
	public String title;
	String content;
	//Constructor
	public PageData()
	{
		depth = Integer.MIN_VALUE;
		title = "-";
		content = "-";
	}
	//Parameterized constructor
	public PageData(int d, String t, String c)
	{
		depth = d;
		title = t;
		content = c;
	}
}
