package git.javapassgen;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.security.SecureRandom;

public class passge {
    String lower = "abcdefghijklmnopqrstuvwxyz";
    String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String sym = "\'\".<>:;{}[]\\|/+=_-)(*&^%$#@!~`";
    String numbers = "0123456789";

    String completestring(boolean number, boolean caps, boolean symbol) {
        String ans = lower;
        if (number)
            ans += numbers;
        if (caps)
            ans += upper;
        if (symbol)
            ans += sym;
        return ans;
    }

    public static void main(String[] args) {
        int len;
        boolean given = false;
        boolean caps = false;
        boolean sym = false;
        boolean num = true;
        boolean start = false;
        boolean end = false;
        try {
            len = Integer.parseInt(args[0]);
            given = true;
        } catch (Exception e) {
            len = 12;
        }
        int init = given ? 1 : 0;
        for (; init < args.length; init++) {
            if (args[init].toLowerCase().equals("start"))
                start = true;
            else if (args[init].toLowerCase().equals("end"))
                end = true;
            else
                switch (args[init]) {
                    case "U":
                        caps = true;
                        break;
                    case "u":
                        caps = false;
                        break;
                    case "S":
                        sym = true;
                        break;
                    case "s":
                        sym = false;
                    case "N":
                        num = true;
                        break;
                    case "n":
                        num = false;
                        break;
                    default:
                        System.out.print("Unsupported argument passed, skipping argument no:" + Integer.toString(init)
                                + " " + args[init] + "\n");
                        break;
                }
        }
        if (len <= 7) {
            System.out.print("Input length less then 8, clipped to 8\n");
            len = 8;
        }
        if (len > 30) {
            System.out.print("Input length greater then 30, clipped to 30\n");
            len = 30;
        }
        passge p = new passge();
        String passString = p.completestring(num, caps, sym);
        String password = "";
        int cryto[] = new int[len];
        SecureRandom s = new SecureRandom();
        for (int i = 0; i < len; i++)
            cryto[i] = Math.abs(s.nextInt());
        boolean map[] = new boolean[passString.length()];
        for (int i = 0; i < len; i++) {
            if (i == 0 && start) {
                password += p.lower.charAt(cryto[i] % 26);
                continue;
            }
            if (i == len - 1 && end) {
                password += p.lower.charAt(cryto[i] % 26);
                break;
            }
            int index = cryto[i] % passString.length();
            while (map[index])
                index = (index + 1) % passString.length();
            map[index] = true;
            password += passString.charAt(index);

        }
        for (int i = 0; i < password.length(); i++) {

            System.out.print("\r" + password.substring(0, i));
            Toolkit.getDefaultToolkit().beep();
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }

        }
        StringSelection stringSelection = new StringSelection(password);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        System.out.print("\nCopied to clipboard!!");

    }
}