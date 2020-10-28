package auction;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class Site {
	private static final String JSON_KEY_SITE_NAME = "name";
	private static final String JSON_KEY_SITE_BIDDERS = "bidders";
	private static final String JSON_KEY_SITE_FLOOR = "floor";
	
	private String name;
	private Set<String> bidders = new HashSet<>();
	private double floor;
	
	public Site(JSONObject siteJSONObject) {
		this.name = siteJSONObject.getString(JSON_KEY_SITE_NAME);
		
		JSONArray siteBiddersJSONArray = siteJSONObject.getJSONArray(JSON_KEY_SITE_BIDDERS);
		for (int i = 0; i < siteBiddersJSONArray.length(); i++) {
			String bidder = siteBiddersJSONArray.getString(i);
			bidders.add(bidder);
		}
		
		this.floor = siteJSONObject.getDouble(JSON_KEY_SITE_FLOOR);
	}
	
	public Site(String name, Set<String> bidders, double floor) {
		this.name = name;
		this.bidders.addAll(bidders);
		this.floor = floor;
	}

	public String getName() {
		return name;
	}

	public Set<String> getBidders() {
		return bidders;
	}

	public double getFloor() {
		return floor;
	}
	
	public boolean isBidderAllowed(String bidderName) {
		return bidders.contains(bidderName);
	}
	
	public boolean isBidValid(double bidValue) {
		return bidValue >= floor;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) 
			return true;
	    if (obj == null) 
	    	return false;
	    if (this.getClass() != obj.getClass()) 
	    	return false;
	    
	    Site site = (Site) obj;
	    return this.name.equals(site.name);
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
}
