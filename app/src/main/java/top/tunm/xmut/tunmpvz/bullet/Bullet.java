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
 * 抽象类 Bullet，表示游戏中的子弹对象。
 */
public abstract class Bullet extends CCSprite {

    // 常量定义
    private static final float DEFAULT_SPEED = 300f; // 默认子弹速度
    private static final int DEFAULT_ATTACK = 20; // 默认子弹攻击力
    private static final float FIRE_SPEED = 250f; // 火焰子弹速度
    private static final int FIRE_ATTACK = 45; // 火焰子弹攻击力
    private static final String FIRE_BULLET_PATH = "bullet/FireButtle/bullet%02d.png"; // 火焰子弹图片路径
    private static final String NORMAL_BULLET_PATH = "bullet/PB11/bullet%02d.png"; // 普通子弹图片路径

    private float speed; // 子弹速度
    private int attack; // 子弹攻击力
    private ShooterPlant shooterPlant; // 发射子弹的植物
    private boolean isLeft; // 子弹是否向左移动
    private boolean isFire; // 子弹是否为火焰子弹

    // 构造方法，用于创建子弹对象
    public Bullet(String filepath, ShooterPlant shooterPlant) {
    }

    // 构造方法，用于创建子弹对象并指定是否向左移动
    public Bullet(String path, ShooterPlant shooterPlant, Boolean isLeft) {
    }

    /**
     * Builder 类，用于构建 Bullet 对象。
     */
    public static abstract class Builder<T extends Bullet> {
        protected float speed = DEFAULT_SPEED; // 子弹速度，默认值为 DEFAULT_SPEED
        protected int attack = DEFAULT_ATTACK; // 子弹攻击力，默认值为 DEFAULT_ATTACK
        protected ShooterPlant shooterPlant; // 发射子弹的植物
        protected boolean isLeft; // 子弹是否向左移动
        protected boolean isFire = false; // 子弹是否为火焰子弹，默认为 false
        protected String filepath; // 子弹图片路径

        // 设置子弹速度
        public Builder<T> setSpeed(float speed) {
            this.speed = speed;
            return this;
        }

        // 设置子弹攻击力
        public Builder<T> setAttack(int attack) {
            this.attack = attack;
            return this;
        }

        // 设置发射子弹的植物
        public Builder<T> setShooterPlant(ShooterPlant shooterPlant) {
            this.shooterPlant = shooterPlant;
            return this;
        }

        // 设置子弹是否向左移动
        public Builder<T> setLeft(boolean isLeft) {
            this.isLeft = isLeft;
            return this;
        }

        // 设置子弹是否为火焰子弹
        public Builder<T> setFire(boolean isFire) {
            this.isFire = isFire;
            return this;
        }

        // 设置子弹图片路径
        public Builder<T> setFilepath(String filepath) {
            this.filepath = filepath;
            return this;
        }

        // 构建 Bullet 对象
        public abstract T build();

        // 设置攻击行数（具体实现由子类提供）
        public abstract ThreeBullet.Builder setAttRow(int row);
    }

    /**
     * 私有构造方法，使用 Builder 构建 Bullet 对象。
     */
    protected Bullet(Builder<?> builder) {
        super(builder.filepath); // 调用父类构造方法，初始化子弹图片
        this.speed = builder.speed; // 设置子弹速度
        this.attack = builder.attack; // 设置子弹攻击力
        this.shooterPlant = builder.shooterPlant; // 设置发射子弹的植物
        this.isLeft = builder.isLeft; // 设置子弹是否向左移动
        this.isFire = builder.isFire; // 设置子弹是否为火焰子弹
        setPosition(shooterPlant.getPosition().x + 20, shooterPlant.getPosition().y + 50); // 设置子弹初始位置
        shooterPlant.getParent().addChild(this, 6); // 将子弹添加到父节点
        shooterPlant.getBullets().add(this); // 将子弹添加到植物的子弹列表中
        move(); // 开始子弹移动
        if (isFire) {
            fire(); // 如果是火焰子弹，加载火焰子弹逻辑
        }
    }

    /**
     * 加载火焰弹的动画帧。
     */
    private void loadAnimationFrames() {
        String path;
        if (isLeft) {
            path = FIRE_BULLET_PATH; // 如果子弹向左移动，使用火焰子弹路径
        } else {
            path = NORMAL_BULLET_PATH; // 否则使用普通子弹路径
        }
        ArrayList<CCSpriteFrame> frames = new ArrayList<>(); // 创建帧列表
        for (int i = 0; i < 2; i++) {
            CCSpriteFrame frame = CCSprite.sprite(String.format(Locale.CHINA, path, i)).displayedFrame(); // 加载帧
            frames.add(frame); // 将帧添加到列表中
        }
        CCAnimation animation = CCAnimation.animationWithFrames(frames, 0.1f); // 创建动画
        CCAnimate animate = CCAnimate.action(animation, true); // 创建动画动作
        CCRepeatForever repeat = CCRepeatForever.action(animate); // 创建无限重复的动画
        runAction(repeat); // 运行动画
    }

    /**
     * 火焰弹的逻辑。
     */
    public void fire() {
        isFire = true; // 设置子弹为火焰子弹
        attack = FIRE_ATTACK; // 设置火焰子弹的攻击力
        speed = FIRE_SPEED; // 设置火焰子弹的速度
        loadAnimationFrames(); // 加载火焰子弹的动画帧
    }

    /**
     * 移动子弹。
     */
    public void move() {
        CGPoint end = isLeft ? CGPoint.ccp(-1400, getPosition().y) : CGPoint.ccp(1400, getPosition().y); // 计算子弹的目标位置
        float t = CGPointUtil.distance(getPosition(), end) / speed; // 计算子弹到达目标位置所需的时间
        CCMoveTo moveAction = CCMoveTo.action(t, end); // 创建移动动作
        CCCallFunc endAction = CCCallFunc.action(this, "end"); // 创建结束动作
        CCSequence sequence = CCSequence.actions(moveAction, endAction); // 创建动作序列
        runAction(sequence); // 运行动作序列
    }

    /**
     * 子弹结束时的逻辑。
     */
    public void end() {
        removeSelf(); // 从场景中移除子弹
        shooterPlant.getBullets().remove(this); // 从植物的子弹列表中移除子弹
    }

    /**
     * 获取子弹的攻击力。
     */
    public int getAttack() {
        return attack;
    }

    /**
     * 设置子弹的攻击力。
     */
    public void setAttack(int attack) {
        this.attack = attack;
    }

    /**
     * 获取子弹的速度。
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * 检查子弹是否为火焰弹。
     */
    public boolean isFire() {
        return isFire;
    }

    /**
     * 显示子弹爆炸效果的抽象方法。
     */
    public abstract void showBulletBlast(Zombie zombie);
}