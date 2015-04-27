package request.processor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.Protocol;
import response.HttpResponseFactory;
import response.ResponseCommand200;
import response.ResponseCommand500;

public class DeleteRequestProcessor extends FileMustExistProcessor{

    /* (non-Javadoc)
     * @see request.processor.FileMustExistProcessor#processExistingFile(protocol.HttpRequest, java.io.File)
     */
    @Override
    protected HttpResponse processExistingFile(HttpRequest request, File file) {
        try {
            if(Files.deleteIfExists(file.toPath())){ 
                return HttpResponseFactory.createHttpResponseFromCommand(new ResponseCommand200(), null, Protocol.CLOSE);
            } else {
                return HttpResponseFactory.createHttpResponseFromCommand(new ResponseCommand500(), null, Protocol.CLOSE);
            }
        } catch (IOException e) {
            return HttpResponseFactory.createHttpResponseFromCommand(new ResponseCommand500(), null, Protocol.CLOSE);
        }
    }

}
