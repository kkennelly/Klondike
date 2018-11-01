package edu.ycp.cs201.cards;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class SelectionTest {
	private Pile mainDeck;
	private Location mainDeckTarget;
	private Selection mainDeckSelection;
	
	private Pile tableauPile;
	private Location tableauPileTarget;
	private Selection tableauPileSelection;
	
	@Before
	public void setUp() {
		// Select from main deck
		mainDeck = new Pile();
		Util.addAllCards(mainDeck);
		ArrayList<Card> selectedFromMainDeck = mainDeck.removeCards(1);
		mainDeckTarget = new Location(LocationType.MAIN_DECK, 0, 51);
		mainDeckSelection = new Selection(mainDeckTarget, selectedFromMainDeck);
		
		// Selection from tableau pile (with multiple cards being selected)
		tableauPile = new Pile();
		tableauPile.addCard(new Card(Rank.SIX, Suit.DIAMONDS));
		tableauPile.addCard(new Card(Rank.FIVE, Suit.SPADES));
		tableauPile.addCard(new Card(Rank.FOUR, Suit.HEARTS));
		tableauPile.addCard(new Card(Rank.THREE, Suit.CLUBS));
		tableauPile.addCard(new Card(Rank.TWO, Suit.DIAMONDS));
		tableauPileTarget = new Location(LocationType.TABLEAU_PILE, 4, 2);
		ArrayList<Card> selectedFromTableauPile = tableauPile.removeCards(3);
		tableauPileSelection = new Selection(tableauPileTarget, selectedFromTableauPile);
	}
	
	@Test
	public void testGetOrigin() throws Exception {
		assertEquals(mainDeckTarget, mainDeckSelection.getOrigin());
		assertEquals(tableauPileTarget, tableauPileSelection.getOrigin());
	}
	
	@Test
	public void testGetCards() throws Exception {
		ArrayList<Card> selectedFromMainDeck = mainDeckSelection.getCards();
		assertEquals(1, selectedFromMainDeck.size());
		assertEquals(new Card(Rank.KING, Suit.SPADES), selectedFromMainDeck.get(0));
		
		ArrayList<Card> selectedFromTableauPile = tableauPileSelection.getCards();
		assertEquals(3, selectedFromTableauPile.size());
		assertEquals(new Card(Rank.FOUR, Suit.HEARTS), selectedFromTableauPile.get(0));
		assertEquals(new Card(Rank.THREE, Suit.CLUBS), selectedFromTableauPile.get(1));
		assertEquals(new Card(Rank.TWO, Suit.DIAMONDS), selectedFromTableauPile.get(2));
		
	}
	
	@Test
	public void testGetNumCards() throws Exception {
		assertEquals(1, mainDeckSelection.getNumCards());
		assertEquals(3, tableauPileSelection.getNumCards());
	}
}
