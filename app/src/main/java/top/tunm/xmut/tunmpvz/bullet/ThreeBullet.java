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
 * 三重子弹类，继承自 Bullet 并实现抽象方法。
 */
public class ThreeBullet extends Bullet {

    private int attRow; // 攻击的行数
    private CGPoint intentPoint; // 子弹的目标点

    /**
     * 具体的 Builder 类，用于构建 ThreeBullet 对象。
     */
    public static class Builder extends Bullet.Builder<ThreeBullet> {
        private int attRow; // 攻击的行数
        private CGPoint intentPoint; // 子弹的目标点

        public Builder setAttRow(int attRow) { // 设置攻击的行数
            this.attRow = attRow;
            return this;
        }

        public Builder setIntentPoint(CGPoint intentPoint) { // 设置子弹的目标点
            this.intentPoint = intentPoint;
            return this;
        }

        @Override
        public ThreeBullet build() { // 构建 ThreeBullet 对象
            return new ThreeBullet(this);
        }
    }

    /**
     * 使用具体的 Builder 构建 ThreeBullet 对象。
     */
    protected ThreeBullet(Builder builder) {
        super(builder); // 调用父类构造方法，初始化子弹的基本属性
        this.attRow = builder.attRow; // 设置攻击的行数
        this.intentPoint = builder.intentPoint; // 设置子弹的目标点
        System.out.println("意图 创建成功 " + attRow); // 打印调试信息
        if (intentPoint != null) {
            reAng(); // 如果有目标点，重新设置子弹的角度和移动路径
        } else {
            move(); // 否则，按默认路径移动
        }
    }

    /**
     * 使用 Builder 构建 ThreeBullet 对象。
     */
    public ThreeBullet(ShooterPlant shooterPlant, int row) {
        this(new Builder() // 使用 Builder 构建 ThreeBullet 对象
                .setShooterPlant(shooterPlant) // 设置发射子弹的植物
                .setAttack(30) // 设置 ThreeBullet 的攻击力
                .setSpeed(350) // 设置 ThreeBullet 的速度
                .setFire(false) // 设置是否为火焰弹
                .setLeft(false) // 设置移动方向
                .setFilepath("bullet/bullet1.png") // 设置子弹图片路径
                .setAttRow(row)); // 设置攻击的行数
    }

    /**
     * 使用 Builder 构建 ThreeBullet 对象，并设置目标点。
     */
    public ThreeBullet(ShooterPlant shooterPlant, CGPoint cgPoint, int row) {
        this(new Builder() // 使用 Builder 构建 ThreeBullet 对象
                .setShooterPlant(shooterPlant) // 设置发射子弹的植物
                .setAttack(30) // 设置 ThreeBullet 的攻击力
                .setSpeed(350) // 设置 ThreeBullet 的速度
                .setFire(false) // 设置是否为火焰弹
                .setLeft(false) // 设置移动方向
                .setFilepath("bullet/bullet1.png") // 设置子弹图片路径
                .setAttRow(row) // 设置攻击的行数
                .setIntentPoint(cgPoint)); // 设置目标点
    }

    /**
     * 重新设置子弹的角度和移动路径。
     */
    public void reAng() {
        CGPoint end = intentPoint; // 获取目标点
        float t = CGPointUtil.distance(getPosition(), end) / getSpeed(); // 计算子弹到达目标点所需的时间
        CCMoveTo moveAction = CCMoveTo.action(t, end); // 创建移动动作
        runAction(moveAction); // 运行动作

        CCDelayTime delay = CCDelayTime.action(t); // 创建延迟动作
        CCCallFunc callFunc = CCCallFunc.action(this, "move"); // 创建调用方法的动作
        CCSequence sequence = CCSequence.actions(delay, callFunc); // 创建动作序列
        runAction(sequence); // 运行动作序列
    }

    /**
     * 实现 showBulletBlast 方法，定义三重子弹的爆炸效果。
     */
    @Override
    public void showBulletBlast(Zombie zombie) {
        if (isFire()) { // 如果是火焰子弹
            AEffect fireEffect = new AEffect("eff/fireBomb/Frame%02d.png", 5, 0.8f, 0.16f); // 创建火焰效果
            fireEffect.setPosition(ccp(zombie.getPosition().x, zombie.getPosition().y - 40)); // 设置火焰效果的位置
            getParent().addChild(fireEffect, 6); // 将火焰效果添加到父节点
            CCDelayTime delay = CCDelayTime.action(0.8f); // 创建延迟动作
            CCHide hide = CCHide.action(); // 创建隐藏动作
            CCSequence sequence = CCSequence.actions(delay, hide); // 创建动作序列
            fireEffect.runAction(sequence); // 运行动作序列
            ToolsSet.effectSound(R.raw.bomb1); // 播放爆炸音效
        } else { // 如果是普通子弹
            AEffect peaEffect = new AEffect("eff/pea/eff%02d.png", 3, 0.3f, 0.1f); // 创建普通子弹爆炸效果
            peaEffect.setPosition(ccp(zombie.getPosition().x, zombie.getPosition().y)); // 设置爆炸效果的位置
            getParent().addChild(peaEffect, 6); // 将爆炸效果添加到父节点
            CCDelayTime delay = CCDelayTime.action(1.0f); // 创建延迟动作
            CCHide hide = CCHide.action(); // 创建隐藏动作
            CCSequence sequence = CCSequence.actions(delay, hide); // 创建动作序列
            peaEffect.runAction(sequence); // 运行动作序列
            ToolsSet.effectSound(R.raw.fight); // 播放战斗音效
        }
    }

    /**
     * 获取攻击的行数。
     */
    public int getAttRow() {
        return attRow; // 返回攻击的行数
    }

    /**
     * 设置攻击的行数。
     */
    public void setAttRow(int attRow) {
        this.attRow = attRow; // 设置攻击的行数
    }
}