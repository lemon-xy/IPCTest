package lemon.pear.ipctest.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import butterknife.BindView;
import butterknife.ButterKnife;
import lemon.pear.ipctest.Config;
import lemon.pear.ipctest.R;
import lemon.pear.ipctest.service.SocketService;

public class SocketActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.btnSend)
    Button btnSend;

    private static final int MESSAGE_RECEIVE_NEW = 1;

    private static final int MESSAGE_SOCKET_CONNECT = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_RECEIVE_NEW:
                    tvMessage.setText(tvMessage.getText() + (String) msg.obj + "\n");
                    break;
                case MESSAGE_SOCKET_CONNECT:
                    btnSend.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };

    private PrintWriter printWriter;

    private Socket client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_socket);
        ButterKnife.bind(this);
        Intent intent = new Intent(this, SocketService.class);
        startService(intent);
        new Thread() {
            @Override
            public void run() {
                connectServer();
            }
        }.start();
        btnSend.setOnClickListener(this);
    }

    private void connectServer() {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("localhost", 50000);
                client = socket;
                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECT);
                Log.i(Config.LOG_TAG, "Server connect");
            } catch (Exception e) {
                SystemClock.sleep(1000);
                e.printStackTrace();
            }
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!this.isFinishing()) {
                String msg = in.readLine();
                Log.i(Config.LOG_TAG, "receive:" + msg);
                if (msg != null) {
                    mHandler.obtainMessage(MESSAGE_RECEIVE_NEW, msg + "\n").sendToTarget();
                }
            }
            printWriter.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSend:
                final String msg = etMessage.getText().toString();
                if (!TextUtils.isEmpty(msg) && printWriter != null) {
                    printWriter.println(msg);
                    tvMessage.setText(tvMessage.getText() + msg + "\n");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (client != null) {
            try {
                client.shutdownInput();
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
