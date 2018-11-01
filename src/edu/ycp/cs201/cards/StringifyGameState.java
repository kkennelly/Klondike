package edu.ycp.cs201.cards;

/**
 * Convert the game state (as stored in the {@link KlondikeModel})
 * to and from strings.  This is useful for testing, since it makes
 * it easy to encode a complete game state as a string.
 * This could also allow games to be saved to and loaded from
 * files.
 */
public class StringifyGameState {
	private static final char SEPARATOR = '!'; // printable character with the lowest character code (!)
	private static final char FIRST_CARD = SEPARATOR+1;
	private static final int EXPOSE_INDEX_NUM_DIGITS = 8; // much larger than it needs to be, just in case
	private static final Card[] CARDS = new Card[52];
	static {
		Suit[] suits = Suit.values();
		Rank[] ranks = Rank.values();
		for (int i = 0; i < 52; i++) {
			CARDS[i] = new Card(ranks[i%13], suits[i/13]);
		}
	}
	
	/**
	 * Convert the game state stored in the given {@link KlondikeModel} object
	 * to a string.
	 * 
	 * @param model the game state
	 * @return a string containing the encoded game state
	 */
	public static String modelToString(KlondikeModel model) {
		StringBuilder buf = new StringBuilder();
		
		buf.append(pileToString(model.getMainDeck()));
		buf.append(SEPARATOR);
		buf.append(pileToString(model.getWastePile()));
		for (int i = 0; i < 4; i++) {
			buf.append(SEPARATOR);
			buf.append(pileToString(model.getFoundationPile(i)));
		}
		for (int i = 0; i < 7; i++) {
			buf.append(SEPARATOR);
			buf.append(pileToString(model.getTableauPile(i)));
		}
		
		return buf.toString();
	}
	
	/**
	 * Decode a string (which was returned by some previous call to
	 * {@link #modelToString(KlondikeModel)}) and store the game state
	 * it encodes to the given {@link KlondikeModel} object.
	 * The model object must be empty (contain only empty {@link Pile}s).
	 * 
	 * @param model an empty {@link KlondikeModel} object
	 * @param s     a string containing an encoded game state returned
	 *              by a previous call to {@link #modelToString(KlondikeModel)}
	 */
	public static void stringToModel(KlondikeModel model, String s) {
		String[] pileData = s.split(String.valueOf(SEPARATOR));
		if (pileData.length != 13) {
			throw new IllegalArgumentException("Invalid game state data (wrong number of piles)");
		}
		stringToPile(model.getMainDeck(), pileData[0]);
		stringToPile(model.getWastePile(), pileData[1]);
		for (int i = 0; i < 4; i++) {
			stringToPile(model.getFoundationPile(i), pileData[i+2]);
		}
		for (int i = 0; i < 7; i++) {
			stringToPile(model.getTableauPile(i), pileData[i+6]);
		}
	}
	
	private static Card charToCard(char c) {
		if (c < FIRST_CARD || c >= (FIRST_CARD+52)) {
			throw new IllegalArgumentException("Character " + c + " doesn't correspond to a legal Card");
		}
		return CARDS[c - FIRST_CARD];
	}
	
	private static char cardToChar(Card card) {
		return (char) (FIRST_CARD + card.getSuit().ordinal() * 13 + card.getRank().ordinal());
	}
	
	private static void stringToPile(Pile pile, String s) {
		if (!pile.isEmpty()) {
			throw new IllegalArgumentException("Pile must be empty");
		}
		for (int i = EXPOSE_INDEX_NUM_DIGITS; i < s.length(); i++) {
			pile.addCard(charToCard(s.charAt(i)));
		}
		pile.setExposeIndex(Integer.parseInt(s.substring(0, EXPOSE_INDEX_NUM_DIGITS)));
	}
	
	private static String pileToString(Pile pile) {
		StringBuilder buf = new StringBuilder();
		
		buf.append(String.format("%0"+EXPOSE_INDEX_NUM_DIGITS+"d", pile.getExposeIndex()));
		for (int i = 0; i < pile.getNumCards(); i++) {
			buf.append(cardToChar(pile.getCard(i)));
		}
		
		return buf.toString();
	}
}
