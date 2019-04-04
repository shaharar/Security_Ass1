import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Encrypter {


    public void encrypt(String path1, String path2, String path3) throws IOException {

        byte[] key = null, message = null;
        byte[][] msgBlock = new byte[4][4];
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
            message = Files.readAllBytes(path_2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int index = 0, idx;
        while (index < message.length){
            for (idx = index; idx < index + 16; idx++){
                for(int row = 0; row < 4; row++){
                    for(int col = 0; col < 4; col++){
                        msgBlock[row][col] = message[idx];
                    }
                }

            }
            index = idx;
            shiftRows(msgBlock);
            addRoundKey(msgBlock, k1);
            shiftRows(msgBlock);
            addRoundKey(msgBlock, k2);
            shiftRows(msgBlock);
            addRoundKey(msgBlock, k3);
            writeToFile(msgBlock, path3);
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
