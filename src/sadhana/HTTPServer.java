package sadhana;

import java.io.*;
import java.net.*;
import java.util.*;

public class HTTPServer implements Runnable{
	Socket csocket;
	   HTTPServer(Socket csocket) {
	      this.csocket = csocket;
	   }
	public static void main(String[] args) {

		int port;
		Socket csock;
		System.out.println("Enter port number \n");
		Scanner input = new Scanner(System.in);
		port = Integer.parseInt(input.next());
		System.out.println("Waiting for clients\n");
			ServerSocket ssock;
			try {
				ssock = new ServerSocket(port);
				while(true){
					csock = ssock.accept();
					new Thread(new HTTPServer(csock)).start();
					}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}

		public void run(){
			Boolean check = true;
			try {
			BufferedReader incom = new BufferedReader(new InputStreamReader(csocket.getInputStream()));
			PrintStream sresp = new PrintStream(csocket.getOutputStream());
			while (check) {
				String request = incom.readLine();
				String s[] = request.split(" ");
				String iString = s[1];
				String file = iString.substring(iString.lastIndexOf("/") + 1);
				// GET Request
				if (request.equalsIgnoreCase("GET /" + file + " HTTP/1.1")) {
					System.out.println("Processing GET request");
					File filename = new File(file);
					if (!filename.exists()) {
						sresp.println("404 Not Found");
						check = false;
					} else {
						// send response to client
						byte[] array = new byte[(int) file.length()];
						BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
						bis.read(array, 0, array.length);
						OutputStream os = csocket.getOutputStream();

						os.write(array, 0, array.length);
						os.flush();
						check = false;
						System.out.println("Request Served");
						System.out.println("Waiting for new clients\n");

						sresp.println("200 OK");

					}
					// PUT Request
				} else if (request.equalsIgnoreCase("PUT /" + file + " HTTP/1.1")) {
					System.out.println("Processing PUT request");
					File putFileName = new File("local" + file);
					if (!putFileName.exists()) {
						sresp.println("200 OK File Created");
					}

					String fileContents = "";
					BufferedWriter bw = new BufferedWriter(new FileWriter(putFileName.getAbsoluteFile()));
					while ((fileContents = incom.readLine()) != null) {
						bw.write(fileContents);
						bw.newLine();
					}
					System.out.println("File created");
					System.out.println("Waiting for new clients\n");

					bw.close();
					check = false;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
}
	


