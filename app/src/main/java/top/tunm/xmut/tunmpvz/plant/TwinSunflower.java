package top.tunm.xmut.tunmpvz.plant;

import org.cocos2d.actions.CCScheduler;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCJumpTo;
import org.cocos2d.actions.interval.CCSequence;

import top.tunm.xmut.tunmpvz.layer.CombatLayer;
import top.tunm.xmut.tunmpvz.bullet.Sun;

/**
 * 双生向日葵类，表示一种植物，能够生成阳光。
 */
public class TwinSunflower extends Plant {
    private Sun sun; // 阳光对象
    private int ang = 1; // 控制阳光的跳跃方向

    public TwinSunflower() {
        super("plant/TwinSunflower/Frame%02d.png", 20); // 加载双生向日葵的动画帧
        scheduleSunCreation(); // 安排阳光生成
        setPrice(150); // 设置植物的价格
    }

    /**
     * 安排阳光生成的定时任务
     */
    private void scheduleSunCreation() {
        CCScheduler.sharedScheduler().schedule("createSun", this, 10, false);
        CCScheduler.sharedScheduler().schedule("createSun", this, 10.1f, false);
    }

    /**
     * 创建阳光并设置其跳跃动画
     *
     * @param t 时间参数
     */
    public void createSun(float t) {
        ang = -ang; // 切换阳光的跳跃方向
        sun = new Sun(); // 创建阳光对象
        sun.setPosition(getPosition().x - 100, getPosition().y + 40); // 设置阳光初始位置
        getParent().getParent().addChild(sun); // 将阳光添加到场景中

        // 设置阳光的跳跃动画
        CCJumpTo jumpAction = CCJumpTo.action(0.5f,
                ccp(50 * ang + (getPosition().x - 100), getPosition().y), 40, 1);

        // 添加阳光到战斗层
        ((CombatLayer) getParent().getParent()).addSun(sun);

        // 设置阳光的消失延迟和回调
        CCDelayTime delayAction = CCDelayTime.action(5);
        CCCallFunc removeSunCall = CCCallFunc.action(this, "removeSun");
        CCSequence actionSequence = CCSequence.actions(jumpAction, delayAction, removeSunCall);
        sun.runAction(actionSequence); // 运行阳光的动作序列
    }

    /**
     * 移除阳光
     */
    public void removeSun() {
        ((CombatLayer) getParent().getParent()).removeSun(sun); // 从战斗层中移除阳光
        if (!sun.isNowCollect()) {
            sun.removeSelf(); // 如果阳光未被收集，则从场景中移除
        }
    }
}
/*
将阳光生成的定时任务安排逻辑提取到 scheduleSunCreation 方法中，使构造函数更简洁。
将阳光的创建逻辑和跳跃动画设置逻辑集中在 createSun 方法中，增强了代码的可读性。
 */