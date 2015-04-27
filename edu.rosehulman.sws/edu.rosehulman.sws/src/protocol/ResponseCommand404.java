package protocol;

import java.io.File;
import java.util.HashMap;

/**
 * Creates a {@link HttpResponse} object for sending not found response.
 * 
 * @param connection Supported values are {@link Protocol#OPEN} and {@link Protocol#CLOSE}.
 * @return A {@link HttpResponse} object represent 404 status.
 */
public class ResponseCommand404 implements ResponseCommand {

	@Override
	public HttpResponse createResponse(File file, String connection) {
		HttpResponse response = new HttpResponse(Protocol.VERSION, Protocol.NOT_FOUND_CODE, 
				Protocol.NOT_FOUND_TEXT, new HashMap<String, String>(), null);
		
		// Lets fill up the header fields with more information
		response.fillGeneralHeader(connection);
		
		return response;
	}

}
