package lemon.pear.ipctest.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;

import lemon.pear.ipctest.Config;

/**
 * 信使测试服务
 */
public class MessengerService extends Service {

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.MSG_FROM_CLIENT:
                    Log.i(Config.LOG_TAG, msg.getData().getString("msg"));
                    //给客户端返回消息
                    Messenger messenger = msg.replyTo;
                    Message message = Message.obtain(null, Config.MSG_FROM_SERVICE);
                    Bundle data = new Bundle();
                    data.putString("reply", "I am service,I have received your message");
                    message.setData(data);
                    try {
                        messenger.send(message);
                        Log.i(Config.LOG_TAG, "reply success");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);

            }
        }
    }

    private final Messenger messenger = new Messenger(new MessengerHandler());

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
