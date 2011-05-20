package com.trilead.ssh2.packets;

import java.io.IOException;

/**
 * PacketUserauthRequestPassword.
 * 
 * @author Christian Plattner, plattner@trilead.com
 * @version $Id: PacketUserauthRequestPassword.java,v 1.1 2007/10/15 12:49:55
 *          cplattne Exp $
 */
public class PacketUserauthRequestPassword {
	byte[] payload;

	String userName;
	String serviceName;
	String password;

	public PacketUserauthRequestPassword(byte payload[], int off, int len)
			throws IOException {
		this.payload = new byte[len];
		System.arraycopy(payload, off, this.payload, 0, len);

		TypesReader tr = new TypesReader(payload, off, len);

		int packet_type = tr.readByte();

		if (packet_type != Packets.SSH_MSG_USERAUTH_REQUEST)
			throw new IOException("This is not a SSH_MSG_USERAUTH_REQUEST! ("
					+ packet_type + ")");

		userName = tr.readString();
		serviceName = tr.readString();

		String method = tr.readString();

		if (method.equals("password") == false)
			throw new IOException(
					"This is not a SSH_MSG_USERAUTH_REQUEST with type password!");

		/* ... */

		if (tr.remain() != 0)
			throw new IOException("Padding in SSH_MSG_USERAUTH_REQUEST packet!");
	}

	public PacketUserauthRequestPassword(String serviceName, String user,
			String pass) {
		this.serviceName = serviceName;
		this.userName = user;
		this.password = pass;
	}

	public byte[] getPayload() {
		if (payload == null) {
			TypesWriter tw = new TypesWriter();
			tw.writeByte(Packets.SSH_MSG_USERAUTH_REQUEST);
			tw.writeString(userName);
			tw.writeString(serviceName);
			tw.writeString("password");
			tw.writeBoolean(false);
			tw.writeString(password);
			payload = tw.getBytes();
		}
		return payload;
	}
}
