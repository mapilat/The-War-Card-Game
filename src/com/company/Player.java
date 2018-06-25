package com.company;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Player {

    private String name;
    private int size;
    private Hand hand;

    public Player(String name, int size, LinkedList<Card> theDeck) {
        this.name = name;
        this.size = size;
        hand = new Hand();

        preparePlayerHand(theDeck, size);
    }

    public String getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }

    public int getSize() {
        return size;
    }

    public void addCards(List newCards) {
        hand.addCards(newCards);
    }

    public int remainingCards() {
        return hand.remainingCards();
    }

    public Card popCard() {
        return hand.popCard();
    }

    private void preparePlayerHand(LinkedList<Card> theDeck, int size) {
        hand.prepareStartingHand(theDeck, size);
    }
}
