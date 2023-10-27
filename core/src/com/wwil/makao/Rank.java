package com.wwil.makao;

public enum Rank {
    AS(Ability.CHANGE_SUIT), TWO(Ability.PLUS_2), THREE(Ability.PLUS_3), FOUR(Ability.WAIT),
    FIVE(null), SIX(null), SEVEN(null), EIGHT(null), NINE(null), TEN(null),
    Q(Ability.ON_EVERYTHING), J(Ability.DEMAND), K(Ability.KING_EXCEPTION), JOKER(Ability.WILD_CARD);
    public final Ability ability;

    Rank(Ability ability) {
        this.ability = ability;
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

}
