package top.tunm.xmut.tunmpvz.plant;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;

import java.util.ArrayList;
import java.util.Locale;

public class WallNut extends Plant {

    public WallNut() {
        super("plant/WallNut/high/Frame%02d.png", 16); // 加载高墙坚果的动画帧
        setPrice(50); // 设置植物的价格
        setHP(600); // 设置植物的初始生命值
    }

    /**
     * 计算受到的伤害，并根据生命值改变植物的状态和动画。
     *
     * @param hurt 造成的伤害
     */
    @Override
    public void hurtCompute(int hurt) {
        super.hurtCompute(hurt); // 调用父类的伤害计算方法
        updateAnimationBasedOnHP(); // 更新动画状态
    }

    /**
     * 根据当前生命值更新动画状态
     */
    private void updateAnimationBasedOnHP() {
        stopAllActions(); // 停止当前所有动作
        ArrayList<CCSpriteFrame> frames = new ArrayList<>();

        // 根据生命值选择动画帧
        if (getHP() >= 200 && getHP() <= 400) {
            frames = loadAnimationFrames("plant/WallNut/middle/Frame%02d.png", 11); // 中等状态动画
        } else if (getHP() < 200) {
            frames = loadAnimationFrames("plant/WallNut/low/Frame%02d.png", 15); // 低状态动画
        }

        // 播放动画
        if (!frames.isEmpty()) {
            playAnimation(frames, 0.2f);
        }
    }

    /**
     * 加载动画帧
     *
     * @param path 动画帧路径格式
     * @param count 帧数量
     * @return 动画帧列表
     */
    private ArrayList<CCSpriteFrame> loadAnimationFrames(String path, int count) {
        ArrayList<CCSpriteFrame> frames = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            CCSpriteFrame frame = CCSprite.sprite(String.format(Locale.CHINA, path, i)).displayedFrame();
            frames.add(frame);
        }
        return frames;
    }

    /**
     * 播放给定的动画帧
     *
     * @param frames 动画帧列表
     * @param duration 帧间隔时间
     */
    private void playAnimation(ArrayList<CCSpriteFrame> frames, float duration) {
        CCAnimation ccAnimation = CCAnimation.animationWithFrames(frames, duration);
        CCAnimate ccAnimate = CCAnimate.action(ccAnimation, true);
        CCRepeatForever ccRepeatForever = CCRepeatForever.action(ccAnimate);
        runAction(ccRepeatForever); // 运行循环动画
    }
}
/*
从hurtCompute分离updateAnimationBasedOnHP 方法来处理收到伤害后的动画状态更新;loadAnimationFrames 方法来加载动画帧，通过参数传入
路径和帧数，避免了重复的 for 循环代码，减少代码的冗余性;playAnimation 方法，封装动画播放逻辑，使代码更清晰。
 */