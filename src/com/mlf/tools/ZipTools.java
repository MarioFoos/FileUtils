package com.mlf.tools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Manejo de ZIP, solo descompresi�n
 * @author Mario
 */
public class ZipTools
{
    private static final int BUFFER_SIZE = 4096;
    
    /**
     * Descomprimir un archivo zip
     * @param inputFile Archivo zip
     * @param outputDir Directorio de destino
     */
    public static void extractFile(String inputFile, String outputDir)
    {
    	if(!outputDir.endsWith("/") && !outputDir.endsWith("\\"))
    	{
    		outputDir += "/";
    	}
		File fileSrc = new File(inputFile);
        File destDir = new File(outputDir);
        if(!destDir.exists())
        {
            destDir.mkdir();
        }
        InputStream is;
        try
        {
			is = (InputStream) new FileInputStream(fileSrc);
		}
        catch (FileNotFoundException e1)
        {
			e1.printStackTrace();
			return;
		}
        ZipInputStream zipIn = new ZipInputStream(is);
        ZipEntry entry;
		try
		{
			entry = zipIn.getNextEntry();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			try
			{
				zipIn.closeEntry();
				zipIn.close();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
			return;
		}

        // iterates over entries in the zip file
        while(entry != null)
        {
            String filePath = outputDir + entry.getName();
            if(!entry.isDirectory())
            {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            }
            else
            {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdirs();
            }
            try
            {
				zipIn.closeEntry();
			}
            catch (IOException e)
            {
				e.printStackTrace();
			}
            try
            {
				entry = zipIn.getNextEntry();
			}
            catch (IOException e)
            {
				e.printStackTrace();
			}
        }
        try
        {
			zipIn.close();
		}
        catch (IOException e)
        {
			e.printStackTrace();
			return;
		}
    }

    /**
     * Descomprimir flujo de entrada zip
     * @param zipIn Flujo de entrada zip
     * @param filePath Directorio de destino
     */
    private static void extractFile(ZipInputStream zipIn, String filePath)
    {
        BufferedOutputStream bos;
		try
		{
			bos = new BufferedOutputStream(new FileOutputStream(filePath));
		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
			return;
		}
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        try
        {
	        while((read = zipIn.read(bytesIn)) != -1)
	        {
					bos.write(bytesIn, 0, read);
	        }
	        bos.close();
		}
        catch(IOException e)
        {
			e.printStackTrace();
		}
    }
}
