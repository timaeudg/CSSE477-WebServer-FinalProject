package response.commands;

import java.io.File;
import java.util.HashMap;

import protocol.HttpResponse;
import protocol.Protocol;
import response.HttpResponseFactory;
import response.ResponseCommand;

/**
 * Creates a {@link HttpResponse} object for sending bad request response.
 * 
 * @param connection Supported values are {@link Protocol#OPEN} and {@link Protocol#CLOSE}.
 * @return A {@link HttpResponse} object represent 400 status.
 */
public class ResponseCommand400 implements ResponseCommand {

	public HttpResponse createResponse(File file, String connection) {
		HttpResponse response = new HttpResponse(Protocol.VERSION, Protocol.BAD_REQUEST_CODE, 
				Protocol.BAD_REQUEST_TEXT, new HashMap<String, String>(), null, null);
		
		// Lets fill up header fields with more information
		HttpResponseFactory.fillGeneralHeader(response, connection);
		
		return response;
	}


}
