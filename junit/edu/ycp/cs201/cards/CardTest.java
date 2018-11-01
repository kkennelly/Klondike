package edu.ycp.cs201.cards;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CardTest {
	private Card jackOfClubs;
	private Card twoOfDiamonds;
	private Card aceOfDiamonds;
	private Card threeOfHearts;
	private Card queenOfSpades;
	
	@Before
	public void setUp() {
		jackOfClubs = new Card(Rank.JACK, Suit.CLUBS);
		twoOfDiamonds = new Card(Rank.TWO, Suit.DIAMONDS);
		aceOfDiamonds = new Card(Rank.ACE, Suit.DIAMONDS);
		threeOfHearts = new Card(Rank.THREE, Suit.HEARTS);
		queenOfSpades = new Card(Rank.QUEEN, Suit.SPADES);
	}
	
	@Test
	public void testGetRank() throws Exception {
		assertEquals(Rank.JACK, jackOfClubs.getRank());
		assertEquals(Rank.TWO, twoOfDiamonds.getRank());
		assertEquals(Rank.ACE, aceOfDiamonds.getRank());
		assertEquals(Rank.THREE, threeOfHearts.getRank());
		assertEquals(Rank.QUEEN, queenOfSpades.getRank());
	}
	
	@Test
	public void testGetSuit() throws Exception {
		assertEquals(Suit.CLUBS, jackOfClubs.getSuit());
		assertEquals(Suit.DIAMONDS, twoOfDiamonds.getSuit());
		assertEquals(Suit.DIAMONDS, aceOfDiamonds.getSuit());
		assertEquals(Suit.HEARTS, threeOfHearts.getSuit());
		assertEquals(Suit.SPADES, queenOfSpades.getSuit());
	}
	
	@Test
	public void testCompareTo() throws Exception {
		assertTrue(jackOfClubs.compareTo(twoOfDiamonds) < 0);
		assertTrue(new Card(Rank.JACK, Suit.CLUBS).compareTo(jackOfClubs) == 0);
		assertTrue(twoOfDiamonds.compareTo(jackOfClubs) > 0);
		assertTrue(queenOfSpades.compareTo(jackOfClubs) > 0);
		assertTrue(threeOfHearts.compareTo(aceOfDiamonds) > 0);
		assertTrue(aceOfDiamonds.compareTo(threeOfHearts) < 0);
		
		// ACE is the low rank
		assertTrue(aceOfDiamonds.compareTo(twoOfDiamonds) < 0);
	}
}
