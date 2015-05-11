/*
 * RequestCache.java
 * May 10, 2015
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
 
package request.processing;

import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import protocol.HttpRequest;
import protocol.HttpResponse;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class RequestCache {
    
    private static final Cache<HttpRequest, HttpResponse> requestCache = 
            CacheBuilder.newBuilder()
            .initialCapacity(100)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .maximumSize(500)
            .concurrencyLevel(4)
            .build();
            
    
    public static boolean checkCacheAndRespond(HttpRequest request, OutputStream out) {
        HttpResponse response = null;
        response = requestCache.getIfPresent(request);
        if(response != null) {
            try {
                response.write(out);
                return true;
            } catch (Exception e) {
                // Again, be sad, not much we could do here
                e.printStackTrace();
            }
        }
        return false;
    }
    
    public static void addToCache(HttpRequest request, HttpResponse response) {
        requestCache.put(request, response);
        requestCache.cleanUp();
    }

}
