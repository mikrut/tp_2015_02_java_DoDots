package resources;

/**
 * Created by mihanik
 * 23.04.15 10:59
 * Package: resources
 */
@SuppressWarnings("UnusedDeclaration")
public class ServerPathResource implements Resource {
    private String signinUrl;
    private String loginUrl;
    private String logoutUrl;
    private String userInfoUrl;
    private String adminInfoUrl;
    private String webSocketUrl;
    private String staticDir;

    public String getSigninUrl() {
        return signinUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }

    public String getAdminInfoUrl() {
        return adminInfoUrl;
    }

    public String getWebSocketUrl() {
        return webSocketUrl;
    }

    public String getStaticDir() {
        return staticDir;
    }
}
