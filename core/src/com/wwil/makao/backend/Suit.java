package com.wwil.makao.backend;

public enum Suit {
    HEART("kier"), DIAMOND("karo"), CLUB("trefl"), SPADE("pik");
    private final String name;

    Suit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

 /*
    Heart - Kier
    Diamond - Karo
    Club - Trefl
    Spade - Pik
 */