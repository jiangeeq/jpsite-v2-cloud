package com.mty.jls.gateway.config.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * @author NZL
 * @version 1.0
 * @date 2019/12/11 9:47
 */
public class SSHConnection {
    String user = "root";
    // 服务器登录名
    String password = "tvchat-544687667D";
    // 登陆密码
    String host = "118.25.144.60";
    //服务器公网IP
    int port = 22;
    // 跳板机ssh开放的接口   默认端口 22
    int local_mysql_port = 3307;
    int local_redis_port = 6380;

    // 这个是本地的端口，很重要！！！选取一个没有占用的port即可
    String remote_host = "127.0.0.1";
    // 要访问的mysql所在的host    服务器局域网IP（127.0.0.1也行）
    int remote_mysql_port = 3306;
    int remote_redis_port = 6379;

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
            session.setPortForwardingL(local_mysql_port, remote_host, remote_mysql_port);
            session.setPortForwardingL(local_redis_port, remote_host, remote_redis_port);

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
