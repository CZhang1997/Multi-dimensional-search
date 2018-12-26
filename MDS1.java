package cxz173430;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;
public class MDS1 {
	/**
	 * Items class create a object with a id, price and an set of description
	 * @author Churong Zhang
	 * @version 1.0
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
	//		Description		Price				ID	 Items has same price
	HashMap<Integer, TreeMap<Integer, HashMap<Integer, Items>>> descrItems;	// store description and items

	// Constructors
	/**
	 * Constructor
	 */
    public MDS1() { 
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
    		if (list.isEmpty())		// check if the list is empty
    		{// only chage price	// if it is empty
    			i.setPrice(price);	// then only change price
    			for(int des: desc)
    			{	// and change the priceMap in descrItems
    				// remove the item that has the old price
    				HashMap<Integer, Items> mapSamePrice = descrItems.get(des).get(OriginalPrice);
    				Items it = mapSamePrice.remove(id);
    				if(mapSamePrice.isEmpty())	// if the map is empty, then remove the map
    					descrItems.get(des).remove(OriginalPrice);
    				it.setPrice(price);
    				///////////////////////////////////////////////////////////////////////
    				// go to the map that represent the new price 
    				// and add the item to that map
    				mapSamePrice = descrItems.get(des).get(price);
    				if(mapSamePrice == null)
    				{
    					mapSamePrice = new HashMap<>();
    					mapSamePrice.put(id, i);
    					descrItems.get(des).put(price, mapSamePrice);
    				}
    				else
    					mapSamePrice.put(id, i);
    			}
    		}
    		else
    		{//change price and list
    			delete(id);	// delete the old item
    			insert(id, price, list);	// add it back as a new item
    		}
    		return 0;
    	}
    	else
    	{
    		i = new Items(id, price); // create the item
    		data.put(id, i); // put the item into the main data base
    		for(Integer d: list) // check and add the item to the description map
    		{
    			TreeMap<Integer, HashMap<Integer, Items>> ent = descrItems.get(d);
    			i.addDescription(d);
    			HashMap<Integer, Items> IDKey;
    			if(ent == null)	// if no items that has the same price
    			{	// First time see this description
    				ent = new TreeMap<>(); // create treeMap for it key = price, value = items with
    				IDKey = new HashMap<>();// map with ID, Items but new
    				IDKey.put(id, i); 	// put the item into the ID map
    				ent.put(price, IDKey);
    			}
    			else // description exist
    			{
    				IDKey = ent.get(price); // go to the hash map that has the same price
    				if(IDKey == null) // with this decri. but not the same price
    				{ // create a new hash map that ID is price, value is Items
    					IDKey = new HashMap<>();
    				}
    				IDKey.put(id, i); 	// put the item into the ID map
    				ent.put(price, IDKey); // put the ID map into the tree Map
    			}
    			descrItems.put(d, ent); // put key(description) and value(TreeMap) into the Hash Map
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
    if(it == null)
    	return 0;
    else
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
			{						// and delete the items from the price map
        		TreeMap<Integer, HashMap<Integer, Items>> priceItem = descrItems.get(des);
        		HashMap<Integer, Items> idItems = priceItem.get(it.getPrice());
        			idItems.remove(id);
        		if(idItems.size() == 0)
        			priceItem.remove(it.getPrice());
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
    TreeMap<Integer, HashMap<Integer, Items>> priceMap = descrItems.get(n);
    if(priceMap != null)
    {
    	Entry<Integer, HashMap<Integer, Items>> ent = priceMap.firstEntry();
    	if(ent != null)
    		return ent.getKey();
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
    	TreeMap<Integer, HashMap<Integer, Items>> priceMap = descrItems.get(n);
        if(priceMap != null)
        {	// find the max price
        	Entry<Integer, HashMap<Integer, Items>> ent = priceMap.lastEntry();
        	if(ent != null)
        		return ent.getKey();
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
    	TreeMap<Integer, HashMap<Integer, Items>> priceMap = descrItems.get(n);
        if(priceMap != null)
        {	// get a map that contain from low end to high end of the price, inclusive
        	Map<Integer, HashMap<Integer, Items>> descSubMap = priceMap.subMap(low, true, high, true);
        	int items = 0;
        	for(Entry<Integer, HashMap<Integer, Items>> idMap : descSubMap.entrySet())
        	{	// add the numbers of items together
        		items += idMap.getValue().size();
        	}
        	return items;
        }
    	return 0;
    }

    /*
      
    */
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
		for(int x: list)
		{
			if(description.remove(x)) // change the description map if remove x is true
			{
				descrItems.get(x).get(it.getPrice()).remove(id);	// delete the item from the description map
				if(descrItems.get(x).get(it.getPrice()).isEmpty())	// remove the price map if the map is empty
					descrItems.get(x).remove(it.getPrice());
				sum += x;
			}
		}
		return sum;
	}
    }
 
}

