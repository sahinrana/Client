import java.io.*;
import java.net.*;
public class Klient implements Runnable{
    //variables which are accessible from everywhere
    static Socket soket = null;
    static PrintStream os = null; //messages from server
    static DataInputStream is = null; //messages to server
    static BufferedReader input = null; //input from keyboard
    static boolean zatvoren = false;
    
    public void run() {		
		String odgovor;
                String status;
		try{ 
			//read whatever the server is sending
	    	while ((odgovor = is.readLine()) != null) {
	    		//print whatever the server is sending
	    		
	    		
	    		
	    		System.out.println(odgovor);
				//if server inserts GOODBYE, the connection closes
				if (odgovor.indexOf("*** GOODBYE") != -1){ 
					break;}
	    	}
                zatvoren=true;
                
                while ((status = is.readLine()) != null) {
	    		//print whatever the server is sending
	    		
	    		
	    		
	    		System.out.println(status);
				//if server inserts GOODBYE, the connection closes
				if (status.indexOf("*** GOODBYE") != -1){ 
					break;}
	    	}
	     	//flag for closing connection
            	zatvoren=false;
           	System.exit(0);
		} catch (IOException e) {
			System.err.println(e);
			}
    }
    
    public static void main(String[] args) {
	    try {
	    	soket = new Socket("localhost", 2222);
	        input = new BufferedReader(new InputStreamReader(System.in));
	        os = new PrintStream(soket.getOutputStream());
	        is = new DataInputStream(soket.getInputStream());
	       
	    } catch (UnknownHostException e) {
	        System.err.println("I don't recognize the server ");
	    } catch (IOException e) {
	        System.err.println("Connection to server can't be established ");
	    }
	   try {
	          //create new thread from client to server
	          new Thread(new Klient()).start();
	        
			while (!zatvoren) {
				//until the connection is open, send to server whatever is inputed from the keyboard
	                     	os.println((input.readLine())); 
	        }
                        while (zatvoren) {
				//until the connection is open, send to server whatever is inputed from the keyboard
	                     	os.println((input.readLine())); 
	        }
                        
			os.close();
			is.close();
			soket.close();   
	        } catch (IOException e) {
	             System.err.println("IO Error:  " + e);
	        }
	    
   	}           
   
  
}
