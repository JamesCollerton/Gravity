import java.util.*;

// This class is used to hold all of the testing functions, but not the tests
// themselves. It uses the 'is' and a complimentary 'is not' to do testing with,
// adding results to a list of tests to be printed at the end.

// Responsibilities: Holding all of the testing functions for the program.
// 					 Storing the results of tests and printing them at the end.

public class Test_Functions 
{
	private static int NUM_TEST_CLASSES = 6;
	private static int MENU = 0, 
					   CONTROLS = 1, 
					   CONTROL_BALL = 2, 
					   MENU_LOGO = 3,
					   GAME = 4,
					   GAME_FIELD = 5;
	static int tests = 0;

	static List<List<String>> test_results = new ArrayList<List<String>>();

	Test_Functions()
	{
		initialise_testing();
	}

	// This initialises the list of tests to we can add test results as they are
	// run.
	static void initialise_testing()
	{
		for(int i = 0; i < NUM_TEST_CLASSES; ++i){
			test_results.add( new ArrayList<String>() );
		}
	}

	// Checks to see if two components are equal. If they are then adds the test
	// to the passed list. Otherwise errors.
	static void is(Object x, Object y, String arg, int test_class) 
	{
 		add_one();
 		if (x == y) {add_to_tests(arg, test_class); return; }
 		if (x != null && x.equals(y)) {add_to_tests(arg, test_class); return; }
 		throw new Error("Test failed: " + x + ", " + y);
	}

	// Same but with components not being equal.
	static void is_not(Object x, Object y, String arg, int test_class) 
	{
 		add_one();
 		if (x != y) {add_to_tests(arg, test_class); return; }
 		if (x != null && !x.equals(y)) {add_to_tests(arg, test_class); return; }
 		throw new Error("Test failed: " + x + ", " + y);
	}

	// Goes through and prints all of the results for each section one after
	// another.
	static void results()
	{
		print_tests("MENU", MENU);
		print_tests("CONTROLS", CONTROLS);
		print_tests("CONTROL BALL", CONTROL_BALL);
		print_tests("MENU LOGO", MENU_LOGO);
		print_tests("GAME", GAME);
		print_tests("GAME FIELD", GAME_FIELD);

		System.out.printf("\n\nTOTAL TESTS PASSED: %d\n\n", tests);
	}

	// This prints out all of the tests for a category. Goes through the whole
	// list printing them, and then gives a summary of the number at the end.
	static void print_tests(String test_area, int array_ind)
	{
		int i;

		System.out.printf("\n\n ---------%s TESTS----------\n\n", test_area);

		for(i = 0; i < test_results.get(array_ind).size(); ++i){
			System.out.printf("\n%s\n", test_results.get(array_ind).get(i));
		}

		System.out.printf("\nTOTAL %s TESTS RUN: %d\n", test_area, 
						  test_results.get(array_ind).size());
	}

	// Used to increment the total number of tests run.
	static void add_one()
	{
		tests++;
	}

	// This adds a test result to the list of results depending on the integer
	// given to tell it where in the list to put it.
	static void add_to_tests(String arg, int i)
	{
		add_one();

		if(i < 0 || i > NUM_TEST_CLASSES){
			System.err.printf("\n\nCan't assign tests here!\n\n");
			System.exit(1);
		}
		else{ test_results.get(i).add(arg);}
	} 

}
