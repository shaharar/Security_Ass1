import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class EncryptionBreaker {

    byte[] cipher, message;
    byte[][] msgBlock, cipherBlock, k1, k2;

    public EncryptionBreaker() {
        message = null;
        cipher = null;
        msgBlock = new byte[4][4];
        cipherBlock = new byte[4][4];
        k1 = new byte[4][4];
        k2 = new byte[4][4];
    }

    public void encBreak(String path1, String path2, String path3) throws IOException {

        Random rnd = new Random();
        for(int row = 0; row < 4; row++){
            rnd.nextBytes(k1[row]);
            do {
                rnd.nextBytes(k2[row]);
            }
            while (k1[row].equals(k2[row]));
        }

        Path path_1 = Paths.get(path1);
        Path path_2 = Paths.get(path2);
        try {
            message = Files.readAllBytes(path_1);
            cipher = Files.readAllBytes(path_2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int idxM = 0, idxC = 0;
        for(int row = 0; row < 4; row++){
            for(int col = 0; col < 4; col++){
                msgBlock[row][col] = message[idxM];
                cipherBlock[row][col] = cipher[idxC];
                idxM++;
                idxC++;
            }
        }
        shiftRows(msgBlock);
        addRoundKey(msgBlock, k1);
        shiftRows(msgBlock);
        addRoundKey(msgBlock, k2);
        shiftRows(msgBlock);
        addRoundKey(msgBlock, cipherBlock);
        writeToFile(msgBlock, path3);
    }

    private void shiftRows(byte[][] msgBlock) {
        int row = 1;
        while (row < 4){
            byte leftCell = msgBlock[row][0];
            for(int col = 1; col < 4; col++){
                msgBlock[row][col-1] = msgBlock[row][col];
            }
            msgBlock[row][3] = leftCell;
            row++;
        }
    }

    private void addRoundKey(byte[][] msgBlock, byte[][] k) {
        for(int row = 0; row < 4; row++){
            for(int col = 0; col < 4; col++){
              //  int xorRes = ((int)msgBlock[row][col])^((int)k[row][col]);
                int xorRes = ((int)(msgBlock[row][col] & 0xff))^((int)(k[row][col]& 0xff));
                msgBlock[row][col] = (byte)xorRes;
            }
        }
    }

    private void writeToFile(byte[][] msgBlock, String path) throws IOException {

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(int row = 0; row < 4; row++){
            out.write(msgBlock[row]);
        }
        out.close();
    }
}
