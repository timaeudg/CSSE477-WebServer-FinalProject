package request.processor;

import protocol.HttpRequest;
import protocol.HttpResponse;

public interface RequestProcessor {

	public HttpResponse process(HttpRequest request);
	
}
