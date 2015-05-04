package request.processing.processors;

import java.io.File;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.Protocol;
import response.commands.ResponseCommand200;

public class GetRequestProcessor extends FileMustExistProcessor{

    /* (non-Javadoc)
     * @see request.processor.FileMustExistProcessor#processExistingFile()
     */
    @Override
    protected HttpResponse processExistingFile(HttpRequest request, File file) {
        HttpResponse toReturnResponse = null;
//      Map<String, String> header = request.getHeader();
//      String date = header.get("if-modified-since");
//      String hostName = header.get("host");
//      
        // Lets create 200 OK response
        toReturnResponse = new ResponseCommand200().createResponse(file, Protocol.CLOSE);
        return toReturnResponse;
    }

}
