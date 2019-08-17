package mas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP extends Thread {
	
	private BufferedReader in;
	private BufferedWriter out;
	private Socket socket = null;
	
	public ServerTCP(Socket socket) {
		this.socket = socket;
	}

	
	public void run() {
		try {
			// Initialize reader writer
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			// Default Fibonacci number = 0
		    String fibStr="0";
		    
		    // Read Fibonacci number from the soldier agent
	    	fibStr = in.readLine(); 
	    	int fibInt = Integer.parseInt(fibStr);
	    	
	        timeConsumingTask(fibInt);
	        
	        // Send response so that soldier can calculate the response time
//	        out.write("calculated "+fibStr+"\n");
	        out.flush();
			
	        // Clean up
	        out.close();
			in.close();
			socket.close();
		}
		catch(Exception e) {
			System.err.println("In run" + e.toString());
		}
		
	}
	
	public void timeConsumingTask(int n) {
		try {
			for (int i = 1; i <= n; i++) {
			    fibonacci(i);
			}
		}   
		catch(Exception e) {
			System.err.println(e.toString());
		}
		
	}
	
	 public static long fibonacci(int n) {
	        if (n <= 1) return n;
	        else return fibonacci(n-1) + fibonacci(n-2);
	    }

	
	 public static void main(String[] args) {
		
		   try {
			   ServerSocket serverSocket = new ServerSocket(9090);
			   Boolean stop = false;
			   while(!stop) {
				   System.out.println("Server waiting for connections...");
				   Socket client = serverSocket.accept(); //wait for a connection
				   System.out.println("Connection from " + client.getInetAddress()+"\n");
				   ServerTCP server = new ServerTCP(client);
				   server.start();
				} 
		   }
		   catch (IOException e) {
			    System.out.println("Address already in use");
				System.out.println(e.toString());
			} 
	}

}
