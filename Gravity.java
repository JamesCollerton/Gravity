// James Collerton
// 46114

// Java - Graphics.

// Main class, creates a display window and runs the menu.

public class Gravity 
{

	public static void main(String[] args) 
    {
    	Display_Window win = new Display_Window();
    	Menu menu = new Menu(win);

    	menu.run();
    }

}