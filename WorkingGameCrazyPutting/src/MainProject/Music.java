package MainProject;

import javafx.scene.media.AudioClip;

class Music {
    private double volume = 0.5;
    private AudioClip backgroundMusic;

    private AudioClip getAudioClip(String path) {
        String full_path = "Sound/" + path;
        AudioClip audio = new AudioClip(getClass().getResource(full_path).toExternalForm());
        return audio;
    }

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
}

class GameMusic extends Music {
    public void playIntroMusic(int level) {
        switch (level) {
            case 0:
                soundEffect("2sec_silent_intro_horn.wav");
                break;
            case 1:
                soundEffect("2sec_silent_harp.wav");
                break;
            case 2:
                soundEffect("2sec_silent_vibes.wav");
                break;
            case 3:
                soundEffect("2sec_silent_rithmic_hits.wav");
                break;
        }
    }

    public void playBackgroundMusic() {
        setBackgroundMusic("13sec_silent_golf_music.wav");
    }
}

class UIMusic extends Music {
    public void playBackgroundMusic() {
        setBackgroundMusic("elevator_background.wav");
    }

    public void playClickSound() {
        soundEffect("click.wav");
    }
}
