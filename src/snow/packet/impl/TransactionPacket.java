package snow.packet.impl;

import snow.packet.Packet;
import snow.packet.PacketProcessor;
import snow.packet.PacketType;
import snow.session.User;
import snow.transaction.Budget;
import snow.transaction.Transaction;

public class TransactionPacket extends Packet {

	public TransactionPacket(String ip, Object[] data) {
		super(PacketType.TRANSACTION, ip, data);
	}
	
	private int ordinal;

	@Override
	public Object[] process() {

		ordinal = (int) data[1];
		PacketProcessor p = PacketProcessor.values()[ordinal];

		switch (p) {
		case ADD:
			return add();
		case REMOVE:
			return remove();
		case SEND:
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

		return new Object[] { type.getPacketId(), ordinal, true, user.getTransactions().values().toArray() };
	}

	private Object[] remove() {
		User user = getUser();

		if (user == null) {
			return new Object[] { type.getPacketId(), ordinal, false,
					"Session not found." };
		}

		String id = (String) data[2];
		
		if (id == null || id.equals("null")) {
			if (user.getTransactions().containsKey("")) {
				user.getTransactions().remove("");
				return new Object[] { type.getPacketId(), ordinal, true, "" };
			}
			return new Object[] { type.getPacketId(), ordinal, false };
		}
		
		if (id.equals("REMOVEALLDATA")) {
			user.getTransactions().clear();
			return new Object[] { type.getPacketId(), ordinal, true, id };
		}

		if (user.getTransactions().containsKey(id)) {
			Transaction t = user.getTransactions().get(id);
			
			if (t.getBudget() != null && !t.getBudget().equals("None")) {
				Budget b = user.getBudgets().get(t.getBudget());
				
				if (!b.getTransactions().contains(t)) {
					System.err.println("This transaction doesn't exist in budget " + t.getBudget());
				} else {
					b.removeTransaction(t);
				}
			}
			
			user.getTransactions().remove(id);
			user.save();
			return new Object[] { type.getPacketId(), ordinal, true, id };
		}

		return new Object[] { type.getPacketId(), ordinal, false };
	}

	private Object[] add() {
		User user = getUser();

		if (user == null) {
			return new Object[] { type.getPacketId(), ordinal, false, "Session not found." };
		}
		
		Transaction t = new Transaction(user, data);
		
		if (user.getTransactions().containsKey(t.getName())) {
			
			return new Object[] { type.getPacketId(), ordinal, false, t.getName() + " already exists." };
		}
		
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
