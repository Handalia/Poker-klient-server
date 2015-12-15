package klient;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Klient {
	public static String CallServer(String path) throws Exception {
        URL server = new URL("http://localhost:8002" + path);

        URLConnection sc = server.openConnection();
        BufferedReader in = new BufferedReader(
        		new InputStreamReader(sc.getInputStream())); // strumien danych

        try {
            return in.readLine();
        } finally {
        	in.close();
        }
	}

    public static void main(String[] args) throws Exception {
    	String iWannaPlayResponse = CallServer("/I-wanna-play");
    	if (iWannaPlayResponse.startsWith("Sorry")) {
    		System.out.printf("Server doesn't want to let us play: %s\nBailing out!", iWannaPlayResponse);
    		return;
    	}

    	Scanner reader = new Scanner(System.in);
    	try {
        	while(true) {
        		String isAlreadyStartedResponse = CallServer("/Is-already-started");
        		if (isAlreadyStartedResponse.startsWith("Yes")) {
        			break;
        		}

        		String howManyPlayersResponse = CallServer("/How-many-players");
        		System.out.printf("There is %s players right now. Type 'start' to begin.\n> ", howManyPlayersResponse);

        		String userInput = reader.nextLine();
        		if (userInput.startsWith("start")) {
        			String letsPlayResponse = CallServer("/Lets-play");
        			if (letsPlayResponse.startsWith("Sorry")) {
        				System.out.printf("Server doesn't let us start the game right now: %s\n", letsPlayResponse);
        			} else {
        				break;
        			}
        		}
        	}
        	
        	System.out.println("Game started.");
    	} finally {
    		reader.close();
    	}
    }
}
