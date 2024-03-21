package com.wwil.makao.backend;

import java.util.List;

public class ComputerPlay extends Play{
    public ComputerPlay(List<Card> cardsPlayed, boolean draw, boolean dropped, boolean skipTurn, boolean demanding) {
        super(cardsPlayed, draw, dropped, skipTurn, demanding);
    }
}
