package trab1;

import java.io.IOException;

public interface IFileServerWS 
{
	
	/**
	 * Metodo criado para a listagem de uma directoria.
	 * @param server
	 * @param user
	 * @param dir
	 * @param currentUser
	 * @return
	 * @throws InfoNotFoundException
	 */
	public String[] dir(String dir) throws InfoNotFoundException;
	
	/**
	 * Metodo criado para auxiliar a criacao de uma nova directoria.
	 * @param dir
	 * @return
	 */
	public boolean mkdir(String dir);
	
	/**
	 * Metodo criado para a eliminacao de uma directoria.
	 * @param dir
	 * @return
	 */
	public boolean rmdir(String dir);
	
	/**
	 * Metodo criado para eliminacao de um ficheiro.
	 * @param dir
	 * @return
	 */
	public boolean rm(String dir);
	
	/**
	 * Metodo que retorna a informacao sobre um ficheiro ou directoria.
	 * @return - FileInfo
	 * @throws InfoNotFoundException 
	 */
	public FileInfo getAttr(String path) throws InfoNotFoundException; 
	
	/**
	 * Metodo que cria um ficheiro dado um array de bytes que o representa.
	 * @param f
	 * @param toPath
	 * @return
	 * @throws IOException
	 */
	public boolean pasteFile(byte[] f, String toPath) throws IOException;
	
	/**
	 * Metodo que devolve um ficheiro em forma de array de bytes.
	 * @param fromPath
	 * @return
	 * @throws IOException
	 */
	public byte[] copyFile(String fromPath) throws IOException;
	

}
