package snow.transaction;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class Budget implements Serializable {
	
	private static final long serialVersionUID = -8239095937973633337L;
	
	private @Getter @Setter String name;
	private @Getter @Setter String description;
	private @Getter @Setter Double target;
	
	public Budget(String name, String desc, Double target) {
		setName(name);
		setDescription(desc);
		setTarget(target);
	}

}
