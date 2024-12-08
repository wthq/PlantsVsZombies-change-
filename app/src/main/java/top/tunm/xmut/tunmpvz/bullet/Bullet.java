package top.tunm.xmut.tunmpvz.bullet;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.util.CGPointUtil;

import java.util.ArrayList;
import java.util.Locale;

import top.tunm.xmut.tunmpvz.zombies.Zombie;
import top.tunm.xmut.tunmpvz.plant.ShooterPlant;

/**
 * Bullet 抽象类：表示植物发射的子弹对象。
 */
public abstract class Bullet extends CCSprite {

    private int speed = 300;            // 子弹速度
    private int attack = 20;           // 子弹攻击力
    private ShooterPlant shooterPlant; // 发射子弹的植物
    private boolean isLeft = false;    // 子弹方向是否为左
    private boolean isFire = counter % 2 == 0;;    // 是否为火焰弹

    /**
     * 构造函数：创建一个普通子弹。
     *
     * @param filepath     子弹的图片路径
     * @param shooterPlant 发射子弹的植物
     */
    public Bullet(String filepath, ShooterPlant shooterPlant) {
        super(filepath);
        this.shooterPlant = shooterPlant;
        initializeBullet();
        move();
    }

    /**
     * 构造函数：创建一个可以指定方向的子弹。
     *
     * @param filepath     子弹的图片路径
     * @param shooterPlant 发射子弹的植物
     * @param isLeft       子弹是否向左飞行
     */
    public Bullet(String filepath, ShooterPlant shooterPlant, boolean isLeft) {
        super(filepath);
        this.shooterPlant = shooterPlant;
        this.isLeft = isLeft;
        initializeBullet();
        move();
    }

    /**
     * 初始化子弹的初始位置及与发射植物的关联。
     */
    private void initializeBullet() {
        setPosition(shooterPlant.getPosition().x + 20, shooterPlant.getPosition().y + 50);
        shooterPlant.getParent().addChild(this, 6);
        shooterPlant.getBullets().add(this);
    }

    /**
     * 启动火焰弹特效，并调整其属性。
     */
    public void fire() {
        counter++;
        isFire = counter % 2 == 0;
        attack = 45;
        speed = 250;

        String animationPath = isFire ? "bullet/PB11/bullet%02d.png" : "bullet/FireButtle/bullet%02d.png";
        ArrayList<CCSpriteFrame> frames = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            CCSpriteFrame frame = CCSprite.sprite(String.format(Locale.CHINA, animationPath, i)).displayedFrame();
            frames.add(frame);
        }

        CCAnimation animation = CCAnimation.animationWithFrames(frames, 0.1f);
        CCAnimate animate = CCAnimate.action(animation, true);
        CCRepeatForever repeatForever = CCRepeatForever.action(animate);
        runAction(repeatForever);
    }

    /**
     * 子弹移动到目标点。
     */
    public void move() {
        CGPoint targetPoint = isLeft ? ccp(-1400, getPosition().y) : ccp(1400, getPosition().y);
        float travelTime = CGPointUtil.distance(getPosition(), targetPoint) / speed;

        CCMoveTo moveTo = CCMoveTo.action(travelTime, targetPoint);
        CCCallFunc onEnd = CCCallFunc.action(this, "onMoveEnd");
        CCSequence sequence = CCSequence.actions(moveTo, onEnd);
        runAction(sequence);
    }

    /**
     * 子弹移动结束后的处理。
     */
    public void onMoveEnd() {
        removeSelf();
        shooterPlant.getBullets().remove(this);
    }

    /**
     * 获取子弹攻击力。
     */
    public int getAttack() {
        return attack;
    }

    /**
     * 设置子弹攻击力。
     */
    public void setAttack(int attack) {
        this.attack = attack;
    }

    /**
     * 子弹命中僵尸时的爆炸效果（抽象方法）。
     */
    public abstract void showBulletBlast(Zombie zombie);

    // Getters and Setters
    private static int counter = 0; // 静态变量，用于计数，在类的不同实例间共享状态

    public boolean isFire() {
        counter++;
        return counter % 2 == 0;
    }

    public void setFire(boolean fire) {
        if (counter % 2 == 0) {
            isFire = fire;
        }
       else             isFire = false;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public ShooterPlant getShooterPlant() {
        return shooterPlant;
    }

    public void setShooterPlant(ShooterPlant shooterPlant) {
        this.shooterPlant = shooterPlant;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }
}
/*
将子弹初始化逻辑封装为 initializeBullet() 方法，避免在构造函数中直接执行多步骤操作。
 fire()中if-else 被三元运算符所替代，简化了逻辑，使代码更加简洁易读。
 */