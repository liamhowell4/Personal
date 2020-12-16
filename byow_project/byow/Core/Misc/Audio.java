package byow.Core.Misc;

/**
 * @author  Wil Aquino
 * Date:    November 27, 2020
 * Project: Build Your Own World (BYOW, Proj 3)
 * Module:  Audio.java
 * Purpose: Plays the project's included music.
 *
 * @source  "Heaven and Hell" by Jeremy Blake.
 * @source  "UI Hover SFX" by Sound Library, on YouTube.
 * Note:    This file makes use of three audio files. If
 *          they are not found, Engine will run into
 *          errors.
 */
public class Audio {

    /** Plays the music. */
    public static void playMusic() {
        StdAudio.loop("Jeremy_Blake_Heaven_And_Hell.wav");
    }

    /** Plays menu sound effect. */
    public static void playMenuSFX() {
        StdAudio.play("Sound_Library_UI_Hover_sfx.wav");
    }

    /** Stops the music. */
    public static void stopMusic() {
        StdAudio.close();
    }
}
