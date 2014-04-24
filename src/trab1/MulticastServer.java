package trab1;

/**
 * SD 13/14 - Trabalho pratico 1
 * Gonçalo Dias da Silva: 41831
 * Joao Francisco Pinto: 41887
 */

import java.net.*;

public class MulticastServer implements Runnable {

	public MulticastServer(){

	}

	@SuppressWarnings("deprecation")
	@Override
	public void run()
	{
		try
		{
			int port = 5000;
			String group = "225.4.5.6";
			int ttl = 1;
			String ip = InetAddress.getLocalHost().getHostAddress().toString();

			while(true)
			{ 
				MulticastSocket s = new MulticastSocket();
				byte buf[] = new byte[1024];				
				buf = ip.getBytes();
				DatagramPacket pack = new DatagramPacket(buf, buf.length, InetAddress.getByName(group), port);
				s.send(pack,(byte)ttl);
				s.close();
			}
		}
		catch(Exception e)
		{
			System.out.println("Erro ao enviar IP por multicast. Falha de rede.");
		}
	}
}
