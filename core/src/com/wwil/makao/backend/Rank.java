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

    public String getName() {
        return name;
    }
}
