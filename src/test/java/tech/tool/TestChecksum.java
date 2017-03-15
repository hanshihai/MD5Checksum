package tech.tool;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by hans on 2017/3/15.
 */
public class TestChecksum {

    @Test
    public void testGenerateMD5Checksum() throws Exception {
        String md5 = "fc5e038d38a57032085441e7fe7010b0";
        Checksum check = new Checksum();
        check.update("helloworld".getBytes("UTF-8"));
        assertEquals(md5, check.digest(true));
    }
}
