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
			return new Object[] { type.getPacketId(), false, "Session not found." };
		}

		return new Object[] { type.getPacketId(), ordinal, user.getTransactions() };
	}

	private Object[] remove() {
		User user = getUser();

		if (user == null) {
			return new Object[] { type.getPacketId(), ordinal, false,
					"Session not found." };
		}

		String id = (String) data[1];
		
		if (id.equals("REMOVEALLDATA")) {
			user.getTransactions().clear();
			return new Object[] { type.getPacketId(), ordinal, true };
		}

		if (user.getTransactions().containsKey(id)) {
			user.getTransactions().remove(id);
			return new Object[] { type.getPacketId(), ordinal, true };
		}

		return new Object[] { type.getPacketId(), ordinal, false };
	}

	private Object[] add() {
		User user = getUser();

		if (user == null) {
			return new Object[] { type.getPacketId(), ordinal, false, "Session not found." };
		}
		System.out.println("Adding Transaction...");
		
		Transaction t = new Transaction(data);
		user.getTransactions().put(t.getId(), t);
		user.save();

		return new Object[] { type.getPacketId(), ordinal, true };
	}

	@Override
	public void debug() {
		StringBuilder sb = new StringBuilder();
		for (Object o : data) {
			sb.append(o == null ? "null " : o.getClass().getSimpleName() + " ");
		}
		System.out.println(sb.toString());
	}

}
