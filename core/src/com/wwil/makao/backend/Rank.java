package com.wwil.makao.backend;

public enum Rank {
    AS(Ability.CHANGE_SUIT, "AS"), TWO(Ability.PLUS_2, "2"), THREE(Ability.PLUS_3, "3"),
    FOUR(Ability.WAIT, "4"), FIVE(null, "5"), SIX(null, "6"),
    SEVEN(null, "7"), EIGHT(null, "8"), NINE(null, "9"),
    TEN(null, "10"), Q(Ability.ON_EVERYTHING, "Q"), J(Ability.DEMAND, "J"),
    K(Ability.KING_EXCEPTION, "K");
    //, JOKER(Ability.WILD_CARD, "JOKER");
    private final Ability ability;
    private final String name;

    Rank(Ability ability, String name) {
        this.ability = ability;
        this.name = name;
    }

    public static Rank specifyRankInWords(int dana) {

        if (dana == 5) {
            return Rank.FIVE;
        } else if (dana == 6) {
            return Rank.SIX;
        } else if (dana == 7) {
            return Rank.SEVEN;
        } else if (dana == 8) {
            return Rank.EIGHT;
        } else if (dana == 9) {
            return Rank.NINE;
        } else if (dana == 10) {
            return Rank.TEN;
        }
        else {
            return null;
        }
    }

    public static Rank specifyRankInWords(String dana) {
        if ("AS".equals(dana)) {
            return Rank.AS;
        } else if ("Q".equals(dana)) {
            return Rank.Q;
        } else if ("J".equals(dana)) {
            return Rank.J;
        } else if ("K".equals(dana)) {
            return Rank.K;
        }
        else {
            return null;
        }
    }

    public Ability getAbility() {
        return ability;
    }

    public String getName() {
        return name;
    }
}
