package trab1;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IFileServer extends Remote{
	
	/**
	 * 
	 * @param server
	 * @param user
	 * @param dir
	 * @param currentUser
	 * @return
	 * @throws InfoNotFoundException
	 * @throws RemoteException
	 */
	public String[] dir(String dir) throws InfoNotFoundException, RemoteException;
	
	/**
	 * 
	 * @param dir
	 * @return
	 * @throws RemoteException
	 */
	public boolean mkdir(String dir) throws RemoteException;
	
	/**
	 * 
	 * @param dir
	 * @return
	 * @throws RemoteException
	 */
	public boolean rmdir(String dir) throws RemoteException;
	
	/**
	 * 
	 * @param dir
	 * @return
	 * @throws RemoteException
	 */
	public boolean rm(String dir) throws RemoteException;
	
	/**
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws InfoNotFoundException 
	 */
	public FileInfo getAttr(String path) throws RemoteException, InfoNotFoundException; 
	
	public boolean pasteFile(byte[] f, String toPath) throws RemoteException, IOException;
	
	public byte[] copyFile(String fromPath) throws RemoteException, IOException;
	

}
