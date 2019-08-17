package mas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Date;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class SoldierAgent extends Agent{
	/*
	 * Default network parameters - localhost:9090
	 */
	String host = "127.0.0.1";
	int port = 9090;
	
	/*
	 * Deafult system parameters
	 */
	int tickerInterval = 10000;
	int fibbInt = 0;
	String fibbStr = "0";
	
	Socket client = null;
	BufferedWriter out = null;
	BufferedReader in = null;
	
	/*
	 * Constructors
	 */
	public SoldierAgent() {
		super();
	}
	
	protected void setup() {
		System.out.println("Soldier agent "+getAID().getName()+" is ready.");
		
		/*
		 * Get arguments passed from the GUI
		 */
		Object[] args = getArguments();
		if (args !=null && args.length >= 0) {
			host = (String) args[0];
		    port = Integer.parseInt((String) args[1]);
		    tickerInterval = Integer.parseInt((String) args[2]);
		    fibbStr = (String) args[3];
		}
    	
				
		/*
		 * ReceiveMessage - cyclic behaviour to listen to a stop message
		 */
		addBehaviour(new ReceiveMessage());
		
		/*
		 * Attack - ticker behaviour
		 */
		addBehaviour(new Attack(this, tickerInterval));
	}
	
	 protected void takeDown() {

		    // Printout a dismissal message
		    System.out.println("Soldier-agent "+getAID().getName()+"is terminating.");
		    try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		  }
	
	
	 /*
	 * Receive stop message and delete
	 */
	public class ReceiveMessage extends CyclicBehaviour {

			private String Message_Content;
			private String SenderName;

			public void action() {
				ACLMessage msg = receive();
				if (msg != null) {

					String command = msg.getContent();
					
					if (command.contains("Stop")){
						doDelete();
					}
				} else {
					block();
				}
			}
		}
	
	public class Attack extends TickerBehaviour {
		
		public Attack (Agent a, int interval) {
			super(a, interval);
		}
        protected void onTick() {
        	try {
        		/*
        		 * Initialize socket parameters
        		 * The socket is connected to the server
        		 */
        		client = new Socket(host, port);
        		out = new BufferedWriter(
        				new OutputStreamWriter(
        						client.getOutputStream()));
        		in = new BufferedReader(
        				new InputStreamReader(
        						client.getInputStream()));
        		
        		
        		long startTime = System.currentTimeMillis();
        		long elapsedTime = 0L;

        		/*
        		 * Send Fibonacci number
        		 */
        		out.write(fibbStr+"\n");
        		out.flush();
        		out.close();
        		/*
        		 * Receive response from the server to check
        		 */
        		String message = in.readLine();
    			elapsedTime = System.currentTimeMillis() - startTime;
        	    System.out.println("server response time: "+ elapsedTime);
        	    System.out.println(message);
        	    
        	    /*
        		 * Clean up
        		 */
        	    in.close();
        	    
        	    client.close();
        	} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
}

/*
DFAgentDescription dfd = new DFAgentDescription();
ServiceDescription sd = new ServiceDescription();
sd.setType("Soldier");
sd.setName(getName());
dfd.setName(getAID());
dfd.addServices(sd);

try {
	DFService.register(this, dfd);
} catch (FIPAException e) {
	System.err.println(getLocalName()+ " registration with DF unsucceeded. Reason: "+ e.getMessage());
	doDelete();
}
*/

/*
// Deregister from the yellow pages
try {
  DFService.deregister(this);
}
catch (FIPAException fe) {
  fe.printStackTrace();
}
*/