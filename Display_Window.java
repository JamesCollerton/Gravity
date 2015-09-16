import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

// This is the actual window that gets displayed on the screen. This class
// creates and displays the window, as well as updating what is shown. It also
// contains the controls object in order to register keyboard presses.

// Responsibilities: Creating the window.
//                   Displaying the window and updating its contents.
//                   Holding the controls.

class Display_Window implements Runnable
{
    private final int WIDTH = Game_Const.CANV_W, 
                      HEIGHT = Game_Const.WIND_H;

    private JFrame w = new JFrame();
    private Controls controls = new Controls();

    Display_Window()
    {
        SwingUtilities.invokeLater(this);
    }

    // Creates the main window for running the program.
    public void run()
    {
        w.setDefaultCloseOperation(w.EXIT_ON_CLOSE);
        w.setTitle("Gravity");
        w.setSize(WIDTH, HEIGHT);
        w.setContentPane(controls);
        w.getContentPane().setBackground(Color.BLACK); 
        w.setLocationByPlatform(true);
        w.setVisible(true);
    }

    // When we call on the window to be updated we invalidate everything
    // on it and repaint over it. Also, we update the object that is on
    // screen within the controls.
    public void update(Box new_screen, Menu_GUI menu_gui, Game game,
                       Control_Screen_GUI cntrl_gui)
    {
        w.getContentPane().removeAll();
        w.add(new_screen);
        w.invalidate();
        w.validate();
        w.repaint();
        controls.update_object(menu_gui, game, cntrl_gui);
    }

}