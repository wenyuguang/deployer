package distributed.deployer.core.net.entity;


import distributed.deployer.core.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class EData {

    private static Logger log = LoggerFactory.getLogger(EData.class);
    public Command command;
    public Map params;
    public byte[] body;

    public EData() {
    }

    public EData(Command command, Map<String, Object> params, byte[] body) {
        this.command = command;
        this.params = params;
        this.body = body;
    }

    public EData(Command command, Object params, byte[] body) {
        this.command = command;
        if (params != null) {
            this.params = JsonUtil.mapper().convertValue(params, Map.class);
        }
        this.body = body;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }


}
