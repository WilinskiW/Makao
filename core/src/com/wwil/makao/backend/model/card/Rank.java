package com.wwil.makao.backend.model.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum Rank {
    AS(Ability.CHANGE_SUIT, "AS"), TWO(Ability.PLUS_2, "2"), THREE(Ability.PLUS_3, "3"),
    FOUR(Ability.WAIT, "4"), FIVE(Ability.NONE, "5"), SIX(Ability.NONE, "6"),
    SEVEN(Ability.NONE, "7"), EIGHT(Ability.NONE, "8"), NINE(Ability.NONE, "9"),
    TEN(Ability.NONE, "10"), Q(Ability.ON_EVERYTHING, "Q"), J(Ability.DEMAND, "J"),
    K(Ability.KING, "K"), JOKER(Ability.WILD_CARD, "JOKER");
    private final Ability ability;
    private final String name;

    Rank(Ability ability, String name) {
        this.ability = ability;
        this.name = name;
    }

    public static Rank getRandomWithAbility() {
        List<Rank> ranksForJoker = new ArrayList<>();
        for (Rank rank : Rank.values()) {
            if (!rank.ability.equals(Ability.NONE) &&
                    !rank.ability.equals(Ability.WILD_CARD) &&
                    !rank.ability.equals(Ability.ON_EVERYTHING)) {
                ranksForJoker.add(rank);
            }
        }
        Random random = new Random();
        int randomIndex = random.nextInt(ranksForJoker.size());
        return ranksForJoker.get(randomIndex);
    }

    public static Rank getRank(String nameOfRank) {
        for (Rank rank : values()) {
            if (rank.name.equals(nameOfRank)) {
                return rank;
            }
        }
        return null;
    }
    public Ability getAbility() {
        return ability;
    }

    public String getName() {
        return name;
    }
}
