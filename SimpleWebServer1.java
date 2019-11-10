import java.io.* ;
import java.net.ServerSocket ;
import java.net.Socket ;
import java.util.StringTokenizer ;
import java.awt.Desktop;


public class SimpleWebServer1 {
    static Boolean existingFile(String fileNamee){
		String fileName = fileNamee.substring(1,fileNamee.length()-1);
        File file = new File(fileName);
        if (file.exists() && !file.isDirectory()){
            return true;
        }
        return false;
    }
	
	ServerSocket serverSocket ;
	
	public static void main(String args[]) {
		SimpleWebServer1 simpleWebServer = new SimpleWebServer1() ;
		simpleWebServer.start() ;
		//System.out.print("HHHH") ;
	}
	
	protected void start() {
		System.out.println("Wed server starting up on port 80") ;
		try {
			serverSocket = new ServerSocket(80) ;
		} catch (Exception e) {
			System.out.println("Error: " + e ) ;
		}
		
		System.out.println("Waiting for connection") ;
		while (true) {
			try{
				Socket socket = serverSocket.accept() ;
				System.out.println("Connect accept") ;
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())) ;
				PrintWriter out = new PrintWriter(socket.getOutputStream()) ;
				
				String httpRequest = in.readLine() ;
				//System.out.println(httpRequest) ;
				
				StringTokenizer stringTokenizer = new StringTokenizer(httpRequest) ;
				
				//String ttookkeenn = stringTokenizer.nextToken() ;
				
				String[] tokens = httpRequest.split(" ");
				System.out.println(tokens.length) ;
				//System.out.println(tokens[0]);
				//System.out.println(tokens[1]);
				//String newNameforDP = tokens[1].substring(1, tokens[1].length()-1); 
				//System.out.println(tokens[2]);
				
				String whatact = "000" ;
				if (tokens[0].equalsIgnoreCase("GET")) {
					whatact = "111" ;
					//System.out.println(whatact);
				} 
				else if (tokens[0].equalsIgnoreCase("HEAD")) {
					whatact = "222" ;
					//System.out.println(whatact);
				}
				else if (tokens[0].equalsIgnoreCase("PUT")) {
					whatact = "333" ;
					//System.out.println(whatact);
				}
				else if (tokens[0].equalsIgnoreCase("DELETE")) {
					whatact = "444" ;
					//System.out.println(whatact);
				}
				else { // BAD REQUEST
					whatact = "555" ;
					//System.out.println(whatact);
				}
				
				if (!tokens[2].equalsIgnoreCase("HTTP/1.1")) {
					out.println( tokens[2] + " 505 HTTP Version Not Supported") ;
					out.println("Content-Type: text/html") ;
					out.println("Server: LAB10527142") ;
					out.println("") ;
					
					out.println("<H1>505 HTTP Version Not Supported</H1>") ;
					out.flush() ;
					socket.close() ;
				} 
				else if ( tokens[1].equalsIgnoreCase("/isdeleted") ) { // only HTTP/1.1 can goin
					out.println("HTTP/1.1 301 Moved Permanently") ;
					out.println("Content-Type: text/html") ;
					out.println("Server: LAB10527142") ;
					out.println("") ;
					
					out.println("<H1>301 Moved Permanently</H1>") ;
					out.println("<a href=https://www.google.com.tw>google</a> ") ;				
					out.flush() ;
					socket.close() ;
				}	
				else if ( tokens[1].equalsIgnoreCase("/isdeleted1") ) { // only HTTP/1.1 can goin
					out.println("HTTP/1.1 301 Moved Permanently") ;
					out.println("Content-Type: text/html") ;
					out.println("Server: LAB10527142") ;
					out.println("") ;
					
					out.println("<H1>301 Moved Permanently</H1>") ;
					out.println("<H3>You will jump to GOOGLE after five seconds</H3>") ;
					out.println("<head><meta http-equiv=refresh content=5;url=https://www.google.com.tw> </head>") ;					
					out.flush() ;
					socket.close() ;
				}
				else if ( whatact.equalsIgnoreCase("111") ) {
					if ( tokens[1].equalsIgnoreCase("/") || existingFile(tokens[1] )) {
						out.println("HTTP/1.1 200 OK") ;
						out.println("Content-Type: text/html") ;
						out.println("Server: LAB10527142") ;
						out.println("") ; 

						out.println("<html>\n" +
									"<head>\n" +
									"<meta charset=utf-8>" +
									"    <title>\n" +
									"This is Head\n" + 
									"	 </title>\n" +
									"	 <meta name=description content = lab>\n" +
									"</head>\n" +
									"<body>\n" +
									"    <H1>Welcome to LAB10527142 Simple-server</H1>\n" +
									"</body>\n" +
									"</html>\n" ) ;
								
						out.flush() ;
						socket.close() ;
					}
					else{
						out.println("HTTP/1.1 404 Not Found ") ;
						out.println("Content-Type: text/html") ;
						out.println("Server: LAB10527142") ;
						out.println("") ;

						out.println("<H1>404 Not Found</H1>" ) ;
						out.flush() ;
						socket.close() ;
					}
				} 
				else if ( whatact.equalsIgnoreCase("222") ) {
					if ( tokens[1].equalsIgnoreCase("/") ||existingFile(tokens[1] )) {
						out.println("HTTP/1.1 200 OK") ;
						out.println("Content-Type: text/html") ;
						out.println("Server: LAB10527142") ;
						out.println("") ;

						out.println("<head>\n" +
									"    <title>This is Head</title>\n" +
									"</head>\n"  ) ;
								
						out.flush() ;
						socket.close() ;
						//System.out.println("**") ;
					}
					else { //404
						out.println("HTTP/1.1 404 Not Found ") ;
						out.println("Content-Type: text/html") ;
						out.println("Server: LAB10527142") ;
						out.println("") ;

						out.println("<H1>404 Not Found</H1>" ) ;
								
						out.flush() ;
						socket.close() ;					
					}
				}
				else if (whatact.equalsIgnoreCase("333")) {
					File f = null;
					f = new File(tokens[1].substring(1,tokens[1].length()-1)) ;
					f.createNewFile();
					//System.out.println("DONE") ;
					out.flush() ;
					socket.close() ;
				}
				else if (whatact.equalsIgnoreCase("444")) {
					if (existingFile(tokens[1])) {
						File f = null;
						f = new File(tokens[1].substring(1,tokens[1].length()-1)) ;
						f.delete();
						//System.out.println("DONE") ;
						out.flush() ;
						socket.close() ;
					}
					else{ // 404
						out.println("HTTP/1.1 404 Not Found ") ;
						out.println("Content-Type: text/html") ;
						out.println("Server: LAB10527142") ;
						out.println("") ;

						out.println("<H1>404 Not Found</H1>" ) ;
								
						out.flush() ;
						socket.close() ;	
					}
				}
				else{
					out.println("HTTP/1.1 400 BAD Request") ;
					out.println("Content-Type: text/html") ;
					out.println("Server: LAB10527142") ;
					out.println("") ;
					
					out.println("<H1>400 BAD Request</H1>") ;
					out.flush() ;
					socket.close() ;
				} // if
			} catch (Exception e) {
				System.out.println("Error111: " + e) ;
			} 
		}
	}
	
}