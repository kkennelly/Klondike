package edu.ycp.cs201.cards;

/**
 * Enumeration representing the different types of "locations":
 * sources and destinations of card moves.
 */
public enum LocationType {
	/** The main deck. */
	MAIN_DECK,
	
	/** One of the four foundation piles. */
	FOUNDATION_PILE,
	
	/** One of the seven tableau piles. */
	TABLEAU_PILE,
	
	/**
	 * The waste pile. Note that this is never an
	 * actual source or destination for moves, but the UI
	 * can use this to detect when the user selects/clicks on
	 * the waste pile in order to draw a card or recycle
	 * the waste pile.
	 */
	WASTE_PILE,
}
