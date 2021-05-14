package com.springboot1.springbootfirstapp;

public class Palindrome {
	private final String word;
	private String reverse;
	public Palindrome(String word)
	{
		this.word = word;
		this.reverse = "";
	}
	
	public boolean findPalindrome()
	{
		int length = this.word.length();
		for (int i = length - 1; i >= 0; i--)
		{
			this.reverse = this.reverse + this.word.charAt(i);
		}
		if (this.word.equals(this.reverse))
		{
			return true;
		}
		return false;
	}
}
