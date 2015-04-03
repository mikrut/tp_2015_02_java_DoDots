package resources;

/**
 * Created by mihanik
 * 04.04.15 1:05
 * Package: resources
 */
public abstract class ResourceProvider  {
    private static ResourceProvider provider = null;

    public abstract  Object getResource(String resourceName);
    public static ResourceProvider getProvider() {
        if (provider == null)
            provider = new ModifiedProvider();
        return provider;
    }
}
