package snow.packet.impl;

import snow.packet.Packet;
import snow.packet.PacketProcessor;
import snow.packet.PacketType;
import snow.session.User;
import snow.transaction.Budget;

public class BudgetPacket extends Packet {

	public BudgetPacket(String ip, Object[] data) {
		super(PacketType.BUDGET, ip, data);
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
	
	private Object[] add() {
		User user = getUser();

		if (user == null) {
			return new Object[] { type.getPacketId(), ordinal, false, "Session not found." };
		}
		
		String name = (String) data[2];
		String desc = (String) data[3];
		Double amount = (Double) data[4];
		
		Budget b = new Budget(name, desc, amount);
		
		if (user.getBudgets().containsKey(b.getName())) {
			
			return new Object[] { type.getPacketId(), ordinal, false, b.getName() + " already exists." };
		}
		
		user.getBudgets().put(b.getName(), b);
		user.save();

		return new Object[] { type.getPacketId(), ordinal, true };
	}
	
	private Object[] remove() {
		User user = getUser();

		if (user == null) {
			return new Object[] { type.getPacketId(), ordinal, false,
					"Session not found." };
		}

		String id = (String) data[2];
		
		if (id.equals("REMOVEALLDATA")) {
			user.getBudgets().clear();
			return new Object[] { type.getPacketId(), ordinal, true, id };
		}

		if (user.getBudgets().containsKey(id)) {
			user.getBudgets().remove(id);
			user.save();
			return new Object[] { type.getPacketId(), ordinal, true, id };
		}

		return new Object[] { type.getPacketId(), ordinal, false };
	}
	
	private Object[] send() {
		User user = getUser();

		if (user == null) {
			return new Object[] { type.getPacketId(), ordinal, false, "Session not found." };
		}

		return new Object[] { type.getPacketId(), ordinal, true, user.getBudgets().values().toArray() };
	}

	@Override
	public void debug() {

	}

}
