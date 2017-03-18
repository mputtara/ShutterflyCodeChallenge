
public class CustomerLTV {
	
	//CustomerLTV constructor
	public CustomerLTV(String customerid, double ltv) {
		this.customerid=customerid;
		this.ltv=ltv;
	}
	String customerid;
	double ltv;	
	
	//Overriding the printing format for the output file
	public String toString()
	{return "{CustomerID: "+customerid+", LTV: "+ltv+"}";}
}