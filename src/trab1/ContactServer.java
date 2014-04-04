package trab1;

import java.net.InetAddress;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ContactServer extends UnicastRemoteObject implements IContactServer {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, InetAddress> serversListIP;
	private Map<String, ArrayList<String>> serversListUsers;
	
	protected ContactServer() throws RemoteException {
		super();
		serversListIP = new HashMap<String, InetAddress>();
		serversListUsers = new HashMap<String, ArrayList<String>>();
	}	

	public void registerServer(String serverName, InetAddress serverIP, String userName)
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
	public String[] listServers(String userName)
	{
		List<String> temp = new ArrayList<String>();		
		
		for (String key : serversListUsers.keySet()) {
		    if (serversListUsers.get(key).contains(userName))
		    	temp.add(key);
		}
				
		return temp.toArray(new String[temp.size()]);
	}
	
	@Override
	public boolean addPermission(String server, String userName) throws RemoteException
	{		
		if(serversListUsers.containsKey(server) && !serversListUsers.get(server).contains(userName))
		{
			serversListUsers.get(server).add(userName);
			return true;
		}		
		else
			return false;		
	}
	
	public static void main( String[] args) throws Exception
	{
		if( args.length != 0) {
			System.out.println("Use: java trab1.ContactServer");
			return;
		}
		
		System.getProperties().put( "java.security.policy", "trab1/policy.all");
		
		if( System.getSecurityManager() == null) {
			System.setSecurityManager( new RMISecurityManager());
		}

		try { // start rmiregistry
			LocateRegistry.createRegistry( 1099);
		} catch( RemoteException e) { 
			// if not start it
			// do nothing - already started with rmiregistry
		}
		
		ContactServer server = new ContactServer();
		Naming.rebind( "/trabalhoSD", server);
		System.out.println( "ContactServer running...");
	}
	
}
