package edu.ycp.cs201.cards;

/**
 * Card suits.
 */
public enum Suit {
	/** Clubs */
	CLUBS("♣", Color.BLACK),
	/** Diamonds */
	DIAMONDS("♦", Color.RED),
	/** Hearts */
	HEARTS("♥", Color.RED),
	/** Spades */
	SPADES("♠", Color.BLACK);
	
	private final String symbol;
	private final Color color;
	
	private Suit(String symbol, Color color) {
		this.symbol = symbol;
		this.color = color;
	}

	/**
	 * @return the suit's {@link Color}
	 */
	public Color getColor() {
		return color;
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
