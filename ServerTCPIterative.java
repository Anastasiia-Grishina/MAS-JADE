package mas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ServerTCPIterative extends Thread {
	
	//Declare socket and writing variables
	private BufferedReader in;
	private BufferedWriter out;
	private Socket socket = null;
//	public static int socketCount = 0;
	
	public ServerTCPIterative(Socket socket) {
		// Initialize socket
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
	        out.write("calculated"+"\n");
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
		long f1 = 0;
		long f2 = 1;
		
		for (int i=0; i<n; i++) {
			long temp = f2;
			f2 = f1 + f2;
			f1 = temp;
		}
			/*
			for (int i = 1; i <= n; i++) {
			    fibonacci(i);
			}
			*/
		
	}
	
	 public static long fibonacci(int n) {
	        if (n <= 1) return n;
	        else return fibonacci(n-1) + fibonacci(n-2);
	    }

	
	 public static void main(String[] args) {
		 int port;

		 if (args.length != 1){
				System.out.println("Port not specified. Default port is 9090");
				port = 9090;
		 } else {
			 port = Integer.parseInt(args[0]);
		 }
			
 		 try {
			 ServerSocket serverSocket = new ServerSocket(port);
			 Boolean stop = false;
			   
			 while(!stop) {
				 System.out.println("Server waiting for connections...");
				 Socket client = serverSocket.accept(); //wait for a connection
//				 socketCount++;
				 System.out.println("Connection from " + client.getInetAddress()+"\n");
				 ServerTCPIterative server = new ServerTCPIterative(client);
				 server.start();
				 } 
		   }
		   catch (IOException e) {
			     System.out.println("Address already in use");
				 System.out.println(e.toString());
				 } 
	}
}

/*
Timer t = new Timer();
t.schedule(new TimerTask() {
    @Override
    public synchronized void run() {
       System.out.println("Socket counter: "+ socketCount);
       socketCount = 0;
    }
}, 0, 1000);
*/
