package response;

import java.io.File;

import protocol.HttpResponse;

public interface ResponseCommand {

	public HttpResponse createResponse(File file, String connection);
}
