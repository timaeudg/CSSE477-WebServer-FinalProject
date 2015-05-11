/*
 * WebServer.java
 * Oct 7, 2012
 *
 * Simple Web Server (SWS) for CSSE 477
 * 
 * Copyright (C) 2012 Chandan Raj Rupakheti
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
 */

package server;

import gui.WebServerGui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.*;

import request.processing.ServletLoader;
import security.ConnectionBlacklister;
import server.Server;

/**
 * The application window for the {@link Server}, where you can update
 * some parameters and start and stop the server.
 * 
 * @author Chandan R. Rupakheti (rupakhet@rose-hulman.edu)
 */
public class WebServer extends JFrame {
	private static final long serialVersionUID = 5042579745743827174L;
	
	private static Server server;
	private static ConnectionBlacklister blacklist = new ConnectionBlacklister();
	private static final ServletLoader servletLoader = ServletLoader.getInstance();

	/**
	 * @return the server
	 */
	public static Server getServer() {
		return server;
	}

	/**
	 * The application start point.
	 * 
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
	    try {
	        Thread t = new Thread(servletLoader, "ServletLoader");
	        t.start();
	        Thread t2 = new Thread(blacklist, "blacklist");
	        t2.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new WebServerGui().setVisible(true);
			}
		});
	}
	
	public static void createServer(String rootDirectory, int port, WebServerGui gui) {
		server = new Server(rootDirectory, port, gui);
		// Now run the server in a separate thread
		new Thread(server).start();
	}
	
	public static void clearServer(){
		server.stop();
		server = null;
	}
}
