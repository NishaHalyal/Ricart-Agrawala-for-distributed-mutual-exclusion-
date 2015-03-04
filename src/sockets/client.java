package sockets;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.net.Socket;

public class client implements Runnable {

	public void run() {

		String node, ipaddr = null, portno;
		int port, nod;

		try {
			// Read from configuration file
			BufferedReader read = new BufferedReader(new FileReader(
					"config.txt"));

			String s;
			while ((s = read.readLine()) != null) {

				String[] inp = s.split(" ");
				nod = Integer.parseInt(inp[0]);
				ipaddr = inp[1];
				port = Integer.parseInt(inp[2]);

				// Connect to nodes with higher node number
				if (nod != Proj.num && nod > Proj.num) {
					Socket echoSocket = new Socket(ipaddr, port);
					System.out.println("Node " + nod + " Connected ");
					Proj.connectArray.put(nod, echoSocket);
					Proj.count++;

					// Write on socket
					DataOutputStream outStream = new DataOutputStream(
							echoSocket.getOutputStream());
					outStream.writeUTF(String.valueOf(Proj.num));

					RecieveMessage msg = new RecieveMessage(echoSocket);
					Thread rmsg = new Thread(msg);
					rmsg.start();
				}
			}
		} catch (Exception e) {
			System.err.println("Couldn't get I/O for connection to " + ipaddr);
		}
	}
}
