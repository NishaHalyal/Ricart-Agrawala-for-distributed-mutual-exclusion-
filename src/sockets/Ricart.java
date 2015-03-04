package sockets;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Ricart {
	public static boolean flag;

	public static void random(int num, int i) throws InterruptedException {
		// Random Number Generation
		if (i <= 20 || (i > 20 && (num % 2) != 0)) {
			int upper = 10, lower = 5;
			int r = (int) (Math.random() * (upper - lower)) + lower;
			int here = Proj.rounds + 1;
			System.out.println("Round "+here+" started");
			System.out.println("Random Number is: " + r);
			try {
				//System.out.println("Waiting started for " + r);
				Thread.sleep(r * 100);
				//System.out.println("End of it");
				/*System.out
						.println("\n");*/
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			int upper = 50, lower = 45;
			int r = (int) (Math.random() * (upper - lower)) + lower;
			System.out.println("Round "+Proj.rounds+" started");
			System.out
					.println("Random Number for even after 20 rounds is: " + r);
			try {
				//System.out.println(" Waiting started " + r);
				Thread.sleep(r * 100);
				//System.out.println("End of it");
				/*System.out
						.println("\n");*/
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Broadcast
		System.out
				.println("\n*********************************Send Message****************************************************\n");
		SendMessage msg = new SendMessage();
		Thread tmsg = new Thread(msg);
		tmsg.start();
	}

	public static void request(String in) throws IOException,
			InterruptedException {
		String str[] = in.split(" ");
		String n = str[4];
		String tim = str[1];
		int node = Integer.parseInt(n);
		if (!Proj.nodes.contains(node))
			Proj.nodes.add(node);

		if (Proj.req == true) {
			 System.out.println("REQUEST TRUE");
			long Recvtime = Long.parseLong(tim);
			if (SendMessage.time == Recvtime) {
				if (Proj.num < node) {
					Proj.totalCount++;
					Proj.defer.add(node);
					System.out.println("Defered List: " + Proj.defer);
				} else {
					DataOutputStream outStream = new DataOutputStream(
							Proj.connectArray.get(node).getOutputStream());
					outStream.writeUTF("REPLY from node " + Proj.num);
					System.out.println("REPLY sent from node " + Proj.num
							+ " to node " + node);
					Proj.totalCount++;
				}
			} else if (SendMessage.time < Recvtime) {
				Proj.defer.add(node);
				System.out.println("Defered List: " + Proj.defer);
			} else {
				DataOutputStream outStream = new DataOutputStream(
						Proj.connectArray.get(node).getOutputStream());
				outStream.writeUTF("REPLY from node " + Proj.num);
				System.out.println("REPLY sent from node " + Proj.num
						+ " to node " + node);
				Proj.totalCount++;
			}
		} else {
			DataOutputStream outStream = new DataOutputStream(Proj.connectArray
					.get(node).getOutputStream());
			outStream.writeUTF("REPLY from node " + Proj.num);
			System.out.println("REPLY sent from node " + Proj.num + " to node "
					+ node);
			Proj.totalCount++;
		}
	}

	public static void criticalsection() throws InterruptedException,
			IOException {
		// TODO Auto-generated method stub
		Proj.critic = false;
		if(Proj.rounds <= 1){
			int messages = Proj.totalCount + Proj.totalreceived;
			System.out.println("Total messages before enterng CS is: "+messages);
		} else {
			int size2 = SendMessage.size;
			int messages2 = (size2 * 2);
			System.out.println("Total messages before enterng CS is: "+messages2);
		}
		
		FileWriter file = new FileWriter("Results.txt", true);
		BufferedWriter writer = new BufferedWriter(file);
		writer.write("Node " + Proj.num
				+ " entering critical section with timestamp "
				+ new SimpleDateFormat("MM/dd/yy h:mm:ss a").format(new Date())
				+ "\n");
		long TimeNow = System.currentTimeMillis();
		Proj.CStime = TimeNow - SendMessage.time;
		System.out.println("Time Latency is: "+Proj.CStime);
		
		
		System.out.println("\nEntering critical section: Node " + Proj.num
				+ " with timestamp " + TimeNow);
		System.out
				.println("----------------------------------------------------------------------------------\n");
		/*for (int i = 1; i <= 3; i++) {
			System.out.println("Time " + i);
			Thread.sleep(100);
		}*/
		Thread.sleep(300);
		System.out.println("Exiting Critical Section at "
				+ System.currentTimeMillis() + "\n");

		writer.write("Node " + Proj.num
				+ " exiting critical section with timestamp "
				+ new SimpleDateFormat("MM/dd/yy h:mm:ss a").format(new Date())
				+ "\n");
		writer.close();
		file.close();

		Proj.req = false;

		HashSet<Integer> deferhash = new HashSet<Integer>(Proj.defer);
		Iterator itr = deferhash.iterator();
		while (itr.hasNext()) {
			int i =  (Integer) itr.next();
			Proj.defer.remove(new Integer(i));
			Socket sock = Proj.connectArray.get(i);
			DataOutputStream outStream = new DataOutputStream(
					sock.getOutputStream());
			outStream.writeUTF("REPLY from node " + Proj.num + " Defered ");
			Proj.totalCount++;
			System.out.println("\nDefered reply sent to node " + i);
			itr.remove();
		}
		Proj.RecCount = 0;
		Proj.ReqCount = 0;
		// Proj.iterations = true;
		System.out.println("DeferList: " + Proj.defer + "\n");
		Proj.rounds++;
		System.out.println("Round " + Proj.rounds + " completed\n");
		
		if(Proj.rounds == 40){
			System.out.println("ENDED with number of rounds = "+Proj.rounds+" at time "+System.currentTimeMillis());
			System.out.println("Total number of messages sent from node "+Proj.num+" is: "+Proj.totalCount);
			System.out.println("Total number of messages received on node "+Proj.num+" is: "+Proj.totalreceived);
			Set keys = Proj.connectArray.keySet();
	 		Iterator iterate = keys.iterator();
	 		int key = 0;
	 		Socket value = null;
	 		//time = System.currentTimeMillis();
	 		//System.out.println("MY TIME: "+time);
	 		//Proj.req=true;
	 		while(iterate.hasNext())
	 		{
	 			key = (Integer)iterate.next();
	 			value = (Socket)Proj.connectArray.get(key);
	 			if(key == 1){
	 				Socket sock1 = Proj.connectArray.get(key);
	 				DataOutputStream outStream = new DataOutputStream(
	 						sock1.getOutputStream());
	 				outStream.writeUTF("TERMINATED from node " + Proj.num);
	 			}
	 		}
		}
		
	}
}
