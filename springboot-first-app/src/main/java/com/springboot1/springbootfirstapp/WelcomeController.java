package com.springboot1.springbootfirstapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

	@GetMapping("/welcome")
	public String welcome(@RequestParam(value = "word",
	defaultValue = "empty") String word)
	{
		Palindrome palindrome = new Palindrome(word);
		boolean isPalindrome = palindrome.findPalindrome();
		if(isPalindrome)
		{
			return String.format("%s is a palindrome", word);
		}
		return String.format("%s is not a palindrome", word);
	}
}
