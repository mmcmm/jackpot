package com.ninjaskins.website.domain.util;

import com.ninjaskins.website.config.DomainConstants;
import com.ninjaskins.website.domain.Jackpot;
import com.ninjaskins.website.domain.JackpotDeposit;
import com.ninjaskins.website.domain.User;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;

/**
 * Utility class for our domain
 */
public final class DomainUtils {

    private DomainUtils() {
    }

    public static Boolean anyObjectsAreNull(Object... objects) {
        for (Object o : objects) {
            if (o == null) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public static Boolean userCanDeposit(User u) {
        return (!DomainUtils.anyObjectsAreNull(u.getFirstName(), u.getLastName(), u.getEmail(),
            u.getAddressStreet(), u.getAddressPostal(), u.getAddressCity(), u.getAddressCountry(),
            u.getBirthDay(), u.getBirthMonth(), u.getBirthYear()) && u.isAgreeTerms() && u.isYearsOld18());
    }

    public static Boolean safeToJackpotDeposit(Jackpot currentJackpot, int amount, int creditsBalance) {
        return (currentJackpot != null && currentJackpot.getWinner() == null
            && amount >= DomainConstants.JACKPOT_MIN_DEPOSIT_AMT && amount <= creditsBalance);
    }

    public static double getSecureRandomNumber(){
        SecureRandom random = new SecureRandom();
        return random.nextDouble();
    }

    /**
     * Returns a hexadecimal encoded SHA-256 hash for the input String.
     * @param data
     * @return
     */
    public static String getSHA256Hash(String data) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return bytesToHex(hash); // make it printable
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * Use javax.xml.bind.DatatypeConverter class in JDK to convert byte array
     * to a hexadecimal string. Note that this generates hexadecimal in upper case.
     * @param hash
     * @return
     */
    private static String bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }

    public static void threadSleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static int CalculateJackpotWinning(List<JackpotDeposit> jackpotDeposits){
        int winning = 0;
        for (JackpotDeposit jackpotDeposit : jackpotDeposits) {
            winning +=  jackpotDeposit.getAmount();
        }
        return (int) (winning - Math.floor(winning * DomainConstants.JACKPOT_PERCENT_FEE));
    }
}
