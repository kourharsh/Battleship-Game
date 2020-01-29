import java.util.*;


import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

/**
 * Class to send and receive UDP messages for Human Play.
 * @author harshkour
 * @since 01-08-2019
 * @version 1.0.0
 */
public class Udp {
	
	static String Received = null;
	public static Arena arena;
	public static Player human;
	
	/**
	 * Function to start the server
	 * @param port Port at which the server will start
	 * @throws SecurityException Data Leakage
	 * @throws IOException throws exception when there is wrong input
	 */
	public static void startServer(int port) throws SecurityException, IOException {
		try {
			Runnable task = () -> {
				receive(port);
			};
			Thread thread = new Thread(task);
			thread.start();
		}catch(Exception e) {
			System.out.println("Exception in start server: " + e);
		}
	}
	
	/**
	 * Receive message for the server on the port
	 * @param port the port number on which message is received
	 */
	 private synchronized static void receive(int port) {
		 DatagramSocket  aSocket = null;
		 try {
			 	String	rep = null;	
			 	boolean flag1 = false;
				aSocket = new DatagramSocket (port);
				System.out.println("Server " + port + " Started............");
				while (true) {
					
					byte[] buffer = new byte[10000];
					DatagramPacket request = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(request);
					Received = new String(request.getData());
					
					System.out.println("Output received is : " + Received);
					
					rep = arena.postHit(Received);
					System.out.println("rep is--" + rep);
					if(rep.equals("P")) {
						
					}else {
						buffer = rep.getBytes();
						DatagramPacket reply = new DatagramPacket(buffer, buffer.length, request.getAddress(),
							request.getPort());
						aSocket.send(reply);
							
						}
				}//P
			} catch (SocketException e) {
				System.out.println("Socket: " + e.getMessage());
			} catch (IOException e) {
				System.out.println("IO: " + e.getMessage());
			} finally {
				if (aSocket != null)
					aSocket.close();
			} 
	 	}
	 	
	 	/**
	 	 * Function to send message over network
	 	 * @param serverPort the port on which message needs to be sent
	 	 * @param Sem The message to be sent 
	 	 */
	   public static void sendMessage(int serverPort, String Sem) {
			DatagramSocket aSocket = null;
			try {
				aSocket = new DatagramSocket();
				byte[] message = Sem.getBytes();
				InetAddress aHost = InetAddress.getByName("132.205.94.104");
				System.out.println("sem length is:" + Sem.length());
				DatagramPacket request = new DatagramPacket(message, Sem.length(), aHost, serverPort);
				aSocket.send(request);
				System.out.println("Request message sent from the client to server with port number " + serverPort + " is: "
						+ new String(request.getData()));
				byte[] buffer = new byte[1000];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(reply);
				Received = null;
				Received = new String(reply.getData()).trim();
					System.out.println("Reply received from the server with port number " + serverPort + " to COMP server is: "
								+ Received);
					String rep = arena.postHit(Received);
					
			} catch (SocketException e) {
				System.out.println("Socket: " + e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("IO: " + e.getMessage());
			} finally {
				if (aSocket != null)
					aSocket.close();
			}
		}	
}
