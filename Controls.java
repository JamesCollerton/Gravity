import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// This class is responsible for all of the user's keyboard input in the
// program. It takes in all of the keystrokes and then feeds them to the
// relevant other classes depending on where it thinks we are in the prog.

// Responsibilities: Taking in all keyboard input from the user.
//                   Redistributing it to the rest of the program.

class Controls extends JPanel
{
    private Game_State state;
    private Menu_GUI menu_gui;
    private Game game;
    private Control_Screen_GUI cntrl_screen_gui;
    private Game_Audio audio = new Game_Audio();

    private String[] commands = { "UP", "DOWN", "LEFT", "RIGHT", "SPACE"};
    private int menu_x, menu_y, controls_x, controls_y;

    private final int UP = 0, 
                      DOWN = 1,
                      SPACE = 4; 

    // The constructor sets up looking for all of the commands contained
    // in the above vector when the window is selected.
    public Controls()
    {
        menu_x = 0;
        menu_y = 0;

        for (int i = 0; i < commands.length; i++)       
            registerKeyboardAction(panelAction,
                            commands[i],
                            KeyStroke.getKeyStroke(commands[i]),
                            JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    // This redistributes the input depending on what it senses it to be.
    private ActionListener panelAction = new ActionListener()
    {   
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            String command = (String) ae.getActionCommand();
            if (command.equals(commands[UP])){ up_down(true); }
            else if (command.equals(commands[DOWN])){ up_down(false); }
            else if (command.equals(commands[2])){}
            else if (command.equals(commands[3])){}
            else if (command.equals(commands[SPACE])){ space(); }

            repaint();  
        }
    };

    // Here we take in an up or down key press and set the indicator
    // accordingly. Then depending on where we ae in the program we scroll
    // up or down in menus or change the gravity.
    private void up_down(boolean up)
    {
        if(state == Game_State.MENU){
            audio.click(); 
            if(up){ menu_y -= 1; } else{ menu_y += 1; } 
            menu_gui.update_y(menu_y); 
        }
        else if(state == Game_State.CONTROLS){
            audio.click();  
            if(up){ controls_y -= 1; } else{ controls_y += 1; } 
            cntrl_screen_gui.update_y(controls_y); 
        }
        else if(state == Game_State.IN_GAME){
            if(up){ game.grav_up(); } 
            else{ game.grav_down(); }
        } 
    }

    // If space has been pressed then we pass this message to the correct part 
    // of the program and have either options be selected or the ball jump. When
    // we press space in the main menu then we reset all the menu positions to
    // their starting position as we will either be starting a new game in which
    // case when we return to the menu we will be starting again, or going to the
    // controls screen and will want the first option selected. 
    private void space()
    {
        if(state == Game_State.MENU){
            audio.accept();
            reset_menu_pos(); 
            reset_options_pos(); 
            menu_gui.register_space();
        }
        else if(state == Game_State.IN_GAME){
            game.jump();
        }
        else if(state == Game_State.CONTROLS){
            audio.accept();
            cntrl_screen_gui.register_space();
        }
        else if(state == Game_State.GAME_OVER){
            menu_gui.set_backing_playing(false);
            menu_gui.run();
        }
    }

    // This updates the current state that we think that the game is in.
    // We feed the method arguments depending on where we are in the program
    // and set others to null. Then whatever is not null is our game state and
    // we update that component of this class.
    public void update_object(Menu_GUI new_menu_gui, Game new_game,
                              Control_Screen_GUI cntrl_gui)
    {
        if( new_menu_gui != null ){
            state = Game_State.MENU;
            menu_gui = new_menu_gui;
        }
        else if( new_game != null ){ 
            state = Game_State.IN_GAME;
            game = new_game;
        }
        else if( cntrl_gui != null ){ 
            state = Game_State.CONTROLS;
            cntrl_screen_gui = cntrl_gui; 
        }
        else{
            state = Game_State.GAME_OVER;
        }
    }

    public void reset_menu_pos()
    {
        menu_y = 0;
    }

    public void reset_options_pos()
    {
        controls_y = 0;
    }

    public Game_State state()
    {
        return(state);
    }
}