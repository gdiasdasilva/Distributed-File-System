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
import java.util.Date;

import javax.jws.*;
import javax.xml.ws.Endpoint;

@WebService
public class FileServerWS implements IFileServerWS {
	@SuppressWarnings("unused")
	private String serverName, contactServerUrl, userName, ip;
	@SuppressWarnings("unused")
	private int port;
	private static String basePath = ".";

	public FileServerWS(String serverName, String contactServerUrl, String userName, String ip, int port) {
		this.serverName = serverName;
		this.contactServerUrl = contactServerUrl;
		this.userName = userName;
		this.ip = ip;
		this.port = port;
	}

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
			System.out.println("Erro ao fazer o lookup do Contact Server.");
			System.exit(0);
		}		
	}

	@WebMethod
	public String[] dir(String dir) throws InfoNotFoundException {

		File f = new File(new File(basePath), dir);

		if(f.exists())
			return f.list();
		else
			throw new InfoNotFoundException("Directory not found: " + dir);
	}

	@WebMethod
	public boolean mkdir(String dir) {
		File m = new File(new File(basePath), dir);
		return m.mkdir();
	}

	@WebMethod
	public boolean rmdir(String dir) {
		File r = new File(new File(basePath), dir);
		if (r.isDirectory() && r.list().length == 0)
			return r.delete();
		else
			return false;
	}

	@WebMethod
	public boolean rm(String dir) {
		File f = new File(new File(basePath), dir);
		if (f.isFile())
			return f.delete();
		else
			return false;
	}

	@WebMethod
	public FileInfo getAttr(String path) throws InfoNotFoundException {
		File f = new File(new File(basePath), path);
		if( f.exists()) {
			return new FileInfo( path, f.length(), new Date(f.lastModified()), f.isFile());
		} else
			return null;
	}

	@WebMethod
	public boolean pasteFile(byte[] f, String toPath) throws IOException {

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

	@WebMethod
	public byte[] copyFile(String fromPath) throws IOException {
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

	@WebMethod
	public void activeTest() {}

	public static void main( String[] args) throws Exception
	{
		if( args.length != 3 && args.length != 2)
		{
			System.out.println("Use: java trab1.FileServerWS serverName contactServerURL userName");
			return;
		}

		String serverName = args[0];
		String contactServerUrl = "";
		String userName = "";
		String ip = InetAddress.getLocalHost().getHostAddress().toString();
		int port = 8080;

		if (args.length == 3)
		{
			contactServerUrl = args[1];
			userName = args[2];
		}
		else
		{
			userName = args[1];

			// Call multicast client to get contact server ip

			int portMulticast = 5000;
			String group = "225.4.5.6";

			try{
				MulticastSocket s = new MulticastSocket(portMulticast);
				s.joinGroup(InetAddress.getByName(group));

				byte buf[] = new byte[1024];
				DatagramPacket pack = new DatagramPacket(buf, buf.length);
				s.setSoTimeout(2000); 
				s.receive(pack);

				contactServerUrl = new String(pack.getData(), 0, pack.getLength());			

				s.leaveGroup(InetAddress.getByName(group));
				s.close();
			} 
			catch(Exception e){
				System.out.println("Erro ao receber o endereco do Contact Server por Multicast.");
				System.exit(0);
			}
		}

		for (;;)
		{
			try
			{
				Endpoint.publish(
						"http://" + ip + ":" + port + "/FileServer",
						new FileServerWS(serverName, contactServerUrl, userName, ip, port));
				break;
			}
			catch( Throwable th) 
			{
				port++;
			}
		}
		
		register(serverName, contactServerUrl, userName, "http://" + ip + ":" + port);
		System.out.println("FileServer WS running in " + ip + ":" + port + " ...");
	}
}
