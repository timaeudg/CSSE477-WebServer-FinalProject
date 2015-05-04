/*
 * ServletLoader.java
 * May 3, 2015
 *
 * Simple Web Server (SWS) for EE407/507 and CS455/555
 * 
 * Copyright (C) 2011 Chandan Raj Rupakheti, Clarkson University
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either 
 * version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 * 
 * Contact Us:
 * Chandan Raj Rupakheti (rupakhcr@clarkson.edu)
 * Department of Electrical and Computer Engineering
 * Clarkson University
 * Potsdam
 * NY 13699-5722
 * http://clarkson.edu/~rupakhcr
 */

package request.processing;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class ServletLoader implements Runnable {

    private static Map<String, Servlet> namesToServlets;
    private static Map<String, Map<String, Servlet>> servletMap;
    private static Path servletDir = Paths.get("./servlets/");
    private static Path servletConfig = Paths.get("./servlets/config.json");
    
    private static final ServletLoader instance = new ServletLoader();
    
    private ServletLoader() {
        
    }

    public static void loadServletsAndConfig() throws IOException, ParseException {
        servletMap = new TreeMap<String, Map<String, Servlet>>();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(servletConfig.toFile()));
        JSONObject jsonObject = (JSONObject) obj;

        JSONArray servletConfigs = (JSONArray) jsonObject.get("config");

        Map<String, Map<String, String>> intermediateMap = new TreeMap<String, Map<String, String>>();
        for (int i = 0; i < servletConfigs.size(); i++) {
            JSONObject servlet = (JSONObject) servletConfigs.get(i);
            String path = (String) servlet.get("path");
            String method = (String) servlet.get("method");
            String servletName = (String) servlet.get("servlet");
            System.out.println("path: " + path);
            System.out.println("method: " + method);
            System.out.println("servletName: " + servletName);

            Map<String, String> serveMap = new TreeMap<String, String>();
            serveMap.put(method, servletName);
            intermediateMap.put(path, serveMap);
        }

        List<File> files = Arrays.asList(servletDir.toFile().listFiles());
        namesToServlets = new TreeMap<String, Servlet>();
        for (File file : files) {
            if (file.getName().contains(".jar")) {
                loadServlet(file.toString());
            }
        }

        for (String path : intermediateMap.keySet()) {
            for (String method : intermediateMap.get(path).keySet()) {
                String servletName = intermediateMap.get(path).get(method);
                for (String otherName : namesToServlets.keySet()) {
                    if (servletName.equalsIgnoreCase(otherName)) {
                        if (!servletMap.containsKey(path)) {
                            TreeMap<String, Servlet> miniMap = new TreeMap<String, Servlet>();
                            miniMap.put(method, namesToServlets.get(servletName));
                            servletMap.put(path, miniMap);
                        } else {
                            servletMap.get(path).put(method, namesToServlets.get(servletName));
                        }
                    }
                }
            }
        }
    }

    public static void loadServlet(String servletDir) {
        try {
            JarFile jarFile = new JarFile(servletDir);
            Enumeration<JarEntry> e = jarFile.entries();

            URL[] urls = { new URL("jar:file:" + servletDir + "!/") };
            URLClassLoader cl = URLClassLoader.newInstance(urls);
            while (e.hasMoreElements()) {
                try {
                    JarEntry je = (JarEntry) e.nextElement();
                    if (je.isDirectory() || !je.getName().endsWith(".class")) {
                        continue;
                    }
                    String className = je.getName().substring(0, je.getName().length() - 6);
                    className = className.replace('/', '.');
                    Class c = cl.loadClass(className);
                    if (Servlet.class.isAssignableFrom(c)) {
                        Constructor<Servlet> constructor = c.getConstructor();
                        Servlet i = constructor.newInstance();
                        namesToServlets.put(c.getSimpleName(), i);
                    } else {
                        continue;
                    }

                } catch (ClassNotFoundException cnfe) {
                    System.err.println("Class not found");
                    cnfe.printStackTrace();
                } catch (NoSuchMethodException e1) {
                    System.err.println("No such method");
                    e1.printStackTrace();
                } catch (SecurityException e1) {
                    System.err.println("Security Exception");
                    e1.printStackTrace();
                } catch (InstantiationException e1) {
                    System.err.println("Instantiation Exception");
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    System.err.println("IllegalAccessException");
                    e1.printStackTrace();
                } catch (IllegalArgumentException e1) {
                    System.err.println("IllegalArgumentException");
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    System.err.println("InvocationTargetException");
                    e1.printStackTrace();
                }

            }
            jarFile.close();
        } catch (IOException e) {
            System.err.println("Not a jarFile, no biggie. Moving on.");
        }
    }

    /**
     * Watch dir monitors the current directory for any new files and calls
     * loadJar on them.
     */
    public static void watchDir() {

        WatchService watcher = null;
        try {
            watcher = FileSystems.getDefault().newWatchService();
            WatchKey key = servletDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
            servletDir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException x) {
            System.err.println(x);
        }

        while (true) {
            WatchKey key = null;
            try {
                key = watcher.take();

            } catch (InterruptedException x) {
                System.err.println("Interrupted Exception");
                x.printStackTrace();
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }

                try {
                    System.out.println("Directory Changed!");
                    Thread.sleep(1000);
                    loadServletsAndConfig();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        }

    }
    
    public static ServletLoader getInstance() {
        return ServletLoader.instance;
    }
    
    public static Map<String, Map<String, Servlet>> getServletMap() {
        return servletMap;
    }
    

    @Override
    public void run() {
        try {
            loadServletsAndConfig();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        watchDir();
    }

}
