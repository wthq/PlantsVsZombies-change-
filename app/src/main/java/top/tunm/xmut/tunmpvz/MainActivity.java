package top.tunm.xmut.tunmpvz;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.sound.SoundEngine;

import top.tunm.xmut.tunmpvz.layer.LogoLayer;

public class MainActivity extends Activity {
    private CCDirector ccDirector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 处理沉浸式全屏及隐藏导航栏相关逻辑
        handleFullScreenSettings();
        // 设置屏幕常亮
        setScreenKeepOn();
        CCGLSurfaceView ccglSurfaceView = new CCGLSurfaceView(this);
        setContentView(ccglSurfaceView);
        ccDirector = CCDirector.sharedDirector();
        ccDirector.attachInView(ccglSurfaceView);
        ccDirector.setDisplayFPS(true);
        ccDirector.setScreenSize(1280, 768);
        CCScene ccScene = CCScene.node();
        ccScene.addChild(new LogoLayer());
        ccDirector.runWithScene(ccScene);
        ToolsSet.preloadSound();
    }

    private void handleFullScreenSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void setScreenKeepOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ccDirector.onPause();
        SoundEngine.sharedEngine().pauseSound();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ccDirector.onResume();
        SoundEngine.sharedEngine().resumeSound();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ccDirector.end();
    }
}