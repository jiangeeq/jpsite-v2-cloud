package com.mty.jls.gateway.config.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * @author NZL
 * @version 1.0
 * @date 2019/12/11 9:47
 */
public class SSHConnection2 {
    String user = "root";
    String password = "tvchat-544687667D";
    String host = "106.52.26.148";
    int port = 22;
    // 这个是本地的端口，很重要！！！选取一个没有占用的port即可
    int localNacosPort = 8848;
    // 要访问的服务所在的host    服务器局域网IP（127.0.0.1也行）
    String remote_host = "127.0.0.1";
    int remoteNacosPort = 8848;
    // 服务器上数据库端口号
    Session session = null;
    /**
     *    建立SSH连接
     */
    public void SSHConnection() throws Exception {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            // 日志打印自己脑补
            session.connect();
            session.setPortForwardingL(localNacosPort, remote_host, remoteNacosPort);

        } catch (Exception e) {
            // do something
        }
    }
    /**
     *    断开SSH连接
     */
    public void closeSSH () throws Exception
    {
        this.session.disconnect();
    }

}
