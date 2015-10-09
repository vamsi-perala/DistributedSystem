import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

class ClientWorker implements Runnable {
	private Socket client;
	private int me;
	private int myToken;
	private ArrayList<String> hostNames;
	private ArrayList<Integer> portNums;
	private PrintWriter writer;
	private ArrayList<Integer> count;
	private int totalPathCount;
	private int myPathCount;
	private ArrayList<Integer> myPathCounter;

	// COnstructor
	public ClientWorker(int me, int myToken, Socket client,
			ArrayList<String> hostNames, ArrayList<Integer> portNums,
			PrintWriter writer, ArrayList<Integer> count, int totalPathCount,
			int myPathCount, ArrayList<Integer> myPathCounter) {
		this.client = client;
		this.me = me;
		this.myToken = myToken;
		this.hostNames = hostNames;
		this.portNums = portNums;
		this.writer = writer;
		this.count = count;
		this.totalPathCount = totalPathCount;
		this.myPathCount = myPathCount;
		this.myPathCounter = myPathCounter;
	}

	public void run() {
		String line;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
		
		} catch (IOException e) {
			System.out.println("in or out failed");
			System.exit(-1);
		}

		try {
			line = in.readLine();
			// Parse this line and get the path and cumulative token values
			// System.out.println("In client worker"+line);
			// update tokenlabel value
			StringTokenizer tokenizer;
			String[] tokens = line.split("del");
			String firstToken = tokens[0];
			int cumulativeValReceived = Integer.parseInt(firstToken);
			int cumulativeValUpdated = cumulativeValReceived + myToken;
		
			ArrayList<Integer> path = new ArrayList<Integer>();
			int currentNodeIsLastNode = 0;
			String hostName_dest;
			int portNum_dest;
			ArrayList<String> pathAndFullPath = new ArrayList<String>();
			for (int i = 1; i < tokens.length; i++) {
				pathAndFullPath.add(tokens[i]);

			}
			// fp delimitted full path
			String fullpath_fp = pathAndFullPath
					.get(pathAndFullPath.size() - 1);
			
			// Remove the full path that is at the end of the pathANdFullPath
			pathAndFullPath.remove(pathAndFullPath.size() - 1);

			// Get the path out of pathAndFullPath and place it in the fullPath
			// arraylist<int>
			for (int i = 0; i < pathAndFullPath.size(); i++) {
				path.add(Integer.parseInt(pathAndFullPath.get(i)));
			
			}
			// Tokenize the fp delimitted fullpath
			ArrayList<Integer> fullPath = new ArrayList<Integer>();

			tokenizer = new StringTokenizer(fullpath_fp, "fp");
			int ind = 0;
			while (tokenizer.hasMoreTokens()) {
				fullPath.add(Integer.parseInt(tokenizer.nextToken()));
				
			}
			
			// Terminatin Condition
			if (path.isEmpty()) {
				
				String line2 = "Net ID: sxp136630";
				String line3 = "Node ID:" + fullPath.get(0);
				String line4 = "Listening on " + hostNames.get(fullPath.get(0))
						+ ":" + portNums.get(fullPath.get(0));
				String line5 = "Random number: " + myToken;
				
				String fullpath_str = Integer.toString(fullPath.get(0));

				synchronized (this) {
					// writer = new PrintWriter(new FileOutputStream( new
					// File("output.txt"),true));
					// Just to keep tracl of the file writes
					myPathCounter.add(1);
					for (int i = 1; i < fullPath.size(); i++) {
						fullpath_str = fullpath_str + "->"
								+ Integer.toString(fullPath.get(i));

					}
					fullpath_str = fullpath_str + "->"
							+ Integer.toString(fullPath.get(0));
					String line6 = "Emitting token " + myPathCounter.size()
							+ " with path " + fullpath_str;
					String line7 = "Received token " + myPathCounter.size()
							+ "           Token sum =" + cumulativeValReceived;
					if (myPathCounter.size() == 1) {
						writer.println(line2);
						writer.println(line3);
						writer.println(line4);
						writer.println(line5);
					}
					writer.println(line6);
					writer.println(line7);
					if (myPathCounter.size() == myPathCount) {
						writer.println("All tokens received");
						writer.close();
					}
				}
				return;

			}
			if (path.size() == 1) {
				currentNodeIsLastNode = 1;

			}

			if (currentNodeIsLastNode == 1) {
				// System.out.println("Came here"+path.size());
				hostName_dest = hostNames.get(fullPath.get(0));
				portNum_dest = portNums.get(fullPath.get(0));
				path = new ArrayList<Integer>();

			} else {
				hostName_dest = hostNames.get(path.get(1));
				portNum_dest = portNums.get(path.get(1));
			}

			
			// Call the client thread with the updated label value of the token
			// and the new path

			Client c = new Client(cumulativeValUpdated, path, fullPath,
					hostName_dest, portNum_dest);
			Thread t = new Thread(c);
			t.start();

		}

		catch (IOException e) {
			System.out.println("Read failed");
			System.exit(-1);
		}
	}
}
