
import java.net.*;
import java.io.*;

public class PlayerServerHandler extends Thread {
	private Server server;
	private Socket clientSock;
	private BufferedReader in;
	private PrintWriter out;

	private int playerID; // this player id is assigned by FBFServer

	public PlayerServerHandler(Socket s, Server serv) {
		clientSock = s;
		server = serv;
		System.out.println("Player connection request");
		try {
			in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
			out = new PrintWriter(clientSock.getOutputStream(), true);     // autoflush
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void run() {
		playerID = server.addPlayer(this);
		if (playerID != -1) {                 // -1 means adding the player was rejected
			sendMessage("ok " + playerID);
			System.out.println("ok " + playerID);         // tell player his/her playerID
			server.tellOther(playerID, "added " + playerID);

			processPlayerInput();

			server.removePlayer(playerID);               // remove player from server data
			server.tellOther(playerID, "removed " + playerID);              // tell others
		} else 
			sendMessage("full");                                           // game is full

		try {                                                  // close socket from player
			clientSock.close();
			System.out.println("Player " + playerID + " connection closed\n");
		} catch (Exception e) {
			System.out.println(e);
		}
	} 

	private void processPlayerInput() {
		String line;
		boolean done = false;
		try {
			while (!done) {
				if ((line = in.readLine()) == null)
					done = true;
				else {
					if (line.trim().equals("disconnect"))
						done = true;
					else
						doRequest(line);
				}
			}
		} catch (IOException e) {
			System.out.println("Player " + playerID + " closed the connection");
		}
	}

	private void doRequest(String line) {
		if (line.startsWith("try")) {
			try {
				int posn = Integer.parseInt(line.substring(4).trim());
				// System.out.println("Player " + playerID + " wants to occupy position " +
				// posn);

				if (server.enoughPlayers())
					server.tellOther(playerID, "otherTurn " + playerID + " " + posn); // pass turn to others
				else
					sendMessage("tooFewPlayers");
			} catch (NumberFormatException e) {
				System.out.println(e);
			}
		}
	}

	/* called by handler and top-level server */
	synchronized public void sendMessage(String msg) {
		try {
			out.println(msg);
		} catch (Exception e) {
			System.out.println("Handler for player " + playerID + "\n" + e);
		}
	} 
} 