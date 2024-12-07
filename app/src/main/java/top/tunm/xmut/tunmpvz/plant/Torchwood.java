package top.tunm.xmut.tunmpvz.plant;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;

import java.util.ArrayList;
import java.util.Iterator;

import top.tunm.xmut.tunmpvz.zombies.Zombie;
import top.tunm.xmut.tunmpvz.effect.AEffect;

/**
 * 火盆类，表示一种植物，能够对经过的僵尸造成伤害。
 */
public class Torchwood extends Plant {

    private boolean isFire = false; // 火盆是否处于点燃状态
    private int hurt = 60; // 火盆造成的伤害

    public Torchwood() {
        super("plant/Torchwood/Frame%02d.png", 9); // 加载火盆的动画帧
        setPrice(175); // 设置植物的价格
    }

    /**
     * 点燃火盆并对经过的僵尸造成伤害
     *
     * @param zombies 经过的僵尸列表
     */
    public void fire(ArrayList<Zombie> zombies) {
        if (isFire) return; // 如果已经点燃，则不再执行

        isFire = true; // 设置为点燃状态
        createFireEffect(); // 创建火焰效果
        damageZombies(zombies); // 对经过的僵尸造成伤害

        // 设置火盆在10秒后熄灭
        CCDelayTime ccDelayTime = CCDelayTime.action(10);
        CCCallFunc ccCallFunc = CCCallFunc.action(this, "openFire");
        CCSequence ccSequence = CCSequence.actions(ccDelayTime, ccCallFunc);
        runAction(ccSequence);
    }

    /**
     * 创建火焰效果
     */
    private void createFireEffect() {
        AEffect fireEffect = new AEffect("eff/fireBig/Frame%02d.png", 8); // 创建火焰效果
        fireEffect.setPosition(ccp(getPosition().x + 180, getPosition().y - 80)); // 设置火焰效果位置
        getParent().addChild(fireEffect, 6); // 将火焰效果添加到场景中
    }

    /**
     * 对经过的僵尸造成伤害
     *
     * @param zombies 经过的僵尸列表
     */
    private void damageZombies(ArrayList<Zombie> zombies) {
        Iterator<Zombie> zombieIterator = zombies.iterator();
        while (zombieIterator.hasNext()) {
            Zombie zombie = zombieIterator.next();
            zombie.hurtCompute(hurt); // 对僵尸造成伤害
            if (zombie.getHP() <= 0) { // 如果僵尸生命值为0或以下
                zombie.death(1); // 处理僵尸死亡
                zombie.removeSelf(); // 从场景中移除僵尸
                zombieIterator.remove(); // 从列表中移除僵尸
            }
        }
    }

    /**
     * 熄灭火盆
     */
    public void openFire() {
        isFire = false; // 设置为未点燃状态
    }

    public boolean isFire() {
        return isFire; // 返回火盆是否点燃状态
    }

    public void setFire(boolean fire) {
        isFire = fire; // 设置火盆的点燃状态
    }
}
/*
将火焰效果的创建逻辑提取到 createFireEffect 方法中，使 fire 方法更加简洁。
将对僵尸造成伤害的逻辑提取到 damageZombies 方法中，增强了代码的可读性和复用性。
在 fire 方法中添加了对 isFire 状态的检查，避免重复点燃。
 */