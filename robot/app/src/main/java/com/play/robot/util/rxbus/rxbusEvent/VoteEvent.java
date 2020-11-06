package com.play.robot.util.rxbus.rxbusEvent;

public class VoteEvent {
    int vote;

    public VoteEvent(int vote) {
        this.vote = vote;
    }

    public int getVote() {
        return vote;
    }

}
