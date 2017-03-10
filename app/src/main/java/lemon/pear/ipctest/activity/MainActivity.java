package lemon.pear.ipctest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import lemon.pear.ipctest.R;

/**
 * 主页面
 **/
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btnBook)
    Button btnBook;
    @BindView(R.id.btnMessenger)
    Button btnMessenger;
    @BindView(R.id.btnProvider)
    Button btnProvider;
    @BindView(R.id.btnSocket)
    Button btnSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main);
        ButterKnife.bind(this);
        btnBook.setOnClickListener(this);
        btnMessenger.setOnClickListener(this);
        btnProvider.setOnClickListener(this);
        btnSocket.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMessenger:
                startActivity(new Intent(this, MessengerActivity.class));
                break;
            case R.id.btnBook:
                startActivity(new Intent(this, ManagerActivity.class));
                break;
            case R.id.btnProvider:
                startActivity(new Intent(this, ProviderActivity.class));
                break;
            case R.id.btnSocket:
                startActivity(new Intent(this, SocketActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
