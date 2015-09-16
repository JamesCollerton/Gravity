import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.nio.file.*;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.util.concurrent.*;

// This class contains all of the audio for the game and is responsible for
// playing it back when called.

// Responsibilities: Playing audio when required.

public class Game_Audio
{

	private final JFXPanel fxPanel = new JFXPanel();
    private BlockingQueue<Queue_Action> audio_queue;

    private Media backing_track, jump, grav_up, grav_down, click, game_over,
                  accept;
    private MediaPlayer backingMediaPlayer;

    // When we construct the class we want to create all of the necessary media.
    // This is so that it gets a little bit of time to load before being called
    // on and so that it can be used again and again when we need it to be. I
    // did look for ways to only play audio once the file had been fully loaded,
    // but I couldn't find any methods.
    Game_Audio()
    {
        audio_queue = new LinkedBlockingQueue<Queue_Action>();

        backing_track = create_media("Sound/MUSIC_2_GAME.mp3");
        jump = create_media("Sound/FX_3_JUMP.mp3");
        grav_up = create_media("Sound/FX_1_UP.mp3");
        grav_down = create_media("Sound/FX_2_DOWN.mp3");
        click = create_media("Sound/MENU_1_MOVE.mp3");
        game_over = create_media("Sound/FX_5_GAMEOVER.mp3");
        accept = create_media("Sound/MENU_2_SELECT.mp3");

    }

    // This reads in a string and then creates a media file according to that.
    private Media create_media(String audio_src)
    {
        Media temp = new Media(Paths.get(audio_src).toUri().toString());
        return(temp);
    }

    // This thread is used to play the backing track. By having a seperate
    // thread to do it we can have it looped in the background.
	public void playSound() 
    {
    	initFX(fxPanel);
        playBackingAudio(backing_track);
    }

    // Here we initialise JavaFX, which we must do in order to use it.
    private void initFX(JFXPanel fxPanel) 
    {
    	Group root = new Group();
    	Scene scene = new Scene(root);
        // This method is invoked on the JavaFX thread
        fxPanel.setScene(scene);
   	}

    // It might look easy to combine the two, but this would mean declaring
    // a new mediaplayer in each of jump, grav_up, grav_down, etc. It is
    // simplest to have them as two slightly different functions.

    // This plays the audio corresponding to the input media.
    private void playBackingAudio(Media audio_media)
    {
        try{
            backingMediaPlayer = new MediaPlayer(backing_track);
            backingMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backingMediaPlayer.play();
        } catch(Exception err) { throw new Error(err); }
    }

    // This plays the audio corresponding to the input media.
    private void playAudio(Media audio_media)
    {
        try{
            MediaPlayer mediaPlayer = new MediaPlayer(audio_media);
            mediaPlayer.play();
        } catch(Exception err) { throw new Error(err); }
    }

    // Each of these can be called at different points in the program to
    // include the necessary sound effect.
    public void jump()
    {
    	playAudio(jump);
    }

    public void grav_up()
    {
    	playAudio(grav_up);
    }

    public void grav_down()
    {
    	playAudio(grav_down);
    }

    public void game_over()
    {
        backingMediaPlayer.stop();
        playAudio(game_over);
    }

    public void click()
    {
    	playAudio(click);
    }

    public void accept()
    {
    	playAudio(accept);
    }

}