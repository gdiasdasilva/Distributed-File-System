package trab1;

import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Classe base do cliente
 * @author nmp
 */
public class FileClient
{
	String contactServerURL;
	String username;
	IContactServer cs;
	
	protected FileClient( String url, String username) throws Exception {
		this.contactServerURL = url;
		this.username = username;	
		cs = (IContactServer) Naming.lookup("//" + contactServerURL + "/trabalhoSD");
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
			System.out.println("Nao foi possivel adicionar a permissao.");
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
			System.out.println("Nao foi possivel remover a permissao.");
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
			return cs.dir(server, user, dir);
		}
		catch (Exception e)
		{
			System.out.println("Nao foi possivel listar a directoria.");
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
		//TODO: completar
		return false;
	}

	/**
	 * Remove a directoria dir no servidor server@user
	 * (ou no sistema de ficheiros do cliente caso server == null).
	 * Devolve false em caso de erro.
	 * NOTA: não deve lançar excepcao. 
	 */
	protected boolean rmdir( String server, String user, String dir) {
		System.err.println( "exec: mkdir " + dir + " no servidor " + server + "@" + user);
		//TODO: completar
		return false;
	}

	/**
	 * Remove o ficheiro path no servidor server@user.
	 * (ou no sistema de ficheiros do cliente caso server == null).
	 * Devolve false em caso de erro.
	 * NOTA: não deve lançar excepcao. 
	 */
	protected boolean rm( String server, String user, String path) {
		System.err.println( "exec: rm " + path + " no servidor " + server + "@" + user);
		//TODO: completar
		return false;
	}

	/**
	 * Devolve informacao sobre o ficheiro/directoria path no servidor server@user.
	 * (ou no sistema de ficheiros do cliente caso server == null).
	 * Devolve false em caso de erro.
	 * NOTA: não deve lançar excepcao. 
	 */
	protected FileInfo getAttr( String server, String user, String path) {
		System.err.println( "exec: getattr " + path +  " no servidor " + server + "@" + user);
		//TODO: completar
		return null;
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
		//TODO: completar
		return false;
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
			} else if( cmd[0].equalsIgnoreCase("addPermission")) {
				String server = cmd[1];
				String user = cmd[2];
				
				boolean b = addPermission( server, user);
				
				if( b)
					System.out.println( "success");
				else
					System.out.println( "error");
			} else if( cmd[0].equalsIgnoreCase("remPermission")) {
				String server = cmd[1];
				String user = cmd[2];
				
				boolean b = remPermission( server, user);
				
				if( b)
					System.out.println( "success");
				else
					System.out.println( "error");
			} else if( cmd[0].equalsIgnoreCase("ls")) {
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
			} else if( cmd[0].equalsIgnoreCase("mkdir")) {
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
			} else if( cmd[0].equalsIgnoreCase("rmdir")) {
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
			} else if( cmd[0].equalsIgnoreCase("rm")) {
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
			} else if( cmd[0].equalsIgnoreCase("getattr")) {
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
			} else if( cmd[0].equalsIgnoreCase("cp")) {
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
			} else if( cmd[0].equalsIgnoreCase("help")) {
				System.out.println("servers - lista URLs dos servidores a que tem acesso");
				System.out.println("addPermission server user - adiciona user a lista de utilizadores com permissoes para aceder a server");
				System.out.println("remPermission server user - remove user da lista de utilizadores com permissoes para aceder a server");
				System.out.println("ls server@user:dir - lista ficheiros/directorias presentes na directoria dir (. e .. tem o significado habitual), caso existam ficheiros com o mesmo nome devem ser apresentados como nome@server;");
				System.out.println("mkdir server@user:dir - cria a directoria dir no servidor server@user");
				System.out.println("rmdir server@user:dir - remove a directoria dir no servidor server@user");
				System.out.println("cp path1 path2 - copia o ficheiro path1 para path2; quando path representa um ficheiro num servidor deve ter a forma server@user:path, quando representa um ficheiro local deve ter a forma path");
				System.out.println("rm path - remove o ficheiro path");
				System.out.println("getattr path - apresenta informação sobre o ficheiro/directoria path, incluindo: nome, boolean indicando se é ficheiro, data da criação, data da última modificacao");
			} else if( cmd[0].equalsIgnoreCase("exit"))
				break;

		}
	}
	
	public static void main( String[] args) throws Exception {
		if( args.length != 2) {
			System.out.println("Use: java trab1.FileClient URL nome_utilizador");
			return;
		}
		try {
			new FileClient( args[0], args[1]).doit();
		} catch (IOException e) {
			System.err.println("Error:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
