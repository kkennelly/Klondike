package edu.ycp.cs201.cards;

public class Util {
	/**
	 * Add all 52 cards to the given Pile.
	 * @param pile the Pile
	 */
	public static void addAllCards(Pile pile) {
		Suit[] suits = Suit.values();
		Rank[] ranks = Rank.values();
		for (Suit s : suits) {
			for (Rank r : ranks) {
				pile.addCard(new Card(r, s));
			}
		}
	}
}
