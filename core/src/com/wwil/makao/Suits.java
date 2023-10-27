package com.wwil.makao;

public enum Suits {
    KIER, KARO, TREFL, PIK;
    public static Suits giveSuit(String color) {
        String danaUpper = color.toUpperCase();

        if ("KIER".equals(danaUpper)) {
            return Suits.KIER;
        } else if ("KARO".equals(danaUpper)) {
            return Suits.KARO;
        } else if ("TREFL".equals(danaUpper)) {
            return Suits.TREFL;
        } else if ("PIK".equals(danaUpper)) {
            return Suits.PIK;
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