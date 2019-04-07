import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Encrypter {

    byte[] key, message, result;
    byte[][] msgBlock, k1, k2, k3;

    public Encrypter() {
        key = null;
        message = null;
        result = null;
        msgBlock = new byte[4][4];
        k1 = new byte[4][4];
        k2 = new byte[4][4];
        k3 = new byte[4][4];
    }

    public void encrypt(String path1, String path2, String path3) throws IOException {

        Path path_1 = Paths.get(path1);
        try {
            key = Files.readAllBytes(path_1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //initialize k1
        int idx = 0;
        for(int row = 0; row < 4; row++){
            for(int col = 0; col < 4; col++){
                k1[col][row] = key[idx];
                idx++;
            }
        }

        //initialize k2
        idx = 16;
        for(int row = 0; row < 4; row++){
            for(int col = 0; col < 4; col++){
                k2[col][row] = key[idx];
                idx++;
            }
        }

        //initialize k3
        idx = 32;
        for(int row = 0; row < 4; row++){
            for(int col = 0; col < 4; col++){
                k3[col][row] = key[idx];
                idx++;
            }
        }

        Path path_2 = Paths.get(path2);
        try {
            message = Files.readAllBytes(path_2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        result = new byte[message.length];

        int idxM = 0, idxR = 0;
        while (idxM < message.length){
            for(int row = 0; row < 4; row++){
                for(int col = 0; col < 4; col++){
                    msgBlock[col][row] = message[idxM];
                    idxM++;
                }
            }
            shiftRows(msgBlock);
            addRoundKey(msgBlock, k1);
            shiftRows(msgBlock);
            addRoundKey(msgBlock, k2);
            shiftRows(msgBlock);
            addRoundKey(msgBlock, k3);
            for(int row = 0; row < 4; row++){
                for(int col = 0; col < 4; col++){
                    result[idxR] = msgBlock[col][row];
                    idxR++;
                }
            }
        }
        writeToFile(result, path3);
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
                int xorRes = ((msgBlock[row][col] & 0xff))^((k[row][col]& 0xff));
                msgBlock[row][col] = (byte)xorRes;
            }
        }
    }

    private void writeToFile(byte[] result, String path) throws IOException {

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        out.write(result);
        out.close();
    }
}
