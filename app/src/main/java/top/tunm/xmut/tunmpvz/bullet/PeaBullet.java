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
 * PeaBullet 类表示豌豆子弹。
 *
 * @author jingyuyan
 * @version 1.0
 * @since 2018/12/17
 */
public class PeaBullet extends Bullet {

    /**
     * 构造函数，使用默认图片路径初始化豌豆子弹对象。
     *
     * @param shooterPlant 发射该子弹的射手植物对象
     */
    public PeaBullet(ShooterPlant shooterPlant) {
        super("bullet/bullet1.png", shooterPlant);
    }

    /**
     * 构造函数，使用自定义图片路径初始化豌豆子弹对象。
     *
     * @param shooterPlant 发射该子弹的射手植物对象
     * @param path         子弹图片的文件路径
     * @param isLeft       是否向左移动
     */
    public PeaBullet(ShooterPlant shooterPlant, String path, boolean isLeft) {
        super(path, shooterPlant, isLeft);
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
            showPeaEffect(zombie);
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
     * 显示豌豆爆炸效果，并播放豌豆爆炸音效。
     *
     * @param zombie 被击中的僵尸对象
     */
    private void showPeaEffect(Zombie zombie) {
        AEffect peaEffect = new AEffect("eff/pea/eff%02d.png", 3, 0.3f, 0.1f);
        peaEffect.setPosition(ccp(zombie.getPosition().x, zombie.getPosition().y));
        getParent().addChild(peaEffect, 6);

        // 创建动作序列：延迟1.0秒后隐藏爆炸效果
        CCDelayTime delay = CCDelayTime.action(1.0f);
        CCHide hide = CCHide.action();
        CCSequence sequence = CCSequence.actions(delay, hide);
        peaEffect.runAction(sequence);

        // 播放豌豆爆炸音效
        ToolsSet.effectSound(R.raw.fight);
    }
}
/*
将 showBulletBlast 方法拆分为两个私有方法：showFireBombEffect 和 showPeaEffect，分别处理火焰炸弹爆炸和豌豆爆炸。
这样可以提高代码的可读性和可维护性，避免在单个方法中处理过多的逻辑。
将爆炸效果的位置参数从硬编码改为通过参数传入，提高代码的灵活性和可扩展性。
 */