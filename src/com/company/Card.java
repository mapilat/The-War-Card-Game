package com.company;

public class Card {
    private Suit suit;
    private Rank rank;

    public Card(Rank rank, Suit suit) {
        this.suit = suit;
        this.rank = rank;
    }

    public String getSuit() {
        return suit.getSuit();
    }

    public int getRank() {
        return rank.getRank();
    }

    @Override
    public String toString() {
        String str = "";
        str += rank.printRank() + " of " + suit.getSuit();
        return str;
    }
}
