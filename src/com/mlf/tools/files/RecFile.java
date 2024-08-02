package com.mlf.tools.files;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Funciones para leer/escribir archivos de tipo header + records
 * o simplemente secuencias de records
 */
public class RecFile
{
	/**
	 * Crear archivo vac�o
	 * @param fileName Ruta del archivo
	 * @return true/false
	 */
	public static boolean Create(String fileName)
	{
		try
		{
			ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(fileName, false));
			salida.close();
			return true;
		}
		catch(FileNotFoundException e1){ e1.printStackTrace(); }
		catch(IOException e1){ e1.printStackTrace(); }
		return false;
	}

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
		ObjectInputStream entrada = null;
		int count = 0;
		try
		{
			entrada = new ObjectInputStream(new FileInputStream(fileName));
			while(true)
			{
				entrada.readObject();
				++count;
			}
		}
		catch(EOFException eof)
		{
			if(entrada != null)
			{
				try
				{
					entrada.close();
					if((count > 0) && hasHeader)
					{
						--count;
					}
					return count;
				}
				catch(IOException e){ e.printStackTrace(); }
			}
		}
		catch(FileNotFoundException e){ e.printStackTrace(); }
		catch(IOException e){ e.printStackTrace(); }
		catch (ClassNotFoundException e){ e.printStackTrace(); }		
		return 0;
	}

	/**
	 * Lee el header del archivo
	 * @param <T> Tipo de registro
	 * @param fileName Nombre del archivo
	 * @return Header le�do
	 */
	public static <T> T ReadHeader(String fileName)
	{
		return ReadRecord(fileName, 0, false);
	}

	/**
	 * Lee un registro en la posici�n indicada
	 * @param <T> Tipo de registro
	 * @param fileName Nombre del archivo
	 * @return Registro le�do
	 */
	public static <T> T ReadRecord(String fileName)
	{
		return ReadRecord(fileName, 0, false);
	}

	/**
	 * Lee un registro en la posici�n indicada
	 * @param <T> Tipo de registro
	 * @param fileName Nombre del archivo
	 * @param index �ndice
	 * @return Registro le�do
	 */
	public static <T> T ReadRecord(String fileName, int index)
	{
		return ReadRecord(fileName, index, false);
	}

	/**
	 * Lee un registro en la posici�n indicada
	 * @param <T> Tipo de registro
	 * @param fileName Nombre del archivo
	 * @param index �ndice
	 * @param hasHeader Indica si tiene header
	 * @return Registro le�do
	 */
	public static <T> T ReadRecord(String fileName, int index, boolean hasHeader)
    {
		ObjectInputStream entrada = null;
		try
		{
			entrada = new ObjectInputStream(new FileInputStream(fileName));
			if(hasHeader)
			{
				entrada.readObject();
			}
			int i = 0;
			while(true)
			{
				@SuppressWarnings("unchecked")
				T record = (T)entrada.readObject();
				if(i == index)
				{
					entrada.close();
					return record;
				}
				++i;
			}
		}
		catch(EOFException eof)
		{
			if(entrada != null)
			{
				try
				{
					entrada.close();
				}
				catch(IOException e){ e.printStackTrace(); }
			}
		}
		catch(FileNotFoundException e){ e.printStackTrace(); }
		catch(IOException e){ e.printStackTrace(); }
		catch (ClassNotFoundException e){ e.printStackTrace(); }		
		return null;
	}

	/**
	 * Lee todos los registros de un archivo
	 * @param <T> Tipo de registro
	 * @param fileName Nombre del archivo
	 * @return Registros le�dos
	 */
	public static <T> ArrayList<T> ReadRecords(String fileName)
	{
		return ReadRecords(fileName, false);
	}

	/**
	 * Lee todos los registros de un archivo
	 * @param <T> Tipo de registro
	 * @param fileName Nombre del archivo
	 * @param hasHeader Indica si tiene header
	 * @return Registros le�dos
	 */
	public static <T> ArrayList<T> ReadRecords(String fileName, boolean hasHeader)
    {
    	ArrayList<T> arrRet = new ArrayList<T>();
		ObjectInputStream entrada = null;
		try
		{
			entrada = new ObjectInputStream(new FileInputStream(fileName));
			if(hasHeader)
			{
				entrada.readObject();
			}
			while(true)
			{
				@SuppressWarnings("unchecked")
				T record = (T)entrada.readObject();
				arrRet.add(record);
			}
		}
		catch(EOFException eof)
		{
			if(entrada != null)
			{
				try
				{
					entrada.close();
					return arrRet;
				}
				catch(IOException e){ e.printStackTrace(); }
			}
		}
		catch(FileNotFoundException e){ e.printStackTrace(); }
		catch(IOException e){ e.printStackTrace(); }
		catch (ClassNotFoundException e){ e.printStackTrace(); }		
		return null;
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
    	Object header = null;
    	ArrayList<Object> records = null;
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
    	Object header = null;
    	ArrayList<Object> records = null;
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
     * @param <T> Tipo de dato
     * @param fileName Nombre del archivo
     * @param header header
     * @return true si se pudo eliminar
     */
	public static <T> boolean WriteHeader(String fileName, T header)
	{
		String fileNameTmp = fileName + ".tmp";
	    ObjectOutputStream salida = null;
		ObjectInputStream entrada = null;
		try
		{
			entrada = new ObjectInputStream(new FileInputStream(fileName));
			salida = new ObjectOutputStream(new FileOutputStream(fileNameTmp, false));
			entrada.readObject();
			salida.writeObject(header);
			while(true)
			{
				salida.writeObject(entrada.readObject());
			}
		}
		catch(EOFException eof)
		{
			if(salida != null)
			{
				try
				{
					entrada.close();
					salida.close();
					FileTools.Delete(fileName);
					return FileTools.Rename(fileNameTmp, fileName);
				}
				catch(IOException e){ e.printStackTrace(); }
			}
		}
		catch(FileNotFoundException e){ e.printStackTrace(); }
		catch(IOException e){ e.printStackTrace(); }
		catch(ClassNotFoundException e){ e.printStackTrace(); }
		return false;
	}

    /**
     * Escribir el archivo completo
     * @param <T> Tipo dato del registro
     * @param fileName Nombre del archivo
     * @param record Registro
     * @return true si se pudo escribir
     */
    public static <T> boolean WriteFile(String fileName, T record)
    {
    	return WriteFile(fileName, null, record);
    }
    
    /**
     * Escribir el archivo completo
     * @param <H> Tipo dato del header
     * @param <T> Tipo dato del registro
     * @param fileName Nombre del archivo
     * @param header Header
     * @param record Registro
     * @return true si se pudo escribir
     */
    public static <H, T> boolean WriteFile(String fileName, H header, T record)
    {
	    ObjectOutputStream salida = null;
		try
		{
			salida = new ObjectOutputStream(new FileOutputStream(fileName, false));
			if(header != null)
			{
				salida.writeObject(header);
			}
			salida.writeObject(record);
			salida.close();
			return true;
		}
		catch(FileNotFoundException e){ e.printStackTrace(); }
		catch(IOException e){ e.printStackTrace(); }
		return false;
	}
	
    /**
     * Escribir el archivo completo
     * @param <T> Tipo dato del registro
     * @param fileName Nombre del archivo
     * @param records Registros
     * @return true si se pudo escribir
     */
    public static <T> boolean WriteFile(String fileName, ArrayList<T> records)
    {
    	return WriteFile(fileName, null, records);
    }

    /**
     * Escribir el archivo completo
     * @param <H> Tipo dato del header
     * @param <T> Tipo dato del registro
     * @param fileName Nombre del archivo
     * @param header Header
     * @param records Registros
     * @return true si se pudo escribir
     */
    public static <H, T> boolean WriteFile(String fileName, H header, ArrayList<T> records)
    {
	    ObjectOutputStream salida = null;
		try
		{
			salida = new ObjectOutputStream(new FileOutputStream(fileName, false));
			if(header != null)
			{
				salida.writeObject(header);
			}
			for(T record : records)
			{
				salida.writeObject(record);
			}
			salida.close();
			return true;
		}
		catch(FileNotFoundException e){ e.printStackTrace(); }
		catch(IOException e){ e.printStackTrace(); }
		return false;
	}

    /**
     * Escribir registros a un archivo
     * @param <H> Tipo dato del header
     * @param <T> Tipo dato del registro
     * @param fileName Nombre del archivo
     * @param records Registros
     * @return true si se pudo escribir
     */
    public static <H, T> boolean WriteRecords(String fileName, ArrayList<T> records)
    {
    	return WriteRecords(fileName, records, false);
    }
    
    /**
     * Escribir registros a un archivo
     * @param <H> Tipo dato del header
     * @param <T> Tipo dato del registro
     * @param fileName Nombre del archivo
     * @param records Registros
     * @param hasHeader Indica si tiene header
     * @return true si se pudo escribir
     */
    public static <H, T> boolean WriteRecords(String fileName, ArrayList<T> records, boolean hasHeader)
    {
    	Object header = null;
    	if(hasHeader)
    	{
    		header = ReadHeader(fileName);
    	}
	    ObjectOutputStream salida = null;
		try
		{
			salida = new ObjectOutputStream(new FileOutputStream(fileName, false));
			if(hasHeader && (header != null))
			{
				salida.writeObject(header);
			}
			for(T record : records)
			{
				salida.writeObject(record);
			}
			salida.close();
			return true;
		}
		catch(EOFException eof)
		{
			if(salida != null)
			{
				try
				{
					salida.close();
				}
				catch(IOException e){ e.printStackTrace(); }
			}
		}
		catch(FileNotFoundException e){ e.printStackTrace(); }
		catch(IOException e){ e.printStackTrace(); }
		return false;
	}
    
    /**
	 * Escribe un registro
	 * @param <T> Tipo de dato
     * @param fileName Nombre del archivo
     * @param record Registro a escribir
     * @return true si se pudo escribir
	 */
    public static <T> boolean WriteRecords(String fileName, T record)
    {
    	return WriteRecords(fileName, record, false);
    }

    /**
     * Escribe los records del archivo manteniendo el header si tiene
     * @param <T> Tipo de dato
     * @param fileName Nombre del archivo
     * @param record Registro a escribir
     * @param hasHeader Indica si tiene header
     * @return true si se pudo escribir
     */
    public static <T> boolean WriteRecords(String fileName, T record, boolean hasHeader)
    {
    	Object header = null;
    	if(hasHeader)
    	{
    		header = ReadHeader(fileName);
    	}
	    ObjectOutputStream salida = null;
		try
		{
			salida = new ObjectOutputStream(new FileOutputStream(fileName, false));
			if(hasHeader && (header != null))
			{
				salida.writeObject(header);
			}
			salida.writeObject(record);
			salida.close();
			return true;
		}
		catch(EOFException eof)
		{
			if(salida != null)
			{
				try
				{
					salida.close();
				}
				catch(IOException e){ e.printStackTrace(); }
			}
		}
		catch(FileNotFoundException e){ e.printStackTrace(); }
		catch(IOException e){ e.printStackTrace(); }
		return false;
	}
    
    /**
     * Actualiza registro en el archivo
     * @param <T> Tipo de dato
     * @param fileName Nombre del archivo
     * @param record Registro
     * @param index �ndice del registro
     * @param hasHeader Indica si tiene header
     * @return true si se pudo actualizar
     */
    public static <T> boolean UpdateRecord(String fileName, T record, int index, boolean hasHeader)
    {
    	ArrayList<T> arrRecords = ReadRecords(fileName, hasHeader);
    	if(arrRecords == null)
    	{
    		return false;
    	}
    	Object header = null;
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
     * @param <T> Tipo de dato
     * @param fileName Nombre del archivo
     * @param record Registro
     * @param index �ndice del registro
     * @return true si se pudo actualizar
     */
    public static <T> boolean UpdateRecord(String fileName, T record, int index)
    {
    	return UpdateRecord(fileName, record, index, false);
    }

    /**
     * Insertar registro en el archivo
     * @param <T> Tipo de dato
     * @param fileName Nombre de archivo
     * @param record Registro
     * @param index �ndice del registro
     * @return true si se pudo insertar
     */
    public static <T> boolean InsertRecord(String fileName, T record, int index)
    {
    	return InsertRecord(fileName, record, index, false);
	}

    /**
     * Insertar registro en el archivo
     * @param <T> Tipo de dato
     * @param fileName Nombre de archivo
     * @param record Registro
     * @param index �ndice del registro
     * @param hasHeader Indica si tiene header
     * @return true si se pudo insertar
     */
    public static <T> boolean InsertRecord(String fileName, T record, int index, boolean hasHeader)
    {
    	Object header = null;
    	if(hasHeader)
    	{
    		header = ReadHeader(fileName);
    		if(header == null)
    		{
    			return false;
    		}
    	}
    	ArrayList<T> arrRecords = ReadRecords(fileName, hasHeader);
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
     * @param <T> Tipo de dato
     * @param fileName Nombre de arhivo
     * @param record Registro
     * @return true si se pudo agregar
     */
    public static <T> boolean AddRecord(String fileName, T record)
    {
    	// Si no existe se crea con ese �nico record
    	if(!FileTools.Exists(fileName))
    	{
    		return WriteRecords(fileName, record);
    	}
    	Object header = ReadHeader(fileName);
    	// Si est� vac�o se crea con ese �nico record
    	if(header == null)
    	{
    		return WriteRecords(fileName, record);
    	}
    	ArrayList<T> arrRecords = ReadRecords(fileName, true);
    	// Si solo ten�a header o un solo record
    	if(arrRecords == null)
    	{
    		arrRecords = new ArrayList<T>();
    	}
		arrRecords.add(record);
		return WriteFile(fileName, header, arrRecords);
	}

    /**
     * Agreagar registros al final del archivo
     * @param <T> Tipo de dato
     * @param fileName Nombre de arhivo
     * @param records Registros
     * @return true si se pudo agregar
     */
    public static <T> boolean AddRecords(String fileName, ArrayList<T> records)
    {
    	// Si no existe se crea con esos records
    	if(!FileTools.Exists(fileName))
    	{
    		return WriteRecords(fileName, records);
    	}
    	Object header = ReadHeader(fileName);
    	// Si est� vac�o se crea con ese �nico record
    	if(header == null)
    	{
    		return WriteRecords(fileName, records);
    	}    	
    	ArrayList<T> arrRecords = ReadRecords(fileName, true);
    	// Si solo ten�a header o un solo record
    	if(arrRecords == null)
    	{
    		arrRecords = new ArrayList<T>();
    	}    	
		arrRecords.addAll(records);
		return WriteFile(fileName, header, arrRecords);
	}
}
