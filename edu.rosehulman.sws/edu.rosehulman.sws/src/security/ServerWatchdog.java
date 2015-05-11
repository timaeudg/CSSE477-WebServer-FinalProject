/*
 * ServerWatchdog.java
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

import java.util.Timer;
import java.util.TimerTask;

import server.Server;
import server.WebServer;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class ServerWatchdog implements Runnable{
    
    private Timer deathTimer;
    private Thread master;

    /**
     * @return the master
     */
    public Thread getMaster() {
        return master;
    }

    /**
     * @param master the master to set
     */
    public void setMaster(Thread master) {
        this.master = master;
    }

    @Override
    public void run() {
        System.out.println("GET DOWN MR PRESIDENT");
        deathTimer = new Timer();
        deathTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("THE PRESIDENT DIED, REVIVE HIM");
                WebServer.clearServer();
                WebServer.restartServer();
            }
        }, 7000, 7000);
        while(true){
            
        }
    }
    
    public void checkIn() {
        System.out.println("POTUS IS SAFE, ALL CLEAR");
        deathTimer.cancel();
        deathTimer = new Timer();
        deathTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("THE PRESIDENT DIED, REVIVE HIM");
                WebServer.clearServer();
                WebServer.restartServer();
            }
        }, 7000, 7000);
    }

}
