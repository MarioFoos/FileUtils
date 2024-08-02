package com.mlf.utils.files;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.apache.hc.core5.net.URIBuilder;


/**
 * Funciones �tiles para SFTP (chequear, descargar, subir, etc)
 * @author Mario
 */
public class SftpTools
{
    private static URI getRemoteURI(String host, int port, String user, String pass, String remoteFilePath)
    {
		// Remote file object
        URIBuilder builder = new URIBuilder();
        builder.setScheme("sftp").setHost(host);
        if(port > 0)
        {
        	builder.setPort(port);
        }
        builder.setPath(remoteFilePath);
        builder.setUserInfo(user, pass);
        try
		{
			return builder.build();
		}
        catch(URISyntaxException e)
		{
			e.printStackTrace();
		}
    	return null;
    }
	
    /**
     * Comprobar si existe el archivo en el host SFTP
     * @param host Host
     * @param user Ususario
     * @param pass Contrase�a
     * @param remoteFilePath Ruta del archivo / directorio remoto - Se usa el separador /
     * @return true/false
     */
    public static boolean exist(String host, String user, String pass, String remoteFilePath)
    {
    	return exist(host, 0, user, pass, remoteFilePath);
    }
    
    /**
     * Comprobar si existe el archivo en el host SFTP
     * @param host Host
     * @param port Puerto
     * @param user Ususario
     * @param pass Contrase�a
     * @param remoteFilePath Ruta del archivo / directorio remoto - Se usa el separador /
     * @return true/false
     */
    public static boolean exist(String host, int port, String user, String pass, String remoteFilePath)
    {
		FileSystemManager manager;
		boolean exists;
		try
		{
			manager = VFS.getManager();
		}
		catch (FileSystemException e1)
		{
			e1.printStackTrace();
			return false;
		}
        try
        {
            URI uri = getRemoteURI(host, port, user, pass, remoteFilePath);
            if(uri == null)
            {
            	return false;
            }
            // Create remote object
            FileObject remoteFile = manager.resolveFile(uri.toString());
            exists = remoteFile.exists();
            return exists;
        }
        catch(Exception e)
        {
			e.printStackTrace();
        }
        finally
        {
            manager.close();
        }
    	return false;
    }

    /**
     * Borrar archivo en un server SFTP
     * @param host Host
     * @param port Puerto
     * @param user Ususario
     * @param pass Contrase�a
     * @param remoteFilePath Ruta del archivo / directorio remoto - Se usa el separador /
     */
    public static void delete(String host, int port, String user, String pass, String remoteFilePath)
    {
		FileSystemManager manager;
		try
		{
			manager = VFS.getManager();
		}
		catch(FileSystemException e1)
		{
			e1.printStackTrace();
			return;
		}
        try
        {
			// Remote file object
            URI uri = getRemoteURI(host, port, user, pass, remoteFilePath);
            if(uri == null)
            {
            	return;
            }
            // Create remote object
            FileObject remoteFile = manager.resolveFile(uri);

            if(remoteFile.exists())
            {
                remoteFile.delete();
            }
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            manager.close();
        }
    }

    /**
     * Descargar un archivo por SFTP
     * @param host Host
     * @param user Ususario
     * @param pass Contrase�a
     * @param localFilePath Ruta del archivo local - Separador \\
     * @param remoteFilePath Ruta del archivo / directorio remoto - Se usa el separador /
     * @return true/false
     */
	public static boolean download(String host, String user, String pass, String localFilePath, String remoteFilePath)
	{
		return download(host, 0, user, pass, localFilePath, remoteFilePath);
	}
	
    /**
     * Descargar un archivo por SFTP
     * @param host Host
     * @param port Puerto
     * @param user Ususario
     * @param pass Contrase�a
     * @param localFilePath Ruta del archivo local - Separador \\
     * @param remoteFilePath Ruta del archivo / directorio remoto - Se usa el separador /
     * @return true/false
     */
	public static boolean download(String host, int port, String user, String pass, String localFilePath, String remoteFilePath)
	{
		FileSystemManager manager;
		try
		{
			manager = VFS.getManager();
		}
		catch(FileSystemException e1)
		{
			e1.printStackTrace();
			return false;
		}
		try
		{
			// Local file object
			FileObject localFile = manager.resolveFile(localFilePath);

			// Remote file object
            URI uri = getRemoteURI(host, port, user, pass, remoteFilePath);
            if(uri == null)
            {
            	return false;
            }
            FileObject remoteFile = manager.resolveFile(uri);

			// Copy local file to sftp server
			localFile.copyFrom(remoteFile, Selectors.SELECT_SELF);
        	return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			manager.close();
		}
    	return false;
	}
    
	/**
     * Descargar un archivo por SFTP
     * @param host Host
     * @param user Ususario
     * @param pass Contrase�a
     * @param localFilePath Ruta del archivo local - Separador \\
     * @param remoteFilePath Ruta del archivo / directorio remoto - Se usa el separador /
     * @return true/false
     */
    public static boolean upload(String host, String user, String pass, String localFilePath, String remoteFilePath)
    {
    	return upload(host, 0, user, pass, localFilePath, remoteFilePath);
    }

    /**
     * Descargar un archivo por SFTP
     * @param host Host
     * @param port Puerto
     * @param user Ususario
     * @param pass Contrase�a
     * @param localFilePath Ruta del archivo local - Separador \\
     * @param remoteFilePath Ruta del archivo / directorio remoto - Se usa el separador /
     * @return true/false
     */
    public static boolean upload(String host, int port, String user, String pass, String localFilePath, String remoteFilePath)
    {
		FileSystemManager manager;
		try
		{
			manager = VFS.getManager();
		}
		catch(FileSystemException e1)
		{
			e1.printStackTrace();
			return false;
		}
        try
        {
            // Create local file object
            FileObject localFile = manager.resolveFile(new File(localFilePath), "");

			// Remote file object
            URI uri = getRemoteURI(host, port, user, pass, remoteFilePath);
            if(uri == null)
            {
            	return false;
            }
            FileObject remoteFile = manager.resolveFile(uri);

            // Copy local file to sftp server
            remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);
        	return true;
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
        	manager.close();
        }
    	return false;
    }

    /**
     * Copiar un archivo remoto a otro archivo remoto
     * @param host Host
     * @param user Ususario
     * @param pass Contrase�a
     * @param remoteSrcFilePath Ruta del archivo remoto de origen
     * @param remoteDestFilePath Ruta del archivo remoto de destino
     * @return true/false
     */
    public static boolean copy(String host, String user, String pass, String remoteSrcFilePath, String remoteDestFilePath)
    {
    	return copy(host, 0, user, pass, remoteSrcFilePath, remoteDestFilePath);
    }

    /**
     * Copiar un archivo remoto a otro archivo remoto
     * @param host Host
     * @param port Puerto
     * @param user Ususario
     * @param pass Contrase�a
     * @param remoteSrcFilePath Ruta del archivo remoto de origen
     * @param remoteDestFilePath Ruta del archivo remoto de destino
     * @return true/false
     */
    public static boolean copy(String host, int port, String user, String pass, String remoteSrcFilePath, String remoteDestFilePath)
    {
		FileSystemManager manager;
		URI uri;
		try
		{
			manager = VFS.getManager();
		}
		catch(FileSystemException e1)
		{
			e1.printStackTrace();
			return false;
		}
        try
        {
			// Remote file object src
            uri = getRemoteURI(host, port, user, pass, remoteSrcFilePath);
            if(uri == null)
            {
            	return false;
            }
            FileObject remoteFileSrc = manager.resolveFile(uri);        	
        	
            // Create remote object
            uri = getRemoteURI(host, port, user, pass, remoteDestFilePath);
            if(uri == null)
            {
            	return false;
            }            
            FileObject remoteFileDest = manager.resolveFile(uri);

            if(remoteFileSrc.exists())
            {
            	remoteFileDest.copyFrom(remoteFileSrc, Selectors.SELECT_SELF);
                return true;
            }
            else
            {
                Log.err("Source file doesn't exist");
                return false;
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            manager.close();
        }
    }
    
    /**
     * Mover un archivo remoto a otro archivo remoto
     * @param host Host
     * @param user Ususario
     * @param pass Contrase�a
     * @param remoteSrcFilePath Ruta del archivo remoto de origen
     * @param remoteDestFilePath Ruta del archivo remoto de destino
     * @return true/false
     */
    public static boolean move(String host, String user, String pass, String remoteSrcFilePath, String remoteDestFilePath)
    {
    	return move(host, 0, user, pass, remoteSrcFilePath, remoteDestFilePath);
    }

    /**
     * Mover un archivo remoto a otro archivo remoto
     * @param host Host
     * @param port Puerto
     * @param user Ususario
     * @param pass Contrase�a
     * @param remoteSrcFilePath Ruta del archivo remoto de origen
     * @param remoteDestFilePath Ruta del archivo remoto de destino
     * @return true/false
     */
    public static boolean move(String host, int port, String user, String pass, String remoteSrcFilePath, String remoteDestFilePath)
    {
		FileSystemManager manager;
		URI uri;
		try
		{
			manager = VFS.getManager();
		}
		catch(FileSystemException e1)
		{
			e1.printStackTrace();
			return false;
		}
        try
        {
			// Remote file object src
            uri = getRemoteURI(host, port, user, pass, remoteSrcFilePath);
            if(uri == null)
            {
            	return false;
            }
            FileObject remoteFileSrc = manager.resolveFile(uri);        	
        	
            // Create remote object
            uri = getRemoteURI(host, port, user, pass, remoteDestFilePath);
            if(uri == null)
            {
            	return false;
            }            
            FileObject remoteFileDest = manager.resolveFile(uri);

            if(remoteFileSrc.exists())
            {
                remoteFileSrc.moveTo(remoteFileDest);;
                return true;
            }
            else
            {
            	Log.err("Source file doesn't exist");
                return false;
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            manager.close();
        }
    }
}
