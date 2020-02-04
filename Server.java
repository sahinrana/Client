import java.io.*;
import java.net.*;
class nizaklienti extends Thread{
    DataInputStream is = null;
    PrintStream os = null;
    Socket klient = null;       
    nizaklienti t[]; 
    //nizaklienti s[];
    String ime_klient;  
    String status_klient;
    int brojnaklienti=10;
    public nizaklienti(Socket soket, nizaklienti[] t){
	klient=soket;
        this.t=t;
        //this.s=s;
    }
    public void run() {
	String poraka, delodporaka="", covek="";
        String ime;
        String status;
        int indeks, prateno;
		try{
		    is = new DataInputStream(klient.getInputStream());
		    os = new PrintStream(klient.getOutputStream());
		    os.println("Insert your name.");
		    ime = is.readLine();
		    ime_klient=ime;
                    
                    os.println("Insert your status.");
                    status=is.readLine();
                    status_klient=status;
                    
		    os.println("Hello "+ime+".\nFor overview of people insert LIST and for the status of the guests insert LIST_STATUS\nTo send a message to someone insert the name, then #, and then the text\n");
		     os.println("For message to all insert ALL#\nFor end insert END.\n"); 
		    
		    int kolku=0;
		    for(int i=0; i<brojnaklienti; i++)
			if (t[i]!=null ) kolku++; 
		    for(int i=0; i<brojnaklienti; i++){
			if (t[i]!=null && t[i]!=this)  
			    t[i].os.println(ime+"is connected !!! There are " + kolku+" connected clients." );
	    		if (t[i]==this)  
			    t[i].os.println("There are " + kolku+" connected clients." );
	    	    }	
	    	    while (true) {
	    		poraka = is.readLine();
	    		System.out.println(ime+ " sent "+poraka);
                	if(poraka.startsWith("END")) 
                		break; 
                	else if(poraka==null ) 
                		break; 
                	else if(poraka.equals("LIST")) 
                	{
                		this.os.println("Guests in the room:");
                		for(int i=0; i<brojnaklienti; i++){
		    			if (t[i]!=null)  
		    				this.os.println(t[i].ime_klient);
                		}
                	}
                        else if(poraka.equals("LIST_STATUS")){
                            this.os.println("Status of the guests:");
                		for(int i=0; i<brojnaklienti; i++){
		    			if (t[i]!=null)  
		    				this.os.println("The status of the guest "+t[i].ime_klient+" is "+t[i].status_klient);
                		}
                        }
                        /*else if(poraka.equals("CHANGE_STATUS")){
                            
                            ime_klient=
                        }*/
                	else if(poraka.startsWith("ALL#")) 
                	{
    				delodporaka=poraka.substring(5); //removing the first 6 characters
    				for(int i=0; i<brojnaklienti; i++)
    		   			if (t[i]!=null && t[i]!=this)  
    		    				t[i].os.println("<"+ime+"> "+delodporaka);
    		       	}
    		    	else{ 
    				prateno=0;
    				indeks=poraka.indexOf('#');
    				if (indeks==-1){
    					//message is for all	
    					for(int i=0; i<brojnaklienti; i++){
        		    			if (t[i]!=null && t[i]!=this){
        		    				  t[i].os.println("<"+ime+"> "+poraka);
        		    				  prateno=1;
        		    			}
        		    		}
    				}
    				else{
    					//message is for someone
    					covek=poraka.substring(0,indeks);
    					delodporaka=poraka.substring(indeks+1);
    					for(int i=0; i<brojnaklienti; i++){
    						if (t[i]!=null && covek.equalsIgnoreCase(t[i].ime_klient)){
    		    				  t[i].os.println("<"+ime+"> "+delodporaka);
    		    				  prateno=1;
    						}
    					}
    				}
    				//If all sent =0, there isn't such a client
    				if (prateno==0)
    					this.os.println("Error in the name of the client, or still not enough connected clients.");
                		}
			}
			for(int i=0; i<brojnaklienti; i++)
				if (t[i]!=null && t[i]!=this)  
		    			t[i].os.println(ime+" is leaving!!!" );
	    			os.println("***Goodbye "+ime+" ***"); 
		    	for(int i=0; i<brojnaklienti; i++)
				if (t[i]==this) t[i]=null;  
			kolku=0;
		    	for(int i=0; i<brojnaklienti; i++)
				if (t[i]!=null ) kolku++; 	
			for(int i=0; i<brojnaklienti; i++)
				if (t[i]!=null && t[i]!=this)  
		    			t[i].os.println("There are "+ kolku+" connected clients." );
		   	 is.close();
	    		 os.close();
	    		klient.close();
		}catch(IOException e){};
    	}
}
public class Server{
    static  Socket klient = null;
    static  ServerSocket server = null;
    static  nizaklienti t[] = new nizaklienti[10];      
    static  nizaklienti s[] = new nizaklienti[10]; 
    public static void main(String args[]) {
	    int porta=2222;
	    try {
		    server = new ServerSocket(porta);
	    }
            catch (IOException e)
	    	{System.out.println(e);
	    }
	    while(true){
		    try {
			klient = server.accept();
			for(int i=0; i<=9; i++){
			    	if(t[i]==null)	{
			    		(t[i] = new nizaklienti(klient,t)).start();
			    		break;
				}
			}
	    	}catch (IOException e) {
			System.out.println();
		}
	}
    }
} 
