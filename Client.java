import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

class Client implements Runnable {
	private int tokenValue;
	private ArrayList<Integer> path;
	private ArrayList<Integer> fullPath;
	private String hostName;
	private int portNum;

	public Client(int tokenValue, ArrayList<Integer> path,
			ArrayList<Integer> fullPath, String hostName, int portNum) {
		// this.isWriteRequest=isWriteRequest;
		// this.dataToWrite=data;
		this.tokenValue = tokenValue;
		this.path = path;
		this.hostName = hostName;
		this.portNum = portNum;
		this.fullPath = fullPath;

	}

	public void run() {
		StringBuffer requestMessage = new StringBuffer();
		String delimiter = "del";
		// Construct the request to the next node. Structure is
		// "tokenValue"+"del"+"pathNode1"+"del"+"pathNode2"...

		requestMessage.append(Integer.toString(tokenValue));
		requestMessage.append(delimiter);
		// Start from 1 index because 0th index is itself
		for (int i = 1; i < path.size(); i++) {

			requestMessage.append(Integer.toString(path.get(i)));
			requestMessage.append(delimiter);
		}

		for (int i = 0; i < fullPath.size(); i++) {
			requestMessage.append(Integer.toString(fullPath.get(i)));
			requestMessage.append("fp");

		}
		try {

			// Create a client socket and connect to server at the server
			// details provided.
			// Make this as a loop, which exits when server connection is
			// established
			Socket clientSocket = null;
			int serverEstablished = 0;
			while (serverEstablished == 0) {
				try {
					clientSocket = new Socket(hostName, portNum);
					serverEstablished = 1;

				} catch (IOException e) {

				}
			}

			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
			writer.println(requestMessage);
			writer.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
