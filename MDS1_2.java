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
public class MDS1_2 {
	public static class Items{
		private int id;
		private int price;
		private Set<Integer> desc;
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
		
		public Items(int id, int price)
		{
			this.id = id;
			this.price = price;
			desc = new HashSet<>();
			
		}
		public boolean addDescription(int d)
		{
			return desc.add(d);
		}
		public boolean removeDescription(int d)
		{
			return desc.remove(d);
		}
		public Set<Integer> getDescription()
		{
			return desc;
		}
		public void clearDescription()
		{
			desc.clear();
		}
		public void setPrice(int p)
		{
			price = p;
		}
		public int getPrice()
		{
			return price;
		}
		
		public String toString()
		{
			return "ID: " + id + ", Price : " + price + ", Descirption: " + desc.toString();
		}
	}
    // Add fields of MDS here
	HashMap<Integer, Items> data; // store id and items
	//		Description		Price				ID	 
	HashMap<Integer, TreeMap<Integer, HashSet<Integer>>> descrItems;	// store description and items

	// Constructors
    public MDS1_2() { 
    	data = new HashMap<>();
    	descrItems = new HashMap<>();
    }

    /* Public methods of MDS. Do not change their signatures.
       __________________________________________________________________
       a. Insert(id,price,list): insert a new item whose description is given
       in the list.  If an entry with the same id already exists, then its
       description and price are replaced by the new values, unless list
       is null or empty, in which case, just the price is updated. 
       Returns 1 if the item is new, and 0 otherwise.
    */
    public int insert(int id, int price, List<Integer> list) {
    	Items i = data.get(id);
    	if(i != null)
    	{
    		int OriginalPrice =i.getPrice();
    		Set<Integer> desc = i.getDescription();
    		if (list.isEmpty())
    		{// only chage price
    			i.setPrice(price);
    			for(int des: desc)
    			{
    				HashSet<Integer> mapSamePrice = descrItems.get(des).get(OriginalPrice);
    				mapSamePrice.remove(id);
    				if(mapSamePrice.isEmpty())
    					descrItems.get(des).remove(OriginalPrice);
    				
    				mapSamePrice = descrItems.get(des).get(price);
    				if(mapSamePrice == null)
    				{
    					mapSamePrice = new HashSet<>();
    					mapSamePrice.add(id);
    					descrItems.get(des).put(price, mapSamePrice);
    				}
    				else
    					mapSamePrice.add(id);
    			}
    		}
    		else
    		{//change price and list
    			delete(id);
    			insert(id, price, list);
    			
    		}
    		return 0;
    	}
    	else
    	{
    		i = new Items(id, price); // create the item
    		data.put(id, i); // put the item into the main data base
    		for(Integer d: list) // check and add the item to the description map
    		{
    			TreeMap<Integer, HashSet<Integer>> ent = descrItems.get(d);
    			if(i.addDescription(d))
    			{
    				HashSet<Integer> IDKey;
        			if(ent == null)	// if no items that has the same price
        			{	// First time see this description
        				ent = new TreeMap<>(); // create treeMap for it key = price, value = items with
        				IDKey = new HashSet<>();// map with ID, Items but new
        				IDKey.add(id); 	// put the item into the ID map
        				ent.put(price, IDKey);
        			}
        			else // description exist
        			{
        				IDKey = ent.get(price); // go to the hash map that has the same price
        				if(IDKey == null) // with this decri. but not the same price
        				{ // create a new hash map that ID is price, value is Items
        					IDKey = new HashSet<>();
        				}
        				IDKey.add(id); 	// put the item into the ID map
        				ent.put(price, IDKey); // put the ID map into the tree Map
        			}
        			descrItems.put(d, ent); // put key(description) and value(TreeMap) into the Hash Map
    			}
    		}
    		
    		return 1;
    	}

    }

    // b. Find(id): return price of item with given id (or 0, if not found).
    public int find(int id) {
    Items it = data.get(id);
    if(it == null)
    	return 0;
    else
    	return it.getPrice();
    }

    /* 
       c. Delete(id): delete item from storage.  Returns the sum of the
       ints that are in the description of the item deleted,
       or 0, if such an id did not exist.
    */
    public int delete(int id) {
    	Items it = data.get(id);
        if(it == null)
        	return 0;
        else
        {
        	int sum = 0;
        	Set<Integer> desc = it.getDescription();
        	for(int des: desc)
			{
        		//descrItems.get(des).get(it.getPrice()).remove(id);
        		TreeMap<Integer, HashSet<Integer>> priceItem = descrItems.get(des);
        		HashSet<Integer> idItems = priceItem.get(it.getPrice());
        		idItems.remove(id);
        		if(idItems.isEmpty())
        			priceItem.remove(it.getPrice());
        		//System.out.println("Size after remove" + idItems.size());
        		sum += des;
			}
        	data.remove(id);
        	return sum;
        }
    }

    /* 
       d. FindMinPrice(n): given an integer, find items whose description
       contains that number (exact match with one of the ints in the
       item's description), and return lowest price of those items.
       Return 0 if there is no such item.
    */
    public int findMinPrice(int n) {
    TreeMap<Integer, HashSet<Integer>> priceMap = descrItems.get(n);
    if(priceMap != null)
    {
    	Entry<Integer, HashSet<Integer>> ent = priceMap.firstEntry();
    	if(ent != null)
    		return ent.getKey();
    }
	return 0;
    }

    /* 
       e. FindMaxPrice(n): given an integer, find items whose description
       contains that number, and return highest price of those items.
       Return 0 if there is no such item.
    */
    public int findMaxPrice(int n) {
    	TreeMap<Integer, HashSet<Integer>> priceMap = descrItems.get(n);
        if(priceMap != null)
        {
        	Entry<Integer, HashSet<Integer>> ent = priceMap.lastEntry();
        	if(ent != null)
        		return ent.getKey();
        }
    	return 0;
    }

    /* 
       f. FindPriceRange(n,low,high): given int n, find the number
       of items whose description contains n, and in addition,
       their prices fall within the given range, [low, high].
    */
    public int findPriceRange(int n, int low, int high) {
    	TreeMap<Integer, HashSet<Integer>> priceMap = descrItems.get(n);
        if(priceMap != null)
        {
        	Map<Integer, HashSet<Integer>> descSubMap = priceMap.subMap(low, true, high, true);
        	int items = 0;
        	for(Entry<Integer, HashSet<Integer>> idMap : descSubMap.entrySet())
        	{
        		items += idMap.getValue().size();
        	}
        	return items;
        }
    	return 0;
    }

    /*
      g. RemoveNames(id, list): Remove elements of list from the description of id.
      It is possible that some of the items in the list are not in the
      id's description.  Return the sum of the numbers that are actually
      deleted from the description of id.  Return 0 if there is no such id.
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
			if(description.remove(x))
			{
				descrItems.get(x).get(it.getPrice()).remove(id);
				if(descrItems.get(x).get(it.getPrice()).isEmpty())
					descrItems.get(x).remove(it.getPrice());
				sum += x;
			}
		}
		return sum;
	}
    }
    
    public void printData()
    {
    	System.out.println("Print by data");
    	for(Entry<Integer, Items> ent : data.entrySet())
    	{
    		System.out.println(ent.getValue());
    	}/*
    	System.out.println("Print by Description");
    	for(Entry<Integer, TreeMap<Integer, HashMap<Integer, Items>>> ent : descrItems.entrySet())
    	{
    		for(Entry<Integer, HashMap<Integer, Items>> ent2: ent.getValue().entrySet())
    		{
    			for(Entry<Integer, Items> ent3: ent2.getValue().entrySet())
    			{
    				System.out.println(ent3.getValue());
    			}
    		}
    	}*/
    }
    public static void main(String [] args)
	{
    	LinkedList<Integer> des = new LinkedList<>();
    	Random rand = new Random();
    	for(int i = 0; i < 3; i++)
    	{
    		des.add(rand.nextInt(20));
    	}
    	HashMap<Integer, Items> map = new HashMap<>();
    	Items i = new Items(101,50, des);
    	map.put(101, i);
    	System.out.println(i);
    	System.out.println(map.get(101));
    	i.setPrice(80);
    	System.out.println(i);
    	System.out.println(map.get(101));
    	/*
    	MDS mds = new MDS();
    	LinkedList<Integer> des = new LinkedList<>();
    	Random rand = new Random();
    	for(int i = 0; i < 3; i++)
    	{
    		des.add(rand.nextInt(20));
    	}
    	int id = 101;
    	int price = 50;
    	mds.insert(id,price, des);
    	//System.out.println(des.toString());
       	for(id = 102; id < 105; id++)
    	{
    		des.clear();
    		for(int i = 0; i < 3; i++)
        	{
        		des.add(rand.nextInt(20));
        	}
    		price = 50 * rand.nextInt(3);
    		mds.insert(id, price, des);
    	}
       	mds.printData();
       	id = 103;
       	mds.delete(id);
       	mds.printData();*/
	}
}


