import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Decrypter {

    byte[] key, cipher;
    byte[][] cipherBlock, k1, k2, k3;
    byte[] result;

    public Decrypter() {
        key = null;
        cipher = null;
        result = null;
        cipherBlock = new byte[4][4];
        k1 = new byte[4][4];
        k2 = new byte[4][4];
        k3 = new byte[4][4];
    }

    public void decrypt(String path1, String path2, String path3) throws IOException {

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
            cipher = Files.readAllBytes(path_2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        result = new byte[cipher.length];

        int idxC = 0, idxR = 0;
        while (idxC < cipher.length){
            for(int row = 0; row < 4; row++){
                for(int col = 0; col < 4; col++){
                    cipherBlock[col][row] = cipher[idxC];
                    idxC++;
                }
            }
            addRoundKey(cipherBlock, k3);
            shiftRows(cipherBlock);
            addRoundKey(cipherBlock, k2);
            shiftRows(cipherBlock);
            addRoundKey(cipherBlock, k1);
            shiftRows(cipherBlock);

            for(int row = 0; row < 4; row++){
                for(int col = 0; col < 4; col++){
                    result[idxR] = cipherBlock[col][row];
                    idxR++;
                }
            }
        }
        writeToFile(result, path3);
    }

    private void shiftRows(byte[][] cipherBlock) {
        for(int row = 1; row < 4; row++){
            shiftRight(cipherBlock, row);
        }
    }

    private void shiftRight(byte[][] cipherBlock, int row) {
        int counter = 0;
        while (counter < row){
            byte rightCell = cipherBlock[row][3];
            for(int col = 2; col >= 0; col--){
                cipherBlock[row][col+1] = cipherBlock[row][col];
            }
            cipherBlock[row][0] = rightCell;
            counter++;
        }
    }

    private void addRoundKey(byte[][] cipherBlock, byte[][] k) {
        for(int row = 0; row < 4; row++){
            for(int col = 0; col < 4; col++){
                int xorRes = (cipherBlock[row][col] & 0xff)^(k[row][col]& 0xff);
                cipherBlock[row][col] = (byte)xorRes;
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

