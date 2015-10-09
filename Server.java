import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

class Server extends Thread {
	private Thread t;
	private ArrayList<String> hostNames;
	private ArrayList<Integer> portNums;
	private int me;
	private int myToken;
	private PrintWriter writer;
	private ArrayList<Integer> writeReqCount;
	private ArrayList<Integer> myPathCounter;
	private int myPathCount;
	int totalPathCount;

	Server(int me, int myToken, ArrayList<String> hostNames,
			ArrayList<Integer> portNums, PrintWriter writer,
			int totalPathCount, int myPathCount) {
		this.me = me;
		this.myToken = myToken;
		this.hostNames = hostNames;
		this.portNums = portNums;
		try {
			this.writer = writer;

		} catch (Exception e) {
			e.printStackTrace();
		}
		writeReqCount = new ArrayList<Integer>();
		this.totalPathCount = totalPathCount;
		this.myPathCount = myPathCount;
		myPathCounter = new ArrayList<Integer>();
	}

	public void run() {

		try {
			String message = "Hello from server at "
					+ InetAddress.getLocalHost().getHostName();

			// Create a server socket on this machine on the portNums[me] port
			ServerSocket serverSock = new ServerSocket(portNums.get(me));
			// Server goes into a permanent loop accepting connections from
			// clients
			while (true) {
				// Listens for a connection to be made to this socket and
				// accepts it
				// The method blocks until a connection is made
				ClientWorker w;
				try {
					// server.accept returns a client connection, which is
					// handled to ClientWorker.
					w = new ClientWorker(me, myToken, serverSock.accept(),
							hostNames, portNums, writer, writeReqCount,
							totalPathCount, myPathCount, myPathCounter);
					Thread t = new Thread(w);
					t.start();
				} catch (IOException e) {
					System.out.println("Accept failed: 4444");
					System.exit(-1);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void start() {

		if (t == null) {
			t = new Thread(this);
			t.start();
		}

	}

}
