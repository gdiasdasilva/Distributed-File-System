package trab1;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IContactServer extends Remote {
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public String[] listServers(String userName) throws RemoteException;
	
	/**
	 * 
	 * @param serverName
	 * @param serverIP
	 * @param userName
	 */
	public void registerServer(String serverName, InetAddress serverIP, String userName) throws RemoteException;

}
