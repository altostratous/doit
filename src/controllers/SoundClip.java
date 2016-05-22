package controllers;

/**
 * Created by HP PC on 5/20/2016.
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.*;

public class SoundClip {

    private final int BUFFER_SIZE = 12800;
    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;
    private Thread playThread;

    public boolean pleaseCancel = false;


    public SoundClip(URL url) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        audioStream = AudioSystem.getAudioInputStream(url);
        audioFormat = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        sourceLine = (SourceDataLine) AudioSystem.getLine(info);
        sourceLine.open(audioFormat);
        FloatControl gainControl = (FloatControl) sourceLine
                .getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(6);
        playThread = new Thread(){
            @Override
            public void run() {

                sourceLine.start();

                int nBytesRead = 0;
                byte[] abData = new byte[BUFFER_SIZE];
                while ((nBytesRead != -1) && (!pleaseCancel)) {
                    try {
                        nBytesRead = audioStream.read(abData, 0, abData.length);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (nBytesRead >= 0) {
                        @SuppressWarnings("unused")
                        int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
                    }
                }

                sourceLine.drain();
                sourceLine.close();
            }
        };
    }

    public void play() {
        playThread.start();
    }

    public void stop() {
        pleaseCancel = true;
    }
}