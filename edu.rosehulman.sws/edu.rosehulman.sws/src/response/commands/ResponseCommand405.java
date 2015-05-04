package response.commands;

import java.io.File;
import java.util.HashMap;

import protocol.HttpResponse;
import protocol.Protocol;
import response.HttpResponseFactory;
import response.ResponseCommand;

/**
 * Creates a {@link HttpResponse} object for sending not found response.
 * 
 * @param connection Supported values are {@link Protocol#OPEN} and {@link Protocol#CLOSE}.
 * @return A {@link HttpResponse} object represent 404 status.
 */
public class ResponseCommand405 implements ResponseCommand {

	@Override
	public HttpResponse createResponse(File file, String connection) {
		HttpResponse response = new HttpResponse(Protocol.VERSION, Protocol.METHOD_NOT_ALLOWED_CODE, 
				Protocol.METHOD_NOT_ALLOWED_TEXT, new HashMap<String, String>(), null);
		
		// Lets fill up the header fields with more information
		HttpResponseFactory.fillGeneralHeader(response, connection);
		
		return response;
	}

}
