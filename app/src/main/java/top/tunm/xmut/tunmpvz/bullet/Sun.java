package top.tunm.xmut.tunmpvz.bullet;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.util.CGPointUtil;

import java.util.ArrayList;
import java.util.Locale;

import top.tunm.xmut.tunmpvz.R;
import top.tunm.xmut.tunmpvz.ToolsSet;
import top.tunm.xmut.tunmpvz.layer.CombatLayer;

/**
 * 阳光类，表示游戏中的阳光物体。  
 */
public class Sun extends CCSprite {
    private boolean isNowCollect; // 是否正在收集  
    private ArrayList<Sun> suns; // 阳光集合  

    // 默认构造函数  
    public Sun() {
        this(null);
    }

    // 带阳光集合的构造函数  
    public Sun(ArrayList<Sun> suns) {
        super("sun/Frame00.png");
        this.suns = suns;
        runAnimation();
        scheduleRemoval();
    }

    // 运行阳光动画  
    private void runAnimation() {
        ArrayList<CCSpriteFrame> frames = new ArrayList<>();
        for (int i = 0; i < 22; i++) {
            CCSpriteFrame frame = CCSprite.sprite(String.format(Locale.CHINA, "sun/Frame%02d.png", i)).displayedFrame();
            frames.add(frame);
        }
        CCAnimation animation = CCAnimation.animationWithFrames(frames, 0.2f);
        CCAnimate animate = CCAnimate.action(animation, true);
        CCRepeatForever repeatForever = CCRepeatForever.action(animate);
        runAction(repeatForever);
    }

    // 调度阳光的移除  
    private void scheduleRemoval() {
        CCDelayTime delay = CCDelayTime.action(14);
        CCCallFunc removeCallback = CCCallFunc.action(this, "remove");
        CCSequence sequence = CCSequence.actions(delay, removeCallback);
        runAction(sequence);
    }

    // 移除阳光  
    public void remove() {
        if (suns != null) {
            suns.remove(this);
        }
        if (!isNowCollect) {
            removeSelf();
        }
    }

    // 收集阳光  
    public void collect() {
        isNowCollect = true;
        CGPoint endPosition = ccp(50, 720);
        float duration = CGPointUtil.distance(getPosition(), endPosition) / 1000;
        CCMoveTo moveToAction = CCMoveTo.action(duration, endPosition);
        CCCallFunc addSunCallback = CCCallFunc.action(this, "addSunNumber");
        CCSequence sequence = CCSequence.actions(moveToAction, addSunCallback);
        runAction(sequence);
        ToolsSet.effectSound(R.raw.get);
    }

    // 添加阳光数量  
    public void addSunNumber() {
        ((CombatLayer) getParent()).addSunNumber(25);
        removeSelf();
    }

    public boolean isNowCollect() {
        return isNowCollect;
    }
}
/*
增加的无参数构造函数Sun,调用了带参数的构造函数 Sun(ArrayList<Sun> suns)，并传入 null，以提供默认值。这种方式减少了代码重复性，同时允许创建 Sun 对象而不立即关联一个阳光集合。
在 Sun 类中提取动画的运行逻辑到 runAnimation() 方法中,提取移除调度的逻辑到 scheduleRemoval() 方法中,进行封装，减少了构造函数的复杂性。
 */