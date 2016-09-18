package com.ninjaskins.website.domain.util;

import com.ninjaskins.website.domain.User;

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
}
