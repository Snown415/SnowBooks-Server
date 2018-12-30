package snow.session;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.Getter;
import lombok.Setter;
import snow.Serialize;

public class User implements Serializable {
	
	private static final long serialVersionUID = 3903711783985379771L;
	
	private @Getter @Setter String securityKey;
	private @Getter @Setter String vectorKey;
	
	private @Getter @Setter byte[] key;
	private @Getter @Setter byte[] vector;
	
	private @Getter @Setter String username;
	private @Getter @Setter String password; // Encrypted
	
	private @Getter @Setter boolean isGuest = true; // Guest account; used before registration / login
	
	public User(String username, String password) {
		setUsername(username);
		
		setSecurityKey(generateSecurityCode());
		setVectorKey(generateSecurityCode());
		setKey(generateArray(securityKey));
		setVector(generateArray(vectorKey));
		
		if (vector == null || key == null) {
			System.err.println("Invalid encryption key or vector!");
			System.err.println(username + " is NOT using password encyption!");
			setPassword(password);
			return;
		}
		
		String encryption = encryptPassword(password);
		setPassword(encryption);
		Serialize.saveUser(this);
	}
	
	public String encryptPassword(String password) {
		try {
			IvParameterSpec iv = new IvParameterSpec(vector);
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(password.getBytes());
			return Base64.getEncoder().encodeToString(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	private byte[] generateArray(String value) {
		byte[] array;
		
		try {
			array = value.getBytes("UTF-8");
			return array;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
			
		return null;
	}
	
	public String generateSecurityCode() {
		String characters = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int length = 16;
		Random range = new Random();
		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = characters.charAt(range.nextInt(characters.length()));
		}
		return new String(text);
	}
	
	/*
	 * private String decryptPassword() { try { IvParameterSpec iv = new
	 * IvParameterSpec(vector); SecretKeySpec skeySpec = new SecretKeySpec(key,
	 * "AES");
	 * 
	 * Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	 * cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv); byte[] original =
	 * cipher.doFinal(Base64.getDecoder().decode(password)); return new
	 * String(original); } catch (Exception ex) { ex.printStackTrace(); }
	 * 
	 * return null; }
	 */
}
