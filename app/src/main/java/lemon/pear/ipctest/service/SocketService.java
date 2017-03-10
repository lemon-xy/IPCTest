package lemon.pear.ipctest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import lemon.pear.ipctest.Config;

/**
 * Socket通信服务
 */
public class SocketService extends Service {

    private boolean isServiceDestoryed = false;
    private String[] definedMessages = new String[]{
            "你好啊，哈哈哈",
            "请问你叫什么名字啊",
            "今天天气不错啊",
            "据说爱笑的的人运气不会太差"
    };

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new TcpServer()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        isServiceDestoryed = true;
        super.onDestroy();
    }

    private class TcpServer implements Runnable {
        @Override
        public void run() {
            ServerSocket serverSocket;
            try {
                //监听端口
                serverSocket = new ServerSocket(50000);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            while (!isServiceDestoryed) {
                try {
                    final Socket socket = serverSocket.accept();
                    new Thread() {
                        @Override
                        public void run() {
                            responseClient(socket);
                        }
                    }.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void responseClient(Socket socket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            while (!isServiceDestoryed) {
                String str = in.readLine();
                Log.i(Config.LOG_TAG, "receive:" + str);
                if (str == null) {
                    break;
                }
                int index = new Random().nextInt(definedMessages.length);
                String msg = definedMessages[index];
                out.println(msg);
                Log.i(Config.LOG_TAG, "send:" + msg);
            }
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
