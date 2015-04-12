package resources;

/**
 * Created by mihanik
 * 07.04.15 8:47
 * Package: resources
 */

@SuppressWarnings("UnusedDeclaration")
public class GameProviderResource implements Resource {
    private String connectSuccessStatus;
    private String connectSuccessMessage;

    public String getConnectSuccessStatus() {
        return connectSuccessStatus;
    }

    public String getConnectSuccessMessage() {
        return connectSuccessMessage;
    }
}
