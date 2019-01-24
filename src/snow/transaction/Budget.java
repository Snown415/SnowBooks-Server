package snow.transaction;

import java.io.Serializable;
import java.util.LinkedList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.chart.PieChart.Data;
import lombok.Getter;
import lombok.Setter;

public class Budget implements Serializable {

	private static final long serialVersionUID = -8239095937973633337L;
	
	private @Getter @Setter String name;
	private @Getter @Setter String description;
	private @Getter @Setter Double target;
	private @Getter @Setter Double additions;
	private @Getter @Setter Double remainder;
	private @Getter LinkedList<Transaction> transactions;
	
	private transient @Getter @Setter ObservableMap<String, Data> data = FXCollections.observableHashMap();

	public Budget(String name, String desc, Double target) {
		setName(name);
		setDescription(desc);
		setTarget(target);
		setAdditions(0.0);
		setRemainder(target);
		transactions = new LinkedList<>();
	}

	public void validate() {
		if (transactions == null)
			transactions = new LinkedList<>();

		if (data == null) {
			data = FXCollections.observableHashMap();
		}
	}
	
	public void removeTransaction(Transaction t) {
		if (t.getAmount() < 0) 
			additions -= t.getAmount();
		else 
			additions -= t.getSavingAmount();
		
		transactions.remove(t);
	}
	
}
