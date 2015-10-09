import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.Math;

public class Project1 {
	public static void main(String args[]) {

		// Fixed inps are a) The num of nodes b) me
		int numOfNodes = Integer.parseInt(args[0]);
		System.out.println("Num nodes" + numOfNodes);
		int me = Integer.parseInt(args[1]);
		// config file name is the third arg
		String config= args[2];
		System.out.println(config);
		String [] tokens = config.split("\\.");
                String configFileName = tokens[0];


		// Until the delimiter, is the path
		ArrayList<ArrayList<String>> paths_str = new ArrayList<ArrayList<String>>();
		ArrayList<String> path_str = new ArrayList<String>();
		int j = 0;

		// Read in multiple paths into a master arraylist, paths
		int i = 3;
		int flag = 0;
		while (!args[i].contains("dc")) {
			flag = 0;
			while (!args[i].equals("del")) {
				path_str.add((args[i]));
				i++;
				flag = 1;
		
			}
			i++;
		
		}
		// This can be helpful for tracking the termination condition
		int totalPathCount = path_str.size();
		ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();
		if (flag == 1)
			for (i = 0; i < path_str.size(); i++) {
		
				ArrayList<Integer> path = new ArrayList<Integer>();

				for (j = 0; j < path_str.get(i).length(); j++) {

					path.add(Character.getNumericValue(path_str.get(i)
							.charAt(j)));
				}
				// paths should contain only the paths starting with present
				// node.
				if (path.get(0) == me)
					paths.add(path);
			}

				
		
		// Now comes the node host names
		ArrayList<String> hostNames = new ArrayList<String>();

		ArrayList<Integer> portNums = new ArrayList<Integer>();

		for (i = 3; i < args.length; i++) {
			if (args[i].contains("dc")) {

				for (j = 0; j < numOfNodes; j++) {
					hostNames.add(args[i]);
		
					i++;

				}
				for (j = 0; j < numOfNodes; j++) {
					portNums.add(Integer.parseInt(args[i]));
		
					i++;

				}

				break;
			}
		}

		int myToken;
		// Generate a random token
		myToken = ((int) (Math.random() * 50)) + 1;
		System.out.println("Token Val " + myToken);

		PrintWriter writer = null;
		String fileName = configFileName+"-sxp136630-" + me + ".out";

		try {
			writer = new PrintWriter(new FileOutputStream(new File(fileName)));
			// writer.println("het");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		int myPathCount = paths.size();
		//Handle the condition where a certain node does no have any paths
		if(myPathCount==0)
		{
			 String line2 = "Net ID: sxp136630";
                         String line3 = "Node ID:" +me;
                         String line4 = "Listening on " + hostNames.get(me)+ ":" + portNums.get(me);
			 
                         String line5 = "Random number: " + myToken;
			 String line6 = "All tokens received";
			 synchronized(writer)
			 {
				writer.println(line2);
				writer.println(line3);
				writer.println(line4);
				writer.println(line5);
				writer.println(line6);
				writer.close();
				writer=null;
			 }	

		}


		// Create the server thread
		Server T1 = new Server(me, myToken, hostNames, portNums,
				writer, totalPathCount, myPathCount);
		T1.start();
		
		// path[1] gives the node number of the immediate node that the current
		// node should connect with.That can be used
		// as an index into the hostNames and portNums lists

		// Launching one client thread each for the paths present in the
		// ArralyList called paths
		for (i = 0; i < paths.size(); i++) {
			String hostName_next_node = null;
			int port_next_node = 0;
			if (paths.get(i).size() > 1)
				hostName_next_node = hostNames.get(paths.get(i).get(1));
			else
				hostName_next_node = hostNames.get(paths.get(i).get(0));
			if (paths.get(i).size() > 1)
				port_next_node = portNums.get(paths.get(i).get(1));
			else
				port_next_node = portNums.get(paths.get(i).get(0));

			ArrayList<Integer> fullPath = new ArrayList<Integer>(paths.get(i));
			Client c = new Client(myToken, paths.get(i), fullPath,
					hostName_next_node, port_next_node);
			Thread t = new Thread(c);
			t.start();
		}
	}
}

