package request.processing;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.Protocol;
import protocol.ProtocolException;
import request.processing.processors.DeleteRequestProcessor;
import request.processing.processors.GetRequestProcessor;
import request.processing.processors.PostRequestProcessor;
import request.processing.processors.PutRequestProcessor;
import request.processing.processors.RequestProcessor;
import response.HttpResponseFactory;
import response.commands.ResponseCommand400;
import response.commands.ResponseCommand405;
import response.commands.ResponseCommand505;

public class RequestProcessorManager {
	
	public static void processRequest(HttpRequest request, OutputStream out) {
	    HttpResponse response = null; 
		
		// Fill in the code to create a response for version mismatch.
		// You may want to use constants such as Protocol.VERSION, Protocol.NOT_SUPPORTED_CODE, and more.
		// You can check if the version matches as follows
		if(!request.getVersion().equalsIgnoreCase(Protocol.VERSION)) {
			// Here you checked that the "Protocol.VERSION" string is not equal to the  
			// "request.version" string ignoring the case of the letters in both strings
		    response = HttpResponseFactory.createHttpResponseFromCommand(new ResponseCommand505(), null, Protocol.CLOSE);
		    try {
                response.write(out);
            } catch (Exception e) {
                e.printStackTrace();
            }
		    return;
		}
		
		String servletRoot = extractRootPath(request);
		Map<String, Map<String, Servlet>> servletMap = ServletLoader.getServletMap();
		Map<String, Servlet> methodMap = servletMap.get(servletRoot);
		if(methodMap == null) {
            if (request.getMethod().equalsIgnoreCase(Protocol.GET)) {
                RequestProcessor getProcessor = new GetRequestProcessor();
                response = getProcessor.process(request);
            } else if (request.getMethod().equalsIgnoreCase(Protocol.DELETE)) {
                RequestProcessor deleteProcessor = new DeleteRequestProcessor();
                response = deleteProcessor.process(request);
            } else if (request.getMethod().equalsIgnoreCase(Protocol.POST)) {
                RequestProcessor postProcessor = new PostRequestProcessor();
                response = postProcessor.process(request);
            } else if (request.getMethod().equalsIgnoreCase(Protocol.PUT)) {
                RequestProcessor putProcessor = new PutRequestProcessor();
                response = putProcessor.process(request);
            }    
            try {
                response.write(out);
            } catch (Exception e) {
                e.printStackTrace();
            }
		} else {
		    Servlet appropriateServlet = methodMap.get(request.getMethod());
		    if(appropriateServlet == null) {
		        response = HttpResponseFactory.createHttpResponseFromCommand(new ResponseCommand405(), null, Protocol.CLOSE);
		        try {
                    response.write(out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
		    } else {
		        appropriateServlet.serveHttpRequest(request, out);		        
		    }
		}
		
		
	}
	
	private static String extractRootPath(HttpRequest request) {
	    String uri = request.getUri();
	    List<String> rootPath = Arrays.asList(uri.split("/"));
	    //This is because the first thing will be the empty string because of the where the slashes are parsed
	    if(rootPath.size() < 2) {
	        return "";
	    }
        return rootPath.get(1);
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
