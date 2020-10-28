package auction;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class Config {
	private static final String JSON_KEY_SITES_ARRAY = "sites";
	private static final String JSON_KEY_BIDDERS_ARRAY = "bidders";
	
	private HashMap<String, Site> sites;
	private HashMap<String, Bidder> bidders;
	
	public Config(JSONObject configuration) {
		this.sites = getSitesFromConfig(configuration);
		this.bidders = getBiddersFromConfig(configuration);
	}
	
	private HashMap<String, Site> getSitesFromConfig(JSONObject configuration) {
		HashMap<String, Site> sites = new HashMap<>();
		
		JSONArray sitesJSONArray = configuration.getJSONArray(JSON_KEY_SITES_ARRAY);
		for (int i = 0; i < sitesJSONArray.length(); i++) {
			JSONObject siteJSONObject = sitesJSONArray.getJSONObject(i);
			Site site = new Site(siteJSONObject);
			sites.put(site.getName(), site);
		}
		
		return sites;
	}
	
	private HashMap<String, Bidder> getBiddersFromConfig(JSONObject configuration) {
		HashMap<String, Bidder> bidders = new HashMap<>();
		
		JSONArray bidderJSONArray = configuration.getJSONArray(JSON_KEY_BIDDERS_ARRAY);
		for (int i = 0; i < bidderJSONArray.length(); i++) {
			JSONObject bidderJSONObject = bidderJSONArray.getJSONObject(i);
			Bidder bidder = new Bidder(bidderJSONObject);
			bidders.put(bidder.getName(), bidder);
		}
		
		return bidders;
	}
	
	public Site getSite(String siteName) {
		return sites.get(siteName);
	}
	
	public Bidder getBidder(String bidderName) {
		return bidders.get(bidderName);
	}
}
