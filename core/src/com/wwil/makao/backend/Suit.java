package com.wwil.makao.backend;

public enum Suit {
    HEART("kier"), DIAMOND("karo"), CLUB("trefl"), SPADE("pik");
    private String name;

    Suit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Suit giveSuit(String color) {
        String danaUpper = color.toUpperCase();

        if ("KIER".equals(danaUpper)) {
            return Suit.HEART;
        } else if ("KARO".equals(danaUpper)) {
            return Suit.DIAMOND;
        } else if ("TREFL".equals(danaUpper)) {
            return Suit.CLUB;
        } else if ("PIK".equals(danaUpper)) {
            return Suit.SPADE;
        } else {
            return null;
        }
    }


}

 /*
    Heart - Kier
    Diamond - Karo
    Club - Trefl
    Spade - Pik
 */