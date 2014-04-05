package trab1;

import java.net.InetAddress;
import java.rmi.Naming;

public class FileServer {
	
	public static void main( String[] args) throws Exception
	{
		if( args.length != 3) {
			System.out.println("Use: java trab1.FileServer serverName contactServerURL userName");
			return;
		}
				
		String serverName = args[0];
		String contactServerUrl = args[1];
		String userName = args[2];
		String ip = InetAddress.getLocalHost().getHostName();
		
		register(serverName, contactServerUrl, userName, ip);
	}
	
	public static void register (String serverName, String contactServerURL, String userName, String ip)
	{
		IContactServer server;
		
		try
		{
			server = (IContactServer) Naming.lookup("//" + contactServerURL + "/trabalhoSD");
			server.registerServer(serverName, ip, userName);
		} 
		catch (Exception e) 
		{
			System.out.println("Erro ao fazer o lookup do Contact Server.");
		}		
	}
	
}
