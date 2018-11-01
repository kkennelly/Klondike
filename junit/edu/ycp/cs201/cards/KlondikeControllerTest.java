package edu.ycp.cs201.cards;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class KlondikeControllerTest {
	@Rule
	public Timeout globalTimeout = Timeout.millis(10000); // 10 seconds

	private KlondikeController controller;

	/**
	 * This is a game state converted to a string by the
	 * {@link StringifyGameState} class.  It corresponds to the
	 * game shown in the <code>testgame.png</code> image file.
	 */
	private static final String TEST_GAME = "00000023G-NE,(MS')=TR#Q0IPB82A%D!" +
			"00000053!00000000\"!00000000/!00000000!00000000!000000001J!00000001<4&?!" +
			"00000002L+6!0000000397OU:!00000004H.>KF!00000000!00000006$5@C*;3";
	private KlondikeModel model;
	
	/**
	 * This is another game state, corresponding to the game shown
	 * in the <code>testgame2.png</code> image file.
	 */
	private static final String TEST_GAME_2 = "00000009G-NE,(MS')!00000053DA28BQT!" +
			"00000000\"#!00000000/0!00000000I!00000000<=!00000000U:!00000000!00000002L+6O4&?!" +
			"000000009!00000004H.>KFR7P!00000000!00000006$5@C*;3%1J";
	private KlondikeModel model2;
	
	/**
	 * This is a game where the player has won.
	 * @param model
	 * @return
	 */
	private static final String WINNING_GAME = "00000000!00000053!00000000\"#$%&'()*+,-.!" +
			"00000000/0123456789:;!00000000IJKLMNOPQRSTU!00000000<=>?@ABCDEFGH!00000000!00000000!" +
			"00000000!00000000!00000000!00000000!00000000";
	private KlondikeModel winningGame;
	
	// helper methods
	
	private static Location getTopOfMainDeckLocation(KlondikeModel model) {
		return new Location(LocationType.MAIN_DECK, 0, model.getMainDeck().getIndexOfTopCard());
	}
	
	// get a Location specifying a tableau pile, but with the card index
	// set to -1 (which is fine for specifying the destination of a move)
	private static Location getTableauPileLocation(int pileIndex) {
		return getTableauPileLocation(pileIndex, -1);
	}

	// get a Location specifying a tableau pile (with both pile index and card index),
	// suitable as the origin of a move
	private static Location getTableauPileLocation(int pileIndex, int cardIndex) {
		return new Location(LocationType.TABLEAU_PILE, pileIndex, cardIndex);
	}
	
	// get a Location specifying a tableau pile (as a destination)
	private static Location getFoundationPileLocation(int pileIndex) {
		return new Location(LocationType.FOUNDATION_PILE, pileIndex, -1);
	}
	
	private static void assertTopCardIs(Card card, Pile pile) {
		assertFalse(pile.isEmpty());
		Card top = pile.getTopCard();
		assertEquals(card, top);
	}
	
	// test methods
	
	@Before
	public void setUp() {
		controller = new KlondikeController();

		model = new KlondikeModel();
		StringifyGameState.stringToModel(model, TEST_GAME);
		
		model2 = new KlondikeModel();
		StringifyGameState.stringToModel(model2, TEST_GAME_2);
		
		winningGame = new KlondikeModel();
		StringifyGameState.stringToModel(winningGame, WINNING_GAME);
	}
	
	// test that the controller's initModel method initializes an empty
	// model object correctly - note that this test does not use
	// the TEST_GAME data
	@Test
	public void testInitModel() throws Exception {
		KlondikeModel aModel = new KlondikeModel();
		controller.initModel(aModel);
		
		// main deck should have 24 cards,
		// and only the top card should be exposed
		assertEquals(24, aModel.getMainDeck().getNumCards());
		assertEquals(23, aModel.getMainDeck().getExposeIndex());
		
		// the waste pile should have its expose index set to
		// a high value (so that cards in the waste pile are
		// never shown)
		assertTrue(aModel.getWastePile().getExposeIndex() >= 52);
		
		// foundation piles should all be empty
		for (int i = 0; i < 4; i++) {
			assertTrue(aModel.getFoundationPile(i).isEmpty());
		}
		
		// tableau piles should contain 1, 2, 3, etc. cards, and
		// the top card of each should be exposed
		for (int i = 0; i < 7; i++) {
			assertEquals(i + 1, aModel.getTableauPile(i).getNumCards());
			assertEquals(i, aModel.getTableauPile(i).getExposeIndex());
		}
	}
	
	// sanity checking to ensure that the initial game state is correct 
	@Test
	public void testInitialGameState() throws Exception {
		String state = StringifyGameState.modelToString(model);
		assertEquals(TEST_GAME, state);
	}
	
	// test drawing a card (move one card from main deck to waste pile)
	@Test
	public void testDrawCard() throws Exception {
		assertTopCardIs(new Card(Rank.NINE, Suit.HEARTS), model.getMainDeck());
		assertEquals(24, model.getMainDeck().getNumCards());
		assertEquals(0, model.getWastePile().getNumCards());
		
		// draw a card
		controller.drawCardOrRecycleWaste(model);
		
		assertTopCardIs(new Card(Rank.FOUR, Suit.CLUBS), model.getMainDeck());
		assertEquals(23, model.getMainDeck().getNumCards());
		assertEquals(1, model.getWastePile().getNumCards());
	}
	
	@Test
	public void testRecycleWastePile() throws Exception {
		// Test that we can draw all of the cards from the main
		// deck (into the waste pile), and that recycling the waste
		// pile results restores the main deck to its original state
		
		// check initial state
		assertEquals(24, model.getMainDeck().getNumCards());
		assertEquals(0, model.getWastePile().getNumCards());
		
		// copy cards from the main deck
		ArrayList<Card> mainDeckCards = new ArrayList<Card>();
		for (int i = 0; i < model.getMainDeck().getNumCards(); i++) {
			mainDeckCards.add(model.getMainDeck().getCard(i));
		}
		assertEquals(24, mainDeckCards.size());
		
		// draw all of the cards from the main deck
		int iter = 0;
		while (!model.getMainDeck().isEmpty() && iter < 100) {
			controller.drawCardOrRecycleWaste(model);
			iter++;
		}
		if (iter >= 100) {
			throw new IllegalStateException("Too many iterations!");
		}
		
		// main deck should now be empty, and the waste pile should have all
		// of the main deck cards
		assertEquals(0, model.getMainDeck().getNumCards());
		assertEquals(24, model.getWastePile().getNumCards());
		
		// recycle the waste pile (moving the cards back to the main deck)
		controller.drawCardOrRecycleWaste(model);
		
		// all cards should now be back in the main deck
		assertEquals(24, model.getMainDeck().getNumCards());
		assertEquals(0, model.getWastePile().getNumCards());
		
		// the main deck cards should be back to the original state
		for (int i = 0; i < 24; i++) {
			assertEquals(mainDeckCards.get(i), model.getMainDeck().getCard(i));
		}
	}
	
	// test selecting a card from the main deck
	@Test
	public void testSelectFromMainDeck() throws Exception {
		// top card on main deck
		Card topOfMainDeck = model.getMainDeck().getTopCard();
		
		assertEquals(24, model.getMainDeck().getNumCards());
		// only the top card on the main deck should be exposed
		assertEquals(23, model.getMainDeck().getExposeIndex());
		
		// select the top card
		Selection selection = controller.select(model, getTopOfMainDeckLocation(model));
		
		// main deck should have one fewer cards, but its expose index
		// should not have changed
		assertEquals(23, model.getMainDeck().getNumCards());
		assertEquals(23, model.getMainDeck().getExposeIndex());
		
		// four of clubs should now be the top card
		assertTopCardIs(new Card(Rank.FOUR, Suit.CLUBS), model.getMainDeck());
		
		// the selection should have the original top card
		assertEquals(topOfMainDeck, selection.getCards().get(0));
	}
	
	@Test
	public void testIllegalSelectionFromMainDeckNotTopCard() throws Exception {
		Location illegalLocation = new Location(LocationType.MAIN_DECK, 0, model.getMainDeck().getIndexOfTopCard() - 1);
		assertNull(controller.select(model, illegalLocation));
	}
	
	@Test
	public void testIllegalSelectionFromEmptyMainDeck() throws Exception {
		// remove all cards from the main deck
		int iter = 0;
		while (!model.getMainDeck().isEmpty() && iter < 100) {
			model.getMainDeck().drawCard();
			iter++;
		}
		if (iter >= 100) {
			throw new IllegalStateException("Too many iterations!");
		}
		assertNull(controller.select(model, getTopOfMainDeckLocation(model)));
	}
	
	@Test
	public void testSelectFromTableauPile() throws Exception {
		// Select the "maximal" selections from each tableau pile,
		// meaning choosing the bottom-most exposed card.
		// Note that we check to ensure that the expose index of
		// the Pile does *not* change as a result of making the selection.
		
		Selection sel1 = controller.select(model, getTableauPileLocation(0, 0));
		assertNotNull(sel1);
		assertEquals(2, sel1.getNumCards());
		assertEquals(0, model.getTableauPile(0).getNumCards());
		assertEquals(0, model.getTableauPile(0).getExposeIndex());
		
		Selection sel2 = controller.select(model, getTableauPileLocation(1, 1));
		assertNotNull(sel2);
		assertEquals(3, sel2.getNumCards());
		assertEquals(1, model.getTableauPile(1).getNumCards());
		assertEquals(1, model.getTableauPile(1).getExposeIndex());
		
		Selection sel3 = controller.select(model, getTableauPileLocation(2, 2));
		assertNotNull(sel3);
		assertEquals(1, sel3.getNumCards());
		assertEquals(2, model.getTableauPile(2).getNumCards());
		assertEquals(2, model.getTableauPile(2).getExposeIndex());
		
		Selection sel4 = controller.select(model, getTableauPileLocation(3, 3));
		assertNotNull(sel4);
		assertEquals(2, sel4.getNumCards());
		assertEquals(3, model.getTableauPile(3).getNumCards());
		assertEquals(3, model.getTableauPile(3).getExposeIndex());
		
		Selection sel5 = controller.select(model, getTableauPileLocation(4, 4));
		assertNotNull(sel5);
		assertEquals(1, sel5.getNumCards());
		assertEquals(4, model.getTableauPile(4).getNumCards());
		assertEquals(4, model.getTableauPile(4).getExposeIndex());
		
		// the sixth tableau pile is empty, so we can't select from it
		
		Selection sel7 = controller.select(model, getTableauPileLocation(6, 6));
		assertNotNull(sel7);
		assertEquals(1, sel7.getNumCards());
		assertEquals(6, model.getTableauPile(6).getNumCards());
		assertEquals(6, model.getTableauPile(6).getExposeIndex());
	}
	
	@Test
	public void testSelectFromTableauPileMiddleCard() throws Exception {
		// Test that it is possible to move "middle" cards
		// in a tableau pile (i.e., not the bottom-most exposed card),
		// as long as it is exposed
		Selection sel = controller.select(model, getTableauPileLocation(1, 2));
		assertNotNull(sel);
		assertTopCardIs(new Card(Rank.SIX, Suit.DIAMONDS), model.getTableauPile(1));
		assertEquals(2, model.getTableauPile(1).getNumCards());
		assertEquals(2, sel.getNumCards());
		assertEquals(new Card(Rank.FIVE, Suit.CLUBS), sel.getCards().get(0));
	}
	
	@Test
	public void testIllegalSelectionFromWastePile() throws Exception {
		// It should not be possible to select from the waste pile
		assertNull(controller.select(model, new Location(LocationType.WASTE_PILE, 0, model.getWastePile().getIndexOfTopCard())));
	}
	
	@Test
	public void testIllegalSelectionFromFoundationPile() throws Exception {
		// It should not be possible to select from a foundation pile
		for (int i = 0; i < 4; i++) {
			assertNull(controller.select(model, new Location(LocationType.FOUNDATION_PILE, i, model.getFoundationPile(i).getIndexOfTopCard())));
		}
	}
	
	@Test
	public void testIllegalSelectionFromTableauPileNonExposedCard() throws Exception {
		// test that it is illegal to select a card from the tableau pile
		// if that card is not exposed
		Selection sel = controller.select(model, getTableauPileLocation(6, 3));
		assertNull(sel);
	}
	
	@Test
	public void testIllegalSelectionFromTableauPileInvalidCardIndex() throws Exception {
		// test that it is illegal to select a nonexistent card from a tableau
		// pile (either a negative card index or a card index greater than
		// or equal to the number of cards)
		Selection sel1 = controller.select(model, getTableauPileLocation(6, -1));
		assertNull(sel1);
		
		Selection sel2 = controller.select(model, getTableauPileLocation(6, 7));
		assertNull(sel2);
	}
	
	// test moving from main deck to a tableau pile
	// (legal move of four of clubs to the seventh tableau
	// pile, which has five of diamonds on the top)
	@Test
	public void testMoveFromMainDeckToTableau() throws Exception {
		// draw to expose the four of clubs
		controller.drawCardOrRecycleWaste(model);
		assertTopCardIs(new Card(Rank.FOUR, Suit.CLUBS), model.getMainDeck());
		assertEquals(23, model.getMainDeck().getNumCards());
		assertEquals(22, model.getMainDeck().getExposeIndex());
		
		// select the four of clubs
		Selection selection = controller.select(model, getTopOfMainDeckLocation(model));
		assertEquals(22, model.getMainDeck().getNumCards());
		assertEquals(22, model.getMainDeck().getExposeIndex());
		
		// it should be possible to move it to the seventh tableau pile
		assertTopCardIs(new Card(Rank.FIVE, Suit.DIAMONDS), model.getTableauPile(6));
		assertEquals(7, model.getTableauPile(6).getNumCards());
		assertTrue(controller.allowMove(model, selection, getTableauPileLocation(6)));
		
		// carry out the move
		controller.moveCards(model, selection, getTableauPileLocation(6));
		
		// seventh tableau pile should now have one more card
		assertEquals(8, model.getTableauPile(6).getNumCards());
		
		// the expose index of the main deck should now have decreased
		// by 1
		assertEquals(21, model.getMainDeck().getExposeIndex());
	}
	
	@Test
	public void testMoveFromMainDeckToFoundation() throws Exception {
		// testing moving an ace from the main deck to a foundation pile
		
		// drawing seven cards from the main deck should expose
		// the main deck
		for (int i = 0; i < 7; i++) {
			controller.drawCardOrRecycleWaste(model);
		}
		assertTopCardIs(new Card(Rank.ACE, Suit.SPADES), model.getMainDeck());
		
		Selection sel = controller.select(model, getTopOfMainDeckLocation(model));
		assertTrue(controller.allowMove(model, sel, getFoundationPileLocation(2)));
		controller.moveCards(model, sel, getFoundationPileLocation(2));
		
		assertEquals(16, model.getMainDeck().getNumCards());
		assertTopCardIs(new Card(Rank.TWO, Suit.DIAMONDS), model.getMainDeck());
		assertEquals(15, model.getMainDeck().getExposeIndex());
		assertEquals(1, model.getFoundationPile(2).getNumCards());
		assertEquals(0, model.getFoundationPile(2).getExposeIndex());
	}
	
	@Test
	public void testMoveFromTableauPileToFoundation() throws Exception {
		// Test a legal move from a tableau pile to a foundation pile.
		// Uses the second test game state (corresponding to testgame2.png).
		Selection sel = controller.select(model2, getTableauPileLocation(6, 9));
		assertNotNull(sel);
		assertTrue(controller.allowMove(model2, sel, getFoundationPileLocation(2)));
		controller.moveCards(model2, sel, getFoundationPileLocation(2));
		assertTopCardIs(new Card(Rank.THREE, Suit.DIAMONDS), model2.getTableauPile(6));
		assertTopCardIs(new Card(Rank.TWO, Suit.SPADES), model2.getFoundationPile(2));
	}
	
	@Test
	public void testMoveFromTableauPileToEmptyTableauPile() throws Exception {
		// test that a King can be moved onto an empty tableau pile
		Selection sel = controller.select(model, getTableauPileLocation(3, 3));
		assertNotNull(sel);
		assertEquals(2, sel.getNumCards());
		assertEquals(new Card(Rank.KING, Suit.SPADES), sel.getCards().get(0));
		
		assertTrue(controller.allowMove(model, sel, getTableauPileLocation(5)));
		
		controller.moveCards(model, sel, getTableauPileLocation(5));
		
		assertEquals(2, model.getTableauPile(5).getNumCards());
		assertEquals(new Card(Rank.KING, Suit.SPADES), model.getTableauPile(5).getCard(0));
		assertEquals(new Card(Rank.QUEEN, Suit.DIAMONDS), model.getTableauPile(5).getCard(1));
		
		assertEquals(3, model.getTableauPile(3).getNumCards());
		assertEquals(2, model.getTableauPile(3).getExposeIndex());
	}
	
	@Test
	public void testIllegalMoveFromMainDeckToTableauWrongRank() throws Exception {
		// nine of hearts is on the top of the main deck
		
		Selection selection = controller.select(model, getTopOfMainDeckLocation(model));
		assertEquals(new Card(Rank.NINE, Suit.HEARTS), selection.getCards().get(0));
		
		// It is not legal to move this selection onto any of the tableau piles
		for (int i = 0; i < 7; i++) {
			assertFalse(controller.allowMove(model, selection, getTableauPileLocation(i)));
		}
	}
	
	@Test
	public void testIllegalMoveFromMainDeckToTableauPileWrongColor() throws Exception {
		// drawing five cards will leave the seven of hearts as the top card:
		// it should not be possible to move it onto the third tableau pile,
		// which has eight of diamonds as its top card
		for (int i = 0; i < 5; i++) {
			controller.drawCardOrRecycleWaste(model);
		}
		assertEquals(new Card(Rank.SEVEN, Suit.HEARTS), model.getMainDeck().getTopCard());
		
		Selection selection = controller.select(model, getTopOfMainDeckLocation(model));
		assertFalse(controller.allowMove(model, selection, getTableauPileLocation(2)));
	}
	
	@Test
	public void testIllegalMoveToFoundationPileMultipleCards() throws Exception {
		// Test that it is not allowed to move multiple cards to
		// a foundation pile.  This is done on the second test game state
		// (corresponding to testgame2.png).
		Selection sel = controller.select(model2, getTableauPileLocation(6, 8));
		assertNotNull(sel);
		assertEquals(2, sel.getNumCards());
		assertEquals(new Card(Rank.THREE, Suit.DIAMONDS), sel.getCards().get(0));
		
		assertFalse(controller.allowMove(model2, sel, getFoundationPileLocation(1)));
	}
	
	@Test
	public void testIllegalMoveToFoundationPileNonAceToEmptyFoundation() throws Exception {
		// it is illegal to move any non-ACE card to an empty foundation pile
		Selection sel = controller.select(model, getTableauPileLocation(2, 2));
		assertNotNull(sel);
		assertFalse(controller.allowMove(model, sel, getFoundationPileLocation(2)));
	}
	
	@Test
	public void testIllegalMoveToFoundationPileWrongRank() throws Exception {
		// Test that the card moved to a foundation pile must have a rank
		// that is one greater than the one currently on the top of the
		// foundation pile.  This is a test that uses the second test
		// game state (corresponding to testgame2.png).
		Selection sel = controller.select(model2, getTableauPileLocation(2, 6));
		assertNotNull(sel);
		assertEquals(1, sel.getNumCards());
		assertEquals(new Card(Rank.FOUR, Suit.HEARTS), sel.getCards().get(0));
		assertTopCardIs(new Card(Rank.TWO, Suit.HEARTS), model2.getFoundationPile(3));
		
		// can't move a four of hearts onto a two of hearts
		// (right suit, wrong rank)
		assertFalse(controller.allowMove(model2, sel, getFoundationPileLocation(3)));
	}
	
	@Test
	public void testIllegalMoveToFoundationPileWrongSuit() throws Exception {
		// Test that a card moved to a foundation pile must have
		// the same suit as the current top card on the foundation pile.
		Selection sel = controller.select(model, getTableauPileLocation(0, 1));
		assertNotNull(sel);
		assertEquals(1, sel.getNumCards());
		assertEquals(new Card(Rank.TWO, Suit.SPADES), sel.getCards().get(0));
		assertTopCardIs(new Card(Rank.ACE, Suit.CLUBS), model.getFoundationPile(0));
		
		// can't move two of spades onto the ace of clubs (wrong suit)
		assertFalse(controller.allowMove(model, sel, getFoundationPileLocation(0)));
	}
	
	@Test
	public void testIllegalMoveToMainDeck() throws Exception {
		// Test that a move to the main deck is not allowed
		
		// draw 9 cards to expose nine of spades
		for (int i = 0; i < 9; i++) {
			controller.drawCardOrRecycleWaste(model);
		}
		assertTopCardIs(new Card(Rank.NINE, Suit.SPADES), model.getMainDeck());
		
		Selection sel = controller.select(model, getTableauPileLocation(2, 2));
		assertNotNull(sel);
		assertEquals(1, sel.getNumCards());
		assertEquals(new Card(Rank.EIGHT, Suit.DIAMONDS), sel.getCards().get(0));
		
		// Don't allow move onto main deck (even though this move
		// would be legal if the main deck were a tableau pile)
		assertFalse(controller.allowMove(model, sel, new Location(LocationType.MAIN_DECK, 0, -1)));
	}
	
	@Test
	public void testIllegalMoveToWastePile() throws Exception {
		// Test that a move to the waste pile is not allowed
		
		// draw 2 cards to put the four of clubs on the top of the waste pile
		controller.drawCardOrRecycleWaste(model);
		controller.drawCardOrRecycleWaste(model);
		assertTopCardIs(new Card(Rank.FOUR, Suit.CLUBS), model.getWastePile());
		
		Selection sel = controller.select(model, getTableauPileLocation(0, 0));
		assertEquals(2, sel.getNumCards());
		assertEquals(new Card(Rank.THREE, Suit.DIAMONDS), sel.getCards().get(0));
		
		// can't move onto the waste pile, even though it would be a legal
		// move if the waste pile were a tableau pile
		assertFalse(controller.allowMove(model, sel, new Location(LocationType.WASTE_PILE, 0, -1)));
	}
	
	@Test
	public void testUnselectFromMainDeck() throws Exception {
		// Test that a selection from the main deck can be canceled
		// (moving the selected card back to the main deck)
		
		Selection sel = controller.select(model, getTopOfMainDeckLocation(model));
		assertNotNull(sel);
		assertEquals(23, model.getMainDeck().getNumCards());
		assertEquals(23, model.getMainDeck().getExposeIndex()); // expose index shouldn't change
		
		controller.unselect(model, sel);
		assertEquals(24, model.getMainDeck().getNumCards());
		assertEquals(new Card(Rank.NINE, Suit.HEARTS), model.getMainDeck().getTopCard());
		assertEquals(23, model.getMainDeck().getExposeIndex());
	}
	
	@Test
	public void testUnselectFromTableauPile() throws Exception {
		// Test that a selection from a tableau pile can be canceled
		// (moving the selected card(s) back to the tableau pile)
		
		Selection sel = controller.select(model, getTableauPileLocation(3, 3));
		assertNotNull(sel);
		assertEquals(3, model.getTableauPile(3).getNumCards());
		assertEquals(3, model.getTableauPile(3).getExposeIndex()); // expose index shouldn't change
		assertEquals(2, sel.getNumCards());
		
		controller.unselect(model, sel);
		assertEquals(5, model.getTableauPile(3).getNumCards());
		assertEquals(3, model.getTableauPile(3).getExposeIndex());
	}
	
	@Test
	public void testIsWin() throws Exception {
		assertFalse(controller.isWin(model));
		assertFalse(controller.isWin(model2));
		assertTrue(controller.isWin(winningGame));
	}
}
