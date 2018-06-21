package com.company;

import java.util.*;

public class Hand {
    private Random rand = new Random();
    private LinkedList<Card> cardList;

    public Hand() {
        this.cardList = new LinkedList<>();
    }

    public void prepareStartingHand(LinkedList<Card> theDeck, int size) {
        for (int i = 0; i < size; i++) {
            Card tempCard = theDeck.pop();  // taking card from the theDeck
            this.cardList.add(tempCard);    // and putting it in hand

        }
    }

    public void printDeck() {
        for (Card card: cardList) {
            System.out.println(card.toString());
        }
    }

    public int remainingCards() {
        return cardList.size();
    }

    public Card popCard() {
        return cardList.pop();
    }

    public void addCards(List newCards) {
        Collections.shuffle(newCards, new Random());
        cardList.addAll(newCards);
    }

    public List<Card> getCardList() {
        return cardList;
    }
}
