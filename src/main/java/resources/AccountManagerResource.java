package resources;

/**
 * Created by mihanik
 * 04.04.15 0:45
 * Package: resources
 */
@SuppressWarnings("UnusedDeclaration")
public class AccountManagerResource implements Resource {
    private String adminName;
    private String adminPassword;
    private String adminEmail;

    private String nullQueryAnswer;
    private String userAlreadyExists;
    private String incorrectPassword;
    private String userNotFound;

    private String authSuccess;

    private String registrationSuccess;

    private String usernameAPIName;
    private String emailAPIName;
    private String passwordAPIName;
    private String loggedInAPIName;
    private String scoreAPIName;
    private String resultsAPIName;
    private String isAdminName;

    private String guestName;
    private String guestEmail;

    private String user1APIName;
    private String user2APIName;

    private String user1ScoreAPIName;
    private String user2ScoreAPIName;

    public String getLoggedInAPIName() {
        return loggedInAPIName;
    }

    public String getScoreAPIName() {
        return scoreAPIName;
    }

    public String getResultsAPIName() {
        return resultsAPIName;
    }

    public String getIsAdminName() {
        return isAdminName;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public String getUser1APIName() {
        return user1APIName;
    }

    public String getUser2APIName() {
        return user2APIName;
    }

    public String getUser1ScoreAPIName() {
        return user1ScoreAPIName;
    }

    public String getUser2ScoreAPIName() {
        return user2ScoreAPIName;
    }

    public AccountManagerResource() {
    }

    public String getPasswordAPIName() {
        return passwordAPIName;
    }

    public String getUsernameAPIName() {
        return usernameAPIName;
    }

    public String getEmailAPIName() {
        return emailAPIName;
    }

    public String getAuthSuccess() {
        return authSuccess;
    }

    public String getRegistrationSuccess() {
        return registrationSuccess;
    }

    public String getAdminName() {
        return adminName;
    }


    public String getAdminPassword() {
        return adminPassword;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public String getNullQueryAnswer() {
        return nullQueryAnswer;
    }

    public String getUserAlreadyExists() {
        return userAlreadyExists;
    }

    public String getIncorrectPassword() {
        return incorrectPassword;
    }

    public String getUserNotFound() {
        return userNotFound;
    }

}
