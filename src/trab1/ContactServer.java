package trab1;

import java.io.File;
import java.net.InetAddress;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ContactServer extends UnicastRemoteObject implements IContactServer {
		
	private static final long serialVersionUID = 1L;
	private Map<String, InetAddress> serversListIP;
	private Map<String, ArrayList<String>> serversListUsers;
	
	protected ContactServer() throws RemoteException {
		super();
		serversListIP = new HashMap<String, InetAddress>();
		serversListUsers = new HashMap<String, ArrayList<String>>(); 
	}	

	public void registerServer(String serverName, InetAddress serverIP, String userName) throws RemoteException
	{
		String svName = serverName + "@" + userName;
		serversListIP.put(svName, serverIP);
		ArrayList<String> users = new ArrayList<String>();
		users.add(userName);
		serversListUsers.put(svName, users);
	}
	
	/**
	 * Lista servidores acessiveis a determinado utilizador
	 * @return
	 */
	public String[] listServers(String userName) throws RemoteException
	{
		List<String> temp = new ArrayList<String>();		
		
		for (String key : serversListUsers.keySet()) {
		    if (serversListUsers.get(key).contains(userName))
		    	temp.add(key);
		}
				
		return temp.toArray(new String[temp.size()]);
	}
	
	@Override
	public boolean addPermission(String server, String userName, String owner) throws RemoteException
	{		
		if(serversListUsers.containsKey(server) && !serversListUsers.get(server).contains(userName) 
				&& serversListUsers.get(server).get(0).equals(owner))
		{
			serversListUsers.get(server).add(userName);
			return true;
		}		
		else
			return false;		
	}
	
	@Override
	public boolean remPermission(String server, String user, String owner) throws RemoteException {

		if(serversListUsers.containsKey(server) && serversListUsers.get(server).contains(user)
				&& serversListUsers.get(server).get(0).equals(owner) && !user.equals(owner))
		{
			serversListUsers.get(server).remove(user);
			return true;
		}
		else	
			return false;
	}
	
	public String[] dir( String server, String user, String dir) throws InfoNotFoundException, RemoteException{
		//incompleto
		
		if(serversListUsers.containsKey(server) && serversListUsers.get(server).contains(user))
		{
			// falta dar o correcto basePath
			String basePath = null;
			File f = new File( basePath, dir);
			if(f.exists())
				return f.list();
			else	
				return null;
		}
		else
			return null;
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
