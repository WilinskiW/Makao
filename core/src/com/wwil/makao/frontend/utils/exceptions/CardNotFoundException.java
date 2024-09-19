package com.wwil.makao.frontend.utils.exceptions;

public class CardNotFoundException extends RuntimeException{
    public CardNotFoundException() {
        super("Card not found in backend. Rank and suit are null.");
    }

    public CardNotFoundException(String message) {
        super(message);
    }
}
