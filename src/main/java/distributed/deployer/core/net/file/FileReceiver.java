package distributed.deployer.core.net.file;

import distributed.deployer.core.net.entity.EData;
import distributed.deployer.core.net.entity.FileInfo;
import distributed.deployer.core.net.entity.FileStatus;
import distributed.deployer.core.utils.JsonUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileReceiver {
    public static void receive(EData data, String dic, FinishCallBack cb) throws IOException {
        FileInfo fileInfo = JsonUtil.mapper().convertValue(data.getParams(), FileInfo.class);
        File f = new File(dic + fileInfo.getFileName());
        if (FileStatus.create.equals(fileInfo.getFileStatus())) {
            FileUtils.deleteQuietly(f);
        } else if (FileStatus.append.equals(fileInfo.getFileStatus())) {
            FileUtils.writeByteArrayToFile(f, data.getBody(), true);
        } else if (FileStatus.finish.equals(fileInfo.getFileStatus())) {
            cb.accept(fileInfo);
        }
    }
}
