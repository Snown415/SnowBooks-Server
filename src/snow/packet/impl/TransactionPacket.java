package snow.packet.impl;

import snow.packet.Packet;
import snow.packet.PacketType;
import snow.session.User;
import snow.transaction.Transaction;

public class TransactionPacket extends Packet {

	public TransactionPacket(String ip, Object[] data) {
		super(PacketType.TRANSACTION, ip, data);
	}

	private enum Processer {
		ADD_TRANSACTION, REMOVE_TRANSACTION, SEND_TRANSACTIONS
	}
	
	private int ordinal;

	@Override
	public Object[] process() {

		ordinal = (int) data[1];
		Processer p = Processer.values()[ordinal];

		switch (p) {
		case ADD_TRANSACTION:
			return add();
		case REMOVE_TRANSACTION:
			return remove();
		case SEND_TRANSACTIONS:
			return send();
		default:
			break;

		}

		return new Object[] { type.getPacketId(), ordinal, false };
	}

	private Object[] send() {
		User user = getUser();

		if (user == null) {
			return new Object[] { type.getPacketId(), ordinal, false, "Session not found." };
		}
		
		System.out.println("Sending packet. " + user.getTransactions().values().size());

		return new Object[] { type.getPacketId(), ordinal, true, user.getTransactions().values().toArray() };
	}

	private Object[] remove() {
		User user = getUser();

		if (user == null) {
			return new Object[] { type.getPacketId(), ordinal, false,
					"Session not found." };
		}

		String id = (String) data[2];
		
		if (id == null) {
			return new Object[] { type.getPacketId(), ordinal, false };
		}
		
		if (id.equals("REMOVEALLDATA")) {
			user.getTransactions().clear();
			return new Object[] { type.getPacketId(), ordinal, true, id };
		}

		if (user.getTransactions().containsKey(id)) {
			user.getTransactions().remove(id);
			return new Object[] { type.getPacketId(), ordinal, true, id };
		}

		return new Object[] { type.getPacketId(), ordinal, false };
	}

	private Object[] add() {
		User user = getUser();

		if (user == null) {
			return new Object[] { type.getPacketId(), ordinal, false, "Session not found." };
		}
		
		Transaction t = new Transaction(data);
		
		if (user.getTransactions().containsKey(t.getName())) {
			return new Object[] { type.getPacketId(), ordinal, false, t.getName() + " already exists." };
		}
		
		System.out.println("Adding Transaction " + t.getName());
		
		user.getTransactions().put(t.getName(), t);
		user.save();

		return new Object[] { type.getPacketId(), ordinal, true };
	}

	@Override
	public void debug() {
		StringBuilder sb = new StringBuilder();
		for (Object o : data) {
			sb.append(o == null ? "null " : o.toString() + " ");
		}
		
		System.out.println(sb.toString());
	}

}
