package trab1;

import java.io.File;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

public class FileServer extends UnicastRemoteObject implements IFileServer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String serverName, contactServerUrl, userName, ip;

	protected FileServer(String serverName, String contactServerUrl, String userName, String ip) throws RemoteException {
		super();
		this.serverName = serverName;
		this.contactServerUrl = contactServerUrl;
		this.userName = userName;
		this.ip = ip;
	}

	private static String basePath = ".";
	
	
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

	@Override
	public String[] dir(String dir) throws InfoNotFoundException,
			RemoteException {
		
		File f = new File(new File(basePath), dir);
		
		if(f.exists())
			return f.list();
		else
			throw new InfoNotFoundException("Directory not found: " + dir);
	}

	@Override
	public boolean mkdir(String dir) throws RemoteException {

		File m = new File(new File(basePath), dir);
		return m.mkdir();
	}

	@Override
	public boolean rmdir(String dir) throws RemoteException {
		File r = new File(new File(basePath), dir);
		if (r.isDirectory() && r.list().length == 0)
			return r.delete();
		else
			return false;
	}

	@Override
	public boolean rm(String dir) throws RemoteException {

		File f = new File(new File(basePath), dir);
		if (f.isFile())
			return f.delete();
		else
			return false;
	}
	
	@Override
	public FileInfo getAttr(String path) throws RemoteException, InfoNotFoundException {
		File dir = new File(new File(path), basePath);
		if( dir.exists()) {
			File f = new File(dir, path);
			if( f.exists())
				return new FileInfo( path, f.length(), new Date(f.lastModified()), f.isFile());
			else
				throw new InfoNotFoundException( "File not found :" + path);
		} else
			throw new InfoNotFoundException( "Directory not found :" + path);
	}
	
	public static void main( String[] args) throws Exception
	{
		if( args.length != 3) {
			System.out.println("Use: java trab1.FileServer serverName contactServerURL userName");
			return;
		}
		
		try { // start rmiregistry
			LocateRegistry.createRegistry( 1099);
		} catch( RemoteException e) { 
			// if not start it
			// do nothing - already started with rmiregistry
		}
				
		String serverName = args[0];
		String contactServerUrl = args[1];
		String userName = args[2];
		String ip = InetAddress.getLocalHost().getHostAddress().toString();
		
		IFileServer server = new FileServer(serverName, contactServerUrl, userName, ip);
		Naming.rebind( "/" + serverName + "@" + userName, server);
		
		register(serverName, contactServerUrl, userName, ip);
	}
	
}
