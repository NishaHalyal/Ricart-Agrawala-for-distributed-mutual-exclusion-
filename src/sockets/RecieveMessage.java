package sockets;

import java.io.DataInputStream;
import java.net.Socket;

public class RecieveMessage implements Runnable {

	Socket listen;

	public RecieveMessage(Socket echoSocket) {
		// TODO Auto-generated constructor stub
		this.listen = echoSocket;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				DataInputStream inStream = new DataInputStream(
						listen.getInputStream());
				String in = inStream.readUTF();
				System.out.println(("Received: " + in));

				if (in.contains("REQUEST")) {
					/*
					 * if(SendMessage.time == 0) { Thread.sleep(10000); }
					 */
					Proj.totalreceived++;
					Ricart.request(in);
				}

				if (in.contains("Connected Successfully")) {
					Proj.ConnectionCount++;
				}

				if (in.equals("START")) {
					// Random Number Generation
					Ricart.flag = false;
					// Ricart random = new Ricart();
					// random.random();
				}
				
				if (in.contains("TERMINATED")) {
					Proj.termination++;
					//System.out.println("Number of termination messages is "+Proj.termination);
					if(Proj.termination == (Proj.no_of_nodes - 1)){
						System.out.println("ALL PROCEESES TERMINATED SUCCESSFULLY.");
						System.out.println("END OF PROGRAM.");
						System.out.println("-------------------------------------------------------------------------------------------------------------------");
					}
				}
				
				if (in.contains("REPLY")) {
					Proj.totalreceived++;
					Proj.RecCount++;
					//Proj.totalCount++;
					String str[] = in.split(" ");
					String n = str[3];
					int node = Integer.parseInt(n);
					System.out.println("Request count: " + Proj.ReqCount
							+ " Received count " + Proj.RecCount + "\n");

					if (Proj.ReqCount > Proj.RecCount) {
						Thread.sleep(20);
					}

					if (Proj.second == true) {
						if (/* Proj.ReqCount == (Proj.no_of_nodes-1) && */Proj.RecCount == (Proj.no_of_nodes - 1)
								&& Proj.critic == true) {
							//Proj.totalreceived++;
							Ricart.criticalsection();
							Proj.abc = false;
							Proj.critic = true;
						} /*
						 * else { System.out.println(""); }
						 */
					} else {
						if (SendMessage.size == Proj.RecCount
								&& Proj.critic == true) {
							//Proj.totalreceived++;
							Ricart.criticalsection();
							Proj.abc = false;
							Proj.critic = true;
						} /*
						 * else { System.out.println(""); }
						 */
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
