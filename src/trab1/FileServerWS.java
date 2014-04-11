package trab1;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;

import javax.jws.*;
import javax.xml.ws.Endpoint;

@WebService
public class FileServerWS implements IFileServerWS {

	private String serverName, contactServerUrl, userName, ip;
	private int port;

	public FileServerWS(String serverName, String contactServerUrl, String userName, String ip, int port) {
		super();
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
			server.registerServer(serverName, ip, userName);
		} 
		catch (Exception e) 
		{
			System.out.println("Erro ao fazer o lookup do Contact Server.");
		}		
	}

	@Override
	public String[] dir(String dir) throws InfoNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean mkdir(String dir) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rmdir(String dir) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rm(String dir) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public FileInfo getAttr(String path) throws InfoNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean pasteFile(byte[] f, String toPath) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte[] copyFile(String fromPath) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main( String[] args) throws Exception
	{
		if( args.length != 3)
		{
			System.out.println("Use: java trab1.FileServerWS serverName contactServerURL userName");
			return;
		}

		String serverName = args[0];
		String contactServerUrl = args[1];
		String userName = args[2];
		String ip = InetAddress.getLocalHost().getHostAddress().toString();		
		int port = 8080;

		for (;;)
		{
			try
			{
				Endpoint.publish(
						"http://" + ip + "/FileServer",
						new FileServerWS(serverName, contactServerUrl, userName, ip, port));
				System.out.println( "FileServer started in port " + port);
				break;
			}
			catch( Throwable th) 
			{
				port++;
			}
		}
	}
}
