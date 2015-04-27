package request.processor;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractMap;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.Protocol;
import protocol.ProtocolException;
import response.HttpResponseFactory;
import response.ResponseCommand400;
import response.ResponseCommand505;

public class RequestProcessorManager {
	
	public static HttpResponse processRequest(HttpRequest request) {
	    HttpResponse toReturnResponse = null; 
		
		// Fill in the code to create a response for version mismatch.
		// You may want to use constants such as Protocol.VERSION, Protocol.NOT_SUPPORTED_CODE, and more.
		// You can check if the version matches as follows
		if(!request.getVersion().equalsIgnoreCase(Protocol.VERSION)) {
			// Here you checked that the "Protocol.VERSION" string is not equal to the  
			// "request.version" string ignoring the case of the letters in both strings
		    toReturnResponse = HttpResponseFactory.createHttpResponseFromCommand(new ResponseCommand505(), null, Protocol.CLOSE);
		}
		else if(request.getMethod().equalsIgnoreCase(Protocol.GET)) {
		    RequestProcessor getProcessor = new GetRequestProcessor();
		    toReturnResponse = getProcessor.process(request);
		} else if(request.getMethod().equalsIgnoreCase(Protocol.DELETE)) {
		    RequestProcessor deleteProcessor = new DeleteRequestProcessor();
            toReturnResponse = deleteProcessor.process(request);
		} else if(request.getMethod().equalsIgnoreCase(Protocol.POST)) {
		    RequestProcessor postProcessor = new PostRequestProcessor();
		    toReturnResponse = postProcessor.process(request);
		} else if(request.getMethod().equalsIgnoreCase(Protocol.PUT)) {
		    RequestProcessor putProcessor = new PutRequestProcessor();
            toReturnResponse = putProcessor.process(request);
		}
		return toReturnResponse;
	}
	
	/**
	 * Typing here is just so we can have a pair with better methods that allows for different types
	 * @param request
	 * @param response
	 * @param inStream
	 * @param outStream
	 * @return
	 */
	public static AbstractMap.SimpleEntry<HttpRequest, HttpResponse> parseRequest(HttpRequest request, InputStream inStream, OutputStream outStream) {
		HttpRequest readRequest = null;
		HttpResponse errorResponse = null;
		try {
			readRequest = HttpRequest.read(inStream);
			System.out.println(readRequest);
		}
		catch(ProtocolException pe) {
			// We have some sort of protocol exception. Get its status code and create response
			// We know only two kind of exception is possible inside fromInputStream
			// Protocol.BAD_REQUEST_CODE and Protocol.NOT_SUPPORTED_CODE
			int status = pe.getStatus();
			if(status == Protocol.BAD_REQUEST_CODE) {
				errorResponse = HttpResponseFactory.createHttpResponseFromCommand(new ResponseCommand400(), null, Protocol.CLOSE);
			} else if (status == Protocol.NOT_SUPPORTED_CODE) {
				errorResponse = HttpResponseFactory.createHttpResponseFromCommand(new ResponseCommand505(), null, Protocol.CLOSE);
				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			// For any other error, we will create bad request response as well
			errorResponse = HttpResponseFactory.createHttpResponseFromCommand(new ResponseCommand400(), null, Protocol.CLOSE);
		}
		return new AbstractMap.SimpleEntry<HttpRequest, HttpResponse>(readRequest, errorResponse);
	}

}
