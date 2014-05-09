package trab1;

/**
 * SD 13/14 - Trabalho pratico 1
 * Gonalo Dias da Silva: 41831
 * Joao Francisco Pinto: 41887
 */

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DropBoxApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.json.simple.parser.ParseException;

public class ProxyDropbox extends UnicastRemoteObject implements IFileServer {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private String serverName, contactServerUrl, userName, ip;
	private static String basePath = ".";
	private OAuthService service;
	private Token token;
	private JSONParser parser;

	private static final String API_KEY = "0porwedsrod7nkr";
	private static final String API_SECRET = "zma8bl6mbeorrjb";
	private static final String SCOPE = "dropbox";
	private static final String AUTHORIZE_URL = "https://www.dropbox.com/1/oauth/authorize?oauth_token=";
	private static final String METADATA_URL = "https://api.dropbox.com/1/metadata/dropbox";
	private static final String CREATE_FOLDER_URL = "https://api.dropbox.com/1/fileops/create_folder?root=dropbox&path=";
	private static final String REMOVE_URL = "https://api.dropbox.com/1/fileops/delete?root=dropbox&path=";

	protected ProxyDropbox(String serverName, String contactServerUrl, String userName, String ip) throws RemoteException {
		super();
		this.serverName = serverName;
		this.contactServerUrl = contactServerUrl;
		this.userName = userName;
		this.ip = ip;
	}

	protected ProxyDropbox(OAuthService service, Token token) throws RemoteException 
	{
		this.service = service;
		this.token = token;
		parser = new JSONParser();
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

		JSONObject res;
		try {
			res = this.getMetaData(dir, "?list=true");
		} catch (ParseException e) {
			return null;
		}

		JSONArray items = (JSONArray) res.get("contents");
		@SuppressWarnings("rawtypes")
		Iterator it = items.iterator();
		String[] tmp = new String[items.size()];
		int i = 0;

		while (it.hasNext())
		{
			JSONObject file = (JSONObject) it.next();
			tmp[i] = file.get("path").toString();		
			i++;
		}

		return tmp;
	}

	@Override
	public synchronized boolean mkdir(String dir) throws RemoteException {
		OAuthRequest request = new OAuthRequest(Verb.POST, CREATE_FOLDER_URL + dir);
		service.signRequest(token, request);
		Response response = request.send();
		if (response.getCode() == 403){
			System.out.println("ERRO");
			return false;
		}
		return true;
	}

	@Override
	public synchronized boolean rmdir(String dir) throws RemoteException {
		OAuthRequest request = new OAuthRequest(Verb.POST, REMOVE_URL + dir);
		service.signRequest(token, request);
		Response response = request.send();
		if (response.getCode() == 404){
			System.out.println("ERRO");
			return false;
		}
		return true;
	}

	@Override
	public synchronized boolean rm(String dir) throws RemoteException {
		OAuthRequest request = new OAuthRequest(Verb.POST, REMOVE_URL + dir);
		service.signRequest(token, request);
		Response response = request.send();
		if (response.getCode() == 404){
			System.out.println("ERRO");
			return false;
		}
		return true;
	}

	@Override
	public FileInfo getAttr(String path) throws RemoteException, InfoNotFoundException{
		JSONObject res;
		try {
			res = this.getMetaData(path, "?list=false");
		} catch (ParseException e) {
			return null;
		}
		
		String[] tmp = path.split("/");
		String name = tmp[tmp.length - 1];
		Long length = Long.parseLong(res.get("bytes").toString());
		SimpleDateFormat dParser = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
		Date modified = null;
		
		try {
			modified = dParser.parse(res.get("modified").toString());
		} catch (java.text.ParseException e) {
			System.err.println(e.getMessage());
		}
		boolean isFile = !Boolean.parseBoolean(res.get("is_dir").toString());
		return new FileInfo(name, length, modified, isFile);
	}

	@Override
	public synchronized boolean pasteFile(byte[] f, String toPath)
			throws RemoteException, IOException {
		return false;
	}

	@Override
	public byte[] copyFile(String fromPath)
			throws IOException {
		return null;
	}

	private JSONObject getMetaData(String path, String params) throws ParseException{
		// Obter listagem do directorio raiz
		String req = METADATA_URL + "/" + path + params;
		OAuthRequest request = new OAuthRequest(Verb.GET, req);
		service.signRequest(token, request);
		Response response = request.send();

		if (response.getCode() != 200)
			throw new RuntimeException("Metadata response code:" + response.getCode());

		return (JSONObject) parser.parse(response.getBody());

	}

	public static void main( String[] args) throws Exception
	{
		try {
			OAuthService service = new ServiceBuilder().provider(DropBoxApi.class).apiKey(API_KEY)
					.apiSecret(API_SECRET).scope(SCOPE).build();
			Scanner in = new Scanner(System.in);
			Token requestToken = service.getRequestToken();
			System.out.println("Tem de obter autorizacao para a aplicacao continuar acedendo ao link:");
			System.out.println(AUTHORIZE_URL + requestToken.getToken());
			System.out.println("E carregar em enter quando der autorizacao");
			System.out.print(">>");
			Verifier verifier = new Verifier(in.nextLine());
			verifier = new Verifier(requestToken.getSecret());
			Token accessToken = service.getAccessToken(requestToken, verifier);				
			IFileServer server = new ProxyDropbox(service, accessToken);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}


		//				if( args.length != 3 && args.length != 2){
		//					System.out.println("Use: java trab1.FileServer serverName contactServerURL userName");
		//					return;
		//				}
		//		
		//				try { // start rmiregistry
		//					LocateRegistry.createRegistry( 1099);
		//				} catch( RemoteException e) { 
		//					// if not start it
		//					// do nothing - already started with rmiregistry
		//				}
		//		
		//				String serverName = args[0];	
		//				String ip = InetAddress.getLocalHost().getHostAddress().toString();
		//				String contactServerUrl = "";
		//				String userName = "";
		//				int flag = 0;
		//		
		//				if (args.length == 3)
		//				{
		//					contactServerUrl = args[1];
		//					userName = args[2];
		//					flag++;
		//				}
		//				else
		//				{
		//					userName = args[1];
		//		
		//					// Call multicast client to get contact server ip
		//		
		//					int port = 5000;
		//					String group = "225.4.5.6";
		//		
		//					try{
		//						MulticastSocket s = new MulticastSocket(port);
		//						s.joinGroup(InetAddress.getByName(group));
		//		
		//						byte buf[] = new byte[1024];
		//						DatagramPacket pack = new DatagramPacket(buf, buf.length);
		//						s.setSoTimeout(2000); 
		//						s.receive(pack);
		//		
		//						contactServerUrl = new String(pack.getData(), 0, pack.getLength());			
		//		
		//						s.leaveGroup(InetAddress.getByName(group));
		//						s.close();
		//						flag++;
		//					} 
		//					catch(Exception e)
		//					{
		//						System.out.println("Erro ao receber o endereco do Contact Server por Multicast.");
		//						System.exit(0);
		//					}
		//				}
		//		
		//				try
		//				{
		//					IFileServer server = new FileServer(serverName, contactServerUrl, userName, ip);
		//					Naming.rebind( "/" + serverName + "@" + userName, server);
		//					flag++;
		//				}
		//				catch(Exception e)
		//				{
		//					System.out.println("Erro ao criar FileServer");
		//					System.exit(0);
		//				}
		//		
		//				if(flag == 2)
		//				{
		//					register(serverName, contactServerUrl, userName, ip);
		//					System.out.println("FileServer RMI running in " + ip + " ...");
		//				}
		//			}

	}
}
