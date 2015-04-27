package response;

import java.io.File;

import protocol.HttpResponse;
import protocol.Protocol;

/**
 * Creates a {@link HttpResponse} object for sending version not supported response.
 * 
 * @param connection Supported values are {@link Protocol#OPEN} and {@link Protocol#CLOSE}.
 * @return A {@link HttpResponse} object represent 505 status.
 */
public class ResponseCommand505 implements ResponseCommand {

	@Override
	public HttpResponse createResponse(File file, String connection) {
		// TODO Auto-generated method stub
		return null;
	}

}
