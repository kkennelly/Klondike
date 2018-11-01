package edu.ycp.cs201.cards.gui;

import edu.ycp.cs201.cards.Rank;
import edu.ycp.cs201.cards.Suit;

public class CardImageKey {
	private final Suit suit;
	private final Rank rank;
	
	public CardImageKey(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
	}
	
	@Override
	public int hashCode() {
		return 43*suit.hashCode() + rank.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof CardImageKey)) {
			return false;
		}
		CardImageKey other = (CardImageKey) obj;
		return this.suit == other.suit && this.rank == other.rank;
	}
}