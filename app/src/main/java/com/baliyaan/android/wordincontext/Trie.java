package com.baliyaan.android.wordincontext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pulkit Singh on 6/13/2017.
 */

// Trie Node, which stores a character and the children in a HashMap
class TrieNode {
    public TrieNode(char ch)  {
        value = ch;
        children = new HashMap<>();
        bIsEnd = false;
    }
    public HashMap<Character,TrieNode> getChildren() {   return children;  }
    public ArrayList<String> getNchildren(String prefix, int n){
        ArrayList<String> resultWords = new ArrayList<>();
        Map<Character,TrieNode> map= children;
        if(bIsEnd==true) //Leaf node
            resultWords.add(prefix+value);
        if(children.size()>0){ //Traverse children
            for (Map.Entry<Character, TrieNode> entry : map.entrySet()) {
                TrieNode child = entry.getValue();
                ArrayList<String> resultsFromChild = child.getNchildren(prefix+value,n);
                for(String oneResultFromChild : resultsFromChild){
                    if(resultWords.size()<n)
                        resultWords.add(oneResultFromChild);
                    else
                        break;
                }
                if(resultWords.size()==n)
                    break;
            }
        }
        return resultWords;
    }

    public char getValue()                           {   return value;     }
    public void setIsEnd(boolean val)                {   bIsEnd = val;     }
    public boolean isEnd()                           {   return bIsEnd;    }

    private char value;
    private HashMap<Character,TrieNode> children;
    private boolean bIsEnd;
}

// Implements the actual Trie
class Trie {
    // Constructor
    public Trie()   {     root = new TrieNode((char)0);       }

    // Method to insert a new word to Trie
    public void add(String word)  {

        // Find length of the given word
        int length = word.length();
        TrieNode crawl = root;

        // Traverse through all characters of given word
        for( int level = 0; level < length; level++)
        {
            HashMap<Character,TrieNode> child = crawl.getChildren();
            char ch = word.charAt(level);

            // If there is already a child for current character of given word
            if( child.containsKey(ch))
                crawl = child.get(ch);
            else   // Else create a child
            {
                TrieNode temp = new TrieNode(ch);
                child.put( ch, temp );
                crawl = temp;
            }
        }

        // Set bIsEnd true for last character
        crawl.setIsEnd(true);
    }

    // The main method that finds out the longest string 'input'
    public String getMatchingPrefix(String input)  {
        String result = ""; // Initialize resultant string
        int length = input.length();  // Find length of the input string

        // Initialize reference to traverse through Trie
        TrieNode crawl = root;

        // Iterate through all characters of input string 'str' and traverse
        // down the Trie
        int level, prevMatch = 0;
        for( level = 0 ; level < length; level++ )
        {
            // Find current character of str
            char ch = input.charAt(level);

            // HashMap of current Trie node to traverse down
            HashMap<Character,TrieNode> child = crawl.getChildren();

            // See if there is a Trie edge for the current character
            if( child.containsKey(ch) )
            {
                result += ch;          //Update result
                crawl = child.get(ch); //Update crawl to move down in Trie

                // If this is end of a word, then update prevMatch
                if( crawl.isEnd() )
                    prevMatch = level + 1;
            }
            else  break;
        }

        // If the last processed character did not match end of a word,
        // return the previously matching prefix
        if( !crawl.isEnd() )
            return result.substring(0, prevMatch);

        else return result;
    }

    public ArrayList<String> getWordsStartingWith(String input, int n){
        String matchedString = "";
        int length = input.length();  // Find length of the input string

        // Initialize reference to traverse through Trie
        TrieNode crawl = root;

        // Iterate through all characters of input string 'str' and traverse
        // down the Trie
        int level, prevMatch = 0;
        for( level = 0 ; level < length; level++ )
        {
            // Find current character of str
            char ch = input.charAt(level);

            // HashMap of current Trie node to traverse down
            HashMap<Character,TrieNode> child = crawl.getChildren();

            // See if there is a Trie edge for the current character
            if( child.containsKey(ch) )
            {
                matchedString += ch;          //Update result
                crawl = child.get(ch); //Update crawl to move down in Trie
            }
            else  break;
        }

        if(matchedString.length()==0)
            return null;
        return crawl.getNchildren(matchedString.substring(0,matchedString.length()-1),n);
    }

    private TrieNode root;
}