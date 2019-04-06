import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Encrypter {

    byte[] key, message;
    byte[][] msgBlock, k1, k2, k3;

    public Encrypter() {
        key = null;
        message = null;
        msgBlock = new byte[4][4];
        k1 = new byte[4][4];
        k2 = new byte[4][4];
        k3 = new byte[4][4];
    }

    public void encrypt(String path1, String path2, String path3) throws IOException {

        Path path_1 = Paths.get("C:\\Users\\User\\key_short");
       // Path path_1 = Paths.get(path1);
        try {
            key = Files.readAllBytes(path_1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //initialize k1
        int idx = 0;
        for(int row = 0; row < 4; row++){
            for(int col = 0; col < 4; col++){
                k1[row][col] = key[idx];
                idx++;
            }
        }

        //initialize k2
        idx = 16;
        for(int row = 0; row < 4; row++){
            for(int col = 0; col < 4; col++){
                k2[row][col] = key[idx];
                idx++;
            }
        }

        //initialize k3
        idx = 32;
        for(int row = 0; row < 4; row++){
            for(int col = 0; col < 4; col++){
                k3[row][col] = key[idx];
                idx++;
            }
        }

        Path path_2 = Paths.get("C:\\Users\\User\\message_short");
        //Path path_2 = Paths.get(path2);
        try {
            message = Files.readAllBytes(path_2);
        } catch (IOException e) {
            e.printStackTrace();
        }


        int index = 0, idxM;
        while (index < message.length){
            idxM = index;
            for(int row = 0; row < 4; row++){
                for(int col = 0; col < 4; col++){
                    msgBlock[row][col] = message[idxM];
                    idxM++;
                }
            }
            index = idx;
            shiftRows(msgBlock);
            addRoundKey(msgBlock, k1);
            shiftRows(msgBlock);
            addRoundKey(msgBlock, k2);
            shiftRows(msgBlock);
            addRoundKey(msgBlock, k3);
            writeToFile(msgBlock, "C:\\Users\\User\\cipher.txt");
          //  writeToFile(msgBlock, path3);
        }
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
