package top.tunm.xmut.tunmpvz.plant;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.types.util.CGPointUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import top.tunm.xmut.tunmpvz.R;
import top.tunm.xmut.tunmpvz.ToolsSet;
import top.tunm.xmut.tunmpvz.zombies.Zombie;
import top.tunm.xmut.tunmpvz.effect.AEffect;

/**
 * 窝瓜类，表示一种攻击性植物，用于攻击和伤害僵尸。  
 */
public class Squash extends Plant {

    private boolean isAttack = false; // 判断窝瓜是否在攻击  
    private int hurt = 2000; // 窝瓜的伤害值  
    private ArrayList<Zombie> zombies; // 在攻击范围内的僵尸列表  

    public Squash() {
        super("plant/Squash/Frame%02d.png", 17); // 加载窝瓜的动画帧  
        setPrice(50); // 设置植物的价格  
    }

    /**
     * 开始攻击指定的僵尸。  
     *
     * @param zombie 被攻击的僵尸  
     * @param zombies 在攻击范围内的僵尸列表  
     */
    public void attack(Zombie zombie, ArrayList<Zombie> zombies) {
        this.zombies = zombies; // 记录当前范围内的僵尸  
        setAttack(true); // 设置攻击状态为真  
        playAttackAnimation(); // 播放攻击动画  
        moveToZombie(zombie); // 移动到目标僵尸的位置  
        schedulePush(zombie); // 安排推击效果  
    }

    // 播放窝瓜的攻击动画  
    private void playAttackAnimation() {
        ArrayList<CCSpriteFrame> frames = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            CCSpriteFrame frame = CCSprite.sprite(String.format(Locale.CHINA,
                    "plant/SquashAttack/Frame%02d.png", i)).displayedFrame();
            frames.add(frame);
        }
        CCAnimation animation = CCAnimation.animationWithFrames(frames, 0.1f);
        CCAnimate animateAction = CCAnimate.action(animation, true);
        CCRepeatForever repeatAction = CCRepeatForever.action(animateAction);
        runAction(repeatAction); // 运行循环攻击动画  
    }

    // 移动窝瓜到目标僵尸的位置  
    private void moveToZombie(Zombie zombie) {
        CCMoveTo moveAction = CCMoveTo.action(0.8f, zombie.getPosition());
        runAction(moveAction); // 执行移动动作  
    }

    // 安排推击效果，在延迟后执行  
    private void schedulePush(Zombie zombie) {
        CCDelayTime delayAction = CCDelayTime.action(0.8f); // 延迟0.8秒  
        CCCallFunc callFunc = CCCallFunc.action(this, "push"); // 调用push方法  
        CCSequence sequence = CCSequence.actions(delayAction, callFunc);
        runAction(sequence); // 运行序列动作  
    }

    // 执行推击效果并对周围僵尸造成伤害  
    public void push() {
        createPushEffect(); // 创建推击效果  
        damageNearbyZombies(); // 对附近的僵尸造成伤害  
        playSuiEffect(); // 播放消除效果  
        scheduleRemoval(); // 安排窝瓜的移除  
        ToolsSet.effectSound(R.raw.tu); // 播放音效  
    }

    // 创建推击的视觉效果  
    private void createPushEffect() {
        AEffect effect = new AEffect("eff/SetEff/Frame%02d.png", 4);
        effect.setPosition(ccp(getPosition().x, getPosition().y - 20));
        getParent().addChild(effect, 8); // 将效果添加到场景中  
    }

    // 对附近的僵尸造成伤害  
    private void damageNearbyZombies() {
        Iterator<Zombie> zombieIterator = zombies.iterator();
        while (zombieIterator.hasNext()) {
            Zombie zombie = zombieIterator.next();
            if (CGPointUtil.distance(zombie.getPosition(), getPosition()) <= 90) {
                zombie.hurtCompute(hurt); // 计算伤害  
                if (zombie.getHP() <= 0) {
                    zombie.death(0); // 僵尸死亡  
                    zombie.removeSelf(); // 移除僵尸  
                    zombieIterator.remove(); // 从列表中移除僵尸  
                }
            }
        }
    }

    // 播放消除效果动画  
    private void playSuiEffect() {
        ArrayList<CCSpriteFrame> frames = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            CCSpriteFrame frame = CCSprite.sprite(String.format(Locale.CHINA,
                    "eff/sui/Frame%02d.png", i)).displayedFrame();
            frames.add(frame);
        }
        CCAnimation animation = CCAnimation.animationWithFrames(frames, 0.1f);
        CCAnimate animateAction = CCAnimate.action(animation, true);
        CCRepeatForever repeatAction = CCRepeatForever.action(animateAction);
        runAction(repeatAction); // 运行循环消除效果动画  
    }

    // 安排窝瓜的移除  
    private void scheduleRemoval() {
        CCDelayTime delayAction = CCDelayTime.action(1.0f); // 延迟1秒  
        CCCallFunc callFunc = CCCallFunc.action(this, "remove"); // 调用remove方法  
        CCSequence sequence = CCSequence.actions(delayAction, callFunc);
        runAction(sequence); // 运行移除序列动作  
    }

    // 移除窝瓜  
    public void remove() {
        removeSelf(); // 从场景中移除自己  
    }

    public boolean isAttack() {
        return isAttack; // 返回攻击状态  
    }

    public void setAttack(boolean attack) {
        isAttack = attack; // 设置攻击状态  
    }

    public int getHurt() {
        return hurt; // 返回伤害值  
    }

    public void setHurt(int hurt) {
        this.hurt = hurt; // 设置伤害值  
    }
}
/*
将 attack 方法中的逻辑分离成多个小方法：playAttackAnimation、moveToZombie、schedulePush、createPushEffect、damageNearbyZombies、playSuiEffect 和 scheduleRemoval。每个方法负责自己特定的功能，提高了代码的可读性和可维护性。
push重构后分别提取成 createPushEffect 和 damageNearbyZombies 方法，避免了重复代码并提高了代码的复用性。
在 damageNearbyZombies 方法中，判断僵尸是否死亡时，改为检查 if (zombie.getHP() <= 0)，从而确保在施加伤害后将其正确处理。
 */