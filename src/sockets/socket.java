package sockets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class socket implements Runnable {

	public void run() {
		int portNumber = 0, node;
		try {
			// Read from configuration file
			BufferedReader read = new BufferedReader(new FileReader(
					"config.txt"));

			String s;
			while ((s = read.readLine()) != null) {
				String[] inp = s.split(" ");
				node = Integer.parseInt(inp[0]);
				if (Proj.num == node) {
					portNumber = Integer.parseInt(inp[2]);
					ServerSocket serverSocket = new ServerSocket(portNumber);

					while (true) {
						Socket clientSocket = serverSocket.accept();
						DataInputStream inStream = new DataInputStream(
								clientSocket.getInputStream());
						Proj.connectArray.put(
								Integer.parseInt(inStream.readUTF()),
								clientSocket);
						Proj.count++;
						System.out.println("Node " + Proj.connectArray.keySet()
								+ "connected " + "to Node " + node);

						RecieveMessage msg = new RecieveMessage(clientSocket);
						Thread rmsg = new Thread(msg);
						rmsg.start();
					}
				}
			}
		} catch (IOException e) {
			System.out
					.println("Exception caught when trying to listen on port "
							+ portNumber + "or listening for a connection");
			System.out.println(e.getMessage());
		}
	}
}