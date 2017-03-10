package lemon.pear.ipctest.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import lemon.pear.ipctest.Config;
import lemon.pear.ipctest.R;
import lemon.pear.ipctest.service.MessengerService;

/**
 * 信使测试
 */
public class MessengerActivity extends AppCompatActivity {

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //给服务端发送消息
            Messenger messenger = new Messenger(iBinder);
            Message msg = Message.obtain(null, Config.MSG_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString("msg", "I am client,I send a message to you");
            msg.setData(data);
            msg.replyTo = getMessenger;//设置接受返回消息的信使
            try {
                messenger.send(msg);
                Log.i(Config.LOG_TAG, "send success");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private Messenger getMessenger = new Messenger(new MessengerHandler());//接受返回消息的信使

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.MSG_FROM_SERVICE:
                    Log.i(Config.LOG_TAG, msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_messenger);
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
}