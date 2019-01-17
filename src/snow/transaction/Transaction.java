package snow.transaction;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

public class Transaction implements Serializable {

	private static final long serialVersionUID = 8934627816010830377L;

	private String[] valueKeys = { "type", "currencytype", "budget", "date", "id", "recipient", "email", "phone",
			"amount", "saving%", "saving" };
	
	private @Getter @Setter String type, currencyType, budget, name, recipient, email, phone;
	private @Getter @Setter LocalDate date;
	private @Getter @Setter double amount, savingPercent, savingAmount, profit;
	
	private @Getter @Setter String month;
	private @Getter @Setter int day;

	public Transaction(Object[] data) {
		
		if (data.length - 2 != valueKeys.length) {
			System.err.println("Invalid data length; continuing to process...");
		}
		
		setType((String) data[2]);
		setCurrencyType((String) data[3]);
		setBudget((String) data[4]);
		setDate((LocalDate) data[5]);
		setName((String) data[6]);
		setRecipient((String) data[7]);
		setEmail((String) data[8]);
		setPhone((String) data[9]);
		setAmount((double) data[10]);
		setSavingPercent((double) data[11]);
		setSavingAmount((double) data[12]);
		setProfit(getAmount() - getSavingAmount());
		setMonth(((LocalDate) date).getMonth().toString());
		setDay(((LocalDate) date).getDayOfMonth());
	}
}
