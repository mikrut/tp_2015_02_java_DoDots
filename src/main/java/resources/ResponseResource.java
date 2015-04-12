package resources;

/**
 * Created by mihanik
 * 07.04.15 8:58
 * Package: resources
 */
public class ResponseResource implements Resource {
    private String status;
    private String message;
    private String ok;
    private String error;

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public String getOk() {
        return ok;
    }

    public String getError() {
        return error;
    }
}
