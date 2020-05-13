package MainProject;

import javafx.scene.media.AudioClip;

class Music {
    private double volume = 0.5;
    private AudioClip backgroundMusic;

    public void soundEffect(String path) {
        AudioClip soundEffect = getAudioClip(path);
        soundEffect.setVolume(volume);
        soundEffect.play();
    }

    public void setBackgroundMusic(String path) {
        backgroundMusic = getAudioClip(path);
        backgroundMusic.setVolume(volume);
        backgroundMusic.play();
    }

    public void stopBackgroundMusic() {
        backgroundMusic.stop();
    }


    public void setMusicVolume(double volume) {
        this.volume = volume;
        backgroundMusic.setVolume(volume);
    }

    private AudioClip getAudioClip(String path) {
        String full_path = "Sound/" + path;
        AudioClip audio = new AudioClip(getClass().getResource(full_path).toExternalForm());
        return audio;
    }
}

class GameMusic extends Music {
    public void playIntroMusic(GameType gameType) {
        switch (gameType) {
            case HUMAN:
                soundEffect("2sec_silent_intro_horn.wav");
                break;
            case EASY_BOT:
                soundEffect("2sec_silent_harp.wav");
                break;
            case MEDIUM_BOT:
                soundEffect("2sec_silent_vibes.wav");
                break;
            case HARD_BOT:
                soundEffect("2sec_silent_rithmic_hits.wav");
                break;
        }
    }

    public void playBackgroundMusic() {
        setBackgroundMusic("13sec_silent_golf_music.wav");
    }
}

class MenuMusic extends Music {
    public void playBackgroundMusic() {
        setBackgroundMusic("elevator_background.wav");
    }

    public void playClickSound() {
        soundEffect("click.wav");
    }
}
