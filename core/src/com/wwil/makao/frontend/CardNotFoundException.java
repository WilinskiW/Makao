package com.wwil.makao.frontend;

public class CardNotFoundException extends RuntimeException{
    CardNotFoundException() {
        super("Card not found in backend. Rank and suit are null.");
    }
}
