package edu.ycp.cs201.cards;

/**
 * A Location object represents the source of a move
 * or the destination of a move: the {@link LocationType},
 * the pile index (for foundation and tableau piles,
 * which pile), and the card index (which card
 * from the source pile is being moved).
 */
public class Location {
	private final LocationType locationType;
	private final int pileIndex;
	private final int cardIndex;
	
	/**
	 * Constructor.
	 * 
	 * @param locationType  the {@link LocationType}
	 * @param pileIndex   the pile index (only used if the location type
	 *                    is either {@link LocationType#FOUNDATION_PILE}
	 *                    or {@link LocationType#TABLEAU_PILE}, will be
	 *                    set to 0 for other locations)
	 * @param cardIndex   the card index within the source pile
	 *                    (not used for move destinations); the card
	 *                    at that index, as well as all cards with higher
	 *                    indices, will be moved
	 */
	public Location(LocationType locationType, int pileIndex, int cardIndex) {
		this.locationType = locationType;
		this.pileIndex = pileIndex;
		this.cardIndex = cardIndex;
	}

	/**
	 * @return the {@link LocationType}
	 */
	public LocationType getLocationType() {
		return locationType;
	}
	
	/**
	 * @return the pile index
	 */
	public int getPileIndex() {
		return pileIndex;
	}
	
	/**
	 * @return the card index
	 */
	public int getCardIndex() {
		return cardIndex;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Location)) {
			return false;
		}
		Location other = (Location) obj;
		return this.locationType == other.locationType
				&& this.pileIndex == other.pileIndex
				&& this.cardIndex == other.cardIndex;
	}
	
	@Override
	public String toString() {
		return String.format("%s[%d]:%d", locationType, pileIndex, cardIndex);
	}
}
