package com.ec.singleOut.appmain;

import com.ec.commons.command.ServerShutdownOption;
import com.ec.commons.server.jetty.SimpleHTTPServer;
import com.ec.singleOut.core.ScheduleCrm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @ClassName: AppMain
 * @author ChenJun
 * @date 2015年8月3日 下午3:20:05
 */
public class Launcher {

    private static ClassPathXmlApplicationContext applicationContext;
    private static Logger log = LogManager.getLogger(Launcher.class);

    public static Launcher appMain = null;
    private SimpleHTTPServer httpServer;
    private ScheduleCrm scheduleCrm;
    private boolean waitingForGracefulShutdown = false;

    public static void main(String[] args) {
        try {
            appMain = new Launcher();
            appMain.init();
            appMain.start();
        } catch (Throwable t) {
            log.fatal("Fatal Error in main", t);
            System.exit(8);
        }
    }

    private void init() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("classpath*:/spring/applicationContext.xml");
        log.info("=========>spring容器初始化完成...");
      //  httpServer = applicationContext.getBean(SimpleHTTPServer.class);
        scheduleCrm =  applicationContext.getBean(ScheduleCrm.class);
    }

    private void start() throws Exception {
        try {
            log.info(this + " start httpServer");
         //   httpServer.startServer(new ServerControlHandler());
            applicationContext.start();
            log.info("=========> started dubboServer");
            scheduleCrm.start();


        } catch (Exception e) {
            log.fatal(this + " Can't start server", e);
            System.exit(8);
        }
        try {
            synchronized (this) {
                this.wait();
            }
            log.debug("Server Stoped!");
            System.exit(0);
        } catch (Exception e) {
            log.error(this + " Interrupted", e);
            System.exit(8);
        }
    }

    public void shutdownServer(ServerShutdownOption shutdownOption) {
        log.info("shutdownServer(), option=" + shutdownOption);

        if (shutdownOption == ServerShutdownOption.FORCEIBLE) {
            stop();
        } else if (shutdownOption == ServerShutdownOption.GRACEFUL_WAIT) {
            waitForGracefulShutdown();
            stop();
        } else {// by default is ServerShutdownOption.GRACEFUL
            if (!waitingForGracefulShutdown) {
                final Launcher appMain = this;
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {

                            appMain.waitForGracefulShutdown();
                            log.info("Notify Server to stop");
                            appMain.stop();
                            scheduleCrm.stop();

                        } catch (Exception e) {
                            log.error(e);
                        }
                    }
                };
                thread.setName("WaitForGracefulShutdownThread");
                thread.start();
            }
        }
    }

    public void waitForGracefulShutdown() {
        log.info("waitingForGracefulShutdown");
        waitingForGracefulShutdown = true;

        if (httpServer != null) {
            try {
                httpServer.stopServer();
                log.info("stoped httpServer");
            } catch (Exception e) {
                log.error("关闭Jetty server发生异常", e);
            }
        }
    }

    public synchronized void stop() {
        this.notify();
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
