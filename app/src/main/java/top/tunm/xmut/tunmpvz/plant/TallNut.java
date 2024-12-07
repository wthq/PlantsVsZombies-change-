package top.tunm.xmut.tunmpvz.plant;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;

import java.util.ArrayList;
import java.util.Locale;

/**
 * 高坚果类，表示一种植物，具有不同的状态动画。  
 */
public class TallNut extends Plant {

    public TallNut() {
        super("plant/TallNut/high/Frame%02d.png", 14); // 加载高坚果的动画帧  
        setPrice(125); // 设置植物的价格  
        setHP(1200); // 设置植物的初始生命值  
    }

    /**
     * 计算受到的伤害，并根据生命值改变状态和动画  
     *
     * @param hurt 造成的伤害  
     */
    @Override
    public void hurtCompute(int hurt) {
        super.hurtCompute(hurt); // 调用父类的伤害计算方法  
        updateAnimationBasedOnHP(); // 更新动画状态  
    }

    /**
     * 根据当前生命值更新动画  
     */
    private void updateAnimationBasedOnHP() {
        stopAllActions(); // 停止所有当前动作  

        if (getHP() >= 400 && getHP() <= 800) {
            playMiddleAnimation(); // 播放中等状态动画  
        } else if (getHP() < 400) {
            playLowAnimation(); // 播放低状态动画  
        }
    }

    /**
     * 播放中等状态动画  
     */
    private void playMiddleAnimation() {
        ArrayList<CCSpriteFrame> frames = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            CCSpriteFrame frame = CCSprite.sprite(String.format(Locale.CHINA,
                    "plant/TallNut/middle/Frame%02d.png", i)).displayedFrame();
            frames.add(frame);
        }
        playAnimation(frames); // 播放动画  
    }

    /**
     * 播放低状态动画  
     */
    private void playLowAnimation() {
        ArrayList<CCSpriteFrame> frames = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            CCSpriteFrame frame = CCSprite.sprite(String.format(Locale.CHINA,
                    "plant/TallNut/low/Frame%02d.png", i)).displayedFrame();
            frames.add(frame);
        }
        playAnimation(frames); // 播放动画  
    }

    /**
     * 播放指定的动画帧  
     *
     * @param frames 动画帧列表  
     */
    private void playAnimation(ArrayList<CCSpriteFrame> frames) {
        CCAnimation animation = CCAnimation.animationWithFrames(frames, 0.2f);
        CCAnimate animateAction = CCAnimate.action(animation, true);
        CCRepeatForever repeatAction = CCRepeatForever.action(animateAction);
        runAction(repeatAction); // 运行循环动画  
    }
}
/*
 hurtCompute将动画更新逻辑分离到 updateAnimationBasedOnHP将中等和低状态动画的播放逻辑分别提取到 playMiddleAnimation 和 playLowAnimation 方法，将播放动画的逻辑提取到 playAnimation 方法提高了代码的可读性与复用性。
 */