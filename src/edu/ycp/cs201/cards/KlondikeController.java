package edu.ycp.cs201.cards;

import java.util.ArrayList;

/**
 * The controller class implements all of the logic required to
 * play a game of Klondike.  All of the data is represented
 * in a {@link KlondikeModel} object, and each controller method
 * takes a reference to the model object as a parameter. 
 */
public class KlondikeController {
		
	private static final int NUMOFTABLEAU = 7;
	private static final int NUMOFFOUNDATION = 4;
	
	
	/**
	 * 		Initialize the model object.
	 * 		Should populate and shuffle the main deck, and then
	 * 		deal cards from the main deck onto the seven tableau piles.
	 * 		It should set the expose index of each tableau pile
	 * 		so that only the top card is exposed.
	 * 		It should set the expose index of the main deck so that
	 * 		only the top card is exposed.
	 * 		It should set the expose index of the waste pile so that
	 * 		no cards can ever be exposed (i.e., set it to a high value,
	 * 		such that even if it contains all 52 cards, none of them
	 *		would be exposed).
	 * 		It should set the expose index of each foundation pile to 0.
	 * 
	 * @param model the {@link KlondikeModel} object to initialize
	 */
	public void initModel(KlondikeModel model) 
	{
		model.getMainDeck().populate();
		
		model.getMainDeck().shuffle();
		
		for (int j = 0; j < NUMOFTABLEAU; j++)
		{
			for (int i = j; i < NUMOFTABLEAU; i++)
			{
				Card toPutOn = model.getMainDeck().drawCard();
				model.getTableauPile(i).addCard(toPutOn);
			}
			
			model.getTableauPile(j).setExposeIndex(j);
		}
			
		int theIndex = model.getMainDeck().getNumCards() - 1;
		model.getMainDeck().setExposeIndex(theIndex);
		
		model.getWastePile().setExposeIndex(100);
		
		for (int i = 0; i < NUMOFFOUNDATION; i++)
		{
			model.getFoundationPile(i).setExposeIndex(0);
		}
	}

	/**
	 * <p>Attempt to create a {@link Selection} that represents one
	 * or more {@link Card}s to be moved from a pile
	 * indicated by a {@link Location}.  The {@link Location} specifies
	 * which pile to move cards from, and which card or cards
	 * within the pile should be moved.  Note that this method
	 * must check to see whether the proposed move is legal:
	 * it should return <code>null</code> if it is not legal
	 * to move the card or cards indicated by the {@link Location}.</p>
	 * 
	 * 		<p>If the {@link Location} has {@link LocationType#MAIN_DECK} as
	 * 		its location type, then the main deck must not be empty,
	 * 		the selected card must be the top card on the main deck.
	 *	 </p>
	 * 
	 *		 <p>If the {@link Location} has {@link LocationType#TABLEAU_PILE}
	 * as its location type, then the {@link Location}'s card index
	 * must refer to a valid card, and the card index must be
	 * 		greater than or equal to the tableau pile's expose index.</p>
	 * 
	 * 		<p>It is not legal to move cards from the waste pile or from
	 * 			a foundation pile, so if the {@link Location}'s location type
	 * 		is {@link LocationType#WASTE_PILE} or {@link LocationType#FOUNDATION_PILE},
	 * 		then the method should return null.</p>
	 * 
	 * <p>If the move is legal, the indicated cards should be removed
	 * from the source pile (as indicated by the {@link Location}),
	 * and transfered into the {@link Selection} object returned from
	 * the method.
	 * 
	 * @param model   the {@link KlondikeModel}
	 * @param location the {@link Location} specifying which card or cards to move
	 * @return a {@link Selection} containing the cards to move and the
	 *         {@link Location}, or null if the {@link Location} does not
	 *         indicate a legal location from which cards can be moved
	 */
	public Selection select(KlondikeModel model, Location location) 
	{
		ArrayList <Card> sel;
		
		if(location.getLocationType() == LocationType.MAIN_DECK)
		{
			if(model.getMainDeck().getNumCards() == 0)
				return null;
			else if(location.getCardIndex() != model.getMainDeck().getNumCards() - 1)
				return null;
			
			sel = model.getMainDeck().removeCards(1);
		}
		else if(location.getLocationType() == LocationType.TABLEAU_PILE)
		{
			Pile thePile = model.getTableauPile(location.getPileIndex());
			
			//is this correct for checking "location's card index must refer to a valid card"
			if(location.getCardIndex() < 0 || location.getCardIndex() >= thePile.getNumCards())
				return null;
			if(location.getCardIndex() < thePile.getExposeIndex())
				return null;
			
			sel = thePile.removeCards(thePile.getNumCards() - location.getCardIndex());
		}
		else
		{
			return null;
		}
		
		return new Selection(location, sel);
		
	}

	/**
	 * "Undo" a selection by moving cards from a {@link Selection} object
	 * back to the pile they were taken from, as indicated by the
	 * selection's origin {@link Location}.  For example, if the location
	 * indicates that the selection's cards were taken from a tableau
	 * pile, then they should be moved back to that tableau pile.
	 * 
	 * @param model      the {@link KlondikeModel}
	 * @param selection  the {@link Selection} to undo
	 */
	public void unselect(KlondikeModel model, Selection selection) 
	{
		LocationType type = selection.getOrigin().getLocationType();
		
		if(type == LocationType.MAIN_DECK)
		{
			model.getMainDeck().addCards(selection.getCards());
		}
		else if(type == LocationType.FOUNDATION_PILE)
		{
			model.getFoundationPile(selection.getOrigin().getPileIndex()).addCards(selection.getCards());
		}
		else if(type == LocationType.TABLEAU_PILE)
		{
			model.getTableauPile(selection.getOrigin().getPileIndex()).addCards(selection.getCards());
		}
	}

	/**
	 * <p>Determine whether it is legal to move the current {@link Selection} to a
	 * destination pile indicated by a {@link Location}.</p>
	 * 
	 * 		<p>If the destination {@link Location} has {@link LocationType#FOUNDATION_PILE}
	 *		 as its {@link LocationType}, then the {@link Selection} must contain a single card.
	 * 		If the foundation pile is empty, then the selected card must be an {@link Rank#ACE}.
	 * 		If the foundation pile is not empty, then the selected card must have the same suit
	 * 		as the top card on the foundation pile, and the ordinal value of its {@link Rank} must
	 * 		be one greater than the top card on the foundation pile.</p>
	 * 
	 * 		<p>If the destination {@link Location} has {@link LocationType#TABLEAU_PILE}
	 * 		as its {@link LocationType}, then:
	 * <ul>
	 *   	<li>If the destination tableau pile is empty, the bottom card of the {@link Selection}
	 *  	 must be {@link Rank#KING} </li>
	 *   	<li>If the destination tableau pile is not empty, the bottom card of the {@link Selection}
	 *  	 must have a {@link Suit} that is a different {@link Color} than the top card of the
	 *   	tableau pile, and the bottom card of the {@link Selection} must have an
	 *  	 {@link Rank} whose ordinal value is one less than the ordinal value of the
	 *   	top card of the tableau pile.</li>
	 * </ul>
	 * </p>
	 * 
	 * 		<p>If the destination {@link Location} has {@link LocationType#MAIN_DECK} or
	 * 		{@link LocationType#WASTE_PILE} as its {@link LocationType}, then the move is not legal.</p>
	 * 
	 * <p>
	 * 		Note that this method just checks to see whether or not a move would
	 * 		be legal: it does not move any cards.
	 * </p>
	 * 
	 * @param model      the {@link KlondikeModel}
	 * @param selection  the {@link Selection}
	 * @param dest       the destination {@link Location}
	 * @return true if the move is legal, false if the move is not legal
	 */
	public boolean allowMove(KlondikeModel model, Selection selection, Location dest) 
	{
		if (dest.getLocationType() == LocationType.FOUNDATION_PILE)
		{
			Pile destPile = model.getFoundationPile(dest.getPileIndex());
			
			if(selection.getNumCards() != 1)
				return false;
			
			if(destPile.getNumCards() == 0)
			{
				if(selection.getCards().get(0).getRank() != Rank.ACE)
					return false;
			}
			else
			{
				Card topCardOfFound = destPile.getTopCard();
				Card bottomCardOfSel = selection.getCards().get(0);
				
				if(bottomCardOfSel.getSuit() != topCardOfFound.getSuit())
					return false;
				if(topCardOfFound.getRank().compareTo(bottomCardOfSel.getRank()) != -1)
					return false;
			}
		}
		else if(dest.getLocationType() == LocationType.TABLEAU_PILE)
		{
			if(model.getTableauPile(dest.getPileIndex()).getNumCards() == 0)
			{
				if(selection.getCards().get(0).getRank() != Rank.KING)
					return false;
			}
			else
			{
				Card topCardOfTab = model.getTableauPile(dest.getPileIndex()).getTopCard();
				Card bottomCardOfSel = selection.getCards().get(0);
				
				if(bottomCardOfSel.getSuit().getColor() == topCardOfTab.getSuit().getColor())
					return false;
				if(topCardOfTab.getRank().compareTo(bottomCardOfSel.getRank()) != 1)
					return false;
			}
		}
		else
			return false;
		
		return true;
	}

	/**
	 * 		<p>Move the cards in a {@link Selection} onto a pile as indicated
	 *		 by the destination {@link Location}.</p>
	 * 
	 * 		<p>The method does <em>not</em> need to check whether or not the move is legal:
	 * 		it can assume that the {@link #allowMove(KlondikeModel, Selection, Location)}
	 * 		method has already determined that the move is legal.</p>
	 * 
	 * 		<p>
	 * 		<strong>Important</strong>: If the pile that the selected cards
	 * 		were taken from is non-empty, and if its top card is not exposed,
	 * 		then its top card should be exposed.
	 * 		</p>
	 * 
	 * 		<p>
	 *		 Note that the expose index of the destination pile should not change.
	 *		 </p>
	 * 
	 * @param model      the {@link KlondikeModel} 
	 * @param selection  the {@link Selection} containing the selected cards
	 * @param dest       the destination {@link Location}
	 */
	public void moveCards(KlondikeModel model, Selection selection, Location dest) 
	{
		if(dest.getLocationType() == LocationType.TABLEAU_PILE)
		{
			model.getTableauPile(dest.getPileIndex()).addCards(selection.getCards());
		}
		else if(dest.getLocationType() == LocationType.FOUNDATION_PILE)
		{
			model.getFoundationPile(dest.getPileIndex()).addCards(selection.getCards());
		}
		
		if(selection.getOrigin().getLocationType() == LocationType.TABLEAU_PILE && model.getTableauPile(selection.getOrigin().getPileIndex()).getNumCards() != 0)
		{
			Pile tabPile = model.getTableauPile(selection.getOrigin().getPileIndex());
			
			tabPile.setExposeIndex(tabPile.getNumCards() - 1);
		}
		else if(selection.getOrigin().getLocationType() == LocationType.MAIN_DECK)
		{
			model.getMainDeck().setExposeIndex(model.getMainDeck().getNumCards() - 1);
		}
	}

	/**
	 *		 <p>If the main deck is not empty, then remove the top card from the main deck
	 * 		and add it to the waste pile.  If the main deck is empty, then all cards
	 * 		should be moved from the waste pile back to the main deck.</p>
	 * 
	 * 		<p>Note: when the waste pile is recycled, it should move cards
	 *		 back to the main deck such that they are in the same order in which
	 * 		they originally occurred.  In other words, drawing all of the cards
	 * 		from the main deck and then moving them back to the main deck
	 * 		should result in the main deck being in its original order.</p>
	 * 
	 * 		<p>Before this method returns, it should check whether the
	 * 		main deck is non-empty, and if it is non-empty, it should ensure
	 * 		that the top card on the main deck is exposed.</p>
	 * 
	 * @param model the {@link KlondikeModel}
	 */
	public void drawCardOrRecycleWaste(KlondikeModel model) 
	{
		if(model.getMainDeck().getNumCards() == 0)
		{
			for(int i = model.getWastePile().getNumCards() - 1; i >= 0; i--)
			{
				model.getMainDeck().addCard(model.getWastePile().drawCard());
			}
		}
		else
		{
			model.getWastePile().addCards(model.getMainDeck().removeCards(1));
			model.getMainDeck().setExposeIndex(model.getMainDeck().getNumCards() - 1);
		}
	}

	/**
	 * Determine if the player has won the game.
	 * 
	 * @param model the {@link KlondikeModel}
	 * @return true if each foundation pile has 13 cards, false otherwise
	 */
	public boolean isWin(KlondikeModel model)
	{
		boolean mainClear = model.getMainDeck().isEmpty();
		boolean wasteClear = model.getWastePile().isEmpty();
		
		for(int i = 0; i < NUMOFTABLEAU; i++)
		{
			if(model.getTableauPile(i).isEmpty() == false)
			{
				return false;
			}
		}
		
		for(int i = 0; i < NUMOFFOUNDATION; i++)
		{
			if(model.getFoundationPile(i).getTopCard().getRank() != Rank.KING)
			{
				return false;
			}
		}
		
		if(mainClear && wasteClear)
			return true;
		return false;
	}
}
