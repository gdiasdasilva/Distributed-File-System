package trab1;

import java.io.File;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class FileServer implements IFileServer{
	
	private static String basePath = "/trabalhoSD";
	
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
			server = (IContactServer) Naming.lookup("//" + contactServerURL + basePath);
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
	
}
