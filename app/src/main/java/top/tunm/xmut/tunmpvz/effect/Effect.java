package top.tunm.xmut.tunmpvz.effect;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Effect类继承自CCSprite，用于创建具有特定动画效果以及包含生命值（HP）和价格（price）属性的对象。
 * 此类提供了不同参数的构造函数用于初始化动画相关资源，并可以对生命值进行计算，获取和设置价格等操作。
 */
public class Effect extends CCSprite {
    // 表示对象的生命值，初始值为100
    private int HP = 100;
    // 表示对象的价格
    private int price;

    /**
     * 构造函数，根据给定的图片资源格式和图片帧数来创建动画效果并应用到当前对象。
     * 首先调用父类构造函数设置初始显示的图片，然后设置锚点，接着循环加载每一帧图片资源创建帧列表，
     * 基于帧列表创建动画，再创建对应的动画动作并设置为循环播放，最后让当前对象执行这个循环动画动作。
     *
     * @param format  图片资源的格式化字符串，用于生成每一帧对应的图片资源路径，例如 "eff/Aeff/Frame%02d.png"
     * @param number  动画的图片帧数，决定了动画循环播放时包含的图片数量
     */
    public Effect(String format, int number) {
        super(String.format(Locale.CHINA, format, 0));
        setAnchorPoint(0.5f, 0);
        ArrayList<CCSpriteFrame> frames = createSpriteFrames(format, number);
        CCAnimation ccAnimation = createAnimation(frames, 0.2f);
        CCAnimate ccAnimate = CCAnimate.action(ccAnimation, false);
        CCRepeatForever ccRepeatForever = CCRepeatForever.action(ccAnimate);
        runAction(ccRepeatForever);
    }

    /**
     * 构造函数，与上一个构造函数类似，不过此构造函数多了一个延迟时间（delay）参数，
     * 用于在创建动画时设置每帧之间的时间间隔，从而控制动画播放的速度。
     *
     * @param format  图片资源的格式化字符串，用于生成每一帧对应的图片资源路径，例如 "eff/Aeff/Frame%02d.png"
     * @param number  动画的图片帧数，决定了动画循环播放时包含的图片数量
     * @param delay   动画每帧之间的时间间隔，用于控制动画播放的速度，单位根据具体业务场景而定（比如秒等）
     */
    public Effect(String format, int number, float delay) {
        super(String.format(Locale.CHINA, format, 0));
        setAnchorPoint(0.5f, 0);
        ArrayList<CCSpriteFrame> frames = createSpriteFrames(format, number);
        CCAnimation ccAnimation = createAnimation(frames, delay);
        CCAnimate ccAnimate = CCAnimate.action(ccAnimation, false);
        CCRepeatForever ccRepeatForever = CCRepeatForever.action(ccAnimate);
        runAction(ccRepeatForever);
    }

    /**
     * 根据给定的图片资源格式和图片帧数创建对应的CCSpriteFrame列表，用于后续构建动画。
     *
     * @param format  图片资源的格式化字符串，用于生成每一帧对应的图片资源路径
     * @param number  动画的图片帧数，决定了需要创建的CCSpriteFrame数量
     * @return 包含所有根据格式和帧数生成的CCSpriteFrame对象的列表
     */
    private ArrayList<CCSpriteFrame> createSpriteFrames(String format, int number) {
        ArrayList<CCSpriteFrame> frames = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            CCSpriteFrame ccSpriteFrame = CCSprite.sprite(String.format(Locale.CHINA, format, i)).displayedFrame();
            frames.add(ccSpriteFrame);
        }
        return frames;
    }

    /**
     * 根据给定的CCSpriteFrame列表和每帧之间的延迟时间创建对应的CCAnimation对象，用于构建动画动作。
     *
     * @param frames 包含动画所有帧的CCSpriteFrame列表
     * @param delay  动画每帧之间的时间间隔，用于控制动画播放的速度
     * @return 基于传入的帧列表和延迟时间创建的CCAnimation对象
     */
    private CCAnimation createAnimation(ArrayList<CCSpriteFrame> frames, float delay) {
        return CCAnimation.animationWithFrames(frames, delay);
    }

    /**
     * 获取对象当前的生命值。
     *
     * @return 当前对象的生命值数值
     */
    public int getHP() {
        return HP;
    }

    /**
     * 设置对象的生命值，若传入的值小于0，则将生命值设置为0。
     *
     * @param HP 要设置的生命值数值
     */
    public void setHP(int HP) {
        this.HP = HP;
        if (this.HP < 0) {
            this.HP = 0;
        }
    }

    /**
     * 根据传入的伤害值对对象的生命值进行扣减计算，若扣减后生命值小于0，则将生命值设置为0。
     *
     * @param hurt 要对对象造成的伤害值
     */
    public void hurtCompute(int hurt) {
        HP -= hurt;
        if (HP < 0) {
            HP = 0;
        }
    }

    /**
     * 获取对象的价格。
     *
     * @return 当前对象的价格数值
     */
    public int getPrice() {
        return price;
    }

    /**
     * 设置对象的价格。
     *
     * @param price 要设置的对象价格数值
     */
    public void setPrice(int price) {
        this.price = price;
    }
}