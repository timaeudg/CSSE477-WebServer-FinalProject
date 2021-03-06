package response.commands;

import java.io.File;
import java.util.HashMap;

import protocol.HttpResponse;
import protocol.Protocol;
import response.HttpResponseFactory;
import response.ResponseCommand;

/**
 * Creates a {@link HttpResponse} object for sending version not supported response.
 * 
 * @param connection Supported values are {@link Protocol#OPEN} and {@link Protocol#CLOSE}.
 * @return A {@link HttpResponse} object represent 505 status.
 */
public class ResponseCommand505 implements ResponseCommand {

	@Override
	public HttpResponse createResponse(File file, String connection) {
		HttpResponse response = new HttpResponse(Protocol.VERSION, Protocol.NOT_SUPPORTED_CODE, 
				Protocol.NOT_SUPPORTED_TEXT, new HashMap<String, String>(), null, null);
		
		// Lets fill up the header fields with more information
		HttpResponseFactory.fillGeneralHeader(response, connection);
		
		return response;
	}

}
