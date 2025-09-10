package com.ken.camel;

import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.sampled.LineUnavailableException;

import com.ken.camel.opencv.MorseCode;

/**
 * 
 */
public class PlayMorseCode {
    private final javax.sound.sampled.AudioFormat audioFormat;
    private final javax.sound.sampled.SourceDataLine sourceDataLine;
    private int sampleRate = 48000; // samples per second

    double tone = 440.0; // A4 note
    // double tone = 523.25; //C5 note
    // double tone = 659.25; //E5 note
    int wpm = 12; // words per minute
    int time = (int) (1200.0 / wpm); // milliseconds
    int ditTime = time;// milliseconds
    int dahTime = time * 3;// milliseconds
    byte[] ditBuffer;
    byte[] ditBlankBuffer = new byte[ditTime * sampleRate / 1000];
    byte[] dahBuffer;
    char[] letterList = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', };
    char[] numberList = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    char[] punctuation = new char[] { '.', ',', '?', '\'', '!', '/', '(', ')', ':', ';', '=', '+', '-', '_', '\"', '$','@', '&', '%' };

    public void setWpm(int wpm) {
        this.wpm = wpm;
        this.time = (int)(1200.0 / this.wpm);
        this.ditTime = this.time;
        this.dahTime = this.time * 3;
        this.makeBuffers();
    }

    PlayMorseCode(boolean startLoop) throws javax.sound.sampled.LineUnavailableException {
        makeBuffers();
        audioFormat = new javax.sound.sampled.AudioFormat(sampleRate, 8, 1, true, false);
        sourceDataLine = javax.sound.sampled.AudioSystem.getSourceDataLine(audioFormat);
        String send = "Enter text to play as Morse code; \n(enter 'letter test' or 'number test' for a quick test or 'exit' to quit):";
        System.out.println(send + " ditTime:" + ditTime + " dahTime:" + dahTime);
        // playString(send);

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        while (startLoop) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("exit")) {
                playString("Good bye");
                break;
            }
            if (line.equalsIgnoreCase("test")) {
                try {
                    System.out.println(
                            " 1 for letterTest\n 2 for numberTest\n 3 for letterNumberTest\n 4 for punctuationTest\n 5 for callsignTest");
                    int testType = Integer.parseInt(scanner.nextLine());
                    System.out.println("How many questions?");
                    int questions = Integer.parseInt(scanner.nextLine());
                    switch (testType) {
                        case 1:
                            randomLetterTest(questions, scanner);
                            break;
                        case 2:
                            randomNumberTest(questions, scanner);
                            break;
                        case 3:
                            randomLetterNumberTest(questions, scanner);
                            break;
                        case 4:
                            randomPuctuationTest(questions, scanner);
                            break;
                        case 5:
                            randomCallsignTest(questions, scanner);
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }
            playString(line.trim());
        }
        scanner.close();
        if(startLoop) {
            sourceDataLine.close();
        }
    }

    private void randomCallsignTest(int questions, java.util.Scanner scanner) {
        int correct = 0;
        String[] prefixStrings = new String[] {
                // AA-AL
                "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL",
                // KA-KZ
                "K", "KA", "KB", "KC", "KD", "KE", "KF", "KG", "KH", "KI", "KJ", "KK", "KL", "KM", "KN", "KO", "KP",
                "KQ", "KR", "KS", "KT", "KU", "KV", "KW", "KX", "KY", "KZ",
                // NA-NZ
                "N", "NA", "NB", "NC", "ND", "NE", "NF", "NG", "NH", "NI", "NJ", "NK", "NL", "NM", "NN", "NO", "NP",
                "NQ", "NR", "NS", "NT", "NU", "NV", "NW", "NX", "NY", "NZ",
                // WA-WZ
                "W", "WA", "WB", "WC", "WD", "WE", "WF", "WG", "WH", "WI", "WJ", "WK", "WL", "WM", "WN", "WO", "WP",
                "WQ", "WR", "WS", "WT", "WU", "WV", "WW", "WX", "WY", "WZ"
        };
        ArrayList<String> callsignList = new ArrayList<>();
        for (int i = 0; i < questions; i++) {
            String randomPrefix = prefixStrings[(int) (Math.random() * prefixStrings.length)];
            int geographicRegion = (int) (Math.random() * 10);
            String prefix = randomPrefix + geographicRegion;
            int suffixLength = 3;
            double rand = Math.random();
            if (randomPrefix.startsWith("A")) {
                suffixLength = rand < 0.5 ? 2 : 1;
            } else if (rand < .334) {
                suffixLength = 1;
            } else if (rand < .667) {
                suffixLength = 2;
            }
            String suffix = "";
            for (int j = 0; j < suffixLength; j++) {
                suffix += letterList[(int) (Math.random() * letterList.length)];
            }
            prefix += suffix;
            System.out.println("Callsign: " + prefix.toUpperCase());
            callsignList.add(prefix.toUpperCase());
        }
        for (String callsign : callsignList) {
            playString("" + callsign);
            String line = scanner.nextLine();
            if (line.toUpperCase().equals("" + callsign)) {
                correct++;
                System.out.println("Correct!");
            } else {
                System.out.println("Wrong! It was " + callsign);
            }
        }
        System.out.println("You got " + correct + " out of " + questions + " correct.");
    }

    private void randomLetterTest(int questions, java.util.Scanner scanner) {
        int correct = 0;
        for (int i = 0; i < questions; i++) {
            char c = letterList[(int) (Math.random() * letterList.length)];
            playString("" + c);
            String line = scanner.nextLine();
            if (line.toUpperCase().equals("" + c)) {
                correct++;
                System.out.println("Correct!");
            } else {
                System.out.println("Wrong! It was " + c);
            }
        }
        System.out.println("You got " + correct + " out of " + questions + " correct.");
    }

    private void randomPuctuationTest(int questions, java.util.Scanner scanner) {
        int correct = 0;
        for (int i = 0; i < questions; i++) {
            char c = punctuation[(int) (Math.random() * punctuation.length)];
            playString("" + c);
            String line = scanner.nextLine();
            if (line.toUpperCase().equals("" + c)) {
                correct++;
                System.out.println("Correct!");
            } else {
                System.out.println("Wrong! It was " + c);
            }
        }
        System.out.println("You got " + correct + " out of " + questions + " correct.");
    }

    private void randomNumberTest(int questions, java.util.Scanner scanner) {
        int correct = 0;
        for (int i = 0; i < questions; i++) {
            char c = numberList[(int) (Math.random() * numberList.length)];
            playString("" + c);
            String line = scanner.nextLine();
            if (line.toUpperCase().equals("" + c)) {
                correct++;
                System.out.println("Correct!");
            } else {
                System.out.println("Wrong! It was " + c);
            }
        }
        System.out.println("You got " + correct + " out of " + questions + " correct.");
    }

    private void randomLetterNumberTest(int questions, java.util.Scanner scanner) {
        int correct = 0;
        char[] letterNumberList = new char[letterList.length + this.numberList.length];
        System.arraycopy(letterList, 0, letterNumberList, 0, letterList.length);
        System.arraycopy(numberList, 0, letterNumberList, letterList.length, numberList.length);

        for (int i = 0; i < questions; i++) {
            char c = letterNumberList[(int) (Math.random() * letterNumberList.length)];
            playString("" + c);
            String line = scanner.nextLine();
            if (line.toUpperCase().equals("" + c)) {
                correct++;
                System.out.println("Correct!");
            } else {
                System.out.println("Wrong! It was " + c);
            }
        }
        System.out.println("You got " + correct + " out of " + questions + " correct.");
    }

    /**
     * Play a string as morse code.
     * 
     * @param string
     */
    void playString(String string) {
        String morse = MorseCode.string2MorseCode(string.toUpperCase()).trim();
        ArrayList<byte[]> buffers = new ArrayList<>();
        for (char c : morse.toCharArray()) {
            switch (c) {
                case '.':
                    buffers.add(ditBuffer);
                    buffers.add(ditBlankBuffer);
                    break;

                case '-':
                    buffers.add(dahBuffer);
                    buffers.add(ditBlankBuffer);
                    break;

                case ' ':
                    // in between letters there is a dah length space, add 2 more
                    // because we already add one after each dit or dah
                    buffers.add(ditBlankBuffer);
                    buffers.add(ditBlankBuffer);
                    break;
                case '_':
                    // a space is 7 dit long, we only need 2 dit because
                    // of the dah space added before and after
                    buffers.add(ditBlankBuffer);
                    buffers.add(ditBlankBuffer);
                    break;
                default:
                    break;
            }
        }
        playWaveBuffer(buffers2buffer(buffers));

        // print the morse code with spaces for letters and double spaces for words
        System.out.println(morse.replaceAll("_", "  "));
    }

    /**
     * Create the sound buffers for a dit and a dah
     * Also cleans up the end of the buffers to avoid clicks
     */
    private void makeBuffers() {
        // the scale of the signed 8 bits is -127 to 127
        int scale = 127;
        int stopClickThreshold = 1;
        ditBuffer = new byte[ditTime * sampleRate / 1000];
        int i = 0;
        for (i = 0; i < ditBuffer.length; i++) {
            double angle = 2.0 * Math.PI * i * tone / sampleRate;
            ditBuffer[i] = (byte) (Math.sin(angle) * scale);
        }
        // clean up the end of the buffer to avoid clicks
        for (i = ditBuffer.length - 1; i > 0 && Math.abs(ditBuffer[i]) > stopClickThreshold; i--) {
            System.out.print(".");
            ditBuffer[i] = 0;
        }
        System.out.println();

        ditBuffer = Arrays.copyOf(ditBuffer, i);

        dahBuffer = new byte[dahTime * sampleRate / 1000];
        for (i = 0; i < dahBuffer.length; i++) {
            double angle = 2.0 * Math.PI * i * tone / sampleRate;
            dahBuffer[i] = (byte) (Math.sin(angle) * scale);
        }
        // clean up the end of the buffer to avoid clicks
        for (i = dahBuffer.length - 1; i > 0 && Math.abs(dahBuffer[i]) > stopClickThreshold; i--) {
            System.out.print(".");
            dahBuffer[i] = 0;
        }
        System.out.println();
        dahBuffer = Arrays.copyOf(dahBuffer, i);

        // better safe then sorry, and a good habit from my c++ days.
        Arrays.fill(ditBlankBuffer, (byte) 0);
    }

    /**
     * Convert an ArrayList of byte arrays into a single byte array
     */
    private byte[] buffers2buffer(ArrayList<byte[]> buffers) {
        int totalLength = 0;
        for (byte[] buffer : buffers) {
            totalLength += buffer.length;
        }
        byte[] allBytes = new byte[totalLength];
        int currentIndex = 0;
        for (byte[] buffer : buffers) {
            System.arraycopy(buffer, 0, allBytes, currentIndex, buffer.length);
            currentIndex += buffer.length;
        }
        return allBytes;
    }

    /**
     * Play a wave buffer using the sourceDataLine
     * 
     * @param buffer
     */
    private void playWaveBuffer(byte[] buffer) {
        long startTime = 0, stopTime = 0;
        System.out.println("Wave file length:" + (buffer.length));
        int playTime = (int) (((float) buffer.length) / (float) (sampleRate / 1000));
        System.out.println("Play time (ms):" + playTime);
        try {
            sourceDataLine.open(audioFormat, buffer.length);
            sourceDataLine.start();
            startTime = System.currentTimeMillis();
            sourceDataLine.write(buffer, 0, buffer.length);
            sourceDataLine.drain();
            stopTime = System.currentTimeMillis();
            sourceDataLine.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("elapsed (ms):" + (stopTime - startTime));
    }

    public static void main(String[] args) {
        System.out.println("Play Morse Code application started!");
        try {
            new PlayMorseCode(true);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
