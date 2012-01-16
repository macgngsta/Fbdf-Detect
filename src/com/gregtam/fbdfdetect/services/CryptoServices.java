package com.gregtam.fbdfdetect.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.google.appengine.repackaged.com.google.common.util.Base64DecoderException;

public class CryptoServices
{
	private static final Logger log = Logger.getLogger(CryptoServices.class
			.getName());

	private static CryptoServices _instance;

	private SecretKey key;
	private Cipher ecipher;
	private Cipher dcipher;

	private CryptoServices()
	{
		try
		{
			this.key = KeyGenerator.getInstance("DES").generateKey();
			this.ecipher = Cipher.getInstance("DES");
			this.dcipher = Cipher.getInstance("DES");
			this.ecipher.init(Cipher.ENCRYPT_MODE, key);
			this.dcipher.init(Cipher.DECRYPT_MODE, key);
		}
		catch (NoSuchAlgorithmException e)
		{
			log.warning("no such algorithm " + e);
			throw new RuntimeException("no such algorithm");
		}
		catch (NoSuchPaddingException e)
		{
			log.warning("no such padding " + e);
			throw new RuntimeException("no such padding");
		}
		catch (InvalidKeyException e)
		{
			log.warning("invalid key " + e);
			throw new RuntimeException("invalid key");
		}
	}

	public static synchronized CryptoServices getInstance()
	{
		if (_instance == null)
		{
			_instance = new CryptoServices();
		}

		return _instance;
	}

	public String encryptString(String input)
	{
		try
		{
			// Encode the string into bytes using utf-8
			byte[] utf8 = input.getBytes("UTF8");

			// Encrypt
			byte[] enc = ecipher.doFinal(utf8);

			// Encode bytes to base64 to get a string
			return Base64.encode(enc);
		}
		catch (javax.crypto.BadPaddingException e)
		{
			log.warning("bad padding " + e);
		}
		catch (IllegalBlockSizeException e)
		{
			log.warning("illegal block size " + e);
		}
		catch (UnsupportedEncodingException e)
		{
			log.warning("unsupported encoding " + e);
		}
		return null;
	}

	public String decodeString(String input)
	{

		try
		{
			byte[] dec = Base64.decode(input);
			byte[] utf8 = dcipher.doFinal(dec);

			return new String(utf8, "UTF8");
		}
		catch (Base64DecoderException e)
		{
			log.warning("base64 decode " + e);
		}
		catch (IllegalBlockSizeException e)
		{
			log.warning("illegal block size " + e);
		}
		catch (BadPaddingException e)
		{
			log.warning("bad padding " + e);
		}
		catch (UnsupportedEncodingException e)
		{
			log.warning("unsupported encoding " + e);
		}

		return null;
	}
}
