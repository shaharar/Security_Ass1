import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class EncryptionBreaker {

    public void encBreak(String path1, String path2, String path3) throws IOException {

        byte[][] msgBlock = new byte[4][4];
        byte[][] cipherBlock = new byte[4][4];
        byte[] message = null;
        byte[] cipher = null;
        byte[][] k1, k2;
        k1 = new byte[4][4];
        k2 = new byte[4][4];
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
        for (int idx = 0; idx < 16; idx++){
                for(int row = 0; row < 4; row++){
                    for(int col = 0; col < 4; col++){
                        msgBlock[row][col] = message[idx];
                        cipherBlock[row][col] = cipher[idx];
                    }
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
        for(int row = 1; row < 4; row++){
            shiftLeft(msgBlock, row);
        }
    }

    private void shiftLeft(byte[][] msgBlock, int row) {
        int counter = 0;
        while (counter < row){
            byte leftCell = msgBlock[row][0];
            for(int col = 1; col < 4; col++){
                msgBlock[row][col-1] = msgBlock[row][col];
            }
            msgBlock[row][3] = leftCell;
            counter++;
        }
    }

    private void addRoundKey(byte[][] msgBlock, byte[][] k) {
        for(int row = 0; row < 4; row++){
            for(int col = 0; col < 4; col++){
                int xorRes = ((int)msgBlock[row][col])^((int)k[row][col]);
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
