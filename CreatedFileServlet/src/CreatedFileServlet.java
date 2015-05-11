import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.Protocol;
import request.processing.Servlet;
import response.HttpResponseFactory;
import response.commands.ResponseCommand200;
import response.commands.ResponseCommand201;
import response.commands.ResponseCommand500;
import server.WebServer;


public class CreatedFileServlet extends Servlet {

    @Override
    public void serveHttpRequest(HttpRequest request, OutputStream output) {
     // Get relative URI path from request
        String uri = getServletURI(request);
        // Get root directory path from server
        String rootDirectory = WebServer.getServer().getRootDirectory();
        // Combine them together to form absolute file path
        File file = new File(rootDirectory + uri);

        char[] body = request.getBody();

        HttpResponse response = null;
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(body);
            writer.close();
        } catch (Exception e) {
            // TODO: Make 500
            response = HttpResponseFactory.createHttpResponseFromCommand(new ResponseCommand500(), null, Protocol.CLOSE);
        }

        response = HttpResponseFactory.createHttpResponseFromCommand(new ResponseCommand201(), null, Protocol.CLOSE);
        try {
            response.write(output);
        } catch (Exception e) {
            // TODO Auto-generated catch block
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

}
