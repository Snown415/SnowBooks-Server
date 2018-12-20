package snow.packet.impl;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import snow.Serialize;
import snow.packet.Packet;
import snow.packet.PacketType;
import snow.session.User;
import sql.MySQL;

public class LoginPacket extends Packet {
	
	public LoginPacket(PacketType type, Object[] data) {
		super(type, data);
	}

	@Override
	public Object[] process() {
		Object[] object;
		
		String username = (String) getData()[1];
		String password = (String) getData()[2];
		
		if (!MySQL.foundUser(username)) {
			object = new Object[] { getPacketId(), false, "There is no user '" + username + "'; Please click 'Register' instead." };
		} else {
			String[] keys = MySQL.getSecurityKeys(username);
			String key = keys[0];
			String vector = keys[1];
			String attempt = encryptPassword(password, key, vector);
			password = MySQL.getPassword(username);
			
			if (attempt.equals(password)) {
				object = new Object[] { getPacketId(), true, username };
			}else {
				object = new Object[] { getPacketId(), false, "Invalid credentials. Please try again." };
			}
		}
		
		return object;
	}
	
	private String encryptPassword(String password, String k, String v) {
		byte[] vector = v.getBytes();
		byte[] key = k.getBytes();
		
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
	
}
