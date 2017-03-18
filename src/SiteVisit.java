import java.util.HashMap;

public class SiteVisit extends Event {
	String customer_id;
	HashMap<String, String> tags;

	//Method to add new site visit to eventsList
	public void newVisit() {
		ShutterflyLTVCalculator.eventsList.put(key, this);
	}
}
