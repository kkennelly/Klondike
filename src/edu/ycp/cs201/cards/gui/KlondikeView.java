package edu.ycp.cs201.cards.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.RoundRectangle2D;


import java.io.FileNotFoundException;

import javax.swing.JPanel;

import edu.ycp.cs201.cards.Card;
import edu.ycp.cs201.cards.KlondikeController;
import edu.ycp.cs201.cards.KlondikeModel;
import edu.ycp.cs201.cards.Location;
import edu.ycp.cs201.cards.LocationType;
import edu.ycp.cs201.cards.Pile;
import edu.ycp.cs201.cards.Selection;
import edu.ycp.cs201.cards.StringifyGameState;

public class KlondikeView extends JPanel {

	private static final long serialVersionUID = 1L;
	
	/** Width of the window */
	private static final int WIDTH = 800;
	
	/** Height of window */
	private static final int HEIGHT = 600;
	
	/** Width of card images */
	private static final int CARD_WIDTH = 80;
	
	/** Height of card images */
	private static final int CARD_HEIGHT = 116;
	
	/** Offset of piles from left edge */
	private static final int LEFT_OFFSET = 30;
	
	/** Offset of top-row piles from top edge */
	private static final int TOP_OFFSET = 20;

	/** Offset of tableau piles from left edge */
	private static final int FOUNDATION_LEFT_OFFSET = 360;

	/** Offset of tableau piles from top edge */
	private static final int TABLEAU_TOP_OFFSET = 160;

	/** Spacing of piles horizontally */
	private static final int HORIZONTAL_PILE_SPACING = 110;

	/** Vertical spacing of cards in tableau piles */
	public static final int VERTICAL_CARD_SPACING = 24;
	
	private KlondikeModel model;
	private KlondikeController controller;
	private Selection theSelection;
	
	private CardImageCollection cardImageCollection;
	
	//Coordinates of Mouse
	private int colXCor;
	private int colYCor;
	
	
	public KlondikeView() {
		setBackground(new Color(0, 100, 0));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		cardImageCollection = new CardImageCollection();
		
		MouseAdapter listener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handleMousePressed(e);
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				handleMouseDragged(e);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				handleMouseReleased(e);
			}
			
			public void mouseMoved(MouseEvent e) {
				handleMouseMoved(e);
			}
		};
		addMouseListener(listener);
		addMouseMotionListener(listener);
		
		colXCor = 0;
		colYCor = 0;
		
		theSelection = null;
	}
	
	public void setModel(KlondikeModel model) {
		this.model = model;
	}
	
	public void setController(KlondikeController controller) {
		this.controller = controller;
	}
	
	protected void handleMousePressed(MouseEvent e) {
		
		int xCor = e.getX();
		int yCor = e.getY();
		
		//tableau pile
		if(yCor > TOP_OFFSET * 2 + CARD_HEIGHT)
		{
			int tabPile = findTabPile(xCor);
			int tabCard;
			
			//if the user clicked on a existent pile
			if(tabPile != -1)
			{
				tabCard = findTabCard(tabPile, yCor);
				
				//if the user clicked on a existent card
				if(tabCard != -1)
				{
					 theSelection = controller.select(model, new Location(LocationType.TABLEAU_PILE, tabPile, tabCard));
				}
			}
		}
		else if(yCor < CARD_HEIGHT + TOP_OFFSET && yCor > TOP_OFFSET)
		{
			//draw pile or main deck
			if(xCor < CARD_WIDTH + LEFT_OFFSET && xCor > LEFT_OFFSET && yCor < CARD_HEIGHT + TOP_OFFSET && yCor > TOP_OFFSET)	//draw pile
			{
				theSelection = controller.select(model, new Location(LocationType.MAIN_DECK, 0, model.getMainDeck().getIndexOfTopCard()));
			}	
			//waste pile
			else if(xCor < LEFT_OFFSET + (CARD_WIDTH * 2) + VERTICAL_CARD_SPACING && xCor > LEFT_OFFSET + CARD_WIDTH + VERTICAL_CARD_SPACING)
			{
				controller.drawCardOrRecycleWaste(model);
			}
		}
		
		repaint();
	}

	protected void handleMouseDragged(MouseEvent e) {
		//move the collection along with the mouse
		colXCor = e.getX();
		colYCor = e.getY();
		
		repaint();
	}

	protected void handleMouseReleased(MouseEvent e) {
		
		//gets the coordinates of point of release and stores them in temporary variables
		int xCor = e.getX();
		int yCor = e.getY();
		
		Location destLoc = getLocationDestination(xCor, yCor);
		
		if(theSelection != null)
		{
			if(destLoc != null && destLoc.getLocationType() != LocationType.WASTE_PILE)	//something is chosen as the selection
			{
				if(controller.allowMove(model, theSelection, destLoc))	//the move attempt is legal
				{
					if(destLoc.getLocationType() == LocationType.TABLEAU_PILE)
					{
						model.getTableauPile(destLoc.getPileIndex()).addCards(theSelection.getCards());
					}
					else if(destLoc.getLocationType() == LocationType.FOUNDATION_PILE)
					{
						model.getFoundationPile(destLoc.getPileIndex()).addCards(theSelection.getCards());
					}
				
					if(theSelection.getOrigin().getLocationType() == LocationType.TABLEAU_PILE)
					{
						Pile origin = model.getTableauPile(theSelection.getOrigin().getPileIndex());
				
						if(origin.getExposeIndex() > origin.getIndexOfTopCard())
						{
							origin.setExposeIndex(origin.getIndexOfTopCard());
						}
					}
					else if(theSelection.getOrigin().getLocationType() == LocationType.MAIN_DECK)
					{
						model.getMainDeck().setExposeIndex(model.getMainDeck().getIndexOfTopCard());
					}
				}
				else
				{
					controller.unselect(model, theSelection);
				}
			}
			else
			{
				controller.unselect(model, theSelection);
			}
		}
		else if(yCor < 590 && yCor > 570 &&
				xCor < 790 && xCor > 750)
		{
			Main.main(null);
		}
		
		theSelection = null;
		
		repaint();
	}
	
	//keeps track of the mouse for a smooth transition when selecting a Selection
	private void handleMouseMoved(MouseEvent e)
	{
		colXCor = e.getX();
		colYCor = e.getY();
	}
	
	//Only for Tableau Piles
	//@returns the pile index given the x coordinate 
	private int findTabPile(int x)
	{
		int num = -1;
		for (int i = 1; i < 8; i++)
		{
			int upperBounds = i * HORIZONTAL_PILE_SPACING;
			if(x < upperBounds && x > upperBounds - CARD_WIDTH)
			{
				num = i - 1;
			}
		}
		
		return num;
	}
	
	//only for Tableau Piles
	//@returns the card index number given the pile index and y coordinate 
	private int findTabCard(int num, int y)
	{
		int card = -1;
		int numOfCards = model.getTableauPile(num).getNumCards();
		
		if(numOfCards == 1)
			card = 0;
		else
		{
			if(y > TABLEAU_TOP_OFFSET + (numOfCards - 1) * VERTICAL_CARD_SPACING && y < TABLEAU_TOP_OFFSET + (numOfCards - 1) * VERTICAL_CARD_SPACING + CARD_HEIGHT)
				card = numOfCards - 1;
			else
			{	
				for(int i = TABLEAU_TOP_OFFSET + (numOfCards - 1) * VERTICAL_CARD_SPACING; i > TABLEAU_TOP_OFFSET; i = i - VERTICAL_CARD_SPACING)
				{
					if(y < i && y > i - VERTICAL_CARD_SPACING)
						card = (y - TABLEAU_TOP_OFFSET) / VERTICAL_CARD_SPACING;
				}
			}
		}		
				
		return card;
	}
	
	//Only for Foundation piles
	//@returns the pile index given the x coordinate
	//@returns -1 if the x coordinate does not correspond to a pile
	private int findFoundationPile(int x)
	{				
		for(int i = 0; i < 4; i++)
		{
			if(x < FOUNDATION_LEFT_OFFSET + CARD_WIDTH + ((CARD_WIDTH + VERTICAL_CARD_SPACING) * i) && 
			   x > FOUNDATION_LEFT_OFFSET + ((CARD_WIDTH + VERTICAL_CARD_SPACING) * i))
				return i;
		}
		
		return -1;
	}
	
	//@returns a Location object of the 
	private Location getLocationDestination(int x, int y)
	{
		if(y > TOP_OFFSET * 2 + CARD_HEIGHT) 	//destination of tableau pile
		{
			int tabPile = findTabPile(x);
			int tabCard;
			
			if(tabPile != -1)
			{
				tabCard = findTabCard(tabPile, y);
				
				if(tabCard != -1 || model.getTableauPile(tabPile).isEmpty())
				{
					return new Location(LocationType.TABLEAU_PILE, tabPile, model.getTableauPile(tabPile).getIndexOfTopCard());
				}
			}
		}
		else if(x > FOUNDATION_LEFT_OFFSET && 
				x < WIDTH && 
				y < TOP_OFFSET * 2 + CARD_HEIGHT && 
				y > TOP_OFFSET)						//destination of foundation pile
		{
			int pile = findFoundationPile(x);
			
			if(pile != -1)
			{
				return new Location(LocationType.FOUNDATION_PILE, pile, model.getFoundationPile(pile).getIndexOfTopCard());
			}
		}	
		else if(x > LEFT_OFFSET * 2 + CARD_WIDTH  &&
				x < LEFT_OFFSET * 2 + CARD_WIDTH * 2 &&
				y < TOP_OFFSET + CARD_HEIGHT &&
				y > TOP_OFFSET)                            
		{
			return new Location(LocationType.WASTE_PILE, 0, model.getWastePile().getIndexOfTopCard());
		}
		return null;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// Paint background
		super.paintComponent(g);
		
		// Paint main deck (showing top card)
		drawPile(g, LEFT_OFFSET, TOP_OFFSET, model.getMainDeck());
		
		model.getWastePile().setExposeIndex(100);
		
		// Paint waste pile
		//System.out.printf("Waste pile expose index=%d\n", model.getWastePile().getExposeIndex());		
		drawPile(g, LEFT_OFFSET + HORIZONTAL_PILE_SPACING, TOP_OFFSET, model.getWastePile());
		
		// Paint foundation piles (showing top card)
		for (int i = 0; i < 4; i++) {
			drawPile(g, FOUNDATION_LEFT_OFFSET + i*HORIZONTAL_PILE_SPACING, TOP_OFFSET, model.getFoundationPile(i));
		}
		
		// Paint tableau piles
		for (int i = 0; i < 7; i++) {
			drawTableauPile(g, LEFT_OFFSET + i*HORIZONTAL_PILE_SPACING, TABLEAU_TOP_OFFSET, model.getTableauPile(i));
		}
		
		//draw restart button
		g.drawRect(730, 570, 60, 20);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 10));
		g.drawString("NEW GAME", 733, 585);
		
		// TODO: draw selection (if there is one)
		if(theSelection != null)
		{
			drawSelection(g);
			Location dest = getLocationDestination(colXCor, colYCor);
			
			if(dest != null && controller.allowMove(model, theSelection, dest))
			{
				drawGreenAroundDestination(g, dest);
			}
				
		}
		
		// TODO: draw congratulatory message if player has won the game
		if(controller.isWin(model) == true)
		{
			g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
			g.drawString("You won the game!", 195, 200);
		}
		
		//Move hint = controller.findHint(model);
		
		//drawGreenAroundDestination(g, hint.getDestination());
		//drawGreenAroundDestination(g, hint.getSelection().getOrigin());
	}
	
	private void drawPile(Graphics g, int x, int y, Pile pile) {
		if (pile.isEmpty()) {
			// Draw outline
			g.setColor(Color.LIGHT_GRAY);
			g.drawRoundRect(x, y, CARD_WIDTH, CARD_HEIGHT, 12, 12);
		} else {
			// Draw image of top card (or card back image if top card is not exposed)
			Card top = pile.getTopCard();
			int indexOfTopCard = pile.getIndexOfTopCard();
			int exposeIndex = pile.getExposeIndex();
			//System.out.printf("topcard=%d,expose=%d\n", indexOfTopCard, exposeIndex);
			boolean isExposed = indexOfTopCard >= exposeIndex;
			BufferedImage img = isExposed ? cardImageCollection.getFrontImage(top) : cardImageCollection.getBackImage();
			g.drawImage(img, x, y, null);
		}
	}
	
	private void drawTableauPile(Graphics g, int x, int y, Pile tableauPile) {
		// Draw cards from bottom of pile towards top.
		// All cards whose indices are greater than or equal to
		// the pile's expose index are drawn face-up.
		int numCards = tableauPile.getNumCards();
		for (int i = 0; i < numCards; i++) {
			Card card = tableauPile.getCard(i);
			BufferedImage img = i >= tableauPile.getExposeIndex()
					? cardImageCollection.getFrontImage(card)
					: cardImageCollection.getBackImage();
			g.drawImage(img, x, y + i*VERTICAL_CARD_SPACING, null);
		}
	}
	
	private void drawSelection(Graphics g)
	{
		//draw the selection that is currently selected when the mouse is pressed
		for(int i = 0; i < theSelection.getNumCards(); i++)
		{
			Card curCard = theSelection.getCards().get(i);
			BufferedImage img = cardImageCollection.getFrontImage(curCard);
			g.drawImage(img, colXCor - 40, colYCor - 20 + i * VERTICAL_CARD_SPACING, null);
		}
	}
	
	private void drawGreenAroundDestination(Graphics g, Location dest)
	{
		//draw green highlight around a valid destination considering the current selection
		g.setColor(Color.GREEN);
		
		Graphics2D g2D = (Graphics2D) g;
		
		g2D.setStroke(new BasicStroke(7));
		
		int width = CARD_WIDTH;
		int x = dest.getPileIndex();
		int height = 0;
		int y = 0;
		if(dest.getLocationType() == LocationType.TABLEAU_PILE)
		{	
			height = (model.getTableauPile(dest.getPileIndex()).getNumCards() - 1) * VERTICAL_CARD_SPACING + CARD_HEIGHT;
			x = x * HORIZONTAL_PILE_SPACING + LEFT_OFFSET;
			y = TABLEAU_TOP_OFFSET;
		}
		else if(dest.getLocationType() == LocationType.FOUNDATION_PILE)
		{
			height = CARD_HEIGHT;
			x = (x + 3) * HORIZONTAL_PILE_SPACING + LEFT_OFFSET;
			y = TOP_OFFSET;
		}
		
		RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(x, y, width, height, 10, 10);
        g2D.draw(roundedRectangle);
	}
}
