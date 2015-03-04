package sockets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Proj {
	static boolean first = true, critic = true, iterations = true, abc,
			second = true;
	static int num, count, ConnectionCount, ReqCount, RecCount, rounds,
			no_of_nodes = 10, totalCount, totalreceived, termination;
	static boolean req = false;
	static HashMap<Integer, Socket> connectArray = new HashMap<Integer, Socket>();
	static HashSet<Integer> nodes = new HashSet<Integer>();
	static HashSet<Integer> defer = new HashSet<Integer>();
	static HashMap<Integer, Socket> even = new HashMap<Integer, Socket>();
	static HashMap<Integer, Socket> odd = new HashMap<Integer, Socket>();
	public static long CStime;

	public static void main(String[] args) throws InterruptedException,
			IOException {
		// Node Number
		System.out.println("Provide Node Number");
		Scanner in = new Scanner(System.in);
		num = Integer.parseInt(in.nextLine());

		// Call to Server
		socket s = new socket();
		Thread ts = new Thread(s);
		ts.start();
		Thread.sleep(15000);

		// Call to Client
		client cl = new client();
		Thread tc = new Thread(cl);
		tc.start();
		Thread.sleep(15000);

		if (count == (no_of_nodes - 1)) {
			Set keys = connectArray.keySet();
			Iterator itr = keys.iterator();
			int key = 0;
			Socket value = null;
			while (itr.hasNext()) {
				key = (Integer) itr.next();
				value = (Socket) connectArray.get(key);
				if (key == 1) {
					DataOutputStream outStream1 = new DataOutputStream(
							value.getOutputStream());
					outStream1.writeUTF("Connected Successfully"
							+ " from node " + Proj.num);
				}
			}
		}

		Thread.sleep(20000);
		if (ConnectionCount == (no_of_nodes - 1)) {
			System.out
					.println("\n*******************Send Start Message*******************");
			SendMessage msg = new SendMessage();
			msg.start(connectArray);
		}

		if (num == 1 || (num != 1 && Ricart.flag == false)) {
			// Random Number Generation
			Ricart random = new Ricart();
			for (int i = 1; i <= 40; i++) {
				while (critic == false || abc == true)
					Thread.sleep(50);
				random.random(num, i);
				abc = true;
				//totalrounds++;
			}
		}		 
	}
}
