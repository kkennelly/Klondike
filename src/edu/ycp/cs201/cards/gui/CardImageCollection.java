package edu.ycp.cs201.cards.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import edu.ycp.cs201.cards.Card;
import edu.ycp.cs201.cards.Rank;
import edu.ycp.cs201.cards.Suit;

public class CardImageCollection {
	private Map<CardImageKey, BufferedImage> imageMap;
	private BufferedImage backImage;
	
	public CardImageCollection() {
		imageMap = new HashMap<CardImageKey, BufferedImage>();
		loadImages();
	}
	
	public BufferedImage getFrontImage(Card card) {
		Suit suit = card.getSuit();
		Rank rank = card.getRank();
		BufferedImage image = imageMap.get(new CardImageKey(suit, rank));
		if (image == null) {
			throw new IllegalArgumentException("No card image for " + suit + "/" + rank);
		}
		return image;
	}
	
	public BufferedImage getBackImage() {
		return backImage;
	}

	private void loadImages() {
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				CardImageKey key = new CardImageKey(suit, rank);
				String fileName = toFileName(suit, rank);
				String resourceName = "edu/ycp/cs201/cards/gui/res/" + fileName;
				BufferedImage image = loadImage(resourceName);
				imageMap.put(key, image);
			}
		}
		backImage = loadImage("edu/ycp/cs201/cards/gui/res/back-sm.png");
	}

	private BufferedImage loadImage(String resourceName) {
		BufferedImage image;
		try {
			System.out.println("Loading " + resourceName);
			URL resource = this.getClass().getClassLoader().getResource(resourceName);
			image =
					ImageIO.read(resource);
		} catch (IOException e) {
			throw new IllegalStateException("Could not load " + resourceName);
		}
		return image;
	}

	private String toFileName(Suit suit, Rank rank) {
		return convertRank(rank) + "_of_" + suit.getMemberName().toLowerCase() + ".png";
	}
	
	private static final Map<String, String> RANK_CONVERSIONS = new HashMap<String, String>();
	static {
		RANK_CONVERSIONS.put("two", "2");
		RANK_CONVERSIONS.put("three", "3");
		RANK_CONVERSIONS.put("four", "4");
		RANK_CONVERSIONS.put("five", "5");
		RANK_CONVERSIONS.put("six", "6");
		RANK_CONVERSIONS.put("seven", "7");
		RANK_CONVERSIONS.put("eight", "8");
		RANK_CONVERSIONS.put("nine", "9");
		RANK_CONVERSIONS.put("ten", "10");
	}

	private String convertRank(Rank rank) {
		String rankLc = rank.getMemberName().toLowerCase();
		return (RANK_CONVERSIONS.containsKey(rankLc)) ? RANK_CONVERSIONS.get(rankLc) : rankLc;
	}
}
