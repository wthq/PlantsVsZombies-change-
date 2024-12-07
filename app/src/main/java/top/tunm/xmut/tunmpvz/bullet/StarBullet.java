package top.tunm.xmut.tunmpvz.bullet;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.instant.CCHide;
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
import top.tunm.xmut.tunmpvz.effect.AEffect;
import top.tunm.xmut.tunmpvz.plant.StarFruit;
import top.tunm.xmut.tunmpvz.zombies.Zombie;

/**
 * 星星子弹类  
 */
public class StarBullet extends CCSprite {
    private int speed = 300; // 子弹速度  
    private int attack = 20; // 子弹攻击力  
    private float angle; // 子弹发射角度  
    private StarFruit starFruit; // 发射子弹的植物  
    private CGPoint intentPoint; // 子弹目标点  

    public StarBullet(StarFruit starFruit, float angle) {
        super("plant/Star/Frame00.png");
        this.starFruit = starFruit;
        this.angle = angle;
        setPosition(starFruit.getPosition().x, starFruit.getPosition().y);

        // 计算目标点  
        calculateIntentPoint();
        starFruit.getParent().addChild(this, 6);
        starFruit.getStarBullets().add(this);
        runAnimation();
        move();
        System.out.println("创建子弹并初始化");
    }

    /**
     * 计算子弹的目标点  
     */
    private void calculateIntentPoint() {
        intentPoint = CGPoint.ccp(
                starFruit.getPosition().x + 1400 * Math.cos(angle * (Math.PI / 180)),
                starFruit.getPosition().y + 1400 * Math.sin(angle * Math.PI / 180)
        );
    }

    /**
     * 运行子弹的动画  
     */
    private void runAnimation() {
        ArrayList<CCSpriteFrame> frames = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CCSpriteFrame frame = CCSprite.sprite(String.format(Locale.CHINA, "plant/Star/Frame%02d.png", i)).displayedFrame();
            frames.add(frame);
        }
        CCAnimation animation = CCAnimation.animationWithFrames(frames, 0.1f);
        CCAnimate animate = CCAnimate.action(animation, true);
        CCRepeatForever repeatForever = CCRepeatForever.action(animate);
        runAction(repeatForever);
    }

    /**
     * 移动子弹  
     */
    public void move() {
        CGPoint end = intentPoint;
        float duration = CGPointUtil.distance(getPosition(), end) / speed;
        CCMoveTo moveToAction = CCMoveTo.action(duration, end);
        CCCallFunc endCallback = CCCallFunc.action(this, "end");
        CCSequence sequence = CCSequence.actions(moveToAction, endCallback);
        runAction(sequence);
    }

    /**
     * 子弹移动结束时调用  
     */
    public void end() {
        removeSelf();
        starFruit.getStarBullets().remove(this);
    }

    /**
     * 显示子弹爆炸效果  
     */
    public void showBulletBlast(Zombie zombie) {
        AEffect effect = new AEffect("eff/star/eff%02d.png", 5, 0.6f, 0.12f);
        effect.setPosition(ccp(zombie.getPosition().x, zombie.getPosition().y - 40));
        getParent().addChild(effect, 6);
        ToolsSet.effectSound(R.raw.bomb1);

        // 设置特效延迟隐藏  
        CCDelayTime delay = CCDelayTime.action(0.8f);
        CCHide hide = CCHide.action();
        CCSequence sequence = CCSequence.actions(delay, hide);
        effect.runAction(sequence);
    }

    // Getter 和 Setter 方法  
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public StarFruit getStarFruit() {
        return starFruit;
    }

    public void setStarFruit(StarFruit starFruit) {
        this.starFruit = starFruit;
    }

    public CGPoint getIntentPoint() {
        return intentPoint;
    }

    public void setIntentPoint(CGPoint intentPoint) {
        this.intentPoint = intentPoint;
    }
}
/*
从StarBullet提取 calculateIntentPoint() 方法，负责计算子弹的目标点，增强了代码的清晰度。
提取 runAnimation() 方法，将动画的创建和运行逻辑分离，使得 StarBullet 构造函数更加简洁。
 */