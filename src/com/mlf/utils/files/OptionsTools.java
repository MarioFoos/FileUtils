package com.mlf.utils.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.mlf.utils.SimpleCrypto;

/**
 * Manejar un archivo de opciones de pare clave/valor
 * @author Mario Foos
 */
public class OptionsTools
{
	private String fileName;				// Ruta del archivo de opciones
	private SimpleDateFormat formatter;		// Formateador de fecha del nombre de archivo

	/**
	 * Constructor
	 * @param fileName Archivo de opciones
	 */
	public OptionsTools(String fileName)
	{
		this.fileName = fileName;
		formatter = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss");	//2020-08-10T23:59:59
	}

	/**
	 * Eliminar una clave del archivo de opciones
	 * @param key Clave
	 */
	public void Remove(String key)
	{
		ArrayList<String> lines = FileTools.ReadLines(fileName, true, false);
		String start = key+"=";
		for(int i = 0; i < lines.size(); ++i)
		{
			String line = lines.get(i);
			if(line.startsWith(start))
			{
				lines.remove(i);
				FileTools.WriteLines(fileName, lines, false);
				return;
			}
		}
	}
	
	/**
	 * Guardar cadena encriptada en el archivo de opciones
	 * @param key Clave
	 * @param value Cadena
	 * @return true/false
	 */
	public boolean WriteEncrypted(String key, String value)
	{
		return Write(key, SimpleCrypto.encrypt(value));
	}

	/**
	 * Guardar valor booleano en el archivo de opciones
	 * @param key Clave
	 * @param flag Valor booleano
	 * @return true/false
	 */
	public boolean Write(String key, boolean flag)
	{
		return Write(key, Boolean.toString(flag));
	}

	/**
	 * Guardar entero en el archivo de opciones
	 * @param key Clave
	 * @param integer Valor entero
	 * @return true/false
	 */
	public boolean Write(String key, int integer)
	{
		return Write(key, Integer.toString(integer));
	}

	/**
	 * Guardar entero en el archivo de opciones
	 * @param key Clave
	 * @param integer Valor entero largo
	 * @return true/false
	 */
	public boolean Write(String key, long integer)
	{
		return Write(key, Long.toString(integer));
	}

	/**
	 * Guardar numero real en el archivo de opciones
	 * @param key Clave
	 * @param real Valor real
	 * @return true/false
	 */
	public boolean Write(String key, double real)
	{
		return Write(key, Double.toString(real));
	}

	/**
	 * Guardar fecha en el archivo de opciones
	 * @param key Clave
	 * @param date Fecha
	 * @return true/false
	 */
	public boolean Write(String key, Date date)
	{
		return Write(key, formatter.format(date));
	}

	/**
	 * Guardar cadena en el archivo de opciones
	 * @param key Clave
	 * @param value Cadena
	 * @return true/false
	 */
	public boolean Write(String key, String value)
	{
		ArrayList<String> lines = FileTools.ReadLines(fileName, true, false);
		String start = key + "=";
		for(int i = 0; i < lines.size(); ++i)
		{
			String line = lines.get(i);
			if(line.startsWith(start))
			{
				// Si ya exist�a lo borro y agrego el nuevo valor
				lines.remove(i);
				lines.add(i, start + value);
				return FileTools.WriteLines(fileName, lines, false);
			}
		}
		// Si llegu� ac� no exist�a la clave, la agrego
		lines.add(start + value);
		return FileTools.WriteLines(fileName, lines, false);
	}
	
	/**
	 * Leer cadena encriptada desde el archivo de opciones
	 * @param key Clave
	 * @return Cadena desencriptada o vac�a
	 */
	public String ReadEncrypted(String key)
	{
		return ReadEncrypted(key, "");
	}

	/**
	 * Leer cadena encriptada desde el archivo de opciones
	 * @param key Clave
	 * @param def Valor por defecto
	 * @return Cadena desencriptada
	 */
	public String ReadEncrypted(String key, String def)
	{
		String value = ReadString(key, "");
		if(value.isEmpty())
		{
			return def;
		}
		return SimpleCrypto.decrypt(value);
	}

	/**
	 * Leer entero desde el archivo de opciones
	 * @param key Clave
	 * @return Valor real o cero
	 */
	public boolean ReadBoolean(String key)
	{
		return ReadBoolean(key, false);
	}

	/**
	 * Leer valor booleano desde el archivo de opciones
	 * @param key Clave
	 * @param def Valor por defecto
	 * @return Entero o valor por defecto
	 */
	public boolean ReadBoolean(String key, boolean def)
	{
		String value = ReadString(key, "");
		if(value.isEmpty())
		{
			return def;
		}
		return Boolean.parseBoolean(value);
	}

	/**
	 * Leer entero desde el archivo de opciones
	 * @param key Clave
	 * @return Valor real o cero
	 */
	public int ReadInt(String key)
	{
		return ReadInt(key, 0);
	}

	/**
	 * Leer entero desde el archivo de opciones
	 * @param key Clave
	 * @param def Valor por defecto
	 * @return Entero o valor por defecto
	 */
	public int ReadInt(String key, int def)
	{
		String value = ReadString(key, "");
		if(value.isEmpty())
		{
			return def;
		}
		return Integer.parseInt(value);
	}

	/**
	 * Leer entero desde el archivo de opciones
	 * @param key Clave
	 * @return Entero o valor por defecto
	 */
	public long ReadLong(String key)
	{
		return ReadLong(key, 0L);
	}

	/**
	 * Leer entero desde el archivo de opciones
	 * @param key Clave
	 * @param def Valor por defecto
	 * @return Entero o valor por defecto
	 */
	public long ReadLong(String key, long def)
	{
		String value = ReadString(key, "");
		if(value.isEmpty())
		{
			return def;
		}
		return Long.parseLong(value);
	}

	/**
	 * Leer valor real desde el archivo de opciones
	 * @param key Clave
	 * @return Valor real o cero
	 */
	public double ReadDouble(String key)
	{
		return ReadDouble(key, 0);
	}

	/**
	 * Leer valor real desde el archivo de opciones
	 * @param key Clave
	 * @param def Valor por defecto
	 * @return Valor real o valor por defecto
	 */
	public double ReadDouble(String key, double def)
	{
		String value = ReadString(key, "");
		if(value.isEmpty())
		{
			return def;
		}
		return Double.parseDouble(value);
	}

	/**
	 * Leer fecha desde el archivo de opciones
	 * @param key Clave
	 * @return Fecha o null
	 */
	public Date ReadDate(String key)
	{
		return ReadDate(key, null);
	}

	/**
	 * Leer fecha desde el archivo de opciones
	 * @param key Clave
	 * @param def Valor por defecto
	 * @return Fecha o valor por defecto
	 */
	public Date ReadDate(String key, Date def)
	{
		String value = ReadString(key, "");
		if(value.isEmpty())
		{
			return def;
		}
		try
		{
			return formatter.parse(value);
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		return def;
	}

	/**
	 * Comprobar si existe una clave
	 * @param key Clave
	 * @return true/false
	 */
	public boolean ExistsKey(String key)
	{
		String value = ReadString(key, null);
		return (value != null);
	}

	/**
	 * Leer cadena desde el archivo de opciones
	 * @param key Clave
	 * @return Valor o cadena vac�a
	 */
	public String ReadString(String key)
	{
		return ReadString(key, "");
	}

	/**
	 * Leer cadena desde el archivo de opciones
	 * @param key Clave
	 * @param def Valor por defecto
	 * @return Valor
	 */
	public String ReadString(String key, String def)
	{
		if(!FileTools.Exists(fileName))
		{
			return def;
		}
		String line;
		String start = key+"=";
        try
		{
        	BufferedReader br = new BufferedReader(new FileReader(fileName));
	        while((line = br.readLine()) != null)
	        {
	        	line = line.trim();
	        	if(line.isEmpty())
	        	{
	        		continue;
	        	}
	        	if(line.startsWith(start))
	        	{
	    	        br.close();
	        		return line.substring(start.length());
	        	}
	        }
	        br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}		
        return def;
	}
}
