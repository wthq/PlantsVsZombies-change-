package top.tunm.xmut.tunmpvz.bullet;

import org.cocos2d.actions.instant.CCHide;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;

import top.tunm.xmut.tunmpvz.R;
import top.tunm.xmut.tunmpvz.ToolsSet;
import top.tunm.xmut.tunmpvz.effect.AEffect;
import top.tunm.xmut.tunmpvz.plant.ShooterPlant;
import top.tunm.xmut.tunmpvz.zombies.Zombie;

/**
 * SnowBullet 类表示雪子弹。
 *
 * @author jingyuyan
 * @version 1.0
 * @since 2018/12/17
 */
public class SnowBullet extends Bullet {

    /**
     * 构造函数，初始化雪子弹对象。
     *
     * @param shooterPlant 发射该子弹的射手植物对象
     */
    public SnowBullet(ShooterPlant shooterPlant) {
        super("bullet/bullet2.png", shooterPlant);
        setAttack(10); // 设置攻击力为10（较小）
    }

    /**
     * 显示子弹爆炸效果，并播放音效。
     *
     * @param zombie 被击中的僵尸对象
     */
    @Override
    public void showBulletBlast(Zombie zombie) {
        if (isFire()) {
            showFireBombEffect(zombie);
        } else {
            showSnowEffect(zombie);
        }
    }

    /**
     * 显示火焰炸弹爆炸效果，并播放火焰炸弹音效。
     *
     * @param zombie 被击中的僵尸对象
     */
    private void showFireBombEffect(Zombie zombie) {
        AEffect fireBombEffect = new AEffect("eff/fireBomb/Frame%02d.png", 5, 0.8f, 0.16f);
        fireBombEffect.setPosition(ccp(zombie.getPosition().x, zombie.getPosition().y - 40));
        getParent().addChild(fireBombEffect, 6);

        // 创建动作序列：延迟0.8秒后隐藏爆炸效果
        CCDelayTime delay = CCDelayTime.action(0.8f);
        CCHide hide = CCHide.action();
        CCSequence sequence = CCSequence.actions(delay, hide);
        fireBombEffect.runAction(sequence);

        // 播放火焰炸弹音效
        ToolsSet.effectSound(R.raw.bomb1);
    }

    /**
     * 显示雪子弹爆炸效果，减缓僵尸速度，并播放雪子弹音效。
     *
     * @param zombie 被击中的僵尸对象
     */
    private void showSnowEffect(Zombie zombie) {
        // 减缓僵尸速度
        zombie.slow();

        // 创建雪子弹爆炸效果
        AEffect snowEffect = new AEffect("eff/SnowEff/Frame%02d.png", 6, 0.6f, 0.1f);
        snowEffect.setPosition(ccp(zombie.getPosition().x, zombie.getPosition().y));
        getParent().addChild(snowEffect, 6);

        // 创建动作序列：延迟1.0秒后隐藏爆炸效果
        CCDelayTime delay = CCDelayTime.action(1.0f);
        CCHide hide = CCHide.action();
        CCSequence sequence = CCSequence.actions(delay, hide);
        snowEffect.runAction(sequence);

        // 播放雪子弹音效
        ToolsSet.effectSound(R.raw.fight);
    }
}
/*
将 showBulletBlast 方法拆分为两个私有方法：showFireBombEffect 和 showSnowEffect，分别处理火焰炸弹爆炸和雪子弹爆炸效果。
将爆炸效果的位置参数从硬编码改为通过参数传入，提高代码的灵活性和可扩展性。
 */