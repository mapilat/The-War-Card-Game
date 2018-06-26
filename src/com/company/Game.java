package com.company;

import Exceptions.OutOfCardsException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Game {
    Scanner scanner = new Scanner(System.in);
    Deck theDeck = new Deck();
    Player player1;
    Player player2;
    String player1Name = "Player";
    String player2Name = "Computer";
    boolean isOver = false;
    boolean autoPlay = false;
    private final int JOKER_VALUE = 23;
    private final int MIN_HAND_SIZE = 5;
    private final int MAX_HAND_SIZE = 27;

    public void start() {
        theDeck.populate(); // filling starting deck (52cards + 2 jokers)

        int turnCounter = 0;
        int amount = 0;

        //checking the starting input (hand size)
        boolean isOk = false;
        while(!isOk) {
            System.out.println("How many cards per hand?(5-27) ");
            amount = scanner.nextInt();
            scanner.nextLine();
            if(amount < MIN_HAND_SIZE || amount > MAX_HAND_SIZE) {
                System.out.println("Wrong amount of cards per hand.");
            } else {
                isOk = true;
            }
        } //end while

        // filling hands with theDeck cards
        player1 = new Player(player1Name, amount, theDeck.getCardList());
        player2 = new Player(player2Name, amount, theDeck.getCardList());

        System.out.println("Game is ready. Press enter to begin...");
        scanner.nextLine(); // ENTER to start the game

        while(!isOver) { // breaks if one of the hand is empty

            boolean autoPlayInput = true;
            while(autoPlayInput){           // interface for playing card by card or printing all turns automatically
                autoPlayInput = false;
                if(!autoPlay) {
                    System.out.println("Press ENTER to play a card. Type \"auto\" to auto complete the game");
                    String choice = scanner.nextLine();
                    if(choice.toLowerCase().equals("auto")){
                        autoPlay = true;
                    } else if(!choice.equals("")) {
                        System.out.println("Wrong input.");
                        autoPlayInput = true;
                    }
                } // end if
            } // end while


            turnCounter++;
            Card player1Card = player1.getHand().popCard(); // popping card from player1 hand
            Card player2Card = player2.getHand().popCard(); //popping card from player2 hand
            System.out.println("Turn ["+turnCounter+"]" + " -> Player: " +
                    player1Card.toString() + ", Computer: " + player2Card.toString());

            try { // handling hand out of cards exception
                if(player1Card.getRank() == JOKER_VALUE && player2Card.getRank() == JOKER_VALUE) {
                    fight(player1Card, player2Card);
                }
                else if(player1Card.getRank() == JOKER_VALUE) {           // player1 played joker
                    System.out.println("A wild Joker appeared...");
                    handleJoker(player2Card, player1Card, player2, player1);
                }else if( player2Card.getRank() == JOKER_VALUE) {  // player2 played joker
                    System.out.println("A wild Joker appeared...");
                    handleJoker(player1Card, player2Card, player1, player2);
                } else { // fight for no-joker cards
                    fight(player1Card, player2Card);
                }
            }catch (OutOfCardsException outOfCardsException) {
                System.out.println(outOfCardsException.getMessage());
            }

            // win condition check
            if(player1.remainingCards() == 0 || player2.remainingCards() == 0) {
                String winner = player1.remainingCards() == 0 ? player2Name : player1Name;// break if player1 hand is 0
                System.out.println("Game Over. " + winner + " won the game!");
                isOver = true;
            } else {
                System.out.println("Player cards: " + player1.remainingCards() +
                        " ,Computer cards: " + player2.remainingCards());  // printing remaining hand size for both hands
            }

        }//end while


    } //end start()


    private void fight(Card player1Card, Card player2Card) throws OutOfCardsException{
        List<Card> temp = new LinkedList<>(); // helping list for storing fighting cards
        temp.add(player1Card);   // add player1 card to helping list
        temp.add(player2Card); // add player2 card to helping list

        if(player1Card.getRank() > player2Card.getRank()) {         // player1 is winner
            player1.addCards(temp);
            System.out.println("Player won the battle!");
        } else if(player1Card.getRank() < player2Card.getRank()) {  // player2 is winner
            player2.addCards(temp);
            System.out.println("Computer won the battle!");
        } else {                                                    // a tie (war)
            System.out.println("War!");
            war(temp); // passing current stack of fighting cards to later add them to winner's hand
        }
    }

    private void war(List<Card> fightingCards) throws OutOfCardsException {
        // creating lists for storing cards that participate in war
        List<Card> player1Stack = new ArrayList<>();     // stack of cards for player1
        List<Card> player2Stack = new ArrayList<>();   // stack of cards for player2

        for (int i = 0; i < 3; i++) { // looping 4 times cuz we need 4 cards each stack

            if(player1.remainingCards() == 0 || player2.remainingCards() == 0) { // throw exception if not enough cards to resolve war
                throw new OutOfCardsException("Oops! Not enough cards!");
            }

            System.out.println("\tPlayer card is xx\n\tComputer card is xx");

            player1Stack.add(player1.popCard());      // popping cards from hand to player1 stack
            player2Stack.add(player2.popCard());  // popping cards from hand to player2 stack

        }//end for


        resolveWar(player1Stack, player2Stack, fightingCards); // handling fight in war
    }

    private void resolveWar(List<Card> player1Stack, List<Card> player2Stack, List<Card> fightingCards)
            throws OutOfCardsException{

        Card player1Card = player1Stack.get(0);       // picking player1 card for fight
        Card player2Card = player2Stack.get(0);   // picking player2 card for fight
        List<Card> temp = new LinkedList<>();       // stack of cards participating in war
        temp.addAll(player1Stack);                   // adding player1 war stack
        temp.addAll(player2Stack);                 // adding player2 war stack

        System.out.println("\tPlayer: " + player1Card.toString() +
                ", Computer: " + player2Card.toString());

        if(player1Card.getRank() > player2Card.getRank()) {         // player1 wins
            player1.addCards(temp);
            player1.addCards(fightingCards);
            System.out.println("\tPlayer won the war!");
        } else if(player1Card.getRank() < player2Card.getRank()) {  // player2 wins
            player2.addCards(temp);
            player2.addCards(fightingCards);
            System.out.println("\tComputer won the war!");
        } else {                                                    // a tie
            System.out.println("\tWar!");
            fightingCards.addAll(temp); // adding all war cards to list
            war(fightingCards);         // and passing it to war()
        }
    }

    private void handleJoker(Card nonJokerCard, Card jokerCard, Player nonJokerPlayer, Player jokerPlayer)
      throws OutOfCardsException{

        List<Card> tempList = new LinkedList<>(); // storing pilled nonJokerPlayer cards
        int tempVal = nonJokerCard.getRank();

        if(nonJokerPlayer.remainingCards() < 2) { // check if nonJokerPlayer has enough cards
            throw new OutOfCardsException("Oops! " + nonJokerPlayer.getName() + " is out of cards!");
        }

        System.out.println("\t" + nonJokerPlayer.getName() +" card: " +
                nonJokerCard.toString() + " has value of " + nonJokerCard.getRank());   // printing first card

        for (int i = 0; i < 2; i++) { // pilling up nonJokerPlayer cards from his hand
            Card card = nonJokerPlayer.popCard();
            tempList.add(card);
            tempVal += card.getRank(); //
            System.out.println("\t" + nonJokerPlayer.getName() +" card: " +
                    card.toString() + " has value of " + card.getRank());
        }
        System.out.println("Total value: " + tempVal + ", joker value is " + JOKER_VALUE);

        tempList.add(nonJokerCard); // bundling cards
        tempList.add(jokerCard);

        if(tempVal > JOKER_VALUE) { // final check if nonJokerPlayer got enough points (>23)
            System.out.println(nonJokerPlayer.getName() + " defended itself from the Joker!");
            nonJokerPlayer.addCards(tempList);
        } else {
            System.out.println(nonJokerPlayer.getName() + " lost against the Joker!");
            jokerPlayer.addCards(tempList);
        }

    }


} // end Game class
