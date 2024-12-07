package top.tunm.xmut.tunmpvz.bullet;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.instant.CCHide;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.util.CGPointUtil;

import top.tunm.xmut.tunmpvz.R;
import top.tunm.xmut.tunmpvz.ToolsSet;
import top.tunm.xmut.tunmpvz.zombies.Zombie;
import top.tunm.xmut.tunmpvz.effect.AEffect;
import top.tunm.xmut.tunmpvz.plant.ShooterPlant;

/**
 * ThreeBullet 类代表一种特殊的子弹类型，可以攻击特定行或目标点。
 */
public class ThreeBullet extends Bullet {

    private int attackRow;       // 子弹攻击的行
    private CGPoint targetPoint; // 子弹瞄准的目标点

    /**
     * 构造方法：创建基于行攻击的子弹。
     *
     * @param shooterPlant 发射子弹的植物对象
     * @param row          攻击的目标行
     */
    public ThreeBullet(ShooterPlant shooterPlant, int row) {
        super("bullet/bullet1.png", shooterPlant);
        this.attackRow = row;
        System.out.println("子弹创建成功，目标行：" + row);
        move(); // 启动子弹移动
    }

    /**
     * 构造方法：创建基于点攻击的子弹。
     *
     * @param shooterPlant 发射子弹的植物对象
     * @param targetPoint  瞄准的具体目标点
     * @param row          攻击的目标行
     */
    public ThreeBullet(ShooterPlant shooterPlant, CGPoint targetPoint, int row) {
        super("bullet/bullet1.png", shooterPlant);
        this.targetPoint = targetPoint;
        this.attackRow = row;
        System.out.println("子弹创建成功，目标行：" + row);
        adjustAngleAndMove(); // 调整角度并移动子弹
    }

    /**
     * 调整子弹的角度，并启动移动到目标点的动作。
     */
    private void adjustAngleAndMove() {
        CGPoint endPoint = ccp(targetPoint.x, targetPoint.y); // 计算目标点
        float travelTime = CGPointUtil.distance(getPosition(), endPoint) / getSpeed(); // 根据速度计算移动时间

        // 创建移动动作
        CCMoveTo moveToAction = CCMoveTo.action(travelTime, endPoint);
        runAction(moveToAction);

        // 创建延迟动作，移动结束后触发下一步
        CCDelayTime delay = CCDelayTime.action(travelTime);
        CCCallFunc moveCallback = CCCallFunc.action(this, "move");
        CCSequence sequence = CCSequence.actions(delay, moveCallback);
        runAction(sequence);
    }

    /**
     * 展示子弹爆炸效果（根据子弹类型决定火焰爆炸或普通爆炸效果）。
     *
     * @param zombie 子弹命中的僵尸
     */
    @Override
    public void showBulletBlast(Zombie zombie) {
        // 根据是否是火焰子弹选择特效
        String effectPath = isFire() ? "eff/fireBomb/Frame%02d.png" : "eff/pea/eff%02d.png";
        int frameCount = isFire() ? 5 : 3; // 特效帧数
        float duration = isFire() ? 0.8f : 0.3f; // 特效持续时间
        float delay = isFire() ? 0.8f : 1.0f;   // 延迟时间

        // 创建特效并设置位置
        AEffect effect = new AEffect(effectPath, frameCount, duration, 0.1f);
        effect.setPosition(ccp(zombie.getPosition().x, zombie.getPosition().y - (isFire() ? 40 : 0)));
        getParent().addChild(effect, 6);

        // 设置特效延迟隐藏
        CCDelayTime ccDelayTime = CCDelayTime.action(delay);
        CCHide ccHide = CCHide.action();
        CCSequence ccSequence = CCSequence.actions(ccDelayTime, ccHide);
        effect.runAction(ccSequence);

        // 播放对应的音效
        int soundEffect = isFire() ? R.raw.bomb1 : R.raw.fight;
        ToolsSet.effectSound(soundEffect);
    }

    // 获取攻击行
    public int getAttRow() {
        return attackRow;
    }

    // 设置攻击行
    public void setAttackRow(int attackRow) {
        this.attackRow = attackRow;
    }
}
/*
引入了 adjustAngleAndMove() 方法，专门处理目标点攻击的子弹。将调整角度和移动逻辑独立出来，提高了代码的清晰度。
使用 CGPointUtil.distance 和 getSpeed() 动态计算子弹到目标点的飞行时间，确保子弹行为符合逻辑。
isFire通过使用条件运算符简化了效果路径、帧数、持续时间和延迟时间的选择逻辑，减少了重复代码。
 */