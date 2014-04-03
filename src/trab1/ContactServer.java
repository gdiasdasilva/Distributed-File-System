package trab1;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactServer {

	private Map<String, InetAddress> serversListIP;
	private Map<String, ArrayList<String>> serversListUsers;
	
	public ContactServer()
	{
		serversListIP = new HashMap<String, InetAddress>();
	}
	
	protected void registerServer(String serverName, InetAddress serverIP, String userName)
	{
		serversListIP.put(serverName, serverIP);
		ArrayList<String> users = new ArrayList<String>();
		users.add(userName);
		serversListUsers.put(serverName, users);
	}
	
}
