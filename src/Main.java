import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {


    public static void main (String[] args){

        Encrypter enc;
        Decrypter dec;
        EncryptionBreaker breaker;
/*        Parser p = new Parser();

        //read commands from cmd
        String command = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            command = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] res;
        res = p.parse(command);*/
   //     res = p.parse("Java –jar aes.jar –e –k key.txt –i message.txt –o cypther.txt");

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
                dec.decrypt(args[2],args[4],args[6]);
                break;
            case "-b":
                breaker = new EncryptionBreaker();
                breaker.encBreak(args[2],args[4],args[6]);
                break;
        }
    }
}
