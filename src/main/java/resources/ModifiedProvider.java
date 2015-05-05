package resources;

import reflector.XMLConfigParser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mihanik
 * 04.04.15 1:08
 * Package: resources
 */
public class ModifiedProvider extends ResourceProvider {
    private final String dir = "./srv_tmpl";
    private final Map<String, Resource> resourceMap = new HashMap<>();
    private XMLConfigParser parser = null;

    public ModifiedProvider() {
        parser = new XMLConfigParser();
        initialise();
    }

    public Resource getResource(String resourceName) {
        if(resourceMap.containsKey(resourceName)) {
            return resourceMap.get(resourceName);
        }

        if(tryGetResource(resourceName)) {
            return resourceMap.get(resourceName);
        } else {
            return null;
        }
    }

    private Boolean tryGetResource(String resourceName) {
        File f = new File(dir+File.separator+resourceName);
        if(f.exists() && !f.isDirectory() && !resourceMap.containsKey(resourceName)) {
            Resource r = (Resource) parser.parseObject(dir+File.separator+resourceName);
            resourceMap.put(resourceName, r);
            return true;
        }
        return false;
    }

    private void initialise() {
        File folder = new File(dir);
        if(folder.exists() && folder.isDirectory()) {
            if(folder.listFiles() != null) {
                //noinspection ConstantConditions
                for (File file : folder.listFiles()) {
                    String filename = file.getAbsolutePath();
                    String extension = "";
                    int i = filename.lastIndexOf('.');
                    if (i > 0) {
                        extension = filename.substring(i + 1);
                    }
                    if (!file.isDirectory() && extension.equals("xml")) {
                        tryGetResource(file.getName());
                    }
                }
            }
        }
    }
}
