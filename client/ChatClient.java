// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************

  /**
   * The interface type variable.  It allows the implementation of
   * the display method in the client.
   */
  ChatIF clientUI;

  String loginId = "";


  //Constructors ****************************************************

  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */

  public ChatClient(String host, int port, ChatIF clientUI, String loginId)
    throws IOException
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginId = loginId;
    openConnection();
  }


  //Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg)
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	if(message.startsWith("#quit")) {
    		quit();
    	} else if (message.startsWith("#logoff")) {
    		closeConnection();
    	} else if (message.startsWith("#sethost")) {
    		if(this.isConnected()) {
    			System.out.println("Cannot change the host if the connection is still alive close it using #logoff");
    		} else {
    			String host = message.replace("#sethost ", "");
    			this.setHost(host);
    		}
    	} else if (message.startsWith("#setport")) {
    		if(this.isConnected()) {
    			System.out.println("Cannot change the port if the connection is still alive close it using #logoff");
    		} else {
    			try {
        			int port = Integer.parseInt(message.replace("#setport ", ""));
        			this.setPort(port);
    			}catch (Throwable t) {
    				System.out.println("Invalid Port Number");
    			}
    		}
    	} else if (message.startsWith("#login")) {
    		if(this.isConnected()) {
    			System.out.println("Cannot Login when there is an already existing connection");
    		}else {
    			this.openConnection();
    		}
    	} else if (message.startsWith("#gethost")) {
    		System.out.println("Host: " + this.getHost());
    	} else if (message.startsWith("#getport")) {
    		System.out.println("Port: " + this.getPort());
    	} else {
    		if(this.isConnected()) {
        	    sendToServer(message);
    		}
    	}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }

  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }

  public void connectionClosed() {
	  System.out.println("Server connection closed");
  }

  public void connectionException() {
	  System.out.println("Server had an unknown exception");
  }

  public void connectionEstablished() {
	  try {
		  this.sendToServer("#login " + loginId);
	  } catch (Throwable t) {

	  }
  }
}
//End of ChatClient class
