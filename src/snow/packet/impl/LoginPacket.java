package snow.packet.impl;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import snow.Serialize;
import snow.Server;
import snow.packet.Packet;
import snow.packet.PacketType;
import snow.session.User;

public class LoginPacket extends Packet {
	
	private String ip;

	public LoginPacket(String ip, Object[] data) {
		super(PacketType.LOGIN, data);
		this.ip = ip;
	}

	@Override
	public Object[] process() {
		String username = (String) getData()[1];
		String password = (String) getData()[2];
		
		User user;
		
		if (Server.getActiveUsers().containsKey(username)) {
			return new Object[] { type.getPacketId(), false, "The user '" + username + "' is already active." };
		} else {
			
			user = Serialize.loadUser(username);
			
			if (user == null) {
				return new Object[] { type.getPacketId(), false, "'" + username + "' isn't in use, please click \"Register\" instead." };
			}
			
			String attempt = encryptPassword(password, user.getSecurityKey(), user.getVectorKey());
			
			if (attempt.equals(user.getPassword())) {
				user.setCurrentIP(ip);
				user.activateUser();
				return new Object[] { type.getPacketId(), true, username };
			} else {
				return new Object[] { type.getPacketId(), false, "Invalid credentials, please try again." };
			}
		}
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

	@Override
	public void debug() {
		System.out.println("Expecting String: Username, String: Password");
		// debugPacket(); Don't debug login; sensitive information shouldn't be debugged
	}

}
