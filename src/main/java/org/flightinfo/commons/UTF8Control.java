package org.flightinfo.commons;

import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;

public class UTF8Control extends ResourceBundle.Control {
    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
            throws IllegalAccessException, InstantiationException, IOException {
        // The below code is copied from default Control#newBundle() implementation.
        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, "properties");
        ResourceBundle bundle = null;
        InputStream stream = null;
        if (reload) {
            URL url = loader.getResource(resourceName);
            if (url != null) {
                URLConnection connection = url.openConnection();
                if (connection != null) {
                    connection.setUseCaches(false);
                    stream = connection.getInputStream();
                }
            }
        } else {
            stream = loader.getResourceAsStream(resourceName);
        }
        if (stream != null) {
            try (InputStreamReader reader = new InputStreamReader(stream, "UTF-8")) {
                bundle = new PropertyResourceBundle(reader);
            }
        }
        return bundle;
    }
}
