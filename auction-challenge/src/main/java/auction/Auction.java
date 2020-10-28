package auction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

public class Auction {
	
	private static final String CONFIG_PATH = "/auction/config.json";
	
	private static final String JSON_KEY_AUCTION_SITE = "site";
	private static final String JSON_KEY_AUCTION_UNITS = "units";
	private static final String JSON_KEY_AUCTION_BIDS = "bids";
	
	private Config config;
	private String siteName;
	private Set<String> units;
	private List<Bid> bids = new ArrayList<>();
	
	public Auction(Config config, JSONObject auctionJSONObject) {
		this.config = config;
		
		siteName = auctionJSONObject.getString(JSON_KEY_AUCTION_SITE);
		
		JSONArray unitsJSONArray = auctionJSONObject.getJSONArray(JSON_KEY_AUCTION_UNITS);
		units = unitsJSONArray.toList().stream().map(o -> String.valueOf(o)).collect(Collectors.toSet());
		
		JSONArray bidsJSONArray = auctionJSONObject.getJSONArray(JSON_KEY_AUCTION_BIDS);
		for (int i = 0; i < bidsJSONArray.length(); i++) {
			JSONObject bidJSONObject = bidsJSONArray.getJSONObject(i);
			bids.add(new Bid(this, bidJSONObject));
		}
	}
	
	public String getSiteName() {
		return siteName;
	}
	
	public Site getSite() {
		return config.getSite(siteName);
	}
	
	public Set<String> getUnits() {
		return units;
	}
	
	public List<Bid> getBids() {
		return bids;
	}
	
	public Bidder getBidderByName(String bidderName) {
		return config.getBidder(bidderName);
	}
	
	private static String readFileToString(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, StandardCharsets.UTF_8);
	}
	
	private static Config loadAuctionConfig(String configPath) throws IOException {
		String configStringContent = readFileToString(configPath);
		JSONObject configJSONObject = new JSONObject(configStringContent);
		return new Config(configJSONObject);
	}
	
	private static JSONArray loadAuctionInput() {
		String inputStringContent = "";
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			inputStringContent += scanner.next();
		}
		scanner.close();
		
		return new JSONArray(inputStringContent);
	}
	
	public JSONArray executeAuction() {
		HashMap<String, Bid> siteBidResults = new HashMap<>();
		JSONArray siteBidResultsJSONArray = new JSONArray();
		
		Site site = getSite();
		if (site == null) {
			return siteBidResultsJSONArray;
		}
		
		Set<String> units = getUnits();
		
		List<Bid> bids = getBids();
		for (Bid bid : bids) {
			
			String bidderName = bid.getBidderName();
			String unit = bid.getUnit();
			Bidder bidder = bid.getBidder();
			
			if (!site.isBidderAllowed(bidderName) || !units.contains(unit) || bidder == null) {
				continue;
			}
			
			double adjustedBidValue = bid.getAdjustedBidValue();
			if (!site.isBidValid(adjustedBidValue)) {
				continue;
			}
			
			Bid currentBid = siteBidResults.get(unit);
			if (currentBid == null) {
				siteBidResults.put(unit, bid);
			} else {
				double currentAdjustedBidValue = currentBid.getAdjustedBidValue();
				if (adjustedBidValue > currentAdjustedBidValue) {
					siteBidResults.put(unit, bid);
				}
			}
		}
		
		siteBidResults.values().stream().forEach(v -> siteBidResultsJSONArray.put(v.getJSONObject()));
		return siteBidResultsJSONArray;
	}
	
	public static JSONArray executeAuctions(Config config, JSONArray input) {
		JSONArray auctionResultsJSONArray = new JSONArray();
		
		for (int i = 0; i < input.length(); i++) {
			JSONObject auctionJSONObject = input.getJSONObject(i);
			
			Auction auction = new Auction(config, auctionJSONObject);
			
			JSONArray siteBidResultsJSONArray = auction.executeAuction();
			
			auctionResultsJSONArray.put(siteBidResultsJSONArray);
		}
		
		return auctionResultsJSONArray;
	}
	
	public static void main(String[] args) throws IOException {
		Config config = loadAuctionConfig(CONFIG_PATH);
		
		JSONArray input = loadAuctionInput();
		
		JSONArray auctionResultsJSONArray = executeAuctions(config, input);
		
		System.out.println(auctionResultsJSONArray.toString());
	}

}
