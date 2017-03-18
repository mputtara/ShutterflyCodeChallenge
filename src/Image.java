
public class Image extends Event {
	String customer_id;
	String cameraMake;
	String cameraModel;

	//Method to upload Image to the eventsList
	public void upload() {
		ShutterflyLTVCalculator.eventsList.put(key, this);
	}
}