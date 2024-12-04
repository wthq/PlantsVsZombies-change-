// 玉米投手
package top.tunm.xmut.tunmpvz.plant;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;

import java.util.Random;

import top.tunm.xmut.tunmpvz.R;
import top.tunm.xmut.tunmpvz.ToolsSet;
import top.tunm.xmut.tunmpvz.effect.AEffect;

/**
 * 玉米投手，可以发射玉米子弹。
 * Created by jingyuyan on 2018/12/17.
 */
public class KernelPult extends PitcherPlant {

    // 攻击状态
    private boolean isYellow;

    // 常量定义
    private static final int ATTACK_FRAMES = 4;
    private static final int EFFECT_FRAMES = 9;
    private static final float EFFECT_DURATION = 0.1f;
    private static final float ATTACK_END_DELAY = 7f;

    /**
     * 构造函数，初始化玉米投手的属性。
     */
    public KernelPult() {
        super("plant/KernelPult/Frame%02d.png", 9,
                "plant/KernelPult/Attack%02d.png", ATTACK_FRAMES,
                "bullet/kernelBullet.png");
        setPrice(100);
    }

    /**
     * 攻击结束后的处理逻辑。
     */
    @Override
    public void launchEnd() {
        // 移除子弹
        getBullet().removeSelf();

        // 设置攻击结束后的延迟动作
        CCDelayTime delay = CCDelayTime.action(ATTACK_END_DELAY);
        CCCallFunc callFunc = CCCallFunc.action(this, "noAtt");
        CCSequence sequence = CCSequence.actions(delay, callFunc);
        runAction(sequence);

        // 创建爆炸特效
        AEffect explosionEffect = new AEffect("eff/boom3/effect_f01_%02d.png", EFFECT_FRAMES, EFFECT_DURATION, EFFECT_DURATION);
        explosionEffect.setPosition(getIntentPoint());
        getParent().addChild(explosionEffect, 6);

        // 播放爆炸音效
        ToolsSet.effectSound(R.raw.bomb1);

        // 如果僵尸存活且处于黄色攻击状态，则执行额外伤害逻辑
        if (getZombie().getHP() != 0 && isYellow) {
            setHurt(80);
            getZombie().stop("bullet/yelloed.png", ATTACK_FRAMES);
        }

        // 计算僵尸受到的伤害
        getZombie().hurtCompute(getHurt());

        // 如果僵尸死亡，则移除僵尸
        if (getZombie().getHP() == 0) {
            getZombie().death(0);
            getZombie().removeSelf();
        }
    }

    /**
     * 重置攻击状态。
     */
    @Override
    public void noAtt() {
        isYellow = false;
        setNoAttack(false);
    }

    /**
     * 判断当前是否处于黄色攻击状态。
     *
     * @return boolean 是否黄色攻击状态
     */
    public boolean isYellow() {
        return isYellow;
    }

    /**
     * 设置黄色攻击状态。
     *
     * @param yellow 是否黄色攻击状态
     */
    public void setYellow(boolean yellow) {
        isYellow = yellow;
    }
}
/*
使用常量或枚举来代替代码中的硬编码数值（如图片路径、帧数等），提高代码的可读性和可维护性。
将攻击结束后的逻辑（如移除子弹、播放特效、伤害计算等）封装到单独的方法中，使 launchEnd 方法更加简洁。
优化 noAtt 方法:确保 noAtt 方法只负责状态重置，避免在状态重置后执行其他逻辑。
将资源路径、帧数等硬编码值提取到常量或枚举中，方便管理和修改。
 */