package auction;

import org.json.JSONObject;

public class Bidder {
	private static final String JSON_KEY_BIDDER_NAME = "name";
	private static final String JSON_KEY_BIDDER_ADJUSTMENT_FACTOR = "adjustment";
	
	private String name;
	private double adjustmentFactor;
	
	public Bidder(JSONObject bidderJSONObject) {
		this.name = bidderJSONObject.getString(JSON_KEY_BIDDER_NAME);
		this.adjustmentFactor = bidderJSONObject.getDouble(JSON_KEY_BIDDER_ADJUSTMENT_FACTOR);
	}
	
	public Bidder(String name, double adjustmentFactor) {
		this.name = name;
		this.adjustmentFactor = adjustmentFactor;
	}

	public String getName() {
		return name;
	}

	public double getAdjustmentFactor() {
		return adjustmentFactor;
	}
	
	public double getAdjustedBidValue(double bidValue) {
		return bidValue * (1 + this.adjustmentFactor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) 
			return true;
	    if (obj == null) 
	    	return false;
	    if (this.getClass() != obj.getClass()) 
	    	return false;
	    
	    Bidder bidder = (Bidder) obj;
	    return this.name.equals(bidder.name);
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
}
