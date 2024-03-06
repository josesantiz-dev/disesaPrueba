package net.giro.cxc.fe;

import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.InvalidKeyException;
import java.security.InvalidAlgorithmParameterException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.NoSuchPaddingException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.spec.IvParameterSpec;
import org.apache.commons.ssl.Base64;

public class DESCryptix {

	/*
	  public static void main(String[] args) throws Exception {
		  String b = new String(encripta(args[0]));
		      System.out.println( "'"+ b + "'" ) ;
							  String p2 = desencripta(b.getBytes());
							  System.out.println("'"+p2+"'");
	  }
	*/
	
	public static byte[] encripta(String clave) {
		String palabra = String.valueOf((char)clave.length()) + clave;
		for ( int faltan = 31  -  clave.length(); faltan>0;faltan-- ) 
				palabra = palabra + String.valueOf((char)255);
		byte[] b = encripta("DES","ECB","NoPadding","SunJCE",palabra);
		return b;
		
	}
	
	public static String desencripta(byte[] clave) {
		String p2 = desencripta("DES","ECB","NoPadding","SunJCE",clave);
		System.out.println("cad" + p2);
		p2 = p2.substring(1,(int)p2.charAt(0)+1);
		return p2;
		
	}

	/** cipher object */
	static private Cipher cipher=null;
	
	/** keygenerator object */
	static private KeyGenerator kg=null;

	static private String llaveAcceso ="con1de2se$";
	
	public static String desencripta(String algorithm, String mode, String padding, 
								String provider, byte[] texto) {
        try {
            // create a cipher object: ("algorithm/mode/padding", provider)
            // currently cryptix only supports padding types: NoPadding, None
            cipher = Cipher.getInstance(
										algorithm+"/"+mode+"/"+padding,provider);
			
            // get a key generator for the algorithm.
            kg = KeyGenerator.getInstance(algorithm,provider);
			
            int strength = 0; // strength of key
            IvParameterSpec spec = null; // initialization vector
            byte[]          iv   = null; // initialization vector as byte[]
			
            // Check which algorithm is used and define key size.
            if (algorithm=="Blowfish")
            {
                // valid values are: starting from 40bit up to 
                // 448 in 8-bit increments
                strength=448; 
            }
            else if (algorithm=="CAST5")
            {
                // valid values are: starting from 40bit up to 128bit using 
                // 8bit steps.
                strength=128;
            }
            else if (algorithm=="DES")
            {
                strength=56;
            }
            else if (algorithm=="TripleDES"||algorithm=="DESede")
            {
                // FIX ME: is that correct ? Waiting for cryptix' suppport 
                // if 3DES.
                strength=3*64; 
            }
            else if (algorithm=="Rijndael")
            {
                strength=256; //valid values are: 128, 192, 256
            }
            else if (algorithm=="SKIPJACK")
            {
                // fixed size: 80 bits
                strength=80;
            }
            else if (algorithm=="Square")
            {
                strength=128;
            }
            else 
            {
                throw new RuntimeException();
            }
			
			
            // init key generator with the key size and some random data. 
            // Use SecureRandom only if use trust it. It is not a really 
            // verified PRNG, yet.
            kg.init(strength, new SecureRandom());
			
            // create a secret key from the keygenerator.
			
			DESKeySpec desKeySpec = new DESKeySpec(llaveAcceso.getBytes());
		    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		    SecretKey key = keyFactory.generateSecret(desKeySpec);//kg.generateKey();
			
			
            // init cipher for encryption
			
            // ECB does not need an initialization vector others do.
            if (mode=="ECB")
            {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }
            else 
            {
                // These modes need an iv with a valid block size in order 
                // to be used.
                SecureRandom sr = new SecureRandom();
                // allocate memory for iv.
                iv = new byte[cipher.getBlockSize()];
                // Get next bytes from the PRNG.
                sr.nextBytes(iv);
                // create the IV class.
                spec = new IvParameterSpec(iv);
            }
			
			
   		
            try 
            {
                // decryption
                Cipher decipher = Cipher.getInstance(algorithm+"/"+mode+"/"+padding,provider);
                decipher.init(Cipher.DECRYPT_MODE, key, spec);
                byte[] deciph1 = decipher.update(Base64.decodeBase64(texto));
				
                
				return new String(deciph1);
				
            }
            catch(InvalidAlgorithmParameterException iape)
            {
                System.out.println(
								   "cipher.init: InvalidAlgorithmParameterException.");
                iape.printStackTrace();
            }
        }
		catch (InvalidKeySpecException nsae)
        {
            System.out.println("No such algorithm!\n");
            nsae.printStackTrace();
        }
		
        catch (NoSuchAlgorithmException nsae)
        {
            System.out.println("No such algorithm!\n");
            nsae.printStackTrace();
        }
        catch (NoSuchPaddingException nspe)
        {
            System.out.println("No such padding!\n");
            nspe.printStackTrace();
        }
        catch (NoSuchProviderException nspre)
        {
            System.out.println("No such provider found!\n");
            nspre.printStackTrace();
        }
        catch (InvalidKeyException ike)
        {
            System.out.println("Invalidkey Exception!\n");
            ike.printStackTrace();
        }
		return null;
		
    }
    /**
     * Starts the application. Create keys, iv¥s, run cipher on a few bytes 
     * and pass values for using the FileDEncryption class.
     *
     * @param algorithm Algorithm.
     * @param mode      Cipher mode.
     * @param padding   Padding scheme the cipher should use.
     * @param provider  Name of provider as string.
     * @param filename  Name of the file to [de|en]crypt.
     */
    public static byte[] encripta(String algorithm, String mode, String padding, 
                    String provider, String texto) {
        try
        {
            // create a cipher object: ("algorithm/mode/padding", provider)
            // currently cryptix only supports padding types: NoPadding, None
            cipher = Cipher.getInstance(
                         algorithm+"/"+mode+"/"+padding,provider);
          
            // get a key generator for the algorithm.
            kg = KeyGenerator.getInstance(algorithm,provider);
          
            int strength = 0; // strength of key
            IvParameterSpec spec = null; // initialization vector
            byte[]          iv   = null; // initialization vector as byte[]
        
            // Check which algorithm is used and define key size.
            if (algorithm=="Blowfish")
            {
                // valid values are: starting from 40bit up to 
                // 448 in 8-bit increments
                strength=448; 
            }
            else if (algorithm=="CAST5")
            {
                // valid values are: starting from 40bit up to 128bit using 
                // 8bit steps.
                strength=128;
            }
            else if (algorithm=="DES")
            {
                strength=56;
            }
            else if (algorithm=="TripleDES"||algorithm=="DESede")
            {
                // FIX ME: is that correct ? Waiting for cryptix' suppport 
                // if 3DES.
                strength=3*64; 
            }
            else if (algorithm=="Rijndael")
            {
                strength=256; //valid values are: 128, 192, 256
            }
            else if (algorithm=="SKIPJACK")
            {
                // fixed size: 80 bits
                strength=80;
            }
            else if (algorithm=="Square")
            {
                strength=128;
            }
            else 
            {
                throw new RuntimeException();
            }
        
          
            // init key generator with the key size and some random data. 
            // Use SecureRandom only if use trust it. It is not a really 
            // verified PRNG, yet.
            kg.init(strength, new SecureRandom());
			
			DESKeySpec desKeySpec = new DESKeySpec(llaveAcceso.getBytes());
		    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		    SecretKey key = keyFactory.generateSecret(desKeySpec);//kg.generateKey();
          
            // init cipher for encryption
          
            // ECB does not need an initialization vector others do.
            if (mode=="ECB")
            {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }
            else 
            {
                // These modes need an iv with a valid block size in order 
                // to be used.
                SecureRandom sr = new SecureRandom();
                // allocate memory for iv.
                iv = new byte[cipher.getBlockSize()];
                // Get next bytes from the PRNG.
                sr.nextBytes(iv);
                // create the IV class.
                spec = new IvParameterSpec(iv);
            }
        

            // Build a few bytes for encryption.
            byte[] text1 = texto.getBytes();
        
            // Usage of [de|en]cryption for a few bytes.
            try 
            {
                // use encryption mode using specified key and IV.
                cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            }
            catch(InvalidAlgorithmParameterException iape)
            {
                System.out.println(
                    "cipher.init: InvalidAlgorithmParameterException.");
                iape.printStackTrace();
            }
          
            // Ask the cipher how big the output will be. 
            // Depending on the padding there will be more 
            // output bytes than the sum of input bytes.
            @SuppressWarnings("unused")
			int outLength = cipher.getOutputSize(text1.length);
			byte [] encr1 = cipher.update(text1);
			return Base64.encodeBase64(encr1);
        }
		catch (NoSuchAlgorithmException nsae)
        {
            System.out.println("No such algorithm!\n");
            nsae.printStackTrace();
        }
        catch (InvalidKeySpecException nsae)
        {
            System.out.println("No such algorithm!\n");
            nsae.printStackTrace();
        }
        catch (NoSuchPaddingException nspe)
        {
            System.out.println("No such padding!\n");
            nspe.printStackTrace();
        }
        catch (NoSuchProviderException nspre)
        {
            System.out.println("No such provider found!\n");
            nspre.printStackTrace();
        }
        catch (InvalidKeyException ike)
        {
            System.out.println("Invalidkey Exception!\n");
            ike.printStackTrace();
        }
        
		return null;
    }

}
