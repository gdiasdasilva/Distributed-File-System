package trab1;

import java.net.InetAddress;
import java.util.*;

public class ContactServer {
	
	static Map<String, InetAddress> serversListIP;
	static Map<String, ArrayList<String>> serversListUsers;
	
	public static void main( String[] args) throws Exception
	{
		if( args.length != 0) {
			System.out.println("Use: java trab1.ContactServer");
			return;
		}
		
		serversListIP = new HashMap<String, InetAddress>();
		serversListUsers = new HashMap<String, ArrayList<String>>();		
	}

	public static void registerServer(String serverName, InetAddress serverIP, String userName)
	{
		serversListIP.put(serverName, serverIP);
		ArrayList<String> users = new ArrayList<String>();
		users.add(userName);
		serversListUsers.put(serverName, users);
	}
	
	/**
	 * Lista servidores acessiveis a determinado utilizador
	 * @return
	 */
	protected String[] listServers(String userName)
	{
		String temp[] = new String[serversListIP.size()];
		temp = (String[]) serversListIP.keySet().toArray();
		return temp;
	}
	
}
