
public class Customer extends Event {
	String last_name;
	String adr_city;
	String adr_state;

	//Method to add new customer to the eventsList
	public void newCustomer() {
		ShutterflyLTVCalculator.eventsList.put(key, this);
	}
	
	//Method to update customer to the eventsList
	public void update() {
		ShutterflyLTVCalculator.eventsList.replace(key, this);
	}
}