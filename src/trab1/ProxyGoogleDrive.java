package trab1;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class ProxyGoogleDrive extends UnicastRemoteObject implements IProxyRest{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OAuthService service;
	private Token token;
	private JSONParser parser;

	private static final String API_ID = "893421333980-gqr98eq20oqmg773ik9c2op4pfuil3l8.apps.googleusercontent.com";
	private static final String API_SECRET = "O1DhvfeBl-HVDsAhqq2_SoOX";
	private static final String SCOPE = "https://www.googleapis.com/auth/drive"; 
	private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
	private static final String GET_URL = "https://www.googleapis.com/drive/v2/files/";
	private static final String UPLOAD = "https://www.googleapis.com/upload/drive/v2/files?uploadType=multipart";



	protected ProxyGoogleDrive(OAuthService service, Token token) throws RemoteException 
	{
		this.service = service;
		this.token = token;
		parser = new JSONParser();
	}

	@Override
	public String[] dir(String dir) throws InfoNotFoundException,
	RemoteException {
		
		String id = null;
		String[] list_dir = null;

		String[] path = dir.split("/");
			
		if(dir.equals(""))
		{
			id = getId("root");
		}
		else
		{
			if(path.length == 1)
			{
				id = getId(path[0]);
			}
			else
			{
				id = getId(path[path.length-1]);
			}
			
		}
		
		OAuthRequest request = new OAuthRequest(Verb.GET, GET_URL + id + "/children");
		service.signRequest(token, request);
		Response response = request.send();
		
		if(response.getCode() != 200)
			throw new InfoNotFoundException("Directoria nao encontrada: " + dir);

		else{
			try {
				JSONObject jo = (JSONObject) parser.parse(response.getBody());
				JSONArray items = (JSONArray) jo.get("items");
				Iterator it = items.iterator();
				list_dir = new String[items.size()];
				int i = 0;
				while (it.hasNext())
				{
					JSONObject file = (JSONObject) it.next();
					String id_child = (String) file.get("id");
					OAuthRequest req = new OAuthRequest(Verb.GET, GET_URL + id_child);
					service.signRequest(token, req);
					Response res = req.send();
					JSONObject json = (JSONObject) parser.parse(res.getBody());
					list_dir[i] = json.get("title").toString();
					i++;
				}	
			}
			catch (Exception e) 
			{
				System.out.println("Erro no listar directoria");
			}
		}

		return list_dir;
	}

	private String getId(String dir){
		//TESTADO
		JSONObject res;
		try {
			res = getMetaData();	
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

		JSONArray items = (JSONArray) res.get("items");
		@SuppressWarnings("rawtypes")
		Iterator it = items.iterator();
		String id = null;
		while (it.hasNext())
		{
			JSONObject file = (JSONObject) it.next();
			if(file.get("title").equals(dir))
				id = (String) file.get("id");
		}
		return id;
	}

	private JSONObject getMetaData() throws ParseException{
		//TESTADO
		OAuthRequest request = new OAuthRequest(Verb.GET, GET_URL);
		service.signRequest(token, request);
		Response response = request.send();

		if (response.getCode() != 200)
			throw new RuntimeException("Metadata response code:" + response.getCode());
		return (JSONObject) parser.parse(response.getBody());
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

	@SuppressWarnings("unchecked")
	@Override
	public boolean mkdir(String dir) throws RemoteException {
		//TESTADO
		String[] path = dir.split("/");
		String name = path[path.length-1];
		String id = null;
		try{
			id = getId(path[path.length-2]);
		}
		catch(Exception e){
			id= "root";
		}	

		OAuthRequest request = new OAuthRequest(Verb.POST, GET_URL);
		service.signRequest(token, request);
		JSONObject tmp = new JSONObject();
		tmp.put("id", id);

		JSONArray array = new JSONArray();
		array.add(tmp);

		JSONObject js = new JSONObject();
		js.put("title", name);
		js.put("parents", array);
		js.put("mimeType", "application/vnd.google-apps.folder");

		request.addHeader("Content-type", "application/json");
		request.addHeader("Accept", "application/json");
		request.addPayload(js.toString());

		Response response = request.send();
		if(response.getCode() != 200)
			return false;

		else 
			return true;
	}

	@Override
	public boolean rmdir(String dir) throws RemoteException, InfoNotFoundException {
		//TESTADO
		if(!getAttr(dir).isFile)
		{
		String id = getId(dir);
		OAuthRequest request = new OAuthRequest(Verb.DELETE, GET_URL + id);
		service.signRequest(token, request);
		Response response = request.send();

		if(response.getCode() != 204)
			return false;
		else 
			return true;
		}
		else
			return false;
	}

	@Override
	public boolean rm(String dir) throws RemoteException, InfoNotFoundException {
		//TESTADO
		if(getAttr(dir).isFile)
		{
		String id = getId(dir);
		OAuthRequest request = new OAuthRequest(Verb.DELETE, GET_URL + id);
		service.signRequest(token, request);
		Response response = request.send();
		
		if(response.getCode() != 204)
			return false;
		else
			return true;
		}	
		else
			return false;
	}

	@Override
	public FileInfo getAttr(String path) throws RemoteException,
	InfoNotFoundException {
		//TESTADO
		String id = getId(path);
		OAuthRequest request = new OAuthRequest(Verb.GET, GET_URL + id);
		service.signRequest(token, request);
		Response response = request.send();

		String name = null;
		long length = 0;
		SimpleDateFormat dParser = new SimpleDateFormat("yyyy-MM-dd'T'h:m:ss.SSS'Z'", Locale.US);
		Date modified = null;
		boolean isFile = false;

		try {
			JSONObject res = (JSONObject) parser.parse(response.getBody());
			name = (String) res.get("title");
			modified = dParser.parse(res.get("modifiedDate").toString());
			if(res.get("mimeType").equals("application/vnd.google-apps.folder")){
				isFile = false;
				length = 0;
			}
			else{
				isFile = true;
				length = Long.parseLong(res.get("fileSize").toString());
			}

		} catch (Exception e) {
			System.out.println("ERRO getAttr");
			return null;		
		}	
		FileInfo f = new FileInfo(name, length, modified, isFile);
		return new FileInfo(name, length, modified, isFile);
	}

	@Override
	public void activeTest() throws RemoteException {}

	@Override
	public boolean copy(String string, String string2) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}


	@SuppressWarnings("unchecked")
	@Override
	public boolean pasteFile(byte[] f, String toPath) throws RemoteException {
		String[] path = toPath.split("/");
		String name = path[path.length-1];
		String id = null;
		try{
			id = getId(path[path.length-2]);
		}
		catch(Exception e){
			id= "root";
		}	

		OAuthRequest request = new OAuthRequest(Verb.POST, UPLOAD);
		service.signRequest(token, request);
		try{
			
			String boundary = "-------314159265358979323846";
		    String delimiter = "\r\n--" + boundary + "\r\n";
		    String close_delim = "\r\n--" + boundary + "--";
		    
		    String contentType = "application/octet-stream";
		
			JSONObject tmp = new JSONObject();
			tmp.put("id", id);

			JSONArray array = new JSONArray();
			array.add(tmp);

			JSONObject js = new JSONObject();
			js.put("title", name);
			js.put("parents", array);

			request.addHeader("Content-type", "multipart/related; boundary=" + "\"" + boundary + "\"");
			request.addHeader("Content-Length", String.valueOf(f.length));
			
			// boundary inicial aqui
			
			ByteArrayOutputStream ba = new ByteArrayOutputStream();		
			
			String multipartRequestBody =
			        delimiter +  "Content-Type: application/json\r\n\r\n" +
			        js.toString() +
			        delimiter + "Content-Type: " + contentType + "\r\n" + "\r\n" +
			        new String(f) +
			        close_delim;
			
			ba.write(multipartRequestBody.getBytes());
			request.addPayload(ba.toString());

			Response response = request.send();
					

			if(response.getCode() != 200)
				return false;
			else
				return true;
		} catch (Exception e) {
			System.out.println("ERRO pasteFile");
			return false;		
		}	
	}

	@Override
	public byte[] copyFile(String fromPath) throws RemoteException {
		
		String id = getId(fromPath);
		OAuthRequest request = new OAuthRequest(Verb.GET, GET_URL + id);
		service.signRequest(token, request);
		
		try {
			Response response = request.send();
			JSONObject res = (JSONObject) parser.parse(response.getBody());
			int tmp = Integer.parseInt((String) res.get("fileSize"));
			byte[] buffer = new byte[tmp];
			String fileUrl = res.get("downloadUrl").toString();
			OAuthRequest req = new OAuthRequest(Verb.GET, fileUrl);
			service.signRequest(token, req);
			response = req.send();
			DataInputStream dis = new DataInputStream(response.getStream());
			dis.readFully(buffer);

			return buffer;

		} catch (Exception e) {
			System.out.println("ERRO copyFile");
			e.printStackTrace();
			return null;		
		}
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
			OAuthService service = new ServiceBuilder().provider(Google2Api.class).apiKey(API_ID).
					apiSecret(API_SECRET).scope(SCOPE).build();
			Scanner in = new Scanner(System.in);
			String authorizationUrl = service.getAuthorizationUrl(null);
			// Obter Request token
			System.out.println("Tem de obter autorizacao para a aplicacao continuar acedendo ao link:");
			System.out.println(authorizationUrl);
			System.out.println("E introduzir o codigo fornecido aqui:");
			System.out.print(">>");
			Verifier verifier = new Verifier(in.nextLine());
			Token accessToken = service.getAccessToken(null, verifier);

			OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/drive/v2/files/root" );
			service.signRequest(accessToken, request);
			Response response = request.send();
			
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
