package trab1;

/**
 * SD 13/14 - Trabalho pratico 1
 * Gonçalo Dias da Silva: 41831
 * Joao Francisco Pinto: 41887
 */

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.util.Date;

public class FileServer extends UnicastRemoteObject implements IFileServer {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
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
			boolean success = server.registerServer(serverName, ip, userName);
			if(!success)
				System.exit(0);
		} 
		catch (Exception e) 
		{
			System.out.println("Contact Server nao encontrado no endereco fornecido");
			System.exit(0);
		}		
	}

	@Override
	public String[] dir(String dir) throws InfoNotFoundException,
	RemoteException {
		File f = new File(new File(basePath), dir);
		if(f.exists())
			return f.list();
		else
			throw new InfoNotFoundException("Directoria nao encontrada: " + dir);
	}

	@Override
	public synchronized boolean mkdir(String dir) throws RemoteException {
		File m = new File(new File(basePath), dir);
		return m.mkdir();
	}

	@Override
	public synchronized boolean rmdir(String dir) throws RemoteException {
		File r = new File(new File(basePath), dir);
		if (r.isDirectory() && r.list().length == 0)
			return r.delete();
		else
			return false;
	}

	@Override
	public synchronized boolean rm(String dir) throws RemoteException {
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
	public synchronized boolean pasteFile(byte[] f, String toPath)
			throws RemoteException, IOException {
		try{
			File file = new File(basePath, toPath);
			OutputStream out = new FileOutputStream(file);
			out.write(f);
			out.close();
			return true;
		} catch(Exception e){
			System.out.println("Erro na gravacao do ficheiro");
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
			System.out.println("Erro ao copiar o ficheiro. Nao encontrado");
			return null;
		}
	}

	public static void main( String[] args) throws Exception
	{
		if( args.length != 3 && args.length != 2){
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
		String ip = InetAddress.getLocalHost().getHostAddress().toString();
		String contactServerUrl = "";
		String userName = "";
		int flag = 0;

		if (args.length == 3)
		{
			contactServerUrl = args[1];
			userName = args[2];
			flag++;
		}
		else
		{
			userName = args[1];

			// Call multicast client to get contact server ip

			int port = 5000;
			String group = "225.4.5.6";

			try{
				MulticastSocket s = new MulticastSocket(port);
				s.joinGroup(InetAddress.getByName(group));

				byte buf[] = new byte[1024];
				DatagramPacket pack = new DatagramPacket(buf, buf.length);
				s.setSoTimeout(2000); 
				s.receive(pack);

				contactServerUrl = new String(pack.getData(), 0, pack.getLength());			

				s.leaveGroup(InetAddress.getByName(group));
				s.close();
				flag++;
			} 
			catch(Exception e)
			{
				System.out.println("Erro ao receber o endereco do Contact Server por Multicast.");
				System.exit(0);
			}
		}

		try
		{
			IFileServer server = new FileServer(serverName, contactServerUrl, userName, ip);
			Naming.rebind( "/" + serverName + "@" + userName, server);
			flag++;
		}
		catch(Exception e)
		{
			System.out.println("Erro ao criar FileServer");
			System.exit(0);
		}

		if(flag == 2)
		{
			register(serverName, contactServerUrl, userName, ip);
			System.out.println("FileServer RMI running in " + ip + " ...");
		}
	}

}
