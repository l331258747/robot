package com.play.robot.widget.rocherView;

import android.util.Log;

public class MyRockerShakeListener implements MyRockerView.OnShakeListener {
    @Override
    public void onStart() {
        Log.e("MyRocker","onStart");
    }

    @Override
    public void direction(MyRockerView.Direction direction) {
        Log.e("MyRocker","摇动方向 : " + direction);
    }

    @Override
    public void directionLevel(int level) {
        Log.e("MyRocker","摇动方向 level：" + level);
    }

    @Override
    public void onFinish() {
        Log.e("MyRocker","onFinish");
    }
}
