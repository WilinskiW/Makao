package com.wwil.makao.backend.model.card;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static Rank getRandomNonFunctional() {
        List<Rank> nonFunctional = getNonFunctionalRanks();
        Collections.shuffle(nonFunctional);
        return nonFunctional.get(0);
    }

    public static Rank getRandomAttackingRank() {
        List<Rank> attackingRanks = new ArrayList<>(Arrays.asList(Rank.values()));
        takeAwayNonAttackingRanks(attackingRanks);
        Collections.shuffle(attackingRanks);
        return attackingRanks.get(0);
    }

    private static void takeAwayNonAttackingRanks(List<Rank> attackingRanks){
        List<Rank> nonFunctionalRanks = getNonFunctionalRanks();
        attackingRanks.removeAll(nonFunctionalRanks);
        attackingRanks.remove(Rank.JOKER);
        attackingRanks.remove(Rank.Q);
    }

    private static List<Rank> getNonFunctionalRanks() {
        return Stream.of(Rank.values())
                .filter(rank -> rank.getAbility() == Ability.NONE)
                .collect(Collectors.toList());
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
