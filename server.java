import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Toolkit;
import java.awt.Rectangle;
import javax.imageio.ImageIO;
 
public class server {
	
	private static ServerSocket server = null;
	private static Socket client = null;
	private static BufferedReader in = null;
	private static String line;
	private static boolean isConnected=true;
	private static Robot robot;
	private static final int SERVER_PORT = 4444;
 
	public static void main(String[] args) {
		boolean leftpressed=false;
		boolean rightpressed=false;
	    try{
	    	robot = new Robot();
			server = new ServerSocket(SERVER_PORT); //Create a server socket on port 8998
			System.out.println("connected");
			client = server.accept(); //Listens for a connection to be made to this socket and accepts it
			in = new BufferedReader(new InputStreamReader(client.getInputStream())); //the input stream where data will come from client
		}catch (IOException e) {
			System.out.println("Error in opening Socket "+e.toString());
			isConnected=false;
			System.exit(-1);
		}catch (AWTException e) {
			System.out.println("Error in creating robot instance");
			System.exit(-1);
		}
			System.out.println("connected "+isConnected);
		//read input from client while it  is connected
	    while(isConnected){      
	        try{
			line = in.readLine(); //read input from client
			System.out.println(line); //print whatever we get from client
			
			//if user clicks on next
			if(line.equalsIgnoreCase("next")){
				//Simulate press and release of key 'n'
				robot.keyPress(KeyEvent.VK_N);
				robot.keyRelease(KeyEvent.VK_N);
			}
			//if user clicks on previous
			else if(line.equalsIgnoreCase("previous")){
				//Simulate press and release of key 'p'
				robot.keyPress(KeyEvent.VK_P);
				robot.keyRelease(KeyEvent.VK_P);		        	
			}
			else if(line.equalsIgnoreCase("start")){
				//simulating ctrl+alt+g
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyPress(KeyEvent.VK_G);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.keyRelease(KeyEvent.VK_ALT);
				robot.keyRelease(KeyEvent.VK_G);
			}
			else if(line.equalsIgnoreCase("snap")){
				//simulating ctrl+alt+g
				// robot.keyPress(KeyEvent.VK_CONTROL);
				// robot.keyPress(KeyEvent.VK_ALT);
				// robot.keyPress(KeyEvent.VK_S);
				// robot.keyRelease(KeyEvent.VK_CONTROL);
				// robot.keyRelease(KeyEvent.VK_ALT);
				// robot.keyRelease(KeyEvent.VK_S);
				BufferedImage screenFullImage = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
    			ImageIO.write(screenFullImage, "png", new File("C:\\Users\\PRANJUL\\Desktop\\RamSirFYP\\DTW_APPROACH\\capture.png"));
			}
			//if user clicks on previous
			else if(line.equalsIgnoreCase("clear")){
				//Simulate press and release of key 'p'
				robot.keyPress(KeyEvent.VK_M);
				robot.keyRelease(KeyEvent.VK_M);	        	
			}
			else if(line.equalsIgnoreCase("left_down")){
				robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			}
			else if(line.equalsIgnoreCase("left_up")){
				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			}
			else if(line.contains("key-")){
				int unicode=Integer.parseInt(line.split("-")[1]);
				System.out.println("unicode is "+unicode);
				int keyCode=KeyEvent.getExtendedKeyCodeForChar(unicode);
				System.out.println("keycode is "+keyCode);
				try{
				
				if(unicode>=65 && unicode<=90){
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyPress(keyCode);
					robot.keyRelease(keyCode);
					robot.keyRelease(KeyEvent.VK_SHIFT);
				}
				else{
					robot.keyPress(keyCode);
					robot.keyRelease(keyCode);
				}
				}catch(Exception ex){
					try{
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyPress(keyCode);
					robot.keyRelease(keyCode);
					robot.keyRelease(KeyEvent.VK_SHIFT);
					}catch(Exception e){
						System.out.println(e.toString());
					}
				}
			}
			//if user clicks on play/pause
			else if(line.equalsIgnoreCase("play")){
				//Simulate press and release of spacebar
				robot.keyPress(KeyEvent.VK_SPACE);
				robot.keyRelease(KeyEvent.VK_SPACE);
			}
			//input will come in x,y format if user moves mouse on mousepad
			else if(line.contains(",")){
				float movex=Float.parseFloat(line.split(",")[0]);//extract movement in x direction
				float movey=Float.parseFloat(line.split(",")[1]);//extract movement in y direction
				Point point = MouseInfo.getPointerInfo().getLocation(); //Get current mouse position
				float nowx=point.x;
				float nowy=point.y;
				robot.mouseMove((int)(nowx+movex),(int)(nowy+movey));//Move mouse pointer to new location
			}
			//if user taps on mousepad to simulate a left click
			else if(line.contains("left_click")){
				//Simulate press and release of mouse button 1(makes sure correct button is pressed based on user's dexterity)
				robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			}
			//Exit if user ends the connection
			else if(line.equalsIgnoreCase("exit")){
				isConnected=false;
				//Close server and client socket
				server.close();
				client.close();
			}
	        } catch (IOException e) {
				System.out.println("Read failed");
				System.exit(-1);
	        }
      	}
	}
}