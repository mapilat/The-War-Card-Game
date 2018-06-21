package com.company;

import Exceptions.OutOfCardsException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Game {
    Scanner scanner = new Scanner(System.in);
    Deck theDeck = new Deck();
    Hand player = new Hand();
    Hand computer = new Hand();
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
        player.prepareStartingHand(theDeck.getCardList(), amount);
        computer.prepareStartingHand(theDeck.getCardList(), amount);

        System.out.println("Game is ready. Press enter to begin...");
        scanner.nextLine(); // ENTER to start the game

        while(true) { // breaks if one of the hand is empty
            turnCounter++;
            Card playerCard = player.popCard(); // popping card from player hand
            Card computerCard = computer.popCard(); //popping card from computer hand
            System.out.println("Turn ["+turnCounter+"]" + " -> Player: " +
                    playerCard.toString() + ", Computer: " + computerCard.toString());

            try { // handling hand out of cards exception
                if(playerCard.getRank() == JOKER_VALUE && computerCard.getRank() == JOKER_VALUE) {
                    fight(playerCard, computerCard);
                }
                else if(playerCard.getRank() == JOKER_VALUE) {           // player played joker
                    System.out.println("A wild Joker appeared...");
                    playerJoker(playerCard, computerCard);
                }else if( computerCard.getRank() == JOKER_VALUE) {  // computer played joker
                    System.out.println("A wild Joker appeared...");
                    computerJoker(playerCard, computerCard);
                } else { // fight for no-joker cards
                    fight(playerCard, computerCard);
                }
            }catch (OutOfCardsException outOfCardsException) {
                System.out.println(outOfCardsException.getMessage());
            }

            if(player.remainingCards() == 0) {              // break if player hand is 0
                System.out.println("Computer won the game.");
                break;
            } else if(computer.remainingCards() == 0) {     // break if computer hand is zero
                System.out.println("Player won the game.");
                break;
            }

            System.out.println("Player cards: " + player.remainingCards() +
                    " ,Computer cards: " + computer.remainingCards());  // printing remaining hand size for both hands

        }//end while


    } //end start()


    private void fight(Card playerCard, Card computerCard) throws OutOfCardsException{
        List<Card> temp = new LinkedList<>(); // helping list for storing fighting cards
        temp.add(playerCard);   // add player card to helping list
        temp.add(computerCard); // add computer card to helping list

        if(playerCard.getRank() > computerCard.getRank()) {         // player is winner
            player.addCards(temp);
            System.out.println("Player won the battle!");
        } else if(playerCard.getRank() < computerCard.getRank()) {  // computer is winner
            computer.addCards(temp);
            System.out.println("Computer won the battle!");
        } else {                                                    // a tie (war)
            System.out.println("War!");
            war(temp); // passing current stack of fighting cards to later add them to winner's hand
        }
    }

    private void war(List<Card> fightingCards) throws OutOfCardsException {
        // creating lists for storing cards that participate in war
        List<Card> playerStack = new ArrayList<>();     // stack of cards for player
        List<Card> computerStack = new ArrayList<>();   // stack of cards for computer

        for (int i = 0; i < 3; i++) { // looping 4 times cuz we need 4 cards each stack

            if(player.remainingCards() == 0) { // throw exception if not enough cards to resolve war
                throw new OutOfCardsException("Oops! Player is out of cards!");
            } else if(computer.remainingCards() == 0) {
                throw new OutOfCardsException("Oops! Computer is out of cards!");
            }

            System.out.println("\tPlayer card is xx\n\tComputer card is xx");

            playerStack.add(player.popCard());      // popping cards from hand to player stack
            computerStack.add(computer.popCard());  // popping cards from hand to computer stack

        }//end for


        resolveWar(playerStack, computerStack, fightingCards); // handling fight in war
    }

    private void resolveWar(List<Card> playerStack, List<Card> computerStack, List<Card> fightingCards)
            throws OutOfCardsException{

        Card playerCard = playerStack.get(0);       // picking player card for fight
        Card computerCard = computerStack.get(0);   // picking computer card for fight
        List<Card> temp = new LinkedList<>();       // stack of cards participating in war
        temp.addAll(playerStack);                   // adding player war stack
        temp.addAll(computerStack);                 // adding computer war stack

        System.out.println("\tPlayer: " + playerCard.toString() +
                ", Computer: " + computerCard.toString());

        if(playerCard.getRank() > computerCard.getRank()) {         // player wins
            player.addCards(temp);
            player.addCards(fightingCards);
            System.out.println("\tPlayer won the war!");
        } else if(playerCard.getRank() < computerCard.getRank()) {  // computer wins
            computer.addCards(temp);
            computer.addCards(fightingCards);
            System.out.println("\tComputer won the war!");
        } else {                                                    // a tie
            System.out.println("\tWar!");
            fightingCards.addAll(temp); // adding all war cards to list
            war(fightingCards);         // and passing it to war()
        }
    }

    private void playerJoker(Card playerCard, Card computerCard) throws OutOfCardsException{
        List<Card> tempList = new LinkedList<>(); // storing pilled computer cards
        int tempVal = 0;

        if(computer.remainingCards() < 3) { // check if computer has enough cards
            throw new OutOfCardsException("Oops! Computer is out of cards!");
        }

        for (int i = 0; i < 3; i++) { // pilling up computer cards from his hand
            Card card = computer.popCard();
            tempList.add(card);
            tempVal += card.getRank(); //
            System.out.println("\tComputer card is xx");
        }
        tempList.add(computerCard); // bundling cards
        tempList.add(playerCard);

        if(tempVal > JOKER_VALUE) {
            System.out.println("Computer defended itself from the Joker!");
            computer.addCards(tempList);
        } else {
            System.out.println("Computer lost against the Joker!");
            player.addCards(tempList);
        }

    }

    private void computerJoker(Card playerCard, Card computerCard) throws OutOfCardsException{
        List<Card> tempList = new LinkedList<>(); // storing pilled player cards
        int tempVal = 0;

        if(player.remainingCards() < 3) { // check if player has enough cards
            throw new OutOfCardsException("Oops! Player is out of cards!");
        }

        for (int i = 0; i < 3; i++) { // pilling up player cards from his hand
            Card card = player.popCard();
            tempList.add(card);
            tempVal += card.getRank();
            System.out.println("\tPlayer card is xx");
        }
        tempList.add(computerCard); // bundling cards
        tempList.add(playerCard);

        if(tempVal > JOKER_VALUE) {
            System.out.println("Player defended itself from the Joker!");
            player.addCards(tempList);
        } else {
            System.out.println("Player lost against the Joker!");
            computer.addCards(tempList);
        }
    }


} // end Game class
