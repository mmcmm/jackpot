package com.ninjaskins.website.config;

/**
 * Application constants.
 */
public final class DomainConstants {

    // jackpot min deposits
    public static final int JACKPOT_MIN_DEPOSIT_AMT = 10;
    // number of jackpot deposits
    public static final int JACKPOT_MIN_DEPOSITS_NR = 10;
    // our jackpot fee
    public static final float JACKPOT_PERCENT_FEE = 0.05f;
    // seconds delay between games
    public static final int JACKPOT_DELAY_SPIN_WINNER = 10;
    // seconds delay after we get winner
    public static final int JACKPOT_DELAY_AFTER_WINNER = 5;

    private DomainConstants() {
    }
}
