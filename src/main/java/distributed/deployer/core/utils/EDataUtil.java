package distributed.deployer.core.utils;

import distributed.deployer.core.net.entity.Command;
import distributed.deployer.core.net.entity.EData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class EDataUtil {
    private static int cbLength = 20;
    private static int paramsLength = 600;
    private static final String charsetName = "utf-8";
    private static Logger log = LoggerFactory.getLogger(EDataUtil.class);

    public static EData toEData(byte[] data) {
        EData d = new EData();
        if (data.length < cbLength + paramsLength) {
            log.error("error: data length " + data.length + "<" + (cbLength + paramsLength));

        }
        ByteArrayInputStream bi = new ByteArrayInputStream(data);
        try {
            byte[] cm = new byte[cbLength];
            bi.read(cm);
            String cmd = new String(cm, charsetName);
            d.command = Command.valueOf(cmd.trim());

            byte[] paramByte = new byte[paramsLength];
            bi.read(paramByte);
            String paramStr = new String(paramByte, charsetName).trim();
            if (paramStr.length() >= 2) {
                d.params = JsonUtil.readString(paramStr, Map.class);
            }
            d.body = new byte[bi.available()];
            bi.read(d.body);
            log.debug("length:" + d.body.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static byte[] eDataToBytes(EData d, int realLength) {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        try {

            byte[] fillSpace = null;
            byte[] cb = d.command.name().getBytes(charsetName);
            if (cb.length > cbLength) {
                log.error("error:" + cb.length + ";command must <=" + cbLength);
                return null;
            }
            bo.write(cb);
            if (cb.length < cbLength) {
                fillSpace = new byte[cbLength - cb.length];
                Arrays.fill(fillSpace, (byte) 32);
                bo.write(fillSpace);
            }
            byte[] pb = null;
            if (d.params != null) {
                pb = JsonUtil.mapper().writeValueAsString(d.params).getBytes(charsetName);
            } else {
                pb = new byte[0];
            }
            if (pb.length > paramsLength) {
                log.error("error:" + pb.length + ";param must <=" + cbLength);
                return null;
            }
            bo.write(pb);
            if (pb.length < paramsLength) {
                fillSpace = new byte[paramsLength - pb.length];
                Arrays.fill(fillSpace, (byte) 32);
                bo.write(fillSpace);
            }
            if (d.body != null) {
                log.debug("length:" + realLength);
                bo.write(d.body, 0, realLength);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bo.toByteArray();
    }

    public static byte[] eDataToBytes(EData d) {
        int length = 0;
        if (d.getBody() != null) {
            length = d.body.length;
        }
        return eDataToBytes(d, length);
    }
}
