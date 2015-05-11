import java.io.File;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.Protocol;
import request.processing.Servlet;
import response.HttpResponseFactory;
import response.commands.ResponseCommand200;
import response.commands.ResponseCommand404;
import server.WebServer;


public class ExampleServlet extends Servlet{

    @Override
    public void serveHttpRequest(HttpRequest request, OutputStream output) {
        HttpResponse response = null;
        if(checkIfFileExists(request)) {
            File file = getFilePath(request);
            
//          Map<String, String> header = request.getHeader();
//          String date = header.get("if-modified-since");
//          String hostName = header.get("host");
//          
            // Lets create 200 OK response
            response = new ResponseCommand200().createResponse(file, Protocol.CLOSE);
        } else {
             response = HttpResponseFactory.createHttpResponseFromCommand(new ResponseCommand404(), null, Protocol.CLOSE);
        }
        try {
            response.write(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected String getServletURI(HttpRequest request) {
        String uri = request.getUri();
        List<String> uriList = Arrays.asList(uri.split("/"));
        List<String> choppedURI = uriList.subList(2, uriList.size());
        
        uri = "";
        for(String part : choppedURI) {
            uri = uri + "/" + part;
        }
        return uri;
    }
    
    protected boolean checkIfFileExists(HttpRequest request) {
        // Get relative URI path from request
        String uri = getServletURI(request);
        // Get root directory path from server
        String rootDirectory = WebServer.getServer().getRootDirectory();
        // Combine them together to form absolute file path
        File file = new File(rootDirectory + uri);
        // Check if the file exists
        if(file.exists()) {
            if(file.isDirectory()) {
                // Look for default index.html file in a directory
                String location = rootDirectory + uri + System.getProperty("file.separator") + Protocol.DEFAULT_FILE;
                file = new File(location);
                if(file.exists()) {
                    return true;
                }
                else {
                    // File does not exist
                    return false;
                }
            }
            else { // Its a file
                return true;
            }
        }
        else {
            // File does not exist
            return false;
        }
    };
    
    protected File getFilePath(HttpRequest request) {
        // Handling GET request here
        // Get relative URI path from request
        String uri = getServletURI(request);
        
        // Get root directory path from server
        String rootDirectory = WebServer.getServer().getRootDirectory();
        // Combine them together to form absolute file path
        File file = new File(rootDirectory + uri);
        // Check if the file exists
        if(file.exists()) {
            if(file.isDirectory()) {
                // Look for default index.html file in a directory
                String location = rootDirectory + uri + System.getProperty("file.separator") + Protocol.DEFAULT_FILE;
                file = new File(location);
                if(file.exists()) {
                    return file;
                }
                else {
                    // File does not exist
                    return null;
                }
            }
            else { // Its a file
                return file;
            }
        }
        else {
            // File does not exist
            return null;
        }
    };

}
