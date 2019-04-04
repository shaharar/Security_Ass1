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
   //     res = p.parse("Java –jar aes.jar –e –k key.txt –i message.txt –o cypther.txt");

        switch (res[3]){
            case "e":
                enc = new Encrypter();
                enc.encrypt(res[0],res[1],res[2]);
                break;
            case "d":
                dec = new Decrypter();
                dec.decrypt(res[0],res[1],res[2]);
                break;
            case "b":
                breaker = new EncryptionBreaker();
                breaker.encBreak(res[0],res[1],res[2]);
                break;
        }
    }
}
