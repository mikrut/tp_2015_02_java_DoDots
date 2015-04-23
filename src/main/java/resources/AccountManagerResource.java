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
