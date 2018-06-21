package com.company;

public enum Suit {
    HEARTS("Hearts"),
    DIAMONDS("Diamonds"),
    CLUBS("Clubs"),
    SPADES("Spades");

    private final String suitText;

    Suit(String suitText) {
        this.suitText = suitText;
    }

    public String getSuit() {
        return suitText;
    }
}
