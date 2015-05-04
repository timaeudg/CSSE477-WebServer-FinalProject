/*
 * PostRequestProcessor.java
 * Apr 27, 2015
 *
 * Simple Web Server (SWS) for EE407/507 and CS455/555
 * 
 * Copyright (C) 2011 Chandan Raj Rupakheti, Clarkson University
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either 
 * version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 * 
 * Contact Us:
 * Chandan Raj Rupakheti (rupakhcr@clarkson.edu)
 * Department of Electrical and Computer Engineering
 * Clarkson University
 * Potsdam
 * NY 13699-5722
 * http://clarkson.edu/~rupakhcr
 */

package request.processing.processors;

import java.io.File;
import java.io.FileWriter;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.Protocol;
import response.HttpResponseFactory;
import response.commands.ResponseCommand200;
import response.commands.ResponseCommand500;
import server.WebServer;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class PostRequestProcessor extends FileMayOrMayNotExist {

    /*
     * (non-Javadoc)
     * 
     * @see
     * request.processor.FileMayOrMayNotExist#fileExisted(protocol.HttpRequest)
     */
    @Override
    public HttpResponse fileExisted(HttpRequest request, File file) {
        return fileDidNotExist(request);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * request.processor.FileMayOrMayNotExist#fileDidNotExist(protocol.HttpRequest
     * )
     */
    @Override
    public HttpResponse fileDidNotExist(HttpRequest request) {
        // Get relative URI path from request
        String uri = request.getUri();
        // Get root directory path from server
        String rootDirectory = WebServer.getServer().getRootDirectory();
        // Combine them together to form absolute file path
        File file = new File(rootDirectory + uri);

        char[] body = request.getBody();

        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(body);
            writer.close();
        } catch (Exception e) {
            // TODO: Make 500
            return HttpResponseFactory.createHttpResponseFromCommand(new ResponseCommand500(), null, Protocol.CLOSE);
        }

        return HttpResponseFactory.createHttpResponseFromCommand(new ResponseCommand200(), null, Protocol.CLOSE);
    }

}
