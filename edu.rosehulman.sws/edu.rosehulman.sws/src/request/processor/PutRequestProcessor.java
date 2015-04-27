package request.processor;

import java.io.File;
import java.io.FileWriter;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.Protocol;
import response.HttpResponseFactory;
import response.ResponseCommand200;
import response.ResponseCommand500;

public class PutRequestProcessor extends PostRequestProcessor{

    /* (non-Javadoc)
     * @see request.processor.FileMayOrMayNotExist#fileExisted(protocol.HttpRequest)
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


}
