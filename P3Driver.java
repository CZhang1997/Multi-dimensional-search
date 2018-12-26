package cxz173430;

import java.io.File;
import java.util.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class P3Driver {
    public static void main(String[] args) throws Exception {
        Scanner in;
        if (args.length > 0 && !args[0].equals("-")) {
            File file = new File(args[0]);
            in = new Scanner(file);
        } else {
            in = new Scanner(System.in);
        }
        in = new Scanner(new File("p3-big-300k.txt"));
     //  in = new Scanner(new File("4.txt"));
       FileWriter file = new FileWriter("output300k2.txt");
       BufferedWriter out = new BufferedWriter(file);
        /*
         
         
         */
//       int num = 0;
	boolean VERBOSE = false;
	if (args.length > 1) { VERBOSE = Boolean.parseBoolean(args[1]); }

        String operation = "";
	int lineno = 0;
	
	MDS3 mds = new MDS3();
	/*
	 msd 14619077
	 MDS1_2 14619077
	 MDS1 14619077
	 
	 
	 */
	
	Timer timer = new Timer();
	int id, result, total = 0, price;
	List<Integer> name = new LinkedList<>();

	whileloop:
        while (in.hasNext()) {
//        num ++;
//        if(num == 42904)
//        	System.out.println("stop");
	    lineno++;
	    result = 0;
	    operation = in.next();
	    if(operation.charAt(0) == '#') {
		in.nextLine();
		continue;
	    }
	    switch (operation) {
	    case "End":
		break whileloop;
	    case "Insert":
		id = in.nextInt();
		price = in.nextInt();
		name.clear();
		while(true) {
		    int val = in.nextInt();
		    if(val == 0) { break; }
		    else { name.add(val); }
		}
		result = mds.insert(id, price, name);
		out.write("Insert return: "+ result);
		out.newLine();
		break;
	    case "Find":
		id = in.nextInt();
		result = mds.find(id);
		out.write("Find return: "+ result);
		out.newLine();
		break;
	    case "Delete":
		id = in.nextInt();
		result = mds.delete(id);
		out.write("Delete return: "+ result);
		out.newLine();
		break;
	    case "FindMinPrice":
		result = mds.findMinPrice(in.nextInt());
		out.write("findMinPrice return: "+ result);
		out.newLine();
		break;
	    case "FindMaxPrice":
		result = mds.findMaxPrice(in.nextInt());
		out.write("findMaxPrice return: "+ result);
		out.newLine();
		break;
	    case "FindPriceRange":
		result = mds.findPriceRange(in.nextInt(), in.nextInt(), in.nextInt());
		out.write("FindPriceRange return: "+ result);
		out.newLine();
		break;
	    case "RemoveNames":
		id = in.nextInt();
		name.clear();
		while(true) {
		    int val = in.nextInt();
		    if(val == 0) { break; }
		    else { name.add(val); }
		}
		result = mds.removeNames(id, name);
		out.write("RemoveNames return: "+ result);
		out.newLine();
		break;
	    default:
		System.out.println("Unknown operation: " + operation);
	    }
	    total += result;
	    if(VERBOSE) { System.out.println(lineno + "\t" + operation + "\t" + result + "\t" + total); }
	}
	out.write("" + total);
	out.close();
	System.out.println(total);
	System.out.println(timer.end());
    }

    public static class Timer {
	long startTime, endTime, elapsedTime, memAvailable, memUsed;

	public Timer() {
	    startTime = System.currentTimeMillis();
	}

	public void start() {
	    startTime = System.currentTimeMillis();
	}

	public Timer end() {
	    endTime = System.currentTimeMillis();
	    elapsedTime = endTime-startTime;
	    memAvailable = Runtime.getRuntime().totalMemory();
	    memUsed = memAvailable - Runtime.getRuntime().freeMemory();
	    return this;
	}

	public String toString() {
	    return "Time: " + elapsedTime + " msec.\n" + "Memory: " + (memUsed/1048576) + " MB / " + (memAvailable/1048576) + " MB.";
	}
    }
}