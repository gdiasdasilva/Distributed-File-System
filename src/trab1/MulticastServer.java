package trab1;

// Import some needed classes
import sun.net.*;

import java.net.*;

public class MulticastServer implements Runnable {

//	public MulticastServer(){
//
//	}

	@Override
	public void run() {
		
//		try{
//			int port = 5000;
//			String group = "225.4.5.6";
//			int ttl = 1;
//			
//			MulticastSocket s = new MulticastSocket();
//			
//			while(true)
//			{
//				byte buf[] = new byte[10];
//				
//				for (int i=0; i<buf.length; i++) 
//					buf[i] = (byte)i;
//				
//				// Create a DatagramPacket 
//				DatagramPacket pack = new DatagramPacket(buf, buf.length,
//						InetAddress.getByName(group), port);
//				
//				// Do a send. Note that send takes a byte for the ttl and not an int.
//				s.send(pack,(byte)ttl);
//				// And when we have finished sending data close the socket
//				s.close();
//			}
//			
//		}
//		catch(Exception e)
//		{
//
//		}
		
		System.out.println("HELLO!");

	}

}
