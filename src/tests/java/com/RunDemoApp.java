package tests.java.com;

public class RunDemoApp {
	public static int CONCURRENT_USERS = 10;
	
	public static void main (String args[]) {
		for (int i = 0; i < CONCURRENT_USERS; i++){
	        new TroiaDemoApp("User"+ i).start();
	    }
	}
}
