package snow.session;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.Getter;
import lombok.Setter;
import snow.Serialize;
import snow.Server;
import snow.transaction.Budget;
import snow.transaction.Transaction;

public class User implements Serializable {
	
	private static final long serialVersionUID = 3903711783985379771L;
	
	private @Getter @Setter String securityKey;
	private @Getter @Setter String vectorKey;
	
	private @Getter @Setter byte[] key;
	private @Getter @Setter byte[] vector;
	
	private transient @Getter @Setter String currentIP;
	private @Getter @Setter String username;
	private @Getter @Setter String password; // Encrypted
	
	private @Getter @Setter long lastPacket;
	private @Getter @Setter boolean timedOut;
	
	private @Getter LinkedHashMap<String, Transaction> transactions;
	private @Getter LinkedHashMap<String, Budget> budgets;
	
	public User(String username, String password, String ip) {
		setUsername(username);
		setCurrentIP(ip);
		transactions = new LinkedHashMap<>();
		budgets = new LinkedHashMap<>();
		
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
		activateUser();
		save();
	}
	
	public void activateUser() {
		Server.getActiveUsers().put(getUsername(), this);
		Server.getActiveSessions().put(currentIP, this);
	}
	
	public void deactivateUser() {
		Server.getActiveUsers().remove(getUsername());
		Server.getActiveSessions().remove(currentIP);
	}
	
	public void validateIntegrity() {
		if (transactions == null)
			transactions = new LinkedHashMap<>();
		
		if (budgets == null)
			budgets = new LinkedHashMap<>();
	}
	
	public void save() {
		validateIntegrity();
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
