package edu.ycp.cs201.cards;

/**
 * Card rank enumeration.
 * Note that {@link #ACE} is considered the low rank
 * (because it is the lowest rank in Klondike solitaire).
 */
public enum Rank {
	/** Ace */
	ACE("A"),
	/** Two */
	TWO("2"),
	/** Three */
	THREE("3"),
	/** Four */
	FOUR("4"),
	/** Five */
	FIVE("5"),
	/** Six */
	SIX("6"),
	/** Seven */
	SEVEN("7"),
	/** Eight */
	EIGHT("8"),
	/** Nine */
	NINE("9"),
	/** Ten */
	TEN("10"),
	/** Jack */
	JACK("J"),
	/** Queen */
	QUEEN("Q"),
	/** King */
	KING("K");
	
	private final String symbol;
	
	private Rank(String symbol) {
		this.symbol = symbol;
	}
	
	/**
	 * @return the symbol representing this card
	 */
	public String getSymbol() {
		return symbol;
	}
	
	@Override
	public String toString() {
		return symbol;
	}
	
	/**
	 * @return the enumeration member name as a string
	 */
	public String getMemberName() {
		return super.toString();
	}
}
