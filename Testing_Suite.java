// Seperate main so that testing can be run alongside the main program.

public class Testing_Suite
{
	public static void main(String[] args) 
    {
    	Testing_Suite_Functions test_suite = new Testing_Suite_Functions();

    	test_suite.run();
    }
}