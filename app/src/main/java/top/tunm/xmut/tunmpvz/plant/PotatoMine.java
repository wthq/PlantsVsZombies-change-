package top.tunm.xmut.tunmpvz.plant;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.types.CGPoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import top.tunm.xmut.tunmpvz.R;
import top.tunm.xmut.tunmpvz.ToolsSet;
import top.tunm.xmut.tunmpvz.zombies.Zombie;
import top.tunm.xmut.tunmpvz.effect.AEffect;

/**
 * 土豆地雷类，负责展示和处理土豆地雷的行为。  
 */
public class PotatoMine extends Plant {

    private boolean isUp = false; // 判断土豆雷是否长出来  
    private int bombHurt = 9999;  // 爆炸伤害  

    public PotatoMine() {
        super("plant/PotatoMine/Stand%02d.png", 2);
        setPrice(25);

        // 延迟3秒后调用up方法  
        CCDelayTime ccDelayTime = CCDelayTime.action(3.0f);
        CCCallFunc ccCallFunc = CCCallFunc.action(this, "up");
        CCSequence ccSequence = CCSequence.actions(ccDelayTime, ccCallFunc);
        runAction(ccSequence);
    }

    /**
     * 将土豆雷状态设置为已长出，并开始动画。  
     */
    public void up() {
        isUp = true;
        ArrayList<CCSpriteFrame> frames = new ArrayList<>();

        // 加载动画帧  
        for (int i = 0; i < 8; i++) {
            CCSpriteFrame ccSpriteFrame = CCSprite.sprite(String.format(Locale.CHINA,
                    "plant/PotatoMine/Frame%02d.png", i)).displayedFrame();
            frames.add(ccSpriteFrame);
        }

        // 创建并运行动画  
        CCAnimation ccAnimation = CCAnimation.animationWithFrames(frames, 0.2f);
        CCAnimate ccAnimate = CCAnimate.action(ccAnimation, true);
        CCRepeatForever ccRepeatForever = CCRepeatForever.action(ccAnimate);
        runAction(ccRepeatForever);
    }

    /**
     * 处理土豆雷的爆炸，造成伤害并生成效果。  
     *
     * @param zombies 当前范围内的僵尸列表  
     */
    public void bomb(ArrayList<Zombie> zombies) {
        // 检查范围内的僵尸并造成伤害  
        for (Zombie zombie : zombies) {
            if (CGPoint.ccpDistance(getPosition(), zombie.getPosition()) <= 90) {
                zombie.hurtCompute(getBombHurt());
                if (zombie.getHP() == 0) {
                    zombie.death(1);
                    zombie.removeSelf();
                }
            }
        }

        // 播放爆炸音效  
        ToolsSet.effectSound(R.raw.ele);
        // 创建爆炸效果  
        AEffect explosionEffect = new AEffect("eff/pota/eff%02d.png", 5, 0.8f, 0.16f);
        explosionEffect.setPosition(ccp(getPosition().x, getPosition().y - 20));
        getParent().addChild(explosionEffect, 6);

        // 处理自身的伤害  
        hurtCompute(9999);
        removeSelf(); // 移除自身  
    }

    // Getter 和 Setter 方法  

    public boolean isUp() {
        return isUp; // 获取土豆雷是否长出  
    }

    public void setUp(boolean up) {
        isUp = up; // 设置土豆雷状态  
    }

    public int getBombHurt() {
        return bombHurt; // 获取爆炸伤害  
    }

    public void setBombHurt(int bombHurt) {
        this.bombHurt = bombHurt; // 设置爆炸伤害  
    }
}
/*
在 bomb 方法中，使用增强的 for 循环替代迭代器，简化了代码并提高了可读性。将音效和效果的创建逻辑集中在 bomb 方法中.
 */