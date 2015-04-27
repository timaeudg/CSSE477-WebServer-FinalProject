package protocol;

import java.io.File;

public interface ResponseCommand {

	public HttpResponse createResponse(File file, String connection);
}
