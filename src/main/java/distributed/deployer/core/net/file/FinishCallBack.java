package distributed.deployer.core.net.file;


import distributed.deployer.core.net.entity.FileInfo;

@FunctionalInterface
public interface FinishCallBack {
    public void accept(FileInfo f);
}
