package request.processor;

import java.io.File;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.Protocol;
import server.WebServer;

public abstract class RequestProcessor {

	public abstract HttpResponse process(HttpRequest request);
	
	protected boolean checkIfFileExists(HttpRequest request) {
        // Get relative URI path from request
        String uri = request.getUri();
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
        String uri = request.getUri();
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
