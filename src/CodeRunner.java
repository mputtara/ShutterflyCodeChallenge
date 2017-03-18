import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class CodeRunner {
	public static final String CUSTOMER = "CUSTOMER";
	public static final String SITE_VISIT = "SITE_VISIT";
	public static final String IMAGE = "IMAGE";
	public static final String ORDER = "ORDER";
	public static final String EVENT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	
	public static void main(String... args) throws Exception {
		SimpleDateFormat inFormat = new SimpleDateFormat(EVENT_DATE_FORMAT);
		//read the input file and convert as json array
		File inputFile = new File("input//input.txt");
		FileReader file = new FileReader(inputFile);
		BufferedReader fileReader = new BufferedReader(file);
		String jsonData = "", line = "";
		while ((line = fileReader.readLine()) != null) {
			jsonData += line + "\n";
		}
		fileReader.close();
		// System.out.println(jsonData);
		JSONArray array = new JSONArray(jsonData);
		
		//reading json array and storing as HashMap
		for (int i = 0; i < array.length(); i++) {
			switch (array.getJSONObject(i).getString("type").toUpperCase()) {
			case CUSTOMER: {
				Customer customer = new Customer();
				customer.key = array.getJSONObject(i).getString("key");
				customer.event_time = inFormat.parse(array.getJSONObject(i).getString("event_time"));
				customer.adr_city = array.getJSONObject(i).getString("adr_city");
				customer.last_name = array.getJSONObject(i).getString("last_name");
				customer.adr_state = array.getJSONObject(i).getString("adr_state");
				if ("NEW".equalsIgnoreCase(array.getJSONObject(i).getString("verb"))) {
					customer.newCustomer();
				} else if ("UPDATE".equalsIgnoreCase(array.getJSONObject(i).getString("verb"))) {
					customer.update();
				}

			}
				break;
			case SITE_VISIT: {
				SiteVisit siteVisit = new SiteVisit();
				siteVisit.key = array.getJSONObject(i).getString("key");
				siteVisit.customer_id = array.getJSONObject(i).getString("customer_id");
				siteVisit.event_time = inFormat.parse(array.getJSONObject(i).getString("event_time"));
				JSONObject tagsJson = array.getJSONObject(i).getJSONObject("tags");
				siteVisit.tags = new HashMap<>();
				Iterator<String> keys = tagsJson.keys();
				String key="";
				while (keys.hasNext()) {
					key=keys.next();
					siteVisit.tags.put(key, tagsJson.getString(key));
				}
				if("NEW".equalsIgnoreCase(array.getJSONObject(i).getString("verb")))
				{
					siteVisit.newVisit();
				}
				
			}
				break;
			case IMAGE: {
				Image image=new Image();
				image.cameraMake=array.getJSONObject(i).getString("camera_make");
				image.cameraModel=array.getJSONObject(i).getString("camera_model");
				image.customer_id=array.getJSONObject(i).getString("customer_id");
				image.event_time=inFormat.parse(array.getJSONObject(i).getString("event_time"));
				image.key=array.getJSONObject(i).getString("key");
				if("UPLOAD".equalsIgnoreCase(array.getJSONObject(i).getString("verb")))
				{
					image.upload();
				}
				
			}
				break;
			case ORDER: {
				Order order=new Order();
				order.customerid=array.getJSONObject(i).getString("customer_id");
				order.event_time=inFormat.parse(array.getJSONObject(i).getString("event_time"));
				order.key=array.getJSONObject(i).getString("key");
				order.amount=Double.parseDouble(array.getJSONObject(i).getString("total_amount").replaceAll("USD", "").trim());
				if ("NEW".equalsIgnoreCase(array.getJSONObject(i).getString("verb"))) {
					order.newOrder();
				} else if ("UPDATE".equalsIgnoreCase(array.getJSONObject(i).getString("verb"))) {
					order.update();
				}
			}
				break;
			default:
				System.out.println("This type is not yet implemented");

			}
		}
		
		//read the input from the console
		Scanner scanner = new Scanner(System.in);				
		System.out.print("Enter the number of top_x LTV customers to be viewed: \n");
		int top_x = scanner.nextInt();		
		
		//write the output to the file
		File f = new File("output//output.txt");		   
		FileOutputStream fos = new FileOutputStream(f);		   
		PrintWriter pw = new PrintWriter(fos);
		//Returns the top x customers with the highest Simple Lifetime Value from as  data eventsList
		String str = ShutterflyLTVCalculator.topXSimpleLTVCustomers(top_x, ShutterflyLTVCalculator.eventsList).toString();
		pw.write(str);
		
		//System.out.println("Customer LTV List: \n" + ShutterflyLTVCalculator.topXSimpleLTVCustomers(top_x, ShutterflyLTVCalculator.eventsList));
		
		scanner.close();
		pw.close();
		        
		
	
	}
	
}