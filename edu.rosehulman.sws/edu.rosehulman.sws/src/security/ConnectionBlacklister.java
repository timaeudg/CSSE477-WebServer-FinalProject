/*
 * ConnectionBlacklister.java
 * May 11, 2015
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
 
package security;

import java.net.InetAddress;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class ConnectionBlacklister implements Runnable{

    private static Cache<InetAddress, Date> blacklist = 
            CacheBuilder.newBuilder()
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .concurrencyLevel(6)
            .maximumSize(500)
            .initialCapacity(100)
            .build();
    
    private static Cache<InetAddress, Integer> counters = 
            CacheBuilder.newBuilder()
            .concurrencyLevel(6)
            .maximumSize(10000)
            .initialCapacity(100)
            .build();
    
    private static Timer counterTimer = new Timer();
    
    public static void updateCounters(InetAddress address) {
        Integer value = counters.getIfPresent(address);
        if(value != null) {
            //it's in the cache, so we need to update it
            value++;
        } else {
            value = 1;
        }
        counters.put(address, value);
    }
    
    private static void updateBlacklist() {
        for(Map.Entry<InetAddress, Integer> entry : counters.asMap().entrySet()) {
            if(entry.getValue() > 2250) {
                System.out.println("Attack detected, blacklisting: " 
            + entry.getKey().toString());
                blacklist.put(entry.getKey(), 
                        new Date((new Date()).getTime() 
                                + TimeUnit.MINUTES.toMillis(20)));
            }
        }
        counters.invalidateAll();
    }
    
    public static boolean checkBlacklist(InetAddress address) {
        if(blacklist.getIfPresent(address) != null) {
            System.out.println("BLACKLISTED IP");
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        counterTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateBlacklist();
            }
            
        }, 100, 15000);
        while(true){
            
        }
    }
    
}
