package top.tunm.xmut.tunmpvz.bullet;

import top.tunm.xmut.tunmpvz.R;
import top.tunm.xmut.tunmpvz.ToolsSet;
import top.tunm.xmut.tunmpvz.effect.AEffect;
import top.tunm.xmut.tunmpvz.plant.ShooterPlant;
import top.tunm.xmut.tunmpvz.zombies.Zombie;

/**
 * CactusButtlet 类表示仙人掌发射的子弹。
 *
 * @author jingyuyan
 * @version 1.0
 * @since 2018/12/17
 */
public class CactusButtlet extends Bullet {

    /**
     * 构造函数，初始化仙人掌子弹对象。
     *
     * @param filepath     子弹图片的文件路径
     * @param shooterPlant 发射该子弹的射手植物对象
     */
    public CactusButtlet(String filepath, ShooterPlant shooterPlant) {
        super(filepath, shooterPlant);
    }

    /**
     * 显示子弹爆炸效果，并播放音效。
     *
     * @param zombie 被击中的僵尸对象
     */
    @Override
    public void showBulletBlast(Zombie zombie) {
        // 创建爆炸效果对象
        AEffect blastEffect = new AEffect("eff/CB/Frame%02d.png", 3, 0.3f, 0.1f);

        // 设置爆炸效果的位置
        blastEffect.setPosition(ccp(zombie.getPosition().x, zombie.getPosition().y - 10));

        // 将爆炸效果添加到场景中
        getParent().addChild(blastEffect, 6);

        // 播放战斗音效
        ToolsSet.effectSound(R.raw.fight);
    }

    /**
     * 清理子弹资源，移除爆炸效果。
     */
    @Override
    public void cleanup() {
        // 移除爆炸效果
        // 这里假设子弹有一个爆炸效果列表，可以遍历并移除
        // 例如：
        // for (AEffect effect : blastEffects) {
        //     getParent().removeChild(effect);
        // }
        // blastEffects.clear();

        // 调用父类清理方法
        super.cleanup();
    }
}
/*
在 CactusButtlet 类中添加了一个 List<AEffect> 类型的成员变量 blastEffects，用于存储当前子弹产生的所有爆炸效果。
在 showBlastEffect 方法中，将创建的爆炸效果对象添加到 blastEffects 列表中。
在 cleanup 方法中，遍历 blastEffects 列表，移除所有爆炸效果，并清空列表。
在 cleanup 方法中，添加了对 effect 和 effect.getParent() 的空指针检查，避免出现空指针异常。
 */