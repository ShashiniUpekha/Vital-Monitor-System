// Java program to illustrate Server side
// Implementation using DatagramSocket
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.*;
import java.io.*;

public class gateway
{
	public static void main(String[] args) throws IOException,ClassNotFoundException
	{
		// Step 1 : Create a socket to listen at port 6000
		DatagramSocket ds = new DatagramSocket(6000);
		byte[] receive = new byte[65535];
		Monitor monitor  = null;
		 Socket socket            = null;
   		DataInputStream  input   = null;
     DataOutputStream out     = null;
 

		DatagramPacket DpReceive = null;
		while (true)
		{

			// create a DatgramPacket to receive the data.
			
			DpReceive = new DatagramPacket(receive, receive.length);
  
            //  revieve the data in byte buffer.
            ds.receive(DpReceive);
			
			 
			
			Monitor mon = ByteArrayToMonitor(DpReceive.getData());

			//To get the Port
			int Port =   mon.getPort();
			
			//To get the Ip
			InetAddress ip_add=mon.getIp();

			System.out.println("Vital monitor discoverd..."+"monitor ip  : "+ip_add +"   monitor Port : "+ Port +"   monitor_id : "+mon.getMonitorID());


						

			//Establish TCP Connection 


			socket = new Socket(ip_add, Port);
            System.out.println("TCP Connection Established..");

			//Handing vital monitors 
			//Multitrading

			MonitorHandler monitorHandler = new MonitorHandler(mon); // create a new thread for each vital
                                                                              // monitor
            monitorHandler.start(); // start the thread

            System.out.println(mon.getMonitorID() + " is Connected..."); // monitor detecting


			// Clear the buffer after every message.
			//receive = new byte[65535];



	
	}
	}
/*
	// A utility method to convert the byte array
	// data into a string representation.
	public static StringBuilder data(byte[] a)
	{
		if (a == null)
			return null;
		StringBuilder ret = new StringBuilder();
		int i = 0;
		while (a[i] != 0)
		{
			ret.append((char) a[i]);
			i++;
		}
		return ret;
	}
*/

	//To convert Bytearray to monitor object 
	private static Monitor ByteArrayToMonitor(byte[] data ) throws IOException ,ClassNotFoundException{
       		Monitor monitor  = null;
       
        	ByteArrayInputStream in = new ByteArrayInputStream(data);
    		ObjectInputStream is = new ObjectInputStream(in);
    		monitor=(Monitor) is.readObject();
	       
   			return monitor;
	


}
/*
 	Socket socket            = null;
    DataInputStream  input   = null;
     DataOutputStream out     = null;

public static Client(String address, int port)
    {
        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");
 
            // takes input from terminal
            input  = new DataInputStream(System.in);
 
            // sends output to the socket
            out    = new DataOutputStream(socket.getOutputStream());
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
 




	}
	*/


}	

//Multitreading 
//To handle vital Monitors 

	class MonitorHandler extends Thread { 

    private final Monitor monitor;

    public static Socket socket; 

    public MonitorHandler(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {

        try {

			//Socket creaetion
            socket = new Socket(monitor.getIp(), monitor.getPort()); 
            System.out.println("monitor = " + monitor.getMonitorID());


			

            //read from vital monitor
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                System.out.println(in.readLine()); 
            }

        } catch (IOException e) {

             e.printStackTrace();
            
        }

    }


}




