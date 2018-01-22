import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Checksum6 {
    public static String AlgorithmName = "MD5";
    private MessageDigest digest;
    private String checksumAlgorithm;

    public String getChecksumAlgorithm() {
        return this.checksumAlgorithm;
    }

    public Checksum6() {
        this.checksumAlgorithm = AlgorithmName;
        if(this.digest == null) {
            try {
                this.digest = MessageDigest.getInstance(this.checksumAlgorithm);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("No MD5 Algorithm!", e);
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
        MessageDigest clone;
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

    public String digest(String filePath) throws IOException {
        if(filePath == null) {
            return null;
        }
        File file = new File(filePath);
        if(file != null && file.exists() && !file.isDirectory()) {
            DigestInputStream digestInput = null;
            try {
                digestInput = this.getDigestInputStream(new FileInputStream(file));
                for(byte[] e = new byte[1024]; digestInput.read(e) > 0; e = new byte[1024]) {
                    ;
                }
                String result = this.digest(true);
                return result;
            } catch (IOException ioe) {
                throw new RuntimeException("Failed to load file :" + filePath, ioe);
            } finally {
                if(digestInput != null) {
                    digestInput.close();
                }
                digestInput = null;
            }
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        Checksum6 check = new Checksum6();
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
            System.out.print("The checksum of the file ");
            System.out.println(args[0] + " is: ");
            System.out.println(check.digest(args[0]));
        }

    }
}
