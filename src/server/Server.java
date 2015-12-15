package server;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {
	static class ServerState {
		int numberOfPlayers = 0;
		boolean isGameStartedAlready = false;
	}

	public static void main(String[] args) throws Exception {
		ServerState state = new ServerState();

		HttpServer server = HttpServer.create(new InetSocketAddress(8002), 0);
		server.createContext("/I-wanna-play", new AddPlayer(state));
		server.createContext("/Is-already-started", new IsAlreadyStarted(state));
		server.createContext("/Lets-play", new NewGame(state));
		server.createContext("/How-many-players", new HowManyPlayers(state));
		server.setExecutor(null);
		server.start();
	}
	
	static abstract class SimpleHandler implements HttpHandler {
		ServerState state;

		public SimpleHandler(ServerState state) {
			this.state = state;
		}

		@Override
		public void handle(HttpExchange t) throws IOException {
			String response = this.response();
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            try {
            	os.write(response.getBytes());
            } finally {
                os.close();
            }
		}

		public abstract String response();
	}

	static class AddPlayer extends SimpleHandler {
		public AddPlayer(ServerState state) {
			super(state);
		}

		@Override
		public String response() {
			if (state.numberOfPlayers >= 10) {
				return "Sorry, there is no space for you\n";
			}
            state.numberOfPlayers++;
			return String.format("Ok, you are player %d\n", state.numberOfPlayers);
		}
	}

//	static class AddPlayer implements HttpHandler {
//		ServerState state;
//
//		public AddPlayer(ServerState state) {
//			this.state = state;
//		}
//		
//		@Override
//		public void handle(HttpExchange t) throws IOException {
//			if (state.numberOfPlayers < 10) {
//	            String response = String.format("Ok, you are player %d\n", state.numberOfPlayers);
//	            t.sendResponseHeaders(200, response.length());
//	            OutputStream os = t.getResponseBody();
//	            os.write(response.getBytes());
//	            os.close();
//	            state.numberOfPlayers++;
//			} else {
//				String response = "Sorry, there is no space for you\n";
//	            t.sendResponseHeaders(200, response.length());
//	            OutputStream os = t.getResponseBody();
//	            os.write(response.getBytes());
//	            os.close();
//			}
//		}
//	}

	static class NewGame extends SimpleHandler {

		public NewGame(ServerState state) {
			super(state);
		}

		@Override
		public String response() {
			if (state.numberOfPlayers > 1) {
				state.isGameStartedAlready = true;
				return "Hurray!\n";
			} else {
				return "Sorry, not enough players\n";
			}
		}
		
	}
//	static class NewGame implements HttpHandler {
//		ServerState state;
//		
//		public NewGame(ServerState state) {
//			this.state = state;
//		}
//
//		@Override
//		public void handle(HttpExchange t) throws IOException {
//			if (state.numberOfPlayers > 2) {
//	            state.isGameStartedAlready = true;
//	            String response = "Hurray!\n";
//	            t.sendResponseHeaders(200, response.length());
//	            OutputStream os = t.getResponseBody();
//	            os.write(response.getBytes());
//	            os.close();
//			} else {
//				String response = "Sorry, not enough players\n";
//	            t.sendResponseHeaders(200, response.length());
//	            OutputStream os = t.getResponseBody();
//	            os.write(response.getBytes());
//	            os.close();
//			}
//		}
//	}

	static class HowManyPlayers extends SimpleHandler {

		public HowManyPlayers(ServerState state) {
			super(state);
		}

		@Override
		public String response() {
			return String.format("Is %d players\n", state.numberOfPlayers);
		}
		
		
	}
	static class IsAlreadyStarted extends SimpleHandler {
		public IsAlreadyStarted(ServerState state) {
			super(state);
		}

		@Override
		public String response() {
			if (state.isGameStartedAlready) {
				return "Yes";
			} else {
				return "Not yet.";
			}
		}
	}
}

//	static class HowManyPlayers implements HttpHandler {
//		ServerState state;
//		
//		public HowManyPlayers(ServerState state) {
//			this.state = state;
//		}
//
//		@Override
//		public void handle(HttpExchange t) throws IOException {
//            String response = String.format("%d\n", state.numberOfPlayers);
//            t.sendResponseHeaders(200, response.length());
//            OutputStream os = t.getResponseBody();
//            os.write(response.getBytes());
//            os.close();
//		}
//	}
//}
