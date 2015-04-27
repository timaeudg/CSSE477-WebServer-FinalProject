package protocol;

import java.io.File;

/**
 * Creates a {@link HttpResponse} object for sending file not modified response.
 * 
 * @param connection Supported values are {@link Protocol#OPEN} and {@link Protocol#CLOSE}.
 * @return A {@link HttpResponse} object represent 304 status.
 */
public class ResponseCommand304 implements ResponseCommand {

	@Override
	public HttpResponse createResponse(File file, String connection) {
		// TODO Auto-generated method stub
		return null;
	}


}
