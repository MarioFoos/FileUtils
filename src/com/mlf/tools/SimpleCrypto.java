package com.mlf.tools;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Clase con m�todos est�ticos simples para encriptar y desencriptar cadenas. No tiene el m�s alto nivel
 * de seguridad pero es simple de usar
 * @author Mario Foos
 */
//@SuppressWarnings("unused")
public class SimpleCrypto
{
	private static final String DEF_KEY = "/=m4r10-f0o5_";
	private static final String ALGO_AES = "AES";
	private static final String ALGO_DES = "DES";
	private static final String ALGO_MODE_PAD = "/CBC/PKCS5Padding";
	private static final int LEN_KEY_AES = 16;
	private static final int LEN_KEY_DES = 8;

	private static byte[] genArr(int len)
	{
		return genArr("", len);
	}

	private static byte[] genArr(String base, int len)
	{
		return genArr(base.getBytes(), len);
	}

	private static byte[] genArr(byte[] base, int len)
	{
		byte[] keyBytes = new byte[len];
		StrTools.copy(keyBytes, DEF_KEY);
		StrTools.copy(keyBytes, base, len);
		return keyBytes;
	}
	
	private static int getKeyLen(String algo)
	{
		if(algo.equalsIgnoreCase(ALGO_DES))
		{
			return LEN_KEY_DES;
		}
		return LEN_KEY_AES;
	}

	private static byte[] enc(byte[] data, byte[] key, byte[] iv, String algo)
	{
		int keyLen = getKeyLen(algo);

		SecretKeySpec keySpec = new SecretKeySpec(genArr(key, keyLen), algo);
		Cipher cipher;
		try
		{
	        IvParameterSpec initVector = new IvParameterSpec(genArr(iv, keyLen));
			cipher = Cipher.getInstance(algo + ALGO_MODE_PAD);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, initVector);
			byte[] message = cipher.doFinal(data);
			return message;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] dec(byte[] data, byte[] key, byte[] iv, String algo)
	{
		int keyLen = getKeyLen(algo);

		SecretKeySpec keySpec = new SecretKeySpec(genArr(key, keyLen), algo);
		Cipher cipher;
		try
		{
	        IvParameterSpec initVector = new IvParameterSpec(genArr(iv, keyLen));
			cipher = Cipher.getInstance(algo + ALGO_MODE_PAD);
			cipher.init(Cipher.DECRYPT_MODE, keySpec, initVector);
			byte[] message = cipher.doFinal(data);
			return message;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Encriptar un array de bytes con la clave y el vector inicial dados
	 * @param data Array de bytes
	 * @param keyBytes Clave
	 * @return Array encriptado
	 */
	public static byte[] encDES(byte[] data, byte[] keyBytes)
	{
		return encDES(data, keyBytes, genArr(LEN_KEY_DES));
	}

	/**
	 * Encriptar un array de bytes con la clave y el vector inicial dados
	 * @param data Array de bytes
	 * @param keyBytes Clave
	 * @param iv Vector de inicializaci�n
	 * @return Array encriptado
	 */
	public static byte[] encDES(byte[] data, byte[] keyBytes, byte[] iv)
	{
		return enc(data, keyBytes, iv, ALGO_DES);
	}

	/**
	 * Encriptar un array de bytes con la clave y el vector inicial dados
	 * @param data Array de bytes
	 * @param keyBytes Clave
	 * @return Array encriptado
	 */
	public static byte[] encAES(byte[] data, byte[] keyBytes)
	{
		return encAES(data, keyBytes, genArr(LEN_KEY_AES));
	}

	/**
	 * Encriptar un array de bytes con la clave y el vector inicial dados
	 * @param data Array de bytes
	 * @param keyBytes Clave
	 * @param iv Vector de inicializaci�n
	 * @return Array encriptado
	 */
	public static byte[] encAES(byte[] data, byte[] keyBytes, byte[] iv)
	{
		return enc(data, keyBytes, iv, ALGO_AES);
	}

	/**
	 * Desencriptar un array de bytes con la clave dada
	 * @param data Array de bytes
	 * @param keyBytes Clave
	 * @return Array desencriptado
	 */
	public static byte[] decDES(byte[] data, byte[] keyBytes)
	{
		return decDES(data, keyBytes, genArr(LEN_KEY_DES));
	}
	
	/**
	 * Desencriptar un array de bytes con la clave y el vector inicial dados
	 * @param data Array de bytes
	 * @param keyBytes Clave
	 * @param iv Vector de inicializaci�n
	 * @return Array desencriptado
	 */
	public static byte[] decDES(byte[] data, byte[] keyBytes, byte[] iv)
	{
		return dec(data, keyBytes, iv, ALGO_DES);
	}

	/**
	 * Desencriptar un array de bytes con la clave dada
	 * @param data Array de bytes
	 * @param keyBytes Clave
	 * @return Array desencriptado
	 */
	public static byte[] decAES(byte[] data, byte[] keyBytes)
	{
		return decAES(data, keyBytes, genArr(LEN_KEY_AES));
	}
	
	/**
	 * Desencriptar un array de bytes con la clave y el vector inicial dados
	 * @param data Array de bytes
	 * @param keyBytes Clave
	 * @param iv Vector de inicializaci�n
	 * @return Array desencriptado
	 */
	public static byte[] decAES(byte[] data, byte[] keyBytes, byte[] iv)
	{
		return dec(data, keyBytes, iv, ALGO_AES);
	}

	/**
	 * Encriptar una cadena con una clave por defecto
	 * @param strToEncrypt Cadena que se quiere encriptar
	 * @return Cadena encriptada o null si hubo alg�n error
	 */
	public static String encrypt(String strToEncrypt)
	{
		return encrypt(strToEncrypt, DEF_KEY);
	}

	/**
	 * Encriptar una cadena con una clave dada
	 * @param strToEncrypt Cadena que se quiere encriptar
	 * @param key Clave de encriptaci�n
	 * @return Cadena encriptada o null si hubo alg�n error
	 */
	public static String encrypt(String strToEncrypt, String key)
	{
		try
		{
			byte[] keyBytes = genArr(key, LEN_KEY_AES);
			byte[] message = enc(strToEncrypt.getBytes(), keyBytes, genArr(LEN_KEY_AES), ALGO_AES);
			if(message != null)
			{
				return Base64.getEncoder().encodeToString(message);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Desencriptar cadena encriptada con el m�todo encrypt con clave por defecto
	 * @param strToDecrypt Cadena a desencriptar
	 * @return Cadena desencriptada o null si hubo alg�n error
	 */
	public static String decrypt(String strToDecrypt)
	{
		return decrypt(strToDecrypt, DEF_KEY);
	}

	/**
	 * Desencriptar cadena encriptada con una clave distinta a la clave por defecto
	 * @param strToDecrypt Cadena a desencriptar
	 * @param key Clave de encriptaci�n
	 * @return Cadena desencriptada o null si hubo alg�n error
	 */
	public static String decrypt(String strToDecrypt, String key)
	{
		try
		{
			byte[] keyBytes = genArr(key, LEN_KEY_AES);
			byte[] decoded = Base64.getDecoder().decode(strToDecrypt.getBytes());
			byte[] message = dec(decoded, keyBytes, genArr(LEN_KEY_AES), ALGO_AES);
			if(message != null)
			{
				return new String(message);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
