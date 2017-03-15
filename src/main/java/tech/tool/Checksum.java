package tech.tool;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Checksum {
    public static String AlgorithmName = "MD5";
    private MessageDigest digest;
    private String checksumAlgorithm;

    public String getChecksumAlgorithm() {
        return this.checksumAlgorithm;
    }

    public Checksum() {
        this.checksumAlgorithm = AlgorithmName;
        if(this.digest == null) {
            try {
                this.digest = MessageDigest.getInstance(this.checksumAlgorithm);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

    }

    public DigestInputStream getDigestInputStream(InputStream input) {
        return new DigestInputStream(input, this.digest);
    }

    public void update(byte[] input) {
        this.digest.update(input);
    }

    public void update(byte input) {
        byte[] buffer = new byte[]{input};
        this.update(buffer);
    }

    public String digest(boolean reset) {
        MessageDigest clone = null;
        if(!reset) {
            try {
                clone = (MessageDigest)this.digest.clone();
            } catch (CloneNotSupportedException var5) {
                clone = this.digest;
            }
        } else {
            clone = this.digest;
        }

        byte[] resultHex = clone.digest();
        String result = String.format("%032x", new Object[]{new BigInteger(1, resultHex)});
        return result;
    }

    public String digest(Path p) {
        if(p != null && Files.exists(p, new LinkOption[0]) && !Files.isDirectory(p, new LinkOption[0])) {

            try (DigestInputStream digestInput = this.getDigestInputStream(Files.newInputStream(p, new OpenOption[0]));){

                for(byte[] e = new byte[1024]; digestInput.read(e) > 0; e = new byte[1024]) {
                    ;
                }

                String result = this.digest(true);
                return result;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        Checksum check = new Checksum();
        if(args.length < 1) {
        	System.out.println(" --- Usage ---");
        	System.out.println(" --- java -jar MD5Checksum -s string     -> calculate string MD5. ---");
        	System.out.println(" --- java -jar MD5Checksum file          -> calculate file MD5.   ---");
        	System.out.println(" --- End ---");
        	return;
        }
        
        if("-s".equalsIgnoreCase(args[0])) {
            check.update(args[1].getBytes("UTF-8"));
            System.out.print("The checksum of ");
            System.out.println(args[1] + " is: ");
            System.out.println(check.digest(true)); 
        } else {
        	Path p = FileSystems.getDefault().getPath(args[0], new String[0]);
            System.out.print("The checksum of the file ");
            System.out.println(args[0] + " is: ");
            System.out.println(check.digest(p));
        }

    }
}
