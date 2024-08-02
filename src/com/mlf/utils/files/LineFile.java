package com.mlf.utils.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Funciones para leer/escribir archivos de tipo header + records
 * o simplemente secuencias de records
 */
public class LineFile
{
	/**
	 * Obtener cantidad de registros del archivo
	 * @param fileName Nombre del archivo
	 * @return Cantidad de registros
	 */
	public static int GetRecordsCount(String fileName)
	{
		return GetRecordsCount(fileName, false);
	}

	/**
	 * Obtener cantidad de registros del archivo
	 * @param fileName Nombre del archivo
	 * @param hasHeader Indica si el archivo tiene header
	 * @return Cantidad de registros
	 */
	public static int GetRecordsCount(String fileName, boolean hasHeader)
	{
		ArrayList<String> lines = ReadRecords(fileName, hasHeader);
		return lines.size();
	}

	/**
	 * Lee el header del archivo
	 * @param fileName Nombre del archivo
	 * @return Header le�do
	 */
	public static String ReadHeader(String fileName)
	{
		return ReadRecord(fileName, 0, false);
	}

	/**
	 * Lee un registro en la posici�n indicada
	 * @param fileName Nombre del archivo
	 * @return Registro le�do
	 */
	public static String ReadRecord(String fileName)
	{
		return ReadRecord(fileName, 0, false);
	}

	/**
	 * Lee un registro en la posici�n indicada
	 * @param fileName Nombre del archivo
	 * @param index �ndice
	 * @return Registro le�do
	 */
	public static String ReadRecord(String fileName, int index)
	{
		return ReadRecord(fileName, index, false);
	}

	/**
	 * Lee un registro en la posici�n indicada
	 * @param fileName Nombre del archivo
	 * @param index �ndice
	 * @param hasHeader Indica si tiene header
	 * @return Registro le�do
	 */
	public static String ReadRecord(String fileName, int index, boolean hasHeader)
    {
		if(!FileTools.Exists(fileName))
		{
			return null;
		}
        try
		{
        	BufferedReader br = new BufferedReader(new FileReader(fileName));
        	if(hasHeader)
			{
        		if(br.readLine() == null)
        		{
        	        br.close();
        			return null;
        		}
			}
        	int i = 0;
    		String line;
        	while(i < index)
        	{
        		line = br.readLine();
        		// Si llegu� al final antes del �ndice salgo
        		if(line == null)
        		{
        	        br.close();
        	        return null;
        		}
        		if(!line.trim().isEmpty())
        		{
        			++i;
        		}
        	}
        	// Ac� estoy en la l�nea pedida
        	line = br.readLine();
        	if(line == null)
        	{
    	        br.close();
        		return null;
        	}
	        br.close();
        	return line.trim();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}		
        return null;
	}

	/**
	 * Lee todos los registros de un archivo
	 * @param fileName Nombre del archivo
	 * @return Registros le�dos
	 */
	public static ArrayList<String> ReadRecords(String fileName)
	{
		return ReadRecords(fileName, false);
	}

	/**
	 * Lee todos los registros de un archivo
	 * @param fileName Nombre del archivo
	 * @param hasHeader Indica si tiene header
	 * @return Registros le�dos
	 */
	public static ArrayList<String> ReadRecords(String fileName, boolean hasHeader)
    {
		ArrayList<String> lines = new ArrayList<String>();
		String line;
		if(!FileTools.Exists(fileName))
		{
			return lines;
		}
        try
		{
        	BufferedReader reader = new BufferedReader(new FileReader(fileName));
        	if(hasHeader)
			{
        		reader.readLine();
			}
	        while((line = reader.readLine()) != null)
	        {
	        	line = line.trim();
	        	if(!line.isEmpty())
	        	{
		        	lines.add(line);
	        	}
	        }
	        reader.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}		
        return lines;
	}

	/**
     * Eliminar el �ltimo registro del archivo
     * @param fileName Nombre del archivo
     * @return true si se puedo remover
	 */
    public static boolean RemoveLastRecord(String fileName)
    {
    	return RemoveLastRecord(fileName, false);
    }

    /**
     * Eliminar el �ltimo registro del archivo
     * @param fileName Nombre del archivo
     * @param hasHeader Indica si tiene header
     * @return true si se puedo remover
     */
    public static boolean RemoveLastRecord(String fileName, boolean hasHeader)
    {
    	String header = null;
    	ArrayList<String> records = null;
    	if(hasHeader)
    	{
    		header = ReadHeader(fileName);
    	}
    	records = ReadRecords(fileName, hasHeader);
    	if((records != null) && (records.size() > 0))
    	{
    		records.remove(records.size() - 1);
    		return WriteFile(fileName, header, records);
    	}
    	return false;
    }

    /**
     * Elimina el registro en el �ndice dado
     * @param fileName Nombre del archivo
     * @param index �ndice dado
     * @return true si se pudo eliminar
     */
    public static boolean RemoveRecord(String fileName, int index)
    {
    	return RemoveRecord(fileName, index, false);
    }

    /**
     * Elimina el registro en el �ndice dado
     * @param fileName Nombre del archivo
     * @param index �ndice dado
     * @param hasHeader Indica si tiene header
     * @return true si se pudo eliminar
     */
    public static boolean RemoveRecord(String fileName, int index, boolean hasHeader)
    {
    	String header = null;
    	ArrayList<String> records = null;
    	if(hasHeader)
    	{
    		header = ReadHeader(fileName);
    	}
    	records = ReadRecords(fileName, hasHeader);
    	if((records != null) && (index < records.size()))
    	{
    		records.remove(index);
    		return WriteFile(fileName, header, records);
    	}
    	return false;
    }

    /**
     * Escribe el header del archivo
     * @param fileName Nombre del archivo
     * @param header header
     * @return true si se pudo eliminar
     */
	public static boolean WriteHeader(String fileName, String header)
	{
		String fileNameTmp = fileName + ".tmp";
		String line;
		try
		{
	    	BufferedReader br = new BufferedReader(new FileReader(fileName));
	    	FileWriter fw = new FileWriter(fileNameTmp, false);
	    	fw.append(header);
			fw.append(System.getProperty("line.separator"));
	    	while((line = br.readLine()) != null)
	    	{
	    		line = line.trim();
	    		if(!line.isEmpty())
				{
	    			fw.append(line);
	    			fw.append(System.getProperty("line.separator"));
				}
	    	}
	    	br.close();
	    	fw.flush();
	    	fw.close();
	    	
	    	FileTools.Delete(fileName);
	    	FileTools.Rename(fileNameTmp, fileName);
		}
		catch(IOException e){ e.printStackTrace(); }
		return false;
	}

    /**
     * Escribir el archivo completo
     * @param fileName Nombre del archivo
     * @param record Registro
     * @return true si se pudo escribir
     */
    public static boolean WriteFile(String fileName, String record)
    {
    	return WriteFile(fileName, null, record);
    }
    
    /**
     * Escribir el archivo completo
     * @param fileName Nombre del archivo
     * @param header Header
     * @param record Registro
     * @return true si se pudo escribir
     */
    public static boolean WriteFile(String fileName, String header, String record)
    {
		try
		{
			FileWriter fw = new FileWriter(fileName, false);
			if((header != null) && !header.isEmpty())
			{
				fw.append(header);
				fw.append(System.getProperty("line.separator"));
			}
			if((record != null) && !record.isEmpty())
			{
				fw.append(record);	
				fw.append(System.getProperty("line.separator"));
			}
			fw.flush();
			fw.close();
			return true;
		}
		catch(IOException e){ e.printStackTrace(); }
		return false;
	}
	
    /**
     * Escribir el archivo completo
     * @param fileName Nombre del archivo
     * @param records Registros
     * @return true si se pudo escribir
     */
    public static boolean WriteFile(String fileName, ArrayList<String> records)
    {
    	return WriteFile(fileName, null, records);
    }

    /**
     * Escribir el archivo completo
     * @param fileName Nombre del archivo
     * @param header Header
     * @param records Registros
     * @return true si se pudo escribir
     */
    public static boolean WriteFile(String fileName, String header, ArrayList<String> records)
    {
        try
		{
			FileWriter fw = new FileWriter(fileName, false);
			if((header != null) && !header.isEmpty())
			{
				fw.append(header);
				fw.append(System.getProperty("line.separator"));
			}
			for(String record : records)
			{
				if((record != null) && !record.isEmpty())
				{
					fw.append(record);	
					fw.append(System.getProperty("line.separator"));
				}
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
     * Escribir registros a un archivo
     * @param fileName Nombre del archivo
     * @param records Registros
     * @return true si se pudo escribir
     */
    public static boolean WriteRecords(String fileName, ArrayList<String> records)
    {
    	return WriteRecords(fileName, records, false);
    }
    
    /**
     * Escribir registros a un archivo
     * @param fileName Nombre del archivo
     * @param records Registros
     * @param hasHeader Indica si tiene header
     * @return true si se pudo escribir
     */
    public static boolean WriteRecords(String fileName, ArrayList<String> records, boolean hasHeader)
    {
    	String header = null;
    	if(hasHeader)
    	{
    		header = ReadHeader(fileName);
    	}
		try
		{
			FileWriter fw = new FileWriter(fileName, false);
			if(hasHeader && (header != null) && !header.isEmpty())
			{
				fw.append(header);
				fw.append(System.getProperty("line.separator"));
			}
			for(String record : records)
			{
				if((record != null) && !record.isEmpty())
				{
					fw.append(record);	
					fw.append(System.getProperty("line.separator"));
				}
			}
			fw.flush();
			fw.close();
			return true;
		}
		catch(IOException e){ e.printStackTrace(); }
		return false;
	}
    
    /**
	 * Escribe un registro
     * @param fileName Nombre del archivo
     * @param record Registro a escribir
     * @return true si se pudo escribir
	 */
    public static boolean WriteRecords(String fileName, String record)
    {
    	return WriteRecords(fileName, record, false);
    }

    /**
     * Escribe los records del archivo manteniendo el header si tiene
     * @param fileName Nombre del archivo
     * @param record Registro a escribir
     * @param hasHeader Indica si tiene header
     * @return true si se pudo escribir
     */
    public static boolean WriteRecords(String fileName, String record, boolean hasHeader)
    {
    	String header = null;
    	if(hasHeader)
    	{
    		header = ReadHeader(fileName);
    	}
        try
		{
			FileWriter fw = new FileWriter(fileName, false);
			if(hasHeader && (header != null))
			{
				fw.append(header);
				fw.append(System.getProperty("line.separator"));
			}
			if((record != null) && !record.isEmpty())
			{
				fw.append(record);
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
     * Actualiza registro en el archivo
     * @param fileName Nombre del archivo
     * @param record Registro
     * @param index �ndice del registro
     * @param hasHeader Indica si tiene header
     * @return true si se pudo actualizar
     */
    public static boolean UpdateRecord(String fileName, String record, int index, boolean hasHeader)
    {
    	ArrayList<String> arrRecords = ReadRecords(fileName, hasHeader);
    	if(arrRecords == null)
    	{
    		return false;
    	}
    	String header = null;
    	if(hasHeader)
    	{
    		header = ReadHeader(fileName);
    		if(header == null)
    		{
    			return false;
    		}
    	}
    	if(index < arrRecords.size())
    	{
    		arrRecords.remove(index);
    		arrRecords.add(index, record);
    		return WriteFile(fileName, header, arrRecords);
    	}
		return false;
	}

    /**
     * Actualiza registro en el archivo
     * @param fileName Nombre del archivo
     * @param record Registro
     * @param index �ndice del registro
     * @return true si se pudo actualizar
     */
    public static boolean UpdateRecord(String fileName, String record, int index)
    {
    	return UpdateRecord(fileName, record, index, false);
    }

    /**
     * Insertar registro en el archivo
     * @param fileName Nombre de archivo
     * @param record Registro
     * @param index �ndice del registro
     * @return true si se pudo insertar
     */
    public static boolean InsertRecord(String fileName, String record, int index)
    {
    	return InsertRecord(fileName, record, index, false);
	}

    /**
     * Insertar registro en el archivo
     * @param fileName Nombre de archivo
     * @param record Registro
     * @param index �ndice del registro
     * @param hasHeader Indica si tiene header
     * @return true si se pudo insertar
     */
    public static boolean InsertRecord(String fileName, String record, int index, boolean hasHeader)
    {
    	String header = null;
    	if(hasHeader)
    	{
    		header = ReadHeader(fileName);
    		if(header == null)
    		{
    			return false;
    		}
    	}
    	ArrayList<String> arrRecords = ReadRecords(fileName, hasHeader);
    	if(arrRecords == null)
    	{
    		return false;
    	}
    	if(index == arrRecords.size())
    	{
    		arrRecords.add(record);
    		return WriteFile(fileName, header, arrRecords);
    	}
    	else if(index < arrRecords.size())
    	{
    		arrRecords.add(index, record);
    		return WriteFile(fileName, header, arrRecords);
    	}
		return false;
	}
    
    /**
     * Agreagar registro al final del archivo
     * @param fileName Nombre de arhivo
     * @param record Registro
     * @return true si se pudo agregar
     */
    public static boolean AddRecord(String fileName, String record)
    {
    	// Si no existe se crea con ese �nico record
    	if(!FileTools.Exists(fileName))
    	{
    		return WriteRecords(fileName, record);
    	}
    	String header = ReadHeader(fileName);
    	// Si est� vac�o se crea con ese �nico record
    	if(header == null)
    	{
    		return WriteRecords(fileName, record);
    	}
    	ArrayList<String> arrRecords = ReadRecords(fileName, true);
    	// Si solo ten�a header o un solo record
    	if(arrRecords == null)
    	{
    		arrRecords = new ArrayList<String>();
    	}
		arrRecords.add(record);
		return WriteFile(fileName, header, arrRecords);
	}

    /**
     * Agreagar registros al final del archivo
     * @param fileName Nombre de arhivo
     * @param records Registros
     * @return true si se pudo agregar
     */
    public static boolean AddRecords(String fileName, ArrayList<String> records)
    {
    	// Si no existe se crea con esos records
    	if(!FileTools.Exists(fileName))
    	{
    		return WriteRecords(fileName, records);
    	}
    	String header = ReadHeader(fileName);
    	// Si est� vac�o se crea con ese �nico record
    	if(header == null)
    	{
    		return WriteRecords(fileName, records);
    	}    	
    	ArrayList<String> arrRecords = ReadRecords(fileName, true);
    	// Si solo ten�a header o un solo record
    	if(arrRecords == null)
    	{
    		arrRecords = new ArrayList<String>();
    	}    	
		arrRecords.addAll(records);
		return WriteFile(fileName, header, arrRecords);
	}
}
