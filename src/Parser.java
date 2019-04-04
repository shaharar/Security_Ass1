public class Parser {

    public String[] parse (String command){

        String[] ans = new String[4];
        command = command.substring(19);
        char instruction = command.charAt(0);
        command = command.substring(5);
        String[] paths = command.split(" ");
        ans[0] = paths[0];
        ans[1] = paths[2];
        ans[2] = paths[4];
        ans[3] = "" + instruction;

        System.out.println(instruction + "; " + ans[0] + ", " + ans[1] + ", " + ans[2]);

        return ans;
    }
}
