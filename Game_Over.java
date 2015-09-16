import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.border.*;
import java.awt.Font;

// This class is used to create the game over screen for the game. It sets up
// the text and displays it to screen.

// Responsibilities: Creating the game over screen to be displayed.

public class Game_Over extends JPanel
{
	private Font font;
	private final int CANVAS_W = Game_Const.CANV_W,
				      CANVAS_H = Game_Const.CANV_H;
	private String score, level;

	Game_Over(Font font, String score, String level){

		setPreferredSize(new Dimension(CANVAS_W, CANVAS_H));
		setForeground(Color.WHITE);
		setBackground(Color.BLACK);

		this.font = font;
		this.score = score;
		this.level = level;

		create_panel();
	}

	// Creates a panel with the title, score, level and a message telling
	// the user how to get back to the main menu.
	private void create_panel()
	{
		Box game_over_panel = Box.createVerticalBox();
		Border border = BorderFactory.createEmptyBorder(20, 0, 0, 0);

		JPanel title_panel = game_over_panel(font.deriveFont(90f), 
										     "Game Over!",
										     border,
										     Color.WHITE);

		JPanel score_panel = game_over_panel(font.deriveFont(50f), 
											 score,
											 border,
											 Color.WHITE);

		JPanel level_panel = game_over_panel(font.deriveFont(50f), 
											 level,
											 border,
											 Color.WHITE);

		JPanel menu_mssge_panel = game_over_panel(font.deriveFont(30f),
												  "Press Space for Menu!",
												   border,
												   Color.GRAY);

		game_over_panel.add(title_panel);
		game_over_panel.add(score_panel);
		game_over_panel.add(level_panel);
		game_over_panel.add(menu_mssge_panel);

		add(game_over_panel);
	}

	// Each of these panels is formatted with the border for spacing and
	// a text field is added with the text. The reason I have added textfields
	// to JPanels in this way is because it centers them easily and flexibly.
	private JPanel game_over_panel(Font font, String text, Border border, 
								   Color color)
	{
		JPanel panel = new JPanel();
		JTextField text_field = game_over_text(font, text, color);
		panel.add(text_field);
		panel.setBorder(border);
		panel.setForeground(Color.WHITE);
        panel.setBackground(Color.BLACK);
        return(panel);
	}

	private JTextField game_over_text(Font font, String text, Color color)
	{
		JTextField text_field = new JTextField();
		text_field.setText(text);
		text_field.setFont(font);
		text_field.setForeground(color);
        text_field.setBackground(Color.BLACK);
        text_field.setBorder(BorderFactory.createEmptyBorder());
		return(text_field);
	}
}