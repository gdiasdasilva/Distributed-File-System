package trab1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
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
	private static String basePath = ".";

	protected FileServer(String serverName, String contactServerUrl, String userName, String ip) throws RemoteException {
		super();
		this.serverName = serverName;
		this.contactServerUrl = contactServerUrl;
		this.userName = userName;
		this.ip = ip;
	}
	
	@Override
	public void activeTest() throws RemoteException{}

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
		File f = new File(new File(basePath), path);
		if( f.exists()) {
			return new FileInfo( path, f.length(), new Date(f.lastModified()), f.isFile());
		} else
			return null;
	}

	@Override
	public boolean pasteFile(byte[] f, String toPath)
			throws RemoteException, IOException {

		try{
			File file = new File(basePath, toPath);
			OutputStream out = new FileOutputStream(file);
			out.write(f);
			out.close();

			return true;

		} catch(Exception e){
			return false;
		}
	}

	@Override
	public byte[] copyFile(String fromPath)
			throws IOException {

		try {
			File f = new File(basePath, fromPath);
			InputStream input = new FileInputStream(f);
			byte[] buffer = new byte[(int) f.length()];
			input.read(buffer);
			input.close();
			return buffer;
		} catch (FileNotFoundException e) {
			return null;
		}

	}

	public static void main( String[] args) throws Exception
	{
		if( args.length != 3) {
			System.out.println("Use: java trab1.FileServer serverName contactServerURL userName");
			return;
		}

		/* sera que e preciso isto aqui em baixo ?! */

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
		System.out.println("FileServer RMI running in " + ip + " ...");

		IFileServer server = new FileServer(serverName, contactServerUrl, userName, ip);
		Naming.rebind( "/" + serverName + "@" + userName, server);

		register(serverName, contactServerUrl, userName, ip);
	}

}
