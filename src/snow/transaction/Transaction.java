package snow.transaction;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import snow.session.User;

public class Transaction implements Serializable {

	private static final long serialVersionUID = 8934627816010830377L;

	private String[] valueKeys = { "type", "currencytype", "budget", "date", "id", "recipient", "email", "phone",
			"amount", "saving%", "saving" };

	private @Getter @Setter String type, currencyType, budget, name, recipient, email, phone;
	private @Getter @Setter LocalDate date;
	private @Getter @Setter Double amount, savingPercent, savingAmount, profit;

	private @Getter @Setter String month;
	private @Getter @Setter int day;

	public Transaction(User user, Object[] data) {

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
		setAmount(determineAmount((double) data[10]));
		setSavingPercent((double) data[11]);
		setSavingAmount((double) data[12]);
		setProfit(getAmount() - getSavingAmount());
		setMonth(((LocalDate) date).getMonth().toString());
		setDay(((LocalDate) date).getDayOfMonth());

		if (budget != null && !budget.isEmpty()) {
			if (user.getBudgets().containsKey(budget)) {
				Budget b = user.getBudgets().get(budget);
				b.validate();
				b.getTransactions().add(this);

				if (type.contains("Expense")) {
					System.out.println(amount);
					b.setAdditions(b.getAdditions() + amount);
				} else {
					b.setAdditions(b.getAdditions() + savingAmount);
				}

				user.save();
			}
		}
	}

	private Double determineAmount(Double amount) {
		if (amount == null || type == null)
			return 0.0;

		if (type.contains("Expense"))
			return amount * -1;
		else
			return amount;
	}
}
