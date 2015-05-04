package response.commands;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;

import protocol.HttpResponse;
import protocol.Protocol;
import response.HttpResponseFactory;
import response.ResponseCommand;

/**
 * Creates a {@link HttpResponse} object for sending file not modified response.
 * 
 * @param connection Supported values are {@link Protocol#OPEN} and {@link Protocol#CLOSE}.
 * @return A {@link HttpResponse} object represent 304 status.
 */
public class ResponseCommand304 implements ResponseCommand {

	@Override
	public HttpResponse createResponse(File file, String connection) {
		HttpResponse response = new HttpResponse(Protocol.VERSION, Protocol.NOT_MODIFIED_CODE, 
				Protocol.NOT_MODIFIED_TEXT, new HashMap<String, String>(), null);
		
		// Lets fill up header fields with more information
		HttpResponseFactory.fillGeneralHeader(response, connection);
		
		// Lets add last modified date for the file
		long timeSinceEpoch = file.lastModified();
		Date modifiedTime = new Date(timeSinceEpoch);
		response.put(Protocol.LAST_MODIFIED, modifiedTime.toString());
		
		return response;
	}


}
