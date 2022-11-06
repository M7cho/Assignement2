// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com


import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer
{
  //Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  private boolean serverClosed = false;
  private boolean serverStopped = false;

  //Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port)
  {
    super(port);
  }


  //Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  if(client != null) {
		  System.out.println("Message received: " + msg + " from " + client.getInfo("loginId"));
	  }

	  String message = (String) msg;
	  if(client.getInfo("loginId") == null) {
		  if(message.startsWith("#login")) {
			  String loginId = message.replace("#login ", "");
			  client.setInfo("loginId",loginId);
			  message = loginId + " has logged on.";
			  System.out.println(message);
		  } else {
			  String loginId = (String) client.getInfo("loginId");
			  message = loginId + "> " + message;
		  }
	  }else {
		  if(message.startsWith("#login")) {
			  try {
				  client.sendToClient("Error: Cannot login after initial login");
				  client.close();
			  }catch(Throwable t) {
			  }
		  } else {
			  String loginId = (String) client.getInfo("loginId");
			  message = loginId + "> " + message;
		  }
	  }
      this.sendToAllClients(message);
  }

  public void handleMessageFromServerUI(String message) {
	  if(message.startsWith("#quit")) {
		 System.exit(0);
	  } else if (message.startsWith("#stop")) {
		  this.stopListening();
		  this.serverStopped = true;
	  } else if (message.startsWith("#close")) {
		  try {
			  this.close();
			  this.serverClosed = true;
			  this.serverStopped = true;
		  }catch (Throwable t) {

		  }
	  } else if (message.startsWith("#setport")) {
		  if(this.serverClosed) {
			  try {
      			int port = Integer.parseInt(message.replace("#setport ", ""));
      			this.setPort(port);
  			}catch (Throwable t) {
  				System.out.println("Invalid Port Number");
  			}
		  }else {
			  System.out.println("The server is not closed close it using #close");
		  }
	  } else if (message.startsWith("#start")) {
		  if(this.serverStopped) {
			  try {
				  this.listen();
				  this.serverStopped = false;
				  this.serverClosed = false;
			  }catch (Exception ex)
			    {
			      System.out.println("ERROR - Could not listen for clients!");
			    }

		  }
	  } else if (message.startsWith("#getport")) {
		  System.out.println("Port: " + this.getPort());
	  } else {
		  this.handleMessageFromClient("SERVER MSG>"+message, null);
	  }
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("New Client Connected");
  }

  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println("A client has disconnected");
  }

  //Class methods ***************************************************

  /**
   * This method is responsible for the creation of
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555
   *          if no argument is entered.
   */
  public static void main(String[] args)
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }

    EchoServer sv = new EchoServer(port);

    try
    {
      sv.listen(); //Start listening for connections
    }
    catch (Exception ex)
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
