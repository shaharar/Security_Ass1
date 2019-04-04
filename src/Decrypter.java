import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Decrypter {

    public void decrypt(String path1, String path2, String path3) throws IOException {

        byte[] key = null, cipher = null;
        byte[][] cipherBlock = new byte[4][4];
        byte[][] k1 = new byte[4][4];
        byte[][] k2 = new byte[4][4];
        byte[][] k3 = new byte[4][4];

        Path path_1 = Paths.get(path1);
        try {
            key = Files.readAllBytes(path_1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //initialize k1
        for (int idx = 0; idx < 16; idx++){
            for(int row = 0; row < 4; row++){
                for(int col = 0; col < 4; col++){
                    k1[row][col] = key[idx];
                }
            }
        }

        //initialize k2
        for (int idx = 16; idx < 32; idx++){
            for(int row = 0; row < 4; row++){
                for(int col = 0; col < 4; col++){
                    k2[row][col] = key[idx];
                }
            }
        }

        //initialize k3
        for (int idx = 32; idx < 48; idx++){
            for(int row = 0; row < 4; row++){
                for(int col = 0; col < 4; col++){
                    k3[row][col] = key[idx];
                }
            }
        }

        Path path_2 = Paths.get(path2);
        try {
            cipher = Files.readAllBytes(path_2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int index = 0, idx;
        while (index < cipher.length){
            for (idx = index; idx < index + 16; idx++){
                for(int row = 0; row < 4; row++){
                    for(int col = 0; col < 4; col++){
                        cipherBlock[row][col] = cipher[idx];
                    }
                }
            }
            index = idx;
            addRoundKey(cipherBlock, k3);
            shiftRows(cipherBlock);
            addRoundKey(cipherBlock, k2);
            shiftRows(cipherBlock);
            addRoundKey(cipherBlock, k1);
            shiftRows(cipherBlock);
            writeToFile(cipherBlock, path3);
        }
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
            for(int col = 2; col > 0; col--){
                cipherBlock[row][col+1] = cipherBlock[row][col];
            }
            cipherBlock[row][0] = rightCell;
            counter++;
        }
    }

    private void addRoundKey(byte[][] cipherBlock, byte[][] k) {
        for(int row = 0; row < 4; row++){
            for(int col = 0; col < 4; col++){
                int xorRes = ((int)cipherBlock[row][col])^((int)k[row][col]);
                cipherBlock[row][col] = (byte)xorRes;
            }
        }
    }

    private void writeToFile(byte[][] cipherBlock, String path) throws IOException {

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(int row = 0; row < 4; row++){
            out.write(cipherBlock[row]);
        }
        out.close();
    }
}

