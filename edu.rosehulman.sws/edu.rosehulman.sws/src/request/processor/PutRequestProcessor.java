package request.processor;

import java.io.File;
import java.io.FileWriter;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.Protocol;
import response.HttpResponseFactory;
import response.ResponseCommand200;
import response.ResponseCommand500;
import server.WebServer;

public class PutRequestProcessor extends FileMayOrMayNotExist{
    /*
     * (non-Javadoc)
     * 
     * @see
     * request.processor.FileMayOrMayNotExist#fileExisted(protocol.HttpRequest)
     */
    @Override
    public HttpResponse fileExisted(HttpRequest request, File file) {
        char[] body = request.getBody();

        try {
            FileWriter writer = new FileWriter(file, true);
            writer.write(body);
            writer.close();
        } catch (Exception e) {
            return HttpResponseFactory.createHttpResponseFromCommand(new ResponseCommand500(), null, Protocol.CLOSE);
        }

        return HttpResponseFactory.createHttpResponseFromCommand(new ResponseCommand200(), null, Protocol.CLOSE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * request.processor.FileMayOrMayNotExist#fileDidNotExist(protocol.HttpRequest
     * )
     */
    @Override
    public HttpResponse fileDidNotExist(HttpRequest request) {
        // Get relative URI path from request
        String uri = request.getUri();
        // Get root directory path from server
        String rootDirectory = WebServer.getServer().getRootDirectory();
        // Combine them together to form absolute file path
        File file = new File(rootDirectory + uri);

        char[] body = request.getBody();

        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(body);
            writer.close();
        } catch (Exception e) {
            // TODO: Make 500
            return HttpResponseFactory.createHttpResponseFromCommand(new ResponseCommand500(), null, Protocol.CLOSE);
        }

        return HttpResponseFactory.createHttpResponseFromCommand(new ResponseCommand200(), null, Protocol.CLOSE);
    }

}
