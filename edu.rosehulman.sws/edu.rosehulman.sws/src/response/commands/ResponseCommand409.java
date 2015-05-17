/*
 * ResponseCommand409.java
 * May 17, 2015
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
 
package response.commands;

import java.io.File;
import java.util.HashMap;

import protocol.HttpResponse;
import protocol.Protocol;
import response.HttpResponseFactory;
import response.ResponseCommand;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class ResponseCommand409 implements ResponseCommand {

    @Override
    public HttpResponse createResponse(File file, String connection) {
        HttpResponse response = new HttpResponse(Protocol.VERSION, Protocol.CONFLICT_CODE, 
                Protocol.CONFLICT_TEXT, new HashMap<String, String>(), null, null);
        
        // Lets fill up the header fields with more information
        HttpResponseFactory.fillGeneralHeader(response, connection);
        
        return response;
    }

}
