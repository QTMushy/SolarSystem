

import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int PORT = 4321;          // server details
	private static final String HOST = "localhost";

	private static final int MAX_PLAYERS = 2;
	private final static int PLAYER1 = 1;
	private final static int PLAYER2 = 2;

	//private RotatingCube rotCube;
	private Multiview runner;
	//private Runner runner2;
	private Socket sock;
	private PrintWriter out;
	                          // game-related
	private int playerID;
	private String status;    // used to place info into the 3D canvas
	private int numPlayers;
	private int currPlayer;       // which player can take a turn now?
	private boolean isDisabled; // to indicate that the game has ended

	public Client() {
		super("Spaceship game");

		playerID = 0;                                   // no id as yet
		status = null;                           // no status to report
		numPlayers = 0;
		currPlayer = 1;                        // player 1 starts first
		isDisabled = false;

		makeContact();

		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		runner = new Multiview(this, playerID);
		c.add(runner);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				disable("exiting");
				System.exit(0);
			}
		});

		pack();
		setResizable(false); // fixed size display
		setVisible(true);
	}

	/* a function to contact the FBFServer and then one of its player handlers */
	private void makeContact() {
		try {
			sock = new Socket(HOST, PORT);
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true); // autoflush

			new Watcher(this, in).start(); // start watching for server msgs
		} catch (Exception e) { // System.out.println(e);
			System.out.println("Cannot contact the NetFourByFour Server");
			System.exit(0);
		}
	}


	private int otherPlayer(int id)	{
		int otherID = ((id == PLAYER1) ? PLAYER2 : PLAYER1);
		return otherID;
	}

	public void gameWon(int pid) {
		if (pid == playerID) { // this client has won
			disable("You've won!");
			runner.youWon();
		}
		else
			disable("Player " + pid + " has won");
	}


	public void setStatus(String msg) {
		status = msg;
	}

	/* a function regularly called from OverlayCanvas, to update its display */
	public String getStatus()
	{
		return status;
	}

	public void addPlayer()	{
		numPlayers++;
		
	} 

	/* a function to remove the other player */

	public void disable(String msg) {
		if (!isDisabled) {                  // the client can only be disabled once
			try {
				isDisabled = true;
				out.println("disconnect"); // tell server
				sock.close();
				setStatus("Game Over: " + msg); 
				// System.out.println("Disabled: " + msg);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
	public void removePlayer() {
		numPlayers--;
		if (numPlayers < MAX_PLAYERS)
			disable("Player " + otherPlayer(playerID) + " has won"); // disable client
	}

	public void setPlayerID(int id) {
		System.out.println("My player ID: " + id);
		playerID = id;
		if (playerID == PLAYER1)
			setTitle("Spaceship game Player 1");
		else // PLAYER2
			setTitle("Spaceship game Player 2");

		numPlayers = id;
		
	} 
	public static void main(String[] args) {
		new Client();
	}
}
