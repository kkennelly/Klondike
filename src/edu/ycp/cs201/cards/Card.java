package edu.ycp.cs201.cards;

/**
 * Class representing a playing card.
 */
public class Card implements Comparable<Card> {
	private Rank rank;
	private Suit suit;
	
	/**
	 * Constructor.
	 * 
	 * @param rank the card's rank
	 * @param suit the card's suit
	 */
	public Card(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}

	/**
	 * @return the card's {@link Rank}
	 */
	public Rank getRank() {
		return rank;
	}
	
	/**
	 * @return the card's {@link Suit}
	 */
	public Suit getSuit() {
		return suit;
	}
	
	@Override
	public String toString() {
		return rank.toString() + suit.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Card)) {
			return false;
		}
		Card other = (Card) obj;
		return this.compareTo(other) == 0;
	}
	
	@Override
	public int compareTo(Card o) {
		int cmp = this.suit.compareTo(o.suit);
		if (cmp != 0) {
			return cmp;
		}
		return this.rank.compareTo(o.rank);
	}
}
