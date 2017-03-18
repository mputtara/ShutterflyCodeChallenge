import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ShutterflyLTVCalculator {
	public static final int SHUTTERFLY_CUSTOMER_LIFESPAN = 10;
	public static HashMap<String, Event> eventsList = new HashMap<>();

	//Method to check whether 2 dates falls within a week
	private static boolean isWithinWeek(Date d1, Date d2) {
		long daysBetween = TimeUnit.DAYS.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);
		Calendar c = Calendar.getInstance();
		c.setTime(d1);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		if (d1.before(d2) && -(daysBetween) <= (7 - dayOfWeek)) {
			return true;
		} else if (d1.after(d2) && daysBetween < dayOfWeek - 1) {
			return true;
		} else if (daysBetween == 0) {
			return true;
		}

		return false;
	}

	//Method to update event in the eventList
	public static void ingest(Event e, HashMap<String, Event> D) {
		D.replace(e.key, e);
	}

	//Method to Return the top x customers with the highest Simple Lifetime Value from data eventsList
	public static List<CustomerLTV> topXSimpleLTVCustomers(int x, HashMap<String, Event> D) {
		List<CustomerLTV> customersLTV = new ArrayList<CustomerLTV>();
		Set<String> keys = D.keySet();
		Iterator<String> keyIterator = keys.iterator();
		while (keyIterator.hasNext()) {
			Event event = D.get(keyIterator.next());
			Event max_order = null;
			int visitsCount = 0;
			double maxExpenditure = 0;
			
			//Finding maxExpenditure for each customer
			if (event instanceof Customer) {
				String customerid = event.key;
				Set<String> keys_order = D.keySet();
				Iterator<String> key_orderIterator = keys_order.iterator();
				while (key_orderIterator.hasNext()) {
					Event event_order = D.get(key_orderIterator.next());
					if (event_order instanceof Order && ((Order) event_order).customerid.equals(customerid)
							&& ((Order) event_order).amount > maxExpenditure) {
						maxExpenditure = ((Order) event_order).amount;
						max_order = event_order;
					}
				}
                
				//Finding the customer's number of visits within a week and Calculating LTV if max_order exists
				if (max_order != null) {
					Set<String> keys_visit = D.keySet();
					Iterator<String> key_visitIterator = keys_visit.iterator();
					while (key_visitIterator.hasNext()) {
						Event event_visit = D.get(key_visitIterator.next());
						if (event_visit instanceof SiteVisit) {
							if (isWithinWeek(event_visit.event_time, max_order.event_time)) {
								visitsCount++;
							}
						}
					}
					double ltv = 52 * (((Order) max_order).amount * visitsCount) * SHUTTERFLY_CUSTOMER_LIFESPAN;
					customersLTV.add(new CustomerLTV(customerid, ltv));
				}
				
				// visits_count, max_expenditure,LTV
				
				
			}
		}
		//Sort the customer based on max LTV
		customersLTV.sort(new Comparator<CustomerLTV>() {
	        @Override
	        public int compare(CustomerLTV ltv2, CustomerLTV ltv1)
	        {
	            return  (int)(ltv1.ltv - ltv2.ltv);
	        }
	    });
		
		//Returns top_x LTV customers
		if (x > customersLTV.size())
			return customersLTV;
		else
			return customersLTV.subList(0, x);
	}

}