package sadhana;

import java.io.*;
import java.net.*;
import java.util.*;


public class HTTPClient {

	public static void main(String[] args) {
		String sname;
		int port;
		String request;
		String file;
		System.out.println("Enter servername,port number,request type and filename in respective order\n");
		//Enter inputs 
		Scanner input = new Scanner(System.in);
		sname = input.next();
		port = Integer.parseInt(input.next());
		request = input.next();
		file = input.next();
		//Client Connection
		try {
			Socket hconn = new Socket(sname, port);
			PrintStream sendreq = new PrintStream(hconn.getOutputStream());
			BufferedReader resp = new BufferedReader(new InputStreamReader(hconn.getInputStream()));
			//GET Request
			if (request.equalsIgnoreCase("GET")) {
				sendreq.println("GET /" + file + " HTTP/1.1");
				sendreq.println("");
				sendreq.flush();
				String response;
				//Print response
				while ((response = resp.readLine()) != null) {
					System.out.println(response);
				}
				resp.close();
			}
			//PUT Request
			if (request.equalsIgnoreCase("PUT")) {
				sendreq.println("PUT /" + file + " HTTP/1.1");
				sendreq.println("");
				sendreq.flush();
                //Write file contents into new file
				File filename = new File(file);
				byte[] array = new byte[(int) filename.length()];
				FileInputStream fileio = new FileInputStream(filename);
				BufferedInputStream buffinp = new BufferedInputStream(fileio);
				buffinp.read(array, 0, array.length);
				OutputStream os = hconn.getOutputStream();
				System.out.println("Request type: PUT");
				os.write(array, 0, array.length);
				os.flush();
				String msg = resp.readLine();
				System.out.println(msg);
				resp.close();
				hconn.close();
			}
		
		} catch (UnknownHostException e) {
			System.out.println("Unkown host");
			e.printStackTrace();
		} catch (IOException e) {
			
		}
		
	}
}
