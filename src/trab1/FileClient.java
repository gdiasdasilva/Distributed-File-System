package trab1;

/**
 * SD 13/14 - Trabalho pratico 1
 * Gonçalo Dias da Silva: 41831
 * Joao Francisco Pinto: 41887
 */

import java.io.*;
import java.util.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import ws.*;

/**
 * Classe base do cliente
 * @author nmp
 */
public class FileClient
{
	String contactServerURL;
	String username;
	IContactServer cs;
	IFileServer fs;
	IProxyRest pr;
	private Map<String, Date> filesListLocal;
	private Map<String, Date> filesListRemote;
	private static String basePath = ".";
	private List<String> dirListArray;

	protected FileClient( String url, String username) throws Exception {
		this.contactServerURL = url;
		this.username = username;	
		cs = (IContactServer) Naming.lookup("//" + contactServerURL + "/trabalhoSD");
		filesListLocal = new HashMap<String, Date>();
		filesListRemote = new HashMap<String, Date>();
		dirListArray = new ArrayList<String>();
	}

	/**
	 * Devolve um array com os servidores a que o utilizador tem acesso.
	 * @throws RemoteException 
	 */
	protected String[] servers() throws RemoteException {
		System.err.println( "exec: servers");
		String[] list = null;
		list = cs.listServers(username);
		return list;
	}

	/**
	 * Adiciona o utilizador user à lista de utilizadores com autorização para aceder ao servidor
	 * server.
	 * Devolve false em caso de erro.
	 * NOTA: não deve lançar excepcao. 
	 */
	protected boolean addPermission( String server, String user) {
		System.err.println( "exec: addPermission in server " + server + " for user " + user);
		try {
			return cs.addPermission(server, user, username);
		} catch (RemoteException e) {
			System.out.println("Não foi possivel adicionar a permissão");
			return false;
		}
	}

	/**
	 * Remove o utilizador user da lista de utilizadores com autorização para aceder ao servidor
	 * server.
	 * Devolve false em caso de erro.
	 * NOTA: não deve lançar excepcao. 
	 */
	protected boolean remPermission( String server, String user) {
		System.err.println( "exec: remPermission in server " + server + " for user " + user);
		try {
			return cs.remPermission(server, user, username);
		} catch (RemoteException e) {
			System.out.println("Não foi possivel remover a permissão");
			return false;
		}
	}

	/**
	 * Devolve um array com os ficheiros/directoria na directoria dir no servidor server@user
	 * (ou no sistema de ficheiros do cliente caso server == null).
	 * Devolve null em caso de erro.
	 * NOTA: não deve lançar excepcao. 
	 */
	protected String[] dir( String server, String user, String dir) {
		System.err.println( "exec: ls " + dir + " no servidor " + server + "@" + user);

		try
		{
			String address = cs.serverAddress(server,username);			
			if(address != null)
			{
				String[] tmp = address.split(":");
				if(tmp[0].equals("http"))
				{
					//WS
					FileServerWSService service = new FileServerWSService( new URL( address + "/FileServer?wsdl"), new QName("http://trab1/", "FileServerWSService"));
					ws.FileServerWS serverWS = service.getFileServerWSPort();
					List<String> list = serverWS.dir(dir);					
					return list.toArray(new String[list.size()]);
				}
				else
				{
					try
					{
						fs = (IFileServer) Naming.lookup("//" + address + "/" + server + "@" + user);				
						return fs.dir(dir);
					}
					catch(Exception e)
					{
						pr = (IProxyRest) Naming.lookup("//" + address + "/" + server + "@" + user);

						String[] dirList = pr.dir(dir);

						for(int i = 0; i < dirList.length;i++)
						{
							int slashes = dirList[i].split("/").length;
							dirList[i] = dirList[i].split("/")[slashes-1];
						}

						return dirList;
					}
				}				
			}
			else{
				System.out.println("Url do servidor inválido");
				return null;
			}
		}
		catch(InfoNotFoundException e)
		{
			System.out.println("Directoria nao encontrada");
			return null;
		}
		catch(InfoNotFoundException_Exception e)
		{
			System.out.println("Directoria nao encontrada");
			return null;
		}
		catch (Exception e)
		{
			System.out.println("Problema no acesso ao servidor");
			return null;
		}
	}

	/**
	 * Cria a directoria dir no servidor server@user
	 * (ou no sistema de ficheiros do cliente caso server == null).
	 * Devolve false em caso de erro.
	 * NOTA: não deve lançar excepcao. 
	 */
	protected boolean mkdir( String server, String user, String dir) {
		System.err.println( "exec: mkdir " + dir + " no servidor " + server + "@" + user);

		try
		{
			String address = cs.serverAddress(server,username);
			if(address != null){

				String[] tmp = address.split(":");
				if(tmp[0].equals("http"))
				{
					//WS
					FileServerWSService service = new FileServerWSService( new URL( address + "/FileServer?wsdl"), new QName("http://trab1/", "FileServerWSService"));
					ws.FileServerWS serverWS = service.getFileServerWSPort();
					return serverWS.mkdir(dir);
				}
				else
				{
					try{
						fs = (IFileServer) Naming.lookup("//" + address + "/" + server + "@" + user);				
						return fs.mkdir(dir);
					}
					catch(Exception e)
					{
						pr = (IProxyRest) Naming.lookup("//" + address + "/" + server + "@" + user);				
						return pr.mkdir(dir);
					}					
				}
			}
			else{
				System.out.println("Url do servidor inválido");
				return false;
			}
		}
		catch (Exception e)
		{
			System.out.println("Problema no acesso ao servidor");
			return false;
		}
	}

	/**
	 * Remove a directoria dir no servidor server@user
	 * (ou no sistema de ficheiros do cliente caso server == null).
	 * Devolve false em caso de erro.
	 * NOTA: não deve lançar excepcao. 
	 */
	protected boolean rmdir( String server, String user, String dir) {
		System.err.println( "exec: rmdir " + dir + " no servidor " + server + "@" + user);

		try
		{
			String address = cs.serverAddress(server,username);
			if(address != null){
				String[] tmp = address.split(":");
				if(tmp[0].equals("http"))
				{
					//WS
					FileServerWSService service = new FileServerWSService( new URL( address + "/FileServer?wsdl"), new QName("http://trab1/", "FileServerWSService"));
					ws.FileServerWS serverWS = service.getFileServerWSPort();
					return serverWS.rmdir(dir);
				}
				else
				{
					try
					{
						fs = (IFileServer) Naming.lookup("//" + address + "/" + server + "@" + user);				
						return fs.rmdir(dir);
					}
					catch (Exception e)
					{
						pr = (IProxyRest) Naming.lookup("//" + address + "/" + server + "@" + user);				
						return pr.rmdir(dir);
					}
				}
			}
			else{
				System.out.println("Url do servidor inválido");
				return false;
			}
		}
		catch (Exception e)
		{
			System.out.println("Problema no acesso ao servidor");
			return false;
		}
	}

	/**
	 * Remove o ficheiro path no servidor server@user.
	 * (ou no sistema de ficheiros do cliente caso server == null).
	 * Devolve false em caso de erro.
	 * NOTA: não deve lançar excepcao. 
	 */
	protected boolean rm( String server, String user, String path) {
		System.err.println( "exec: rm " + path + " no servidor " + server + "@" + user);

		try
		{
			String address = cs.serverAddress(server,username);
			if(address != null){
				String[] tmp = address.split(":");
				if(tmp[0].equals("http"))
				{
					//WS
					FileServerWSService service = new FileServerWSService( new URL( address + "/FileServer?wsdl"), new QName("http://trab1/", "FileServerWSService"));
					ws.FileServerWS serverWS = service.getFileServerWSPort();
					return serverWS.rm(path);
				}
				else
				{
					try{
						fs = (IFileServer) Naming.lookup("//" + address + "/" + server + "@" + user);				
						return fs.rm(path);
					}
					catch(Exception e)
					{
						pr = (IProxyRest) Naming.lookup("//" + address + "/" + server + "@" + user);				
						return pr.rm(path);
					}
				}
			}
			else{
				System.out.println("Url do servidor inválido");
				return false;
			}
		}
		catch (Exception e)
		{
			System.out.println("Problema no acesso ao servidor");
			return false;
		}
	}

	/**
	 * Devolve informacao sobre o ficheiro/directoria path no servidor server@user.
	 * (ou no sistema de ficheiros do cliente caso server == null).
	 * Devolve false em caso de erro.
	 * NOTA: não deve lançar excepcao. 
	 */
	protected FileInfo getAttr( String server, String user, String path) {
		System.err.println( "exec: getattr " + path +  " no servidor " + server + "@" + user);

		try
		{
			String address = cs.serverAddress(server,username);
			if(address != null){
				String[] tmp = address.split(":");
				if(tmp[0].equals("http"))
				{
					//WS
					FileServerWSService service = new FileServerWSService( new URL( address + "/FileServer?wsdl"), new QName("http://trab1/", "FileServerWSService"));
					ws.FileServerWS serverWS = service.getFileServerWSPort();
					ws.FileInfo f = serverWS.getAttr(path);
					return new FileInfo(f.getName(), f.getLength(), f.getModified().toGregorianCalendar().getTime(), f.isIsFile());
				}
				else
				{
					try{
						fs = (IFileServer) Naming.lookup("//" + address + "/" + server + "@" + user);	
						return fs.getAttr(path);
					}
					catch (Exception e)
					{
						pr = (IProxyRest) Naming.lookup("//" + address + "/" + server + "@" + user);
						return pr.getAttr(path);
					}

				}
			}
			else{
				System.out.println("Url do servidor inválido");
				return null;
			}
		}
		catch(InfoNotFoundException e)
		{
			System.out.println("Directoria nao encontrada");
			return null;
		}
		catch(InfoNotFoundException_Exception e)
		{
			System.out.println("Directoria nao encontrada");
			return null;
		}
		catch (Exception e)
		{
			System.out.println("Problema no acesso ao servidor");
			return null;
		}
	}

	/**
	 * Copia ficheiro de fromPath no servidor fromServer@fromUser para o ficheiro 
	 * toPath no servidor toServer@toUser.
	 * (caso fromServer/toServer == local, corresponde ao sistema de ficheiros do cliente).
	 * Devolve false em caso de erro.
	 * NOTA: não deve lançar excepcao. 
	 */
	protected boolean cp( String fromServer, String fromUser, String fromPath,
			String toServer, String toUser, String toPath) {
		System.err.println( "exec: cp " + fromPath + " no servidor " + fromServer +"@" + fromUser + " para " +
				toPath + " no servidor " + toServer +"@" + toUser);
		try
		{
			String fromAddress=null;
			String toAddress=null;

			if(fromServer == null && fromUser == null){
				toAddress = cs.serverAddress(toServer, username);
			}
			else if(toServer == null && toUser == null)
				fromAddress = cs.serverAddress(fromServer, username);

			else if(fromServer != null && fromUser != null && toServer != null && toUser !=null){
				toAddress = cs.serverAddress(toServer, username);

				fromAddress = cs.serverAddress(fromServer, username);
			}
			if(fromAddress == null)
			{
				String[] tmpTo = toAddress.split(":");
				byte[] tmp = this.copyFile(fromPath);

				if(tmpTo[0].equals("http"))
				{
					FileServerWSService serviceTo = new FileServerWSService( new URL( toAddress + "/FileServer?wsdl"), new QName("http://trab1/", "FileServerWSService"));
					ws.FileServerWS serverWSTo = serviceTo.getFileServerWSPort();
					return serverWSTo.pasteFile(tmp, toPath);
				}
				else
				{
					try
					{
						IFileServer fs2 = (IFileServer) Naming.lookup("//" + toAddress + "/" + toServer + "@" + toUser);
						return fs2.pasteFile(tmp, toPath);
					}
					catch(Exception e2)
					{
						IProxyRest pr2 = (IProxyRest) Naming.lookup("//" + toAddress + "/" + toServer + "@" + toUser);
						return pr2.pasteFile(tmp, toPath);
					}
				}					
			}
			else if(toAddress == null)
			{
				String[] tmpFrom = fromAddress.split(":");
				byte[] tmp = null;

				if(tmpFrom[0].equals("http"))
				{
					//WS
					FileServerWSService serviceFrom = new FileServerWSService( new URL( fromAddress + "/FileServer?wsdl"), new QName("http://trab1/", "FileServerWSService"));
					ws.FileServerWS serverWSFrom = serviceFrom.getFileServerWSPort();
					tmp = serverWSFrom.copyFile(fromPath);
				}
				else
				{
					try{
						fs = (IFileServer) Naming.lookup("//" + fromAddress + "/" + fromServer + "@" + fromUser);
						tmp = fs.copyFile(fromPath);
					}
					catch(Exception e1)
					{
						pr = (IProxyRest) Naming.lookup("//" + fromAddress + "/" + fromServer + "@" + fromUser);
						tmp = pr.copyFile(fromPath);
					}
				}

				this.pasteFile(tmp, toPath);
				return true;
			}

			if(fromAddress != null && toAddress != null)
			{
				byte[] bf;
				String[] tmpFrom = fromAddress.split(":");
				if(tmpFrom[0].equals("http"))
				{
					//WS
					FileServerWSService serviceFrom = new FileServerWSService( new URL( fromAddress + "/FileServer?wsdl"), new QName("http://trab1/", "FileServerWSService"));
					ws.FileServerWS serverWSFrom = serviceFrom.getFileServerWSPort();
					bf = serverWSFrom.copyFile(fromPath);
				}
				else
				{
					try{
						fs = (IFileServer) Naming.lookup("//" + fromAddress + "/" + fromServer + "@" + fromUser);
						bf = fs.copyFile(fromPath);
					}
					catch(Exception e)
					{
						pr = (IProxyRest) Naming.lookup("//" + fromAddress + "/" + fromServer + "@" + fromUser);
						bf = pr.copyFile(fromPath);
					}

				}

				String[] tmpTo = toAddress.split(":");

				if(tmpTo[0].equals("http"))
				{
					FileServerWSService serviceTo = new FileServerWSService( new URL( toAddress + "/FileServer?wsdl"), new QName("http://trab1/", "FileServerWSService"));
					ws.FileServerWS serverWSTo = serviceTo.getFileServerWSPort();
					return serverWSTo.pasteFile(bf, toPath);
				}
				else
				{	
					try
					{
						IFileServer fs2 = (IFileServer) Naming.lookup("//" + toAddress + "/" + toServer + "@" + toUser);
						return fs2.pasteFile(bf, toPath);
					}
					catch(Exception e)
					{

						IProxyRest pr2 = (IProxyRest) Naming.lookup("//" + toAddress + "/" + toServer + "@" + toUser);
						return pr2.pasteFile(bf, toPath);
					}
				}
			}
			else
			{
				System.out.println("Erro ao executar o cp");
				return false;
			}
		}
		catch(IOException e)
		{
			System.out.println("Directoria nao encontrada");
			return false;
		}
		catch(IOException_Exception e)
		{
			System.out.println("Directoria nao encontrada");
			return false;
		}
		catch (Exception e)
		{
			System.out.println("Problema no acesso ao servidor");
			return false;
		}
	}

	/**
	 * 
	 * @param fromPath
	 * @return
	 */
	protected byte[] copyFile(String fromPath){
		try {
			File f = new File(basePath, fromPath);
			InputStream input = new FileInputStream(f);
			byte[] buffer = new byte[(int) f.length()];
			input.read(buffer);
			input.close();
			return buffer;
		} catch (Exception e) {
			System.out.println("Erro ao copiar o ficheiro. Nao encontrado");
			return null;
		}
	}

	/**
	 * 
	 * @param f
	 * @param toPath
	 * @return
	 */
	protected boolean pasteFile(byte[] f, String toPath){
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

	protected boolean firstSync(String dir_local, String server, String user, String dir)
	{	
		File f = new File(new File("."), dir_local);

		if(f.isDirectory())
		{			
			if(f.list().length == 0) // pode dar problemas com o DS_Store que fica oculto. Atencao!
			{ //directoria local vazia
				try
				{	
					String[] dirListTmp = this.dir(server, user, dir);

					List<String> dirListArray = new ArrayList<String>();

					for(int x = 0; x < dirListTmp.length; x++)
					{
						if(!dirListTmp[x].equals(".DS_Store"))
							dirListArray.add(dirListTmp[x]);
					}

					String[] tmp = dirListArray.toArray(new String[dirListArray.size()]);

					for(int i = 0; i < tmp.length; i++)
					{
						byte[] buffer = pr.copyFile(dir + "/" + tmp[i]);
						File file = new File(".", dir_local + "/" + tmp[i]);

						if(pr.getAttr(dir + "/" + tmp[i]).isFile)
						{
							OutputStream out = new FileOutputStream(file);
							out.write(buffer);
							out.close();
							System.out.println("Sincronizado ficheiro: " + file.getName());

							filesListLocal.put(dir_local + "/" + tmp[i], this.getFileInfo(dir_local + "/" + tmp[i]).modified);
							filesListRemote.put(dir + "/" + tmp[i], pr.getAttr(dir + "/" + tmp[i]).modified);
						}
						else
						{
							file.mkdir();
							filesListLocal.put(dir_local + "/" + tmp[i], this.getFileInfo(dir_local + "/" + tmp[i]).modified);
							filesListRemote.put(dir + "/" + tmp[i], pr.getAttr(dir + "/" + tmp[i]).modified);
							this.firstSync(dir_local + "/" + file.getName(), server, user, dir + "/"+ file.getName());
							System.out.println("Sincronizada directoria: " + file.getName());
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					return false;	
				}
			}
			else
			{ // directoria remota vazia
				String[] tmp = f.list();

				try
				{
					pr = (IProxyRest) Naming.lookup("//" + cs.serverAddress(server, user) + "/" + server + "@" + user);
				} 
				catch (Exception e) 
				{
					System.out.println("Problema ao encontrar proxy dropbox.");
				}

				for(int i = 0; i < f.list().length; i++)
				{
					try
					{
						if(new File(".", dir_local + "/" + tmp[i]).isDirectory())
						{
							pr.mkdir(dir + "/" + tmp[i]);
							filesListRemote.put(dir + "/" + tmp[i], pr.getAttr(dir + "/" + tmp[i]).modified);
							filesListLocal.put(dir_local + "/" + tmp[i], this.getFileInfo(dir_local + "/" + tmp[i]).modified);
							this.firstSync(dir_local + "/" + tmp[i], server, user, dir + "/" + tmp[i]);
							System.out.println("Sincronizada directoria: " + tmp[i]);
						}
						else
						{
							@SuppressWarnings("resource")
							RandomAccessFile file = new RandomAccessFile(dir_local + "/" + tmp[i], "r");
							byte[] b = new byte[(int)file.length()];
							file.read(b);
							pr.pasteFile(b, dir + "/" + tmp[i]);
							System.out.println("Sincronizado ficheiro: " + tmp[i]);

							filesListRemote.put(dir + "/" + tmp[i], pr.getAttr(dir + "/" + tmp[i]).modified);
							filesListLocal.put(dir_local + "/" + tmp[i], this.getFileInfo(dir_local + "/" + tmp[i]).modified);
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		else if(f.isFile())
		{
			System.out.println(dir_local + "nao e uma directoria.");
			return false;
		}
		else
		{
			f.mkdir();
			this.firstSync(dir_local, server, user, dir);
		}	

		return true;
	}

	@SuppressWarnings("resource")
	protected boolean sync(String dir_local, String server, String user, String dir)
	{
		if(filesListLocal.isEmpty() && filesListRemote.isEmpty())
			firstSync(dir_local, server, user, dir);
		else
		{
			System.out.println("mapas nao vazios");
			try
			{
				pr = (IProxyRest) Naming.lookup("//" + cs.serverAddress(server, user) + "/" + server + "@" + user);
			} 
			catch (Exception e) 
			{
				System.out.println("Problema ao encontrar proxy dropbox.");
				return false;
			}

			File f = new File(new File("."), dir_local);
			String[] dirListTmp = f.list();
//			List<String> dirListArray = new ArrayList<String>();

			for(int x = 0; x < dirListTmp.length; x++)
			{
				if(!dirListTmp[x].equals(".DS_Store"))
					dirListArray.add(dirListTmp[x]);
			}

			String[] dirList = dirListArray.toArray(new String[dirListArray.size()]);
			for(int i = 0; i<dirList.length; i++){
				System.out.println(dirList[i]);
			}
			String[] dropList = null;

			Iterator<String> it = filesListLocal.keySet().iterator();
			List<String> keyList = new ArrayList<String>();

			while(it.hasNext())
			{	
				String key = it.next();
				boolean hasFile = false;
//				System.out.println("KEY: " + key);
				for(int i = 0; i < dirList.length; i++)
				{
					String[] stringSplitLocal = key.split("/");
					if(filesListLocal.containsKey(key))
					{
//						System.out.println("DIR LIST: "+ dirList[i]);
//						System.out.println("KEY SPLIT: "+ stringSplitLocal[stringSplitLocal.length-1]);
						if(dirList[i].contains(stringSplitLocal[stringSplitLocal.length-1]))
						{
							hasFile = true;
						}	
					}
				}
				if(!hasFile)
				{
					keyList.add(key);
					try
					{
						String[] tmp = filesListRemote.keySet().toArray(new String[filesListRemote.size()]);

						for(int j = 0; j < tmp.length; j++)
						{
							String[] stringSplit = key.split("/");
							if(tmp[j].contains(stringSplit[stringSplit.length-1]))
							{
								pr.rm(tmp[j]);
								filesListRemote.remove(tmp[j]);
							}
						}
					} 
					catch (Exception e) 
					{
						System.out.println("Erro ao apagar o ficheiro na sincronizacao da dropbox");
						e.printStackTrace();
					}
				}
			}

			String[] keys = keyList.toArray(new String[keyList.size()]);

			for(int k = 0; k < keys.length; k++)
			{
				filesListLocal.remove(keys[k]);
			}

			try
			{
				dropList = pr.dir(dir);
			} 
			catch (Exception e) 
			{
				System.out.println("Erro ao lista directoria no metodo sync. A dir era " + dir);
			} 
			System.out.println("Lista Local: " +filesListLocal.keySet().toString());
			System.out.println("Lista Remoto: " +filesListRemote.keySet().toString());
			

			for(int i = 0; i < dirList.length;i++)
			{
				if(filesListLocal.containsKey(dir_local + "/" + dirList[i]))
				{
					try
					{
						if(filesListLocal.get(dir_local + "/" + dirList[i]).before(this.getFileInfo(dir_local + "/" + dirList[i]).modified))
						{
							// ficheiro foi modificado
							if(this.getFileInfo(dir_local + "/" + dirList[i]).isFile)
							{
								RandomAccessFile file = new RandomAccessFile(dir_local + "/" + dirList[i], "r");
								byte[] b = new byte[(int)file.length()];
								file.read(b);
								pr.pasteFile(b, dir + "/" + dirList[i]);
								System.out.println("Sincronizado ficheiro: " + dirList[i]);
								filesListLocal.put(dir_local + "/" + dirList[i], this.getFileInfo(dir_local + "/" + dirList[i]).modified);
								filesListRemote.put(dir + "/" + dirList[i], pr.getAttr(dir + "/" + dirList[i]).modified);								
							}
							else
							{	// cria directoria na dropbox
								pr.mkdir(dir + "/" + dirList[i]);
								System.out.println("Sincronizada directoria: " + dirList[i]);
								filesListLocal.put(dir_local + "/" + dirList[i], this.getFileInfo(dir_local + "/" + dirList[i]).modified);
								filesListRemote.put(dir + "/" + dirList[i], pr.getAttr(dir + "/" + dirList[i]).modified);
								sync(dir_local + "/" + dirList[i], server, user, dir + "/" + dirList[i]);
							}
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
						return false;
					} 	
				}
				else
				{	
					// ficheiro novo na pasta. nao esta no mapa
					try
					{
						System.out.println("Ficheiro novo nao esta no mapa");
						System.out.println("DIR LOCAL: " + dir_local);
						System.out.println(dirList[i]);
						if(this.getFileInfo(dir_local + "/" + dirList[i]).isFile)
						{
							RandomAccessFile fi = new RandomAccessFile(dir_local + "/" + dirList[i], "r");
							byte[] b = new byte[(int)fi.length()];
							fi.read(b);
							pr.pasteFile(b, dir + "/" + dirList[i]);
							System.out.println("Sincronizado ficheiro: " + dirList[i]);
							filesListLocal.put(dir_local + "/" + dirList[i], this.getFileInfo(dir_local + "/" + dirList[i]).modified);
							filesListRemote.put(dir + "/" + dirList[i], pr.getAttr(dir + "/" + dirList[i]).modified);
						}
						else
						{	// cria directoria na dropbox
							System.out.println("Ficheiro novo e sou pasta");
							pr.mkdir(dir + "/" + dirList[i]);
							System.out.println("Sincronizada directoria: " + dirList[i]);
							filesListLocal.put(dir_local + "/" + dirList[i], this.getFileInfo(dir_local + "/" + dirList[i]).modified);
							filesListRemote.put(dir + "/" + dirList[i], pr.getAttr(dir + "/" + dirList[i]).modified);
							sync(dir_local + "/" + dirList[i], server, user, dir + "/" + dirList[i]);
						}
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
						return false;
					}
				}
			}	

//			for(int j = 0; j < dropList.length; j++)
//			{
//				if(filesListRemote.containsKey(dropList[j].substring(1)))
//				{
//					try 
//					{
//						if(filesListRemote.get(dropList[j].substring(1)).before(pr.getAttr(dropList[j].substring(1)).modified))
//						{
//							byte[] buffer = pr.copyFile(dropList[j].substring(1));
//							File file = new File(dir_local, pr.getAttr(dropList[j].substring(1)).name);
//
//							if(pr.getAttr(dropList[j].substring(1)).isFile)
//							{
//								OutputStream out = new FileOutputStream(file);
//								out.write(buffer);
//								out.close();
//								System.out.println("Sincronizado ficheiro: " + file.getName());
//								filesListRemote.put(dropList[j].substring(1), pr.getAttr(dropList[j].substring(1)).modified);
//								filesListLocal.put(dir_local + "/" + file.getName(), this.getFileInfo(dir_local + "/" + file.getName()).modified);
//							}
//							else
//							{
//								file.mkdir();
//								filesListRemote.put(dropList[j].substring(1), pr.getAttr(dropList[j].substring(1)).modified);
//								filesListLocal.put(dir_local + "/" + file.getName(), this.getFileInfo(dir_local + "/" + file.getName()).modified);
//								System.out.println("Sincronizada directoria: " + file.getName());
//								this.sync(dir_local + "/" + file.getName(), server, user, dir + "/" + file.getName());
//							}
//						}
//					}
//					catch (Exception e) 
//					{
//						e.printStackTrace();
//					} 
//				}
//				else
//				{
//					try 
//					{
//						byte[] buffer = pr.copyFile(dropList[j].substring(1));
//						File file = new File(dir_local, pr.getAttr(dropList[j].substring(1)).name);
//
//						if(pr.getAttr(dropList[j].substring(1)).isFile)
//						{
//							OutputStream out = new FileOutputStream(file);
//							out.write(buffer);
//							out.close();
//							System.out.println("Sincronizado ficheiro: " + file.getName());
//							filesListRemote.put(dropList[j].substring(1), pr.getAttr(dropList[j].substring(1)).modified);							
//							filesListLocal.put(dir_local + "/" + file.getName(), this.getFileInfo(dir_local + "/" + file.getName()).modified);
//						}
//						else
//						{
//							file.mkdir();
//							filesListRemote.put(dropList[j].substring(1), pr.getAttr(dropList[j].substring(1)).modified);							
//							filesListLocal.put(dir_local + "/" + file.getName(), this.getFileInfo(dir_local + "/" + file.getName()).modified);
//							System.out.println("Sincronizada directoria: " + file.getName());
//							this.sync(dir_local + "/" + file.getName(), server, user, dir + "/" + file.getName());
//						}
//					} 
//					catch (Exception e)
//					{
//						e.printStackTrace();
//					}
//				}
//			}
		}

		return true;
	}

	protected FileInfo getFileInfo(String path) throws RemoteException, InfoNotFoundException {
		File f = new File(new File("."), path);
		if( f.exists()) {
			return new FileInfo( path, f.length(), new Date(f.lastModified()), f.isFile());
		} else
			return null;
	}

	protected void doit() throws IOException {
		BufferedReader reader = new BufferedReader( new InputStreamReader( System.in));

		for( ; ; ) {
			String line = reader.readLine();
			if( line == null)
				break;
			String[] cmd = line.split(" ");
			if( cmd[0].equalsIgnoreCase("servers")) {
				String[] s = servers();
				if( s == null)
					System.out.println( "error");
				else {
					System.out.println( s.length);
					for( int i = 0; i < s.length; i++)
						System.out.println( s[i]);
				}
			} 
			else if( cmd[0].equalsIgnoreCase("addPermission")) {
				String server = cmd[1];
				String user = cmd[2];

				boolean b = addPermission( server, user);

				if( b)
					System.out.println( "success");
				else
					System.out.println( "error");
			} 
			else if( cmd[0].equalsIgnoreCase("remPermission")) {
				String server = cmd[1];
				String user = cmd[2];

				boolean b = remPermission( server, user);

				if( b)
					System.out.println( "success");
				else
					System.out.println( "error");
			} 
			else if( cmd[0].equalsIgnoreCase("ls")) {
				String[] dirserver = cmd[1].split(":");
				String[] serveruser = dirserver[0].split("@");

				String server = dirserver.length == 1 ? null : serveruser[0];
				String user = dirserver.length == 1 || serveruser.length == 1 ? null : serveruser[1];
				String dir = dirserver.length == 1 ? dirserver[0] : dirserver[1];

				String[] res = dir( server, user, dir);
				if( res != null) {
					System.out.println( res.length);
					for( int i = 0; i < res.length; i++)
						System.out.println( res[i]);
				} else
					System.out.println( "error");
			} 
			else if( cmd[0].equalsIgnoreCase("mkdir")) {
				String[] dirserver = cmd[1].split(":");
				String[] serveruser = dirserver[0].split("@");

				String server = dirserver.length == 1 ? null : serveruser[0];
				String user = dirserver.length == 1 || serveruser.length == 1 ? null : serveruser[1];
				String dir = dirserver.length == 1 ? dirserver[0] : dirserver[1];

				boolean b = mkdir( server, user, dir);
				if( b)
					System.out.println( "success");
				else
					System.out.println( "error");
			} 
			else if( cmd[0].equalsIgnoreCase("rmdir")) {
				String[] dirserver = cmd[1].split(":");
				String[] serveruser = dirserver[0].split("@");

				String server = dirserver.length == 1 ? null : serveruser[0];
				String user = dirserver.length == 1 || serveruser.length == 1 ? null : serveruser[1];
				String dir = dirserver.length == 1 ? dirserver[0] : dirserver[1];

				boolean b = rmdir( server, user, dir);
				if( b)
					System.out.println( "success");
				else
					System.out.println( "error");
			} 
			else if( cmd[0].equalsIgnoreCase("rm")) {
				String[] dirserver = cmd[1].split(":");
				String[] serveruser = dirserver[0].split("@");

				String server = dirserver.length == 1 ? null : serveruser[0];
				String user = dirserver.length == 1 || serveruser.length == 1 ? null : serveruser[1];
				String path = dirserver.length == 1 ? dirserver[0] : dirserver[1];

				boolean b = rm( server, user, path);
				if( b)
					System.out.println( "success");
				else
					System.out.println( "error");	
			} 
			else if( cmd[0].equalsIgnoreCase("getattr")) {
				String[] dirserver = cmd[1].split(":");
				String[] serveruser = dirserver[0].split("@");

				String server = dirserver.length == 1 ? null : serveruser[0];
				String user = dirserver.length == 1 || serveruser.length == 1 ? null : serveruser[1];
				String path = dirserver.length == 1 ? dirserver[0] : dirserver[1];

				FileInfo info = getAttr( server, user, path);
				if( info != null) {
					System.out.println( info);
					System.out.println( "success");
				} else
					System.out.println( "error");
			} 
			else if( cmd[0].equalsIgnoreCase("cp")) {
				String[] dirserver1 = cmd[1].split(":");
				String[] serveruser1 = dirserver1[0].split("@");

				String fromServer = dirserver1.length == 1 ? null : serveruser1[0];
				String fromUser = dirserver1.length == 1 || serveruser1.length == 1 ? null : serveruser1[1];
				String fromPath = dirserver1.length == 1 ? dirserver1[0] : dirserver1[1];

				String[] dirserver2 = cmd[2].split(":");
				String[] serveruser2 = dirserver2[0].split("@");

				String toServer = dirserver2.length == 1 ? null : serveruser2[0];
				String toUser = dirserver2.length == 1 || serveruser2.length == 1 ? null : serveruser2[1];
				String toPath = dirserver2.length == 1 ? dirserver2[0] : dirserver2[1];

				boolean b = cp( fromServer, fromUser, fromPath, toServer, toUser, toPath);
				if( b)
					System.out.println( "success");
				else
					System.out.println( "error");
			} 
			else if( cmd[0].equalsIgnoreCase("help")) {
				System.out.println("servers - lista URLs dos servidores a que tem acesso");
				System.out.println("addPermission server user - adiciona user a lista de utilizadores com permissoes para aceder a server");
				System.out.println("remPermission server user - remove user da lista de utilizadores com permissoes para aceder a server");
				System.out.println("ls server@user:dir - lista ficheiros/directorias presentes na directoria dir (. e .. tem o significado habitual), caso existam ficheiros com o mesmo nome devem ser apresentados como nome@server;");
				System.out.println("mkdir server@user:dir - cria a directoria dir no servidor server@user");
				System.out.println("rmdir server@user:dir - remove a directoria dir no servidor server@user");
				System.out.println("cp path1 path2 - copia o ficheiro path1 para path2; quando path representa um ficheiro num servidor deve ter a forma server@user:path, quando representa um ficheiro local deve ter a forma path");
				System.out.println("rm path - remove o ficheiro path");
				System.out.println("getattr path - apresenta informação sobre o ficheiro/directoria path, incluindo: nome, boolean indicando se é ficheiro, data da criação, data da última modificacao");
			} 	
			else if( cmd[0].equalsIgnoreCase("sync")) {
				String dir_local = cmd[1];
				String[] dirserver = cmd[2].split(":");
				String[] serveruser = dirserver[0].split("@");

				String server = dirserver.length == 1 ? null : serveruser[0];
				String user = dirserver.length == 1 || serveruser.length == 1 ? null : serveruser[1];
				String dir = dirserver.length == 1 ? dirserver[0] : dirserver[1];

				boolean b = sync( dir_local, server, user, dir);
				if( b)
					System.out.println( "success");
				else
					System.out.println( "error");

			} 
			else if( cmd[0].equalsIgnoreCase("exit"))
				break;
		}
	}

	public static void main( String[] args) throws Exception {
		if( args.length != 2 && args.length != 1) {
			System.out.println("Use: java trab1.FileClient URL nome_utilizador");
			return;
		}

		if(args.length == 1)
		{
			int port = 5000;
			String group = "225.4.5.6";

			try
			{   // Call multicast client to get contact server ip
				MulticastSocket s = new MulticastSocket(port);
				s.joinGroup(InetAddress.getByName(group));

				byte buf[] = new byte[1024];
				DatagramPacket pack = new DatagramPacket(buf, buf.length);
				s.setSoTimeout(2000); 
				s.receive(pack);

				String contactServerUrl = new String(pack.getData(), 0, pack.getLength());			

				s.leaveGroup(InetAddress.getByName(group));
				s.close();
				FileClient fc = new FileClient(contactServerUrl, args[0]);
				fc.doit();
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				System.out.println("Erro no comando");
			}
			catch(Exception e)
			{
				System.out.println("Erro ao receber o endereco do Contact Server por Multicast.");
				System.exit(0);
			}
		}
		else
			new FileClient(args[0], args[1]).doit();
	}

}
