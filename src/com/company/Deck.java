package com.company;

import java.util.*;

public class Deck {
    private LinkedList<Card> cardList;

    public Deck() {
        this.cardList = new LinkedList<>();
    }

    public void populate() {

        for(Suit suit: Suit.values()) {
            for(Rank rank: Rank.values()) {
                if(rank.getRank() != 23){
                    cardList.add(new Card(rank, suit));
                }
            }
        }
        cardList.add(new Card(Rank.RED_JOKER, Suit.HEARTS));
        cardList.add(new Card(Rank.BLACK_JOKER, Suit.SPADES));


        Collections.shuffle(cardList, new Random());
    }

    public LinkedList<Card> getCardList() {
        return cardList;
    }

    public void printDeck() {
        for (Card card: cardList) {
            System.out.println(card.toString());
        }
    }
}
