package edu.ycp.cs201.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Class to represent a pile of {@link Card}s.
 * Each card has an index, with index 0 being the card
 * at the bottom of the pile.
 * Each pile has an "expose index": all cards whose
 * indices are greater than or equal to the expose index
 * are face-up, and all other cards are face-down.
 */
public class Pile {
	
	private ArrayList <Card> pile;
	private int expIndex;
	/**
	 * Constructor.  The pile will be empty initially,
	 * and its expose index will be set to 0.
	 */
	public Pile() 
	{
		pile = new ArrayList <Card> ();
		expIndex = 0;
	}

	/**
	 * @return the expose index
	 */
	public int getExposeIndex() 
	{
		return expIndex;
	}
	
	/**
	 * Set the expose index.
	 * 
	 * @param exposeIndex the expose index to set
	 */
	public void setExposeIndex(int exposeIndex) 
	{
		expIndex = exposeIndex;
	}
	
	/**
	 * Add a {@link Card} to the pile.  The card added is placed
	 * on top of the cards currently in the pile.
	 * 
	 * @param card the {@link Card} to add
	 */
	public void addCard(Card card) 
	{
		pile.add(card);
	}

	/**
	 * @return the number of @{link Card}s in the pile
	 */
	public int getNumCards() 
	{
		return pile.size();
	}
	
	/**
	 * @return true if the pile is empty, false otherwise
	 */
	public boolean isEmpty() 
	{
		if (pile.size() == 0)
			return true;
		return false;
	}
	
	/**
	 * Get the {@link Card} whose index is given.
	 * 
	 * @param index the index of the card to get
	 * @return the {@link Card} at the index
	 * @throws NoSuchElementException if the index does not refer to a valid card
	 */
	public Card getCard(int index) 
	{
		try
		{
			Card theCard = pile.get(index);
			return theCard;
		}
		catch (Exception IndexOutOfBoundsException)
		{
			throw new NoSuchElementException();
		}
	}

	/**
	 * Get the {@link Card} on top of the pile.
	 * 
	 * @return the {@link Card} on top of the pile
	 * @throws NoSuchElementException if the pile is empty
	 */
	public Card getTopCard() 
	{
		try
		{
			return pile.get(pile.size() - 1);
		}
		catch (Exception IndexOutOfBoundsException)
		{
			throw new NoSuchElementException();
		}
	}
	
	/**
	 * @return the index of the top {@link Card}, or -1 if the pile is empty
	 */
	public int getIndexOfTopCard()
	{
		return pile.size() - 1;
	}
	
	/**
	 * Remove given number of {@link Card}s from the top of the pile.
	 * 
	 * @param numCards number of cards to remove
	 * @return an ArrayList containing the removed cards
	 * @throws IllegalArgumentException if the pile does not have enough {@link Card}s to satisfy the request
	 */
	public ArrayList<Card> removeCards(int numCards) 
	{
		try
		{
			ArrayList <Card> removed = new ArrayList <Card> ();
			for (int i = 0; i < numCards; i++)
			{
				removed.add(0, pile.remove(pile.size() - 1));
			}
			
			return removed;
		}
		catch(Exception OutOfBoundException)
		{
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Add {@link Card}s to the top of the pile.
	 * 
	 * @param cardsToAdd an ArrayList containing the {@link Card}s to add
	 */
	public void addCards(ArrayList<Card> cardsToAdd) 
	{
		for (int i = 0; i < cardsToAdd.size(); i ++)
		{
			pile.add(cardsToAdd.get(i));
		}
	}
	
	/**
	 * Populate the pile by adding 52 {@link Card}s
	 * representing all possible combinations of
	 * {@link Suit} and {@link Rank}.
	 */
	public void populate() 
	{
		Suit[] allSuits = Suit.values();
		Rank[] allRanks = Rank.values();

		for (int j = 0; j < allSuits.length; j++) {
		    for (int i = 0; i < allRanks.length; i++) {
		    	pile.add(new Card(allRanks[i], allSuits[j]));
		    }
		}
	}

	/**
	 * Shuffle the {@link Card}s in the pile by rearranging
	 * them randomly.
	 */
	public void shuffle() 
	{
		Collections.shuffle(pile);
	}
	
	/**
	 * Remove the top {@link Card} on the pile and return it.
	 * 
	 * @return the removed {@link Card}
	 * @throws NoSuchElementException if the pile is empty
	 */
	public Card drawCard()
	{
		try
		{
			return pile.remove(pile.size() - 1);
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new NoSuchElementException();
		}
	}
}
