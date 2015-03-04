package sockets;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class SendMessage implements Runnable{
	int node = Proj.num;
	static int size;
	public static long time;
	
	public void run() {
		 try {
			 	if(Proj.first == true){
			 		Proj.first=false;
			 		Set keys = Proj.connectArray.keySet();
			 		Iterator itr = keys.iterator();
			 		int key = 0;
			 		Socket value = null;
			 		time = System.currentTimeMillis();
			 		System.out.println("MY TIME: "+time);
			 		Proj.req=true;
			 		while(itr.hasNext())
			 		{
			 			key = (Integer)itr.next();
			 			value = (Socket)Proj.connectArray.get(key);   
			 			//System.out.println(key + " - "+ value);
			 			DataOutputStream outStream = new DataOutputStream(value.getOutputStream( ));
			 			outStream.writeUTF("REQUEST "+time+" from node "+node);
			 			System.out.println("*********************Sending Request********************"+ key);
			 			Proj.ReqCount++;
			 			Proj.totalCount++;
			 		}
			 	} else {
			 		Proj.second = false;
			 		size = Proj.nodes.size();
			 		/*Set keys = Proj.connectArray.keySet();
			 		Iterator itr = keys.iterator();*/
			 		System.out.println("Array List: "+Proj.nodes);
			 		HashSet<Integer> nodeslist;
			 		synchronized(Proj.nodes)
			 		{
			 		nodeslist = new HashSet<Integer>(Proj.nodes);
			 		Proj.nodes.clear();
			 		}
			 		if(size == 0 && (Proj.critic == true)){
			 			Ricart.criticalsection();
						Proj.critic = true;
						Proj.abc=false;
			 		} 
			 		else {	
			 			
			 			Iterator itr2 = nodeslist.iterator();
			 		int key = 0;
			 		Socket value = null;
			 		time = System.currentTimeMillis();
			 		System.out.println("MY TIME: "+time);
			 		Proj.req=true;
			 		while(itr2.hasNext())
			 		{
			 			key = (Integer) itr2.next();
			 			Proj.nodes.remove(new Integer(key));
			 			value = (Socket)Proj.connectArray.get(key);   
			 			//System.out.println(key + " - "+ value);
			 			Proj.ReqCount++;
			 			Proj.totalCount++;
			 			
			 			DataOutputStream outStream = new DataOutputStream(value.getOutputStream( ));
			 			outStream.writeUTF("REQUEST "+time+" from node "+node);
			 			System.out.println("*********************Sending Request********************   "+key);
			 			itr2.remove();
			 	}
			   }
			 		//Proj.nodes.clear();
			 }
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	}
	
	public void start(HashMap<Integer, Socket> connectArray) {
		// TODO Auto-generated method stub
		try {
		 	Set keys = connectArray.keySet();
		    Iterator itr = keys.iterator();
	        int key = 0;
	        Socket value = null;
	        while(itr.hasNext())
	        {
	            key = (Integer)itr.next();
	            value = connectArray.get(key); 
	            //System.out.println("KEY: "+key);
	            DataOutputStream outStream = new DataOutputStream(value.getOutputStream( ));
				outStream.writeUTF("START");
	        }
        } catch(Exception e) {
            e.printStackTrace();
        }	
	}
}