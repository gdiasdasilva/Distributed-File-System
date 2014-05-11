package trab1;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import org.json.simple.parser.JSONParser;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class ProxyGoogleDrive extends UnicastRemoteObject implements IProxyRest{

	private OAuthService service;
	private Token token;
	private JSONParser parser;
	
	private static final String API_ID = "893421333980.apps.googleusercontent.com";
	private static final String API_SECRET = "s7_K5O0O5NnZ8LPu73SE9wPl";
	protected static final String SCOPE = "https://www.googleapis.com/auth/drive.file"; 
	protected static final String AUTHORIZE_URL = "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=";

	
	protected ProxyGoogleDrive(OAuthService service, Token token) throws RemoteException 
	{
		this.service = service;
		this.token = token;
		parser = new JSONParser();
	}
	
	@Override
	public String[] dir(String dir) throws InfoNotFoundException,
			RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static void register (String serverName, String contactServerURL, String userName, String ip)
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
	public boolean mkdir(String dir) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rmdir(String dir) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rm(String dir) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public FileInfo getAttr(String path) throws RemoteException,
			InfoNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void activeTest() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean copy(String string, String string2) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static void main( String[] args) throws Exception
	{
		if( args.length != 3 && args.length != 2){
			System.out.println("Use: java -cp json-simple-1.1.1.jar:scribe-1.3.2.jar:"
					+ "commons-codec-1.7.jar:. trab1.ProxyGoogleDrive serverName contactServerUrl userName");
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
			// comunicacao com a google drive
			OAuthService service = new ServiceBuilder().provider(GoogleApi.class).apiKey(API_ID)
					.apiSecret(API_SECRET).scope(SCOPE).build();
			Scanner in = new Scanner(System.in);
			// Problema a obter o token
			
			Token requestToken = service.getRequestToken();
			
			System.out.println("Tem de obter autorizacao para a aplicacao continuar acedendo ao link:");
			System.out.println(AUTHORIZE_URL + requestToken.getToken());
			System.out.println("E copia-la para aqu");
			System.out.print(">>");
			
			Verifier verifier = new Verifier(in.nextLine());
			
			//verifier = new Verifier(requestToken.getSecret());
			Token accessToken = service.getAccessToken(requestToken, verifier);	
			IProxyRest server = new ProxyGoogleDrive(service, accessToken);

			Naming.rebind( "/" + serverName + "@" + userName, server);
			flag++;
		}
		catch(Exception e)
		{
			System.out.println("Erro ao criar ProxyGoogleDrive");
			e.printStackTrace();
			System.exit(0);
		}

		if(flag == 2)
		{
			register(serverName, contactServerUrl, userName, ip);
			System.out.println("ProxyGoogleDrive RMI running in " + ip + " ...");
		}
	}

}
