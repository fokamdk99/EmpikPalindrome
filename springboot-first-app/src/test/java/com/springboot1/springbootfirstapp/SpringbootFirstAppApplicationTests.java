package com.springboot1.springbootfirstapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

class SpringbootFirstAppApplicationTests {

	@Test
	public void findPalindromeReturnsTrue()
	{
		Palindrome palindrome = new Palindrome("AnnA");
		assertTrue(palindrome.findPalindrome());
	}
	
	@Test
	public void findPalindromeReturnsFalse()
	{
		Palindrome palindrome = new Palindrome("notPalindrome");
		assertFalse(palindrome.findPalindrome());
	}

}
