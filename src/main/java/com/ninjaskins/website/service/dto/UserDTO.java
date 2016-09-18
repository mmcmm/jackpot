package com.ninjaskins.website.service.dto;

import com.ninjaskins.website.config.Constants;

import com.ninjaskins.website.domain.Authority;
import com.ninjaskins.website.domain.User;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.*;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 100)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    private boolean activated = false;

    @Size(min = 2, max = 5)
    private String langKey;

    private Set<String> authorities;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this(user.getLogin(), user.getFirstName(), user.getLastName(),
            user.getEmail(), user.getActivated(), user.getLangKey(),
            user.getAuthorities().stream().map(Authority::getName)
                .collect(Collectors.toSet()));
    }

    public UserDTO(String login, String firstName, String lastName,
        String email, boolean activated, String langKey, Set<String> authorities) {

        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activated = activated;
        this.langKey = langKey;
        this.authorities = authorities;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", addressStreet='" + addressStreet + '\'' +
            ", addressPostal='" + addressPostal + '\'' +
            ", addressCity='" + addressCity + '\'' +
            ", addressCountry='" + addressCountry + '\'' +
            ", birthDay='" + birthDay + '\'' +
            ", birthMonth='" + birthMonth + '\'' +
            ", birthYear='" + birthYear + '\'' +
            ", agreeTerms='" + agreeTerms + '\'' +
            ", yearsOld18='" + yearsOld18 + '\'' +
            ", email='" + email + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", authorities=" + authorities +
            "}";
    }

    // next custom profile fields

    public UserDTO setProfileInfo(User user, String country)
    {
        this.addressStreet = user.getAddressStreet();
        this.addressPostal = user.getAddressPostal();
        this.addressCity = user.getAddressCity();
        this.addressCountry = user.getAddressCountry() == null ? country : user.getAddressCountry();
        this.birthDay = user.getBirthDay();
        this.birthMonth = user.getBirthMonth();
        this.birthYear = user.getBirthYear();
        this.agreeTerms = user.isAgreeTerms();
        this.yearsOld18 = user.isYearsOld18();
        return this;
    }

    @Size(min = 2, max = 2)
    private String birthDay;

    @Size(min = 2, max = 2)
    private String birthMonth;

    @Size(min = 4, max = 4)
    private String birthYear;

    @Size(max = 95)
    private String addressStreet;

    @Size(max = 11)
    private String addressPostal;

    @Size(max = 65)
    private String addressCity;

    @Size(min = 2, max = 2)
    private String addressCountry;

    private Boolean agreeTerms = false;

    private Boolean yearsOld18 = false;

    public String getBirthDay() {
        return birthDay;
    }

    public String getBirthMonth() {
        return birthMonth;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public String getAddressPostal() {
        return addressPostal;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public Boolean isAgreeTerms() {
        return agreeTerms;
    }

    public Boolean isYearsOld18() {
        return yearsOld18;
    }
}
