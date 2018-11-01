package edu.ycp.cs201.cards;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class KlondikeModelTest {
	private KlondikeModel model;
	
	@Before
	public void setUp() {
		model = new KlondikeModel();
	}
	
	@Test
	public void testMainDeck() throws Exception {
		// Main deck should exist and be empty
		Pile mainDeck = model.getMainDeck();
		assertNotNull(mainDeck);
		assertTrue(mainDeck.isEmpty());
	}
	
	@Test
	public void testWastePile() throws Exception {
		// Waste pile should exist and be empty
		Pile wastePile = model.getWastePile();
		assertNotNull(wastePile);
		assertTrue(wastePile.isEmpty());
	}
	
	@Test
	public void testFoundationPiles() throws Exception {
		// Four foundation piles should exist and be empty
		for (int i = 0; i < 4; i++) {
			Pile foundationPile = model.getFoundationPile(i);
			assertNotNull(foundationPile);
			assertTrue(foundationPile.isEmpty());
		}
	}
	
	@Test
	public void testTableauPiles() throws Exception {
		// Seven tableau piles should exist and be empty
		for (int i = 0; i < 7; i++) {
			Pile tableauPile = model.getTableauPile(i);
			assertNotNull(tableauPile);
			assertTrue(tableauPile.isEmpty());
		}
	}
	
	@Test
	public void testTableauPilesAreIndependent() throws Exception {
		// Make sure that each tableau pile is represented by a
		// different Pile object
		Rank[] ranks = Rank.values();
		for (int i = 0; i < 7; i++) {
			model.getTableauPile(i).addCard(new Card(ranks[i], Suit.SPADES));
		}
		
		for (int i = 0; i < 7; i++) {
			assertEquals(1, model.getTableauPile(i).getNumCards());
			assertEquals(new Card(ranks[i], Suit.SPADES), model.getTableauPile(i).getCard(0));
		}
	}
	
	@Test
	public void testFoundationPilesAreIndependent() throws Exception {
		// Make sure that each foundation pile is represented by a
		// different Pile object
		Rank[] ranks = Rank.values();
		for (int i = 0; i < 4; i++) {
			model.getFoundationPile(i).addCard(new Card(ranks[i], Suit.HEARTS));
		}
		
		for (int i = 0; i < 4; i++) {
			assertEquals(1, model.getFoundationPile(i).getNumCards());
			assertEquals(new Card(ranks[i], Suit.HEARTS), model.getFoundationPile(i).getCard(0));
		}
	}
}
