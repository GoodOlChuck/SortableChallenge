package auction;

import org.json.JSONObject;

public class Bid {
	private static final String JSON_KEY_BID_BIDDER = "bidder";
	private static final String JSON_KEY_BID_UNIT = "unit";
	private static final String JSON_KEY_BID_VALUE = "bid";
	
	private Auction auction;
	private JSONObject jsonObject;
	private String bidderName;
	private String unit;
	private double bidValue;
	
	public Bid(Auction auction, JSONObject bidJSONObject) {
		this.auction = auction;
		jsonObject = bidJSONObject;
		bidderName = bidJSONObject.getString(JSON_KEY_BID_BIDDER);
		unit = bidJSONObject.getString(JSON_KEY_BID_UNIT);
		bidValue = bidJSONObject.getDouble(JSON_KEY_BID_VALUE);
	}
	
	public JSONObject getJSONObject() {
		return jsonObject;
	}
	
	public String getBidderName() {
		return bidderName;
	}
	
	public Bidder getBidder() {
		return auction.getBidderByName(bidderName);
	}
	
	public String getUnit() {
		return unit;
	}
	
	public double getBidValue() {
		return bidValue;
	}
	
	public double getAdjustedBidValue() {
		Bidder bidder = getBidder();
		return bidder.getAdjustedBidValue(bidValue);
	}
}
