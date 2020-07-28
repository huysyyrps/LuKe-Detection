package com.example.luke_detection;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.luke_detection.http.base.BaseRecyclerAdapter;
import com.sakuramomoko.searchinganimview.SearchingAnimView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private IntentFilter intentFilter;
    private BroadcastReceiver mReceiver;
    private static boolean isExit = false;
    BaseRecyclerAdapter baseRecyclerAdapter;
    List<BluetoothDevice> itemList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SearchingAnimView searchingAnimView=(SearchingAnimView)findViewById(R.id.searching_anim_view);
        //开始动画
        searchingAnimView.startAnimations();
//        //停止动画
//        searchingAnimView.stopAnimations();
        BluetoothAdapter bluetoothAdapter  = BluetoothAdapter.getDefaultAdapter();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mReceiver = new BroadcastReceiver();
        registerReceiver(mReceiver,intentFilter);
        if (bluetoothAdapter == null){
            Toast.makeText(this, "当前设备不支持蓝牙链接", Toast.LENGTH_SHORT).show();
        }else {
            if (bluetoothAdapter.isEnabled()){
//                //提示用户开启蓝牙
//                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivity(intent);
                bluetoothAdapter.enable();//直接开启蓝牙
                //开启蓝牙后，需设置蓝牙为可发现状态，这样其它的蓝牙设备才能搜索到。
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                //最后的参数设置可见的时间，最长为300s,设为0表示一直可见
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
                startActivity(discoverableIntent);
                //成功打开蓝牙后开始搜索附近的蓝牙设备
                bluetoothAdapter.startDiscovery();
                intentFilter = new IntentFilter();
                intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                mReceiver = new BroadcastReceiver();
                registerReceiver(mReceiver,intentFilter);
                //停止搜索：mBluetoothAdapter.cancelDiscovery();
            }
        }
    }

    class BroadcastReceiver extends android.content.BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //避免重复添加已绑定的设备
                if (device.getBondState() != BluetoothDevice.BOND_BONDED){
                    itemList.add(device);
                }
            }else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                Toast.makeText(context, "正在搜索", Toast.LENGTH_SHORT).show();
            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                Toast.makeText(context, "搜索完毕", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //推出程序
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    //推出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, getResources().getString(R.string.again_out), Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }
}
