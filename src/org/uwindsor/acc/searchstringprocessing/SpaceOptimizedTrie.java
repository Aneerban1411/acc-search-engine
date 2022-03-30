package org.uwindsor.acc.searchstringprocessing;

import java.util.HashMap;
import java.util.Map;

/**
* Implements a d-way Trie with HashMap instead of the regular
* pointers in a d-way trie node.
* This is done to save the space occupied by possible empty pointers
* to save the space over-head, which is high for d-way Tries 
* 
* @author  Prasham Thawani
* @StudentId 110065917
*/
public class SpaceOptimizedTrie {
	private boolean isLeaf;
    private Map<Character, SpaceOptimizedTrie> children;
 
    // Constructor
    public SpaceOptimizedTrie()
    {
        isLeaf = false;
        children = new HashMap<>();
    }
 
    /**
    * Inserts string keys into a d-way Trie
    * @param key A string key to be inserted
    */
    public void insert(String key)
    {
        //root node
        SpaceOptimizedTrie currentNode = this;
 
        //iterate over all keys in a node
        for (char c: key.toCharArray())
        {
            //create a new node if it doesn't exist
        	currentNode.children.putIfAbsent(c, new SpaceOptimizedTrie()); 
            //move to the next node
        	currentNode = currentNode.children.get(c);
        } 
        //mark currentNode as a leaf
        currentNode.isLeaf = true;
    }
 
    /**
     * Search for a key in the d-way Trie
     * @param key A string key to be searched
     * @return boolean True if key is found; false, if not
     */
    public boolean search(String key)
    {
        SpaceOptimizedTrie currentNode = this;
 
        //iterate over all keys in a node
        for (char c: key.toCharArray())
        {
            //move to the next node
        	currentNode = currentNode.children.get(c); 
            //end of path reached
            if (currentNode == null)
                return false;
        } 
        //end of path, key is found
        return currentNode.isLeaf;
    }
}
