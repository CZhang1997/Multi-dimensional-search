package cxz173430;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.TreeMap;
/**
 * @author 		Churong Zhang 
 * 				cxz173430
 * 				November 11 2018
 * 				Dr. Raghavachari
 * 				This class is for Project 3
 * 				Multi-dimensional search
 * 		Multi-dimensional search: Consider the web site of a seller like Amazon.  
	They carry tens of thousands of products, and each product has many
	attributes (Name, Size, Description, Keywords, Manufacturer, Price, etc.).  
	The search engine allows users to specify attributes of products that
	they are seeking, and shows products that have most of those
	attributes.  To make search efficient, the data is organized using
	appropriate data structures, such as balanced trees.  But, if products
	are organized by Name, how can search by price implemented efficiently?
	The solution, called indexing in databases, is to create a new set of
	references to the objects for each search field, and organize them to
	implement search operations on that field efficiently.  As the objects
	change, these access structures have to be kept consistent.
 */
public class MDS {
	/**
	 * Items class create a object with a id, price and an set of description
	 * @author Churong Zhang
	 * @version 4.0
	 */
	public static class Items{
		private int id;		// the id of the item
		private int price;	// the price of the item
		private Set<Integer> desc;	// the set of description
		/**
		 * Create an item with id, price and the descriptions
		 * @param id the id of the item
		 * @param price the price of the item
		 * @param list the list of description
		 */
		public Items(int id, int price, List<Integer> list)
		{
			this.id = id;		
			this.price = price;
			desc = new HashSet<>();
			for(Integer i: list)
			{
				desc.add(i);
			}
		}
		/**
		 * Create an item with id, and price
		 * @param id the id of the item
		 * @param price the price of the item
		 */
		public Items(int id, int price)
		{
			this.id = id;
			this.price = price;
			desc = new HashSet<>();
			
		}
		/**
		 * Add a description to the set
		 * @param d the description
		 * @return true if added to the set
		 */
		public boolean addDescription(int d)
		{
			return desc.add(d);
		}
		/**
		 * Remove a description
		 * @param d the description to remove
		 * @return true if removed
		 */
		public boolean removeDescription(int d)
		{
			return desc.remove(d);
		}
		/**
		 * get the set of descriptions
		 * @return the set of descriptions
		 */
		public Set<Integer> getDescription()
		{
			return desc;
		}
		/**
		 * clear the description
		 */
		public void clearDescription()
		{
			desc.clear();
		}
		/**
		 * change the price 
		 * @param p the new price
		 */
		public void setPrice(int p)
		{
			price = p;
		}
		/**
		 * get the price
		 * @return the price
		 */
		public int getPrice()
		{
			return price;
		}
		/**
		 * @return a string version of the object
		 */
		public String toString()
		{
			return "ID: " + id + ", Price : " + price + ", Descirption: " + desc.toString();
		}
	}
    // Add fields of MDS here
	HashMap<Integer, Items> data; // store id and items
	//		Description		Price		number of Items has same price
	HashMap<Integer, TreeMap<Integer, Integer>> descrItems;	// store description, prices and number of items

	/**
	 * Constructor
	 */
    public MDS() { 
    	data = new HashMap<>();
    	descrItems = new HashMap<>();
    
    }


    /**
     * Public methods of MDS. Do not change their signatures.
       __________________________________________________________________
       a. Insert(id,price,list): insert a new item whose description is given
       in the list.  If an entry with the same id already exists, then its
       description and price are replaced by the new values, unless list
       is null or empty, in which case, just the price is updated. 
       Returns 1 if the item is new, and 0 otherwise.
     * @param id the id of the item to insert
     * @param price the price of the item to insert
     * @param list the descriptions of the item to insert
     * @return 1 if the item is new, 0 if the item already exist
     */
    public int insert(int id, int price, List<Integer> list) {
    	Items i = data.get(id);
    	if(i != null)
    	{
    		int OriginalPrice = data.get(id).getPrice(); // get the original price
    		Set<Integer> desc = i.getDescription();		// get the original description
    		i.setPrice(price);	// change price
    		if (list.isEmpty())		// check if the list is empty
    		{// only chage price	// if it is empty
    			for(int des: desc)
    			{	// and change the priceMap in descrItems
    				// remove the item that has the old price
    				Integer size = descrItems.get(des).get(OriginalPrice);
    				if(size == 1)
    					descrItems.get(des).remove(OriginalPrice);
    				else
    					descrItems.get(des).put(OriginalPrice, size - 1);
    				///////////////////////////////////////////////////////////////////////
    				// go to the map that represent the new price 
    				// and add the item to that map
    				size = descrItems.get(des).get(price);
    				if(size == null)
    					descrItems.get(des).put(price, 1);
    				else
    					descrItems.get(des).put(price, size + 1);
    			}
    		}
    		else
    		{//change price and list
    			Set<Integer> description = i.getDescription();
    			Integer size;
    			//////////////////////////////////////
    			//// remove the from the old price list
    			for(int des: description)
    			{
    				size = descrItems.get(des).get(OriginalPrice);
    				if(size == 1) // if is one, then it mean this price only has one item that has this price and description
    					descrItems.get(des).remove(OriginalPrice);
    				else	// else decrease the number of items by one 
    					descrItems.get(des).put(OriginalPrice, size - 1);
    			}
    			description.clear();	// remove the description
    			///////////////////////////////////////////
    			/// add the item to the new price list
    			for(int des : list)
    			{
    				if(description.add(des))
    				{
    					TreeMap<Integer, Integer> ent = descrItems.get(des);
    					if(ent == null)
    					{// if this is a new description 
    						ent = new TreeMap<>();
    						ent.put(price, 1);
    						descrItems.put(des, ent);
    					}
    					else
    					{
    						size = ent.get(price);
            				if(size == null)	// if this is a new price in this description
            					descrItems.get(des).put(price, 1);
            				else
            					descrItems.get(des).put(price, size + 1);
    					}
    				}
    			}
    		}
    		return 0;
    	}
    	else
    	{
    		i = new Items(id, price); // create the item
    		data.put(id, i); // put the item into the main data base
    		for(Integer d: list) // check and add the item to the description map
    		{
    			if(i.addDescription(d))
    			{
    				TreeMap<Integer, Integer> ent = descrItems.get(d);
    				if(ent == null)	// if no items that has the same price
        			{	// First time see this description
        				ent = new TreeMap<>(); // create treeMap for it key = price, value = number of items 
        				ent.put(price, 1);	// first time with this price
        			}
        			else // description exist
        			{
        				Integer size = ent.get(price); // get number of items that has the same price
        				if(size == null) // if this number does not exist
        				{ // then put an one on this price
        					ent.put(price, 1); 
        				}
        				else	// else increase it by one
        					ent.put(price, size + 1);
        			}
        			descrItems.put(d, ent); // put key(description) and value(TreeMap) into the Hash Map
    			}
    		}
    		return 1;
    	}

    }

    /**
     * Find(id): return price of item with given id (or 0, if not found).
     * @param id the id of the item that is looking for
     * @return the price of the item
     */
    public int find(int id) {
    Items it = data.get(id);
    if(it == null) // return 0 if it does not exist
    	return 0;
    else			// else return the price
    	return it.getPrice();
    }

    /**
     * c. Delete(id): delete item from storage.  Returns the sum of the
       ints that are in the description of the item deleted,
       or 0, if such an id did not exist.
     * @param id the id of the object to delete
     * @return the sum of the description
     */
    public int delete(int id) {
    	Items it = data.get(id);
        if(it == null)
        	return 0;
        else
        {
        	int sum = 0;
        	Set<Integer> desc = it.getDescription();
        	for(int des: desc)			// sum the description
			{						// and decrease the number of item that has this price
        		Integer size = descrItems.get(des).get(it.getPrice());
				if(size == 1)	// remove it if the size is one
					descrItems.get(des).remove(it.getPrice());
				else
					descrItems.get(des).put(it.getPrice(), size - 1);
        		sum += des;
			}
        	data.remove(id);
        	return sum;
        }
    }

    /**
     * d. FindMinPrice(n): given an integer, find items whose description
       contains that number (exact match with one of the ints in the
       item's description), and return lowest price of those items.
       Return 0 if there is no such item.
     * @param n the description
     * @return the lowest price on a given description
     */
    public int findMinPrice(int n) {
    TreeMap<Integer, Integer> priceMap = descrItems.get(n);// get this description
    if(priceMap != null)
    {	// if this description exist, then get the first entry
    	Entry<Integer, Integer> ent = priceMap.firstEntry();
    	if(ent != null)
    		return ent.getKey();	// return the price
    }
	return 0;
    }

    /**
     * e. FindMaxPrice(n): given an integer, find items whose description
       contains that number, and return highest price of those items.
       Return 0 if there is no such item.
     * @param n the description
     * @return the highest price on a given description
     */
    public int findMaxPrice(int n) {
    	TreeMap<Integer,Integer> priceMap = descrItems.get(n);// get this description
        if(priceMap != null)
        {	// if this description exist, then get the last entry
        	Entry<Integer, Integer> ent = priceMap.lastEntry();
        	if(ent != null)
        		return ent.getKey(); // return the price
        }
    	return 0;
    }

    /**
     * f. FindPriceRange(n,low,high): given int n, find the number
       of items whose description contains n, and in addition,
       their prices fall within the given range, [low, high].
     * @param n the description
     * @param low low price 
     * @param high the high price
     * @return the number of items that has the description n and within the price range
     */
    public int findPriceRange(int n, int low, int high) {
    	TreeMap<Integer, Integer> priceMap = descrItems.get(n);
        if(priceMap != null)
        {	// get a map that contain from low end to high end of the price, inclusive
        	Map<Integer, Integer> descSubMap = priceMap.subMap(low, true, high, true);
        	int items = 0;
        	for(Entry<Integer, Integer> idMap : descSubMap.entrySet())
        	{	// add the numbers of items together
        		items += idMap.getValue();
        	}
        	return items;
        }
    	return 0;
    }

    /**
     * g. RemoveNames(id, list): Remove elements of list from the description of id.
      It is possible that some of the items in the list are not in the
      id's description.  Return the sum of the numbers that are actually
      deleted from the description of id.  Return 0 if there is no such id.
     * @param id the it of the item to change
     * @param list the list of description to remove
     * @return the sum of the descriptions that remove
     */
    public int removeNames(int id, java.util.List<Integer> list) {
	Items it = data.get(id);
	if(it == null)
		return 0;
	else
	{
		int sum = 0;
		Set<Integer> description = it.getDescription();
		for(int des: list)
		{
			if(description.remove(des)) // change the description map if remove x is true
			{
				Integer size = descrItems.get(des).get(it.getPrice());
				if(size == 1)
					descrItems.get(des).remove(it.getPrice());
				else
					descrItems.get(des).put(it.getPrice(), size - 1);
				sum += des;
			}
		}
		return sum;
	}
    }
 
}

