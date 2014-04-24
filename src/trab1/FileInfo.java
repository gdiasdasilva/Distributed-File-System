package trab1;

/**
 * SD 13/14 - Trabalho pratico 1
 * Gonçalo Dias da Silva: 41831
 * Joao Francisco Pinto: 41887
 */

import java.util.*;

public class FileInfo implements java.io.Serializable
{
	private static final long serialVersionUID = -4498079336259690561L;

	public String name;
	public long length;
	public Date modified;
	public boolean isFile;
	
	FileInfo(){
		
	}
	
	public FileInfo( String name, long length, Date modified, boolean isFile) {
		this.name = name;
		this.length = length;
		this.modified = modified;
		this.isFile = isFile;
	}
	
	public String toString() {
		return "Name : " + name + "\nLength: " + length + "\nData modified: " + modified + "\nisFile : " + isFile; 
	}
}
