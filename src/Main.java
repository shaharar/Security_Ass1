import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {


    public static void main (String[] args){

        Encrypter enc;
        Decrypter dec;
        EncryptionBreaker breaker;

        Parser p = new Parser();

        //read commands from cmd
        String command = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            command = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] res;
        res = p.parse(command);

        switch (res[3]){
       // switch (args[0]){
            case "e":
          //  case "-e":
                enc = new Encrypter();
                try {
                    enc.encrypt(res[0],res[1],res[2]);
                 //   enc.encrypt(args[2],args[4],args[6]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "d":
           // case "-d":
                dec = new Decrypter();
                try {
                    dec.decrypt(res[0],res[1],res[2]);
                //    dec.decrypt(args[2],args[4],args[6]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "b":
          //  case "-b":
                breaker = new EncryptionBreaker();
                try {
                    breaker.encBreak(res[0],res[1],res[2]);
                  //  breaker.encBreak(args[2],args[4],args[6]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}

//Java –jar security_Ass1B.jar –e –k C:\\Users\\User\\key_short –i C:\\Users\\User\\message_short –o C:\\Users\\User\\cipher.txt
