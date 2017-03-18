
public class Order extends Event {
	String customerid;
	double amount; // stores amount in USD

	//Method to add new orders to the eventsList
	public void newOrder() {
		ShutterflyLTVCalculator.eventsList.put(key, this);
	}
	
	//Method to update orders in the eventsList
	public void update() {
		ShutterflyLTVCalculator.eventsList.replace(key, this);
	}
}
