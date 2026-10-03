/*******************************************************************************
 * Solitaire
 *
 * Copyright (C) 2025 by Martin P. Robillard
 *
 * See: https://github.com/prmr/Solitaire
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see http://www.gnu.org/licenses/.
 *******************************************************************************/
package ca.mcgill.solitaire.cards;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a deck of 52 cards in a pre-determined order that allows the
 * game to be won.
 */
public class Deck {
	private CardStack aCards;

	/**
	 * Creates a new deck of 52 cards in a pre-determined, winnable order.
	 */
	public Deck() {
		shuffle();
	}

	/**
	 * Reinitializes the deck with all 52 cards in the pre-determined,
	 * winnable order.
	 */
	public void shuffle() {
		Rank[] ranks = Rank.values();
		Suit[] suits = Suit.values();

		// 24 cards for the stock: ACE..SIX, in increasing rank
		List<Card> stock = new ArrayList<>();
		for (int r = 0; r < 6; r++) {
			for (Suit suit : suits) {
				stock.add(Card.get(ranks[r], suit));
			}
		}

		// 28 cards for the tableau: SEVEN..KING, in increasing rank
		List<Card> tableauCards = new ArrayList<>();
		for (int r = 6; r < ranks.length; r++) {
			for (Suit suit : suits) {
				tableauCards.add(Card.get(ranks[r], suit));
			}
		}

		// columns.get(j) lists the cards of pile j; index 0 is the card that
		// will end up face up on top, the last index is the bottom card.
		List<List<Card>> columns = new ArrayList<>();
		for (int j = 0; j < 7; j++) {
			columns.add(new ArrayList<>());
		}
		int index = 0;
		for (int layer = 0; layer < 7; layer++) {
			for (int j = layer; j < 7; j++) {
				columns.get(j).add(tableauCards.get(index));
				index++;
			}
		}

		// The tableau is dealt one pile at a time. Within a pile, cards are dealt
		// from the bottom to the top, so the face-up card (index 0) comes last.
		List<Card> drawOrder = new ArrayList<>();
		for (int j = 0; j < 7; j++) {
			for (int k = j; k >= 0; k--) {
				drawOrder.add(columns.get(j).get(k));
			}
		}
		drawOrder.addAll(stock);;

		// draw() pops from the top of the stack, so push in reverse order
		aCards = new CardStack();
		for (int k = drawOrder.size() - 1; k >= 0; k--) {
			aCards.push(drawOrder.get(k));
		}
	}

	/**
	 * Places pCard on top of the deck.
	 *
	 * @param pCard The card to place on top of the deck.
	 * @pre pCard !=null
	 */
	public void push(Card pCard) {
		assert pCard != null;
		aCards.push(pCard);
	}

	/**
	 * Draws a card from the deck and removes the card from the deck.
	 *
	 * @return The card drawn.
	 * @pre !isEmpty()
	 */
	public Card draw() {
		assert !isEmpty();
		return aCards.pop();
	}

	/**
	 * @return True iff there are no cards in the deck.
	 */
	public boolean isEmpty() {
		return aCards.isEmpty();
	}
}