package top.tunm.xmut.tunmpvz.plant;

import org.cocos2d.actions.CCScheduler;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

import java.util.ArrayList;

import top.tunm.xmut.tunmpvz.R;
import top.tunm.xmut.tunmpvz.ToolsSet;
import top.tunm.xmut.tunmpvz.effect.AEffect;
import top.tunm.xmut.tunmpvz.zombies.Zombie;

/**
 * 路灯花类，负责发射攻击并处理与僵尸的交互。  
 */
public class Plantern extends Plant {

    private boolean isAtt;                     // 是否正在攻击  
    private int hurt;                          // 攻击伤害  
    private CCSprite mj;                       // 子弹精灵  
    private ArrayList<Zombie> zombies;         // 当前范围内的僵尸  
    private ArrayList<Zombie> removeZombies;   // 已经处理的僵尸  
    private float xh;                          // 动画进度  
    private boolean islu;                      // 控制状态标志  

    public Plantern() {
        super("plant/Plantern/Frame%02d.png", 19);
        setPrice(120);
        hurt = 50;
        removeZombies = new ArrayList<>();
        zombies = new ArrayList<>();
        xh = 2;
    }

    /**
     * 发射攻击，创建光线效果并移动子弹。  
     *
     * @param zombies 当前范围内的僵尸列表  
     */
    public void launch(ArrayList<Zombie> zombies) {
        this.zombies = zombies;

        // 创建光线效果  
        AEffect line = new AEffect("eff/lightLine/Frame%02d.png", 9, 1.2f, 1.2f / 9f);
        line.setPosition(getPosition().x + 140, getPosition().y - 60);
        getParent().addChild(line, 6);
        isAtt = true;

        // 创建并移动子弹  
        mj = CCSprite.sprite("bullet/bullet1.png");
        getParent().addChild(mj);
        mj.setOpacity(0);
        mj.setPosition(getPosition());
        CCMoveTo ccMoveTo = CCMoveTo.action(1.0f, ccp(getPosition().x + 340, getPosition().y));
        mj.runAction(ccMoveTo);

        xh = 0;
        islu = true;
        CCScheduler.sharedScheduler().schedule("range", this, 0.05f, false);

        // 设置攻击结束后的延迟  
        CCDelayTime ccDelayTime = CCDelayTime.action(6);
        CCCallFunc ccCallFunc = CCCallFunc.action(this, "reAtt");
        CCSequence ccSequence = CCSequence.actions(ccDelayTime, ccCallFunc);
        runAction(ccSequence);
    }

    /**
     * 检查当前范围内的僵尸并处理攻击逻辑。  
     *
     * @param t 时间间隔  
     */
    public void range(float t) {
        if (xh >= 1.2) {
            mj.removeSelf(); // 移除子弹  
            removeZombies.clear(); // 清空已处理的僵尸列表  
            islu = false;
            CCScheduler.sharedScheduler().unschedule("range", this); // 停止调度  
        } else {
            // 检查并处理僵尸  
            for (Zombie zombie : zombies) {
                if (!removeZombies.contains(zombie) && CGPoint.ccpDistance(mj.getPosition(), zombie.getPosition()) <= 20 && zombie.getPosition().x > getPosition().x) {
                    handleZombieHit(zombie); // 处理僵尸被击中  
                }
            }
            xh += 0.05f; // 更新动画进度  
        }
    }

    /**
     * 处理僵尸被击中的逻辑。  
     *
     * @param zombie 被击中的僵尸  
     */
    private void handleZombieHit(Zombie zombie) {
        AEffect aEffect = new AEffect("eff/star/eff%02d.png", 5, 0.6f, 0.12f);
        aEffect.setPosition(ccp(zombie.getPosition().x, zombie.getPosition().y - 40));
        getParent().addChild(aEffect, 6);
        ToolsSet.effectSound(R.raw.bomb1); // 播放音效  
        zombie.hurtCompute(getHurt()); // 计算伤害  
        if (zombie.getHP() == 0) {
            zombie.death(0); // 处理僵尸死亡  
            zombie.removeSelf(); // 移除僵尸  
        }
        removeZombies.add(zombie); // 添加到已处理列表  
    }

    /**
     * 重置攻击状态。  
     */
    public void reAtt() {
        isAtt = false; // 攻击状态重置  
    }

    // Getter 和 Setter 方法  

    public boolean isAtt() {
        return isAtt; // 获取攻击状态  
    }

    public void setAtt(boolean att) {
        isAtt = att; // 设置攻击状态  
    }

    public int getHurt() {
        return hurt; // 获取伤害值  
    }

    public void setHurt(int hurt) {
        this.hurt = hurt; // 设置伤害值  
    }
}
/*
将处理僵尸被击中的逻辑从 range 方法提取到 handleZombieHit 方法中，使得 range 方法的逻辑更加清晰，
并遵循单一职责原则。在 range 方法中，简化了对僵尸的检查和处理逻辑，使用逻辑条件组合来提高代码简洁性。
在 handleZombieHit 方法中，处理播放音效的逻辑，使得代码更集中，便于将来调整音效资源。
 */