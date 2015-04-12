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

    public AccountManagerResource() {

    }


    public String getAuthSuccess() {
        return authSuccess;
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
