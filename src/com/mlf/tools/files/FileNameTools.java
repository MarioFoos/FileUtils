package com.mlf.tools.files;

/**
 * Funciones de manejo de archivos
 * @author Mario
 */
public class FileNameTools
{
	/** Posici�n */
	public static enum EPos
	{
		/** Primero */
		FIRST,
		/** �ltimo */
		LAST,
		/** Al principio */
		BEGIN,
		/** Al final */
		END
	};
	private static final char[] SLASHES = new char[]{ '/', '\\' };
	private static final char DOT = '.';
	private static final char[] SLASHES_DOT = new char[]{ '.', '/', '\\' };
	
	/**
	 * Chequear si el texto contiene alg�n caracter de la lista
	 * @param text Texto
	 * @param cList Lista de caracteres
	 * @return true/false
	 */
	private static boolean contains(String text, char... cList)
    {
    	if((text != null) && (cList != null))
    	{
        	for(char c : cList)
        	{
                if(text.indexOf(c) >= 0)
                {
                    return true;
                }
        	}
    	}
   		return false;
    }

	private static boolean endsWith(String text, char... cList)
    {
		if((text == null) || text.isEmpty() || (cList == null) || (cList.length == 0))
		{
			return false;
		}
		int posLast = text.length() - 1;
    	for(char c : cList)
    	{
    		if(text.charAt(posLast) == c)
    		{
    			return true;
    		}
    	}
   		return false;
    }
	
	private static String addSlash(String text, char slash)
    {
		if((text == null) || text.isEmpty())
		{
			return "";
		}
		if(endsWith(text, SLASHES))
		{
			return text;
		}
		return text + slash;
    }

	private static String addSlash(String text)
    {
		return addSlash(text, slash(text));
    }

	private static char slash(String text)
    {
		return slash(text, '/');
    }

	private static char slash(String text, char defSlash)
    {
    	if(text != null)
    	{
        	for(char c : SLASHES)
        	{
                if(text.indexOf(c) >= 0)
                {
                    return c;
                }
        	}
    	}
   		return defSlash;
    }
	
	/**
	 * Limpiar caracteres de una cadena
	 * @param text Cadena
	 * @param pos Posici�n
	 * @param cList Lista de caracteres
	 * @return Nueva cadena
	 */
	private static String clean(String text, EPos pos, char... cList)
    {
		if((text == null) || text.isEmpty() || (cList == null) || (cList.length == 0))
		{
			return (text == null) ? "" : text;
		}
		if(pos == EPos.BEGIN)
		{
			boolean found = false;
			while(!text.isEmpty())
			{
				for(char c : cList)
				{
					if(text.charAt(0) == c)
					{
						found = true;
						break;
					}
				}
				if(found)
				{
					text = text.substring(1);
					found = false;
				}
				else
				{
					break;
				}
			}
		}
		else
		{
			boolean found = false;
			while(!text.isEmpty())
			{
				for(char c : cList)
				{
					if(text.charAt(text.length() - 1) == c)
					{
						found = true;
						break;
					}
				}
				if(found)
				{
					text = text.substring(0, text.length() - 1);
					found = false;
				}
				else
				{
					break;
				}
			}
		}
   		return text;
    }

	/**
	 * Formatear directorio
	 * @param dir Directorio
	 * @return nuevo directorio
	 */
	public static String formatDir(String dir)
    {
		if((dir == null) || dir.isEmpty())
		{
			return "";
		}
    	if(endsWith(dir, DOT))
    	{
    		return dir.substring(0, dir.length() - 1);
    	}
   		return addSlash(dir);
    }

	
	/**
     * Obtener el texto antes del �ltimo o primer caracter de la lista
     * @param text Texto
     * @param include Incluir el caracter encontrado
     * @param cList Caracteres
     * @return Texto encontrado
     */
	private static String extractBefore(String text, EPos posToCheck, boolean includeChar, char... cList)
    {
    	if(text != null)
    	{
        	int cPos;
        	for(char c : cList)
        	{
                cPos = (posToCheck == EPos.FIRST) ? text.indexOf(c) : text.lastIndexOf(c);
                if(cPos >= 0)
                {
                    return text.substring(0, includeChar ? cPos + 1 : cPos);
                }
        	}
    	}
   		return "";
    }

    /**
     * Obtener el texto despu�s del primer caracter de la lista
     * @param text Texto
     * @param includeChar Incluir el caracter encontrado
     * @param cList Caracteres
     * @return Texto encontrado
     */
	private static String extractAfter(String text, EPos posToCheck, boolean includeChar, char... cList)
    {
    	if(text != null)
    	{
        	int cPos;
        	for(char c : cList)
        	{
                cPos = (posToCheck == EPos.FIRST) ? text.indexOf(c) : text.lastIndexOf(c);
                if(cPos >= 0)
                {
                    return text.substring(includeChar ? cPos : cPos + 1);
                }
        	}
    	}
   		return "";
    }

    /**
	 * Obtener el nombre de un archivo con extensi�n
	 * @param file Ruta completa o parcial
	 * @return Nombre de un archivo
	 */
    public static String extractName(String file)
    {
    	return extractName(file, true);
    }

    /**
	 * Obtener el nombre de un archivo a partir de la ruta completa o parcial
	 * @param file Ruta completa o parcial
	 * @param includeExt Incluir la extensi�n
	 * @return Nombre de un archivo
	 */
    public static String extractName(String file, boolean includeExt)
    {
    	if((file == null) || file.isEmpty())
    	{
    		return "";
    	}
    	if(contains(file, SLASHES))
    	{
    		file = extractAfter(file, EPos.LAST, false, SLASHES);
    	}
    	if(includeExt)
    	{
    		return file;
    	}
    	if(contains(file, DOT))
    	{
    		return extractBefore(file, EPos.FIRST, false, '.');	
    	}
    	return file;
    }

    /**
     * Obtener el directorio de un archivo con la barra final
     * @param file Archivo
     * @return Directorio
     */
    public static String extractDir(String file)
    {
    	return extractDir(file, true);
    }

    /**
     * Obtener el directorio de un archivo
     * @param file Archivo
     * @param includeSlash Incluir barra
     * @return Directorio
     */
    public static String extractDir(String file, boolean includeSlash)
    {
    	if(contains(file, SLASHES))
    	{
        	return extractBefore(file, EPos.LAST, includeSlash, SLASHES);
    	}
    	return "";
    }

    /**
     * Obtener la extensi�n del archivo sin el punto
     * @param file Archivo
     * @return Extensi�n
     */
    public static String extractExt(String file)
    {
    	return extractExt(file, false);
    }

    /**
     * Obtener la extensi�n del archivo
     * @param file Archivo
     * @param includeDot Incluir el punto
     * @return Extensi�n
     */
    public static String extractExt(String file, boolean includeDot)
    {
    	file = extractName(file);
    	if(contains(file, DOT))
    	{
        	return extractAfter(file, EPos.LAST, includeDot, DOT);
    	}
    	return "";
    }
    
    /**
     * Cambiar la extensi�n del archivo
     * @param file Archivo
     * @param newExt Nueva extensi�n o archivo de donde extraerla
     * @return Nuevo nombre
     */
    public static String changeExt(String file, String newExt)
    {
    	if((file == null) || file.isEmpty())
    	{
    		return "";
    	}
    	if(newExt == null)
    	{
    		newExt = "";
    	}
    	else if(contains(newExt, DOT))
    	{
    		newExt = extractExt(newExt, false);
    	}
    	return extractDir(file, true) + extractName(file, false) + DOT + newExt;
    }

    /**
     * Cambiar el directorio del archivo
     * @param file Archivo
     * @param newDir Nuevo directorio
     * @return Nuevo nombre
     */
    public static String changeDir(String file, String newDir)
    {
    	if((newDir == null) || newDir.isEmpty())
    	{
    		newDir = extractName(file);
    	}
    	return addSlash(newDir) + extractName(file);
    }

    /**
     * Cambiar el nombre del archivo
     * @param file Archivo
     * @param newName Nuevo nombre o archivo de donde extraerlo
     * @return Nuevo nombre
     */
    public static String changeName(String file, String newName)
    {
    	return changeName(file, newName, false);
    }

    /**
     * Cambiar el nombre del archivo
     * @param file Archivo
     * @param newName Nuevo nombre o archivo de donde extraerlo
     * @param includeExt Cambiar tambi�n la extensi�n
     * @return Nuevo nombre
     */
    public static String changeName(String file, String newName, boolean includeExt)
    {
    	if(includeExt)
    	{
    		return extractDir(file) + extractName(newName, false) + DOT + extractExt(newName);
    	}
    	return extractDir(file) + extractName(newName, false) + DOT + extractExt(file);
    }

    /**
     * Construir ruta de archivo
     * @param parts Partes a concatenar
     * @return Nuevo nombre
     */
    public static String build(String... parts)
    {
    	if((parts == null) || (parts.length == 0))
    	{
    		return "";
    	}
    	if(parts.length == 1)
    	{
    		return parts[0];
    	}
    	StringBuilder sb = new StringBuilder();
    	String part;
    	char slash = '/';
		for(int i = 0; i < parts.length; ++i)
		{
			if(contains(parts[i], '\\', ':'))
			{
				slash = '\\';
				break;
			}
		}
		for(int i = 0; i < parts.length; ++i)
		{
			part = parts[i];
			if(i == 0)
			{
				// El inicial puede empezar con lo que sea, limpio y agrego barra
				part = clean(part, EPos.END, SLASHES_DOT);
				part += slash;
			}
			else if(i == parts.length - 1)
			{
				// Del final limpio el inicio
				part = clean(part, EPos.BEGIN, SLASHES_DOT);
			}
			else
			{
				// De los del medio limpio todo y agrego la barra final
				part = clean(part, EPos.BEGIN, SLASHES_DOT);
				part = clean(part, EPos.END, SLASHES_DOT);
				part += slash;
			}
    		sb.append(part);
		}
		return sb.toString();
    }
}
