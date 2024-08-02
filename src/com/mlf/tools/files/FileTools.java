package com.mlf.tools.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Funciones de manejo de archivos
 * @author Mario
 */
public class FileTools
{
	/**
	 * Leer la lista de l�neas de un archivo
	 * @param is Flujo de entrada
	 * @return Contenido del archivo
	 */
	public static String ReadToString(InputStream is)
	{
		StringBuffer sb = new StringBuffer();
		int car;
        try
		{
	        while((car = is.read()) >= 0)
	        {
	        	sb.append((char) car);
	        }
	        is.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}		
        return sb.toString();
	}

	/**
	 * Leer la lista de l�neas de un archivo
	 * @param fileName Ruta del archivo
	 * @return Contenido del archivo
	 */
	public static String ReadToString(String fileName)
	{
		StringBuffer sb = new StringBuffer();
		int car;
		if(!Exists(fileName))
		{
			return "";
		}
        try
		{
        	BufferedReader br = new BufferedReader(new FileReader(fileName));
	        while((car = br.read()) >= 0)
	        {
	        	sb.append((char) car);
	        }
	        br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}		
        return sb.toString();
	}

	/**
	 * Escribir a un archivo
	 * @param fileName Ruta del archivo
	 * @param text Texto a escribir
	 * @return true si se puedo agregar la l�nea
	 */
	public static boolean WriteText(String fileName, String text)
	{
		return WriteText(fileName, text, false);
	}
	
	/**
	 * Escribir a un archivo
	 * @param fileName Ruta del archivo
	 * @param text Texto a escribir
	 * @param append Indica si agregar al final
	 * @return true si se puedo agregar la l�nea
	 */
	public static boolean WriteText(String fileName, String text, boolean append)
	{
        try
		{
			FileWriter fw = new FileWriter(fileName, append);
			fw.append(text);
			fw.flush();
			fw.close();
		}
        catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Leer la lista de l�neas de un archivo
	 * @param fileName Ruta del archivo
	 * @param excludeEmpty Indica si omitir la l�neas vac�as
	 * @param excludeFirst Indica si omitir la primer l�nea
	 * @return Lista de l�neas
	 */
	public static ArrayList<String> ReadLines(String fileName, boolean excludeEmpty, boolean excludeFirst)
	{
		ArrayList<String> lines = new ArrayList<String>();
		String line;
		if(!Exists(fileName))
		{
			return lines;
		}
        try
		{
        	BufferedReader br = new BufferedReader(new FileReader(fileName));
        	if(excludeFirst)
			{
        		br.readLine();
			}
	        while((line = br.readLine()) != null)
	        {
	        	line = line.trim();
	        	if(line.isEmpty() && excludeEmpty)
	        	{
	        		continue;
	        	}
	        	lines.add(line);
	        }
	        br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}		
        return lines;
	}

	/**
	 * Escribir a un archivo
	 * @param fileName Ruta del archivo
	 * @param lines Lista de l�neas a escribir
	 * @param append Indica si agregar al final
	 * @return true si se puedo agregar la l�nea
	 */
	public static boolean WriteLines(String fileName, ArrayList<String> lines, boolean append)
	{
        try
		{
			FileWriter fw = new FileWriter(fileName, append);
			for(String line : lines)
			{
				fw.append(line);
				fw.append(System.getProperty("line.separator"));
			}
			fw.flush();
			fw.close();
		}
        catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Chequea si el archivo existe
	 * @param fileName Ruta completa del archivo
	 * @return true/false
	 */
	public static boolean Exists(String fileName)
	{
		if(fileName == null || fileName.isEmpty())
		{
	        return false;
		}
		File file = new File(fileName);
		return (file.exists() && file.isFile());
	}
	
	/**
	 * Tama�o en bytes del archivo
	 * @param fileName Ruta completa del archivo
	 * @return Tama�o en bytes del archivo
	 */
	public static long Size(String fileName)
	{
		if(fileName == null || fileName.isEmpty())
		{
	        return 0;
		}
		File file = new File(fileName);
		return file.length();
	}

	/**
	 * Crear archivo
	 * @param fileName Ruta completa del archivo
	 * @return true/false
	 */
	public static boolean Create(String fileName)
	{
		File file = new File(fileName);
		if(file.exists() && file.isFile())
		{
			file.delete();
		}
		try
		{
			return file.createNewFile();
		}
		catch (IOException e){ e.printStackTrace(); }
		return false;
	}

	/**
	 * Eliminar archivo
	 * @param fileName Ruta completa del archivo
	 * @return true/false
	 */
	public static boolean Delete(String fileName)
	{
		if(fileName == null || fileName.isEmpty())
		{
	        return false;
		}
		File file = new File(fileName);
		return file.delete();
	}

	/**
	 * Renombrar archivo
	 * @param fileName Ruta completa del archivo
	 * @param newName Nueva ruta completa del archivo
	 * @return true/false
	 */
	public static boolean Rename(String fileName, String newName)
	{
		if(fileName == null || fileName.isEmpty() || newName == null || newName.isEmpty())
		{
	        return false;
		}
		File file = new File(fileName);
		File fileNew = new File(newName);
		return file.renameTo(fileNew);
	}

	/**
	 * Copiar archivo
	 * @param src Ruta completa del archivo
	 * @param dst Ruta completa del archivo de destino
	 * @return Bytes copiados
	 */
	public static int Copy(String src, String dst)
	{
		if(src == null || src.isEmpty() || dst == null || dst.isEmpty())
		{
	        return 0;
		}		
    	FileInputStream is = null;
    	FileOutputStream os = null;
        byte[] buffer = new byte[1024];
        int readed, total = 0;
	    try
	    {
	    	is = new FileInputStream(new File(src));
	    	os = new FileOutputStream(new File(dst));
	        while((readed = is.read(buffer)) > 0)
	        {
	            os.write(buffer, 0, readed);
	            total += readed;
	        }
			is.close();
			os.close();
	    }
	    catch(IOException e)
		{
			e.printStackTrace();
		}
	    return total;
	}
}
