import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.Font;

// This class is used to create options lists as seen in the main menu and the
// controls screen. It is initialised and then fed a list of options, one of
// which is initially highlighted, and then creates a menu that can be navigated
// using the keyboard.

// Responsibilities: Creating lists of options for use in the menu.
// 					 Changing their properties depending on what is highlighted.

public class Options_List 
{
	private Font font;
	private String highlighted_option;
	private Border border = BorderFactory.createEmptyBorder(20, 20, 0, 20);

	Options_List(Font font)
	{
		this.font = font;
	}

	// Creates a box with the list of options and returns it.
	public Box create_list(ArrayList<String> options, String highlight)
	{
		Box list = Box.createVerticalBox();
		for( String option : options ){
			list.add(make_option(option, highlight));
		}
        return(list);
	}

	// Each of the options in the list uses underscores instead of spaces and
	// so they are replaced. Then we create a new button using the text and
	// set the font accordingly. If we want the current string to be highlighted
	// we increase its size and highlight it white, otherwise it is grey. 
	// Finally we get rid of all formatting and add the border for spacing.

	// (Only public for testing.)
	public Box make_option(String option, String highlight)
	{
		Box menu_box = Box.createHorizontalBox();

		String option_text = adjust_text(option);
		JButton option_lab = new JButton(option_text);
		option_lab.setFont(font);

		if(highlight.equals(option)){ 
			option_lab.setForeground(Color.WHITE);
			option_lab.setFont(font.deriveFont(font.getSize() + 10f)); 
		}
		else{ option_lab.setForeground(Color.GRAY); }
		
		option_lab.setBorderPainted(false);
		option_lab.setFocusPainted(false);
		option_lab.setContentAreaFilled(false);

		menu_box.add(option_lab);
		menu_box.setBorder(border);

		return(menu_box);
	}

	// Replaces underscores with spaces so the strings are more readable.
	public String adjust_text(String text)
	{
		return(text.replace("_", " "));
	}

	// This is called from other classes when we want to highlight a new 
	// option. We increment the value of the list position as long as it is
	// below zero. Then take the modulus so it is within the range of our
	// list of options, highlighting the new one.
	public String find_new_option(ArrayList<String> options, int new_pos)
	{
		int num_options = options.size();
		int list_position = new_pos % num_options;

		while(list_position < 0){ list_position += num_options; }

		highlighted_option = options.get(list_position % num_options);

		return(highlighted_option);
	}

}