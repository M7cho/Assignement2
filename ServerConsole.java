import java.util.Scanner;

import common.ChatIF;

public class ServerConsole implements ChatIF {


	final public static int DEFAULT_PORT = 5555;

	private EchoServer server;

	Scanner fromConsole;

	public ServerConsole(int port) {
		server = new EchoServer(port);
		try
	    {
			server.listen(); //Start listening for connections
	    }
	    catch (Exception ex)
	    {
	      System.out.println("ERROR - Could not listen for clients!");
	    }

		fromConsole = new Scanner(System.in);
	}


	@Override
	public void display(String message) {
		// TODO Auto-generated method stub
	}

	public void accept()
	  {
	    try
	    {

	      String message;

	      while (true)
	      {
	        message = fromConsole.nextLine();
	        server.handleMessageFromServerUI(message);
	      }
	    }
	    catch (Exception ex)
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }


	public static void main(String[] args) {
		int port = 0; //Port to listen on

	    try
	    {
	      port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      port = DEFAULT_PORT; //Set port to 5555
	    }
	    ServerConsole serverConsole= new ServerConsole(port);
	    serverConsole.accept();  //Wait for console data
	}

}
