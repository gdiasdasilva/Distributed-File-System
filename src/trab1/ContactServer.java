package trab1;

import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URL;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

import ws.FileServerWSService;

public class ContactServer extends UnicastRemoteObject implements IContactServer {

	private static final long serialVersionUID = 1L;
	private Map<String, String> serversListIP;
	private Map<String, ArrayList<String>> serversListUsers;

	protected ContactServer() throws RemoteException {
		super();
		serversListIP = new HashMap<String, String>();
		serversListUsers = new HashMap<String, ArrayList<String>>(); 
	}	

	public void clearUnavailableServers() throws RemoteException
	{	// Feito com iteradores por causa da ConcurrentModificationException
		Iterator<Map.Entry<String,String>> iterIP = serversListIP.entrySet().iterator();
		Iterator<Entry<String, ArrayList<String>>> iterUsers = serversListUsers.entrySet().iterator();
		while (iterIP.hasNext())
		{
			Map.Entry<String, String> entryIP = iterIP.next();
			Map.Entry<String, ArrayList<String>> entryUsers = iterUsers.next();

			try
			{
				if(entryIP.getValue().startsWith("http"))
				{
					FileServerWSService service = new FileServerWSService( new URL( entryIP.getValue() + "/FileServer?wsdl"), new QName("http://trab1/", "FileServerWSService"));
					ws.FileServerWS serverWS = service.getFileServerWSPort();
					serverWS.activeTest();
				}
				else
				{
					String tmp = entryIP.getValue();
					IFileServer s = (IFileServer) Naming.lookup("//"  + tmp + "/" + entryUsers.getKey() + "@" + entryUsers.getValue().get(0));
					s.activeTest();
				}
			}
			catch(Exception e)
			{
				iterIP.remove();
				iterUsers.remove();
			}
		}
	}

	public void registerServer(String serverName, String serverIP, String userName) throws RemoteException
	{		
		if(!serversListUsers.containsKey(serverName))
		{
			serversListIP.put(serverName, serverIP);
			ArrayList<String> users = new ArrayList<String>();
			users.add(userName);
			serversListUsers.put(serverName, users);
		}
	}

	/**
	 * Lista servidores acessiveis a determinado utilizador
	 * @return
	 */
	public String[] listServers(String userName) throws RemoteException
	{
		clearUnavailableServers();
		List<String> temp = new ArrayList<String>();		
		for (String key : serversListUsers.keySet()) {
			if (serversListUsers.get(key).contains(userName))
			{
				temp.add(key + "@" + serversListUsers.get(key).get(0));
			}
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

	public String serverAddress(String server, String user) throws RemoteException
	{
		clearUnavailableServers();
		if(serversListUsers.get(server).contains(user))
			return serversListIP.get(server).toString();
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
		String ip = InetAddress.getLocalHost().getHostAddress().toString();
		System.out.println( "ContactServer running in " + ip + " ...");
	    
	    new Thread(new MulticastServer()).start();
	}
}
