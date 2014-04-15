package trab1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.rmi.Naming;
import java.util.Date;

import javax.jws.*;
import javax.xml.ws.Endpoint;

@WebService
public class FileServerWS implements IFileServerWS {

	private String serverName, contactServerUrl, userName, ip;
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
			server.registerServer(serverName, ip, userName);
		} 
		catch (Exception e) 
		{
			System.out.println("Erro ao fazer o lookup do Contact Server.");
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
			return null;
		}
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
						"http://" + ip + ":" + port + "/FileServer",
						new FileServerWS(serverName, contactServerUrl, userName, ip, port));
				System.out.println( "FileServer started in address " + ip);
				System.out.println( "FileServer started in port " + port);
				break;
			}
			catch( Throwable th) 
			{
				th.printStackTrace();;
				port++;
				break;
			}

		}
		System.out.println(ip);
		register(serverName, contactServerUrl, userName, "http://" + ip + ":" + port);
	}
}
