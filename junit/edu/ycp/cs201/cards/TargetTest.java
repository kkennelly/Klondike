package edu.ycp.cs201.cards;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TargetTest {
	private Location mainDeckTarget;
	private Location wastePileTarget;
	private Location foundationPileTarget;
	private Location tableauPileTarget;
	
	@Before
	public void setUp() {
		mainDeckTarget = new Location(LocationType.MAIN_DECK, 0, 31);
		wastePileTarget = new Location(LocationType.WASTE_PILE, 0, -1);
		foundationPileTarget = new Location(LocationType.FOUNDATION_PILE, 2, 3);
		tableauPileTarget = new Location(LocationType.TABLEAU_PILE, 5, 10);
	}
	
	@Test
	public void testGetTargetType() throws Exception {
		assertEquals(LocationType.MAIN_DECK, mainDeckTarget.getLocationType());
		assertEquals(LocationType.WASTE_PILE, wastePileTarget.getLocationType());
		assertEquals(LocationType.FOUNDATION_PILE, foundationPileTarget.getLocationType());
		assertEquals(LocationType.TABLEAU_PILE, tableauPileTarget.getLocationType());
	}
	
	@Test
	public void testGetPileIndex() throws Exception {
		assertEquals(0, mainDeckTarget.getPileIndex());
		assertEquals(0, wastePileTarget.getPileIndex());
		assertEquals(2, foundationPileTarget.getPileIndex());
		assertEquals(5, tableauPileTarget.getPileIndex());
	}
	
	@Test
	public void testGetCardIndex() throws Exception {
		assertEquals(31, mainDeckTarget.getCardIndex());
		assertEquals(-1, wastePileTarget.getCardIndex());
		assertEquals(3, foundationPileTarget.getCardIndex());
		assertEquals(10, tableauPileTarget.getCardIndex());
	}
}
