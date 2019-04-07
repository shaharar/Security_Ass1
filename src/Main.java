import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main (String[] args){

        Encrypter enc;
        Decrypter dec;
        EncryptionBreaker breaker;

        switch (args[0]){
            case "-e":
                enc = new Encrypter();
                try {
                    enc.encrypt(args[2],args[4],args[6]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "-d":
                dec = new Decrypter();
                try {
                    dec.decrypt(args[2],args[4],args[6]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "-b":
                breaker = new EncryptionBreaker();
                try {
                    breaker.encBreak(args[2],args[4],args[6]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}