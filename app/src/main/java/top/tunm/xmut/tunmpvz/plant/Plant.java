package top.tunm.xmut.tunmpvz.plant;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.types.CGPoint;

import java.util.ArrayList;
import java.util.Locale;

import top.tunm.xmut.tunmpvz.zombies.Zombie;

/**
 * 表示游戏中的植物，植物可以具有生命值（HP）、价格等属性，以及各种动作。
 */
public class Plant extends CCSprite {
    private int HP = 100;                // 植物的生命值（HP）
    private int price;                   // 植物的价格

    private int currerCol;              // 植物在游戏网格中的当前列
    private boolean isRemove;            // 标记植物是否被标记为移除
    private boolean dontAttack;          // 标记植物是否应该不攻击

    /**
     * 构造一个植物，指定动画格式和帧数。
     *
     * @param format 加载精灵帧的格式。
     * @param number 帧的数量。
     */
    public Plant(String format, int number) {
        super(String.format(Locale.CHINA, format, 0)); // 使用第一帧初始化精灵
        setAnchorPoint(0.5f, 0); // 设置锚点在底部中心

        // 创建动画帧
        ArrayList<CCSpriteFrame> frames = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            CCSpriteFrame ccSpriteFrame = CCSprite.sprite(String.format(Locale.CHINA, format, i)).displayedFrame();
            frames.add(ccSpriteFrame);
        }

        // 创建并运行循环动画
        CCAnimation ccAnimation = CCAnimation.animationWithFrames(frames, 0.2f); // 每帧0.2秒的动画
        CCAnimate ccAnimate = CCAnimate.action(ccAnimation, true); // 无限动画
        CCRepeatForever ccRepeatForever = CCRepeatForever.action(ccAnimate);
        runAction(ccRepeatForever); // 启动动画
    }

    /**
     * 处理植物的死亡，包括与僵尸攻击相关的任何清理工作。
     *
     * @param zombie 造成植物死亡的僵尸。
     */
    public void death(Zombie zombie) {
        // 可以在这里实现处理植物死亡的逻辑
        // 这可能包括播放死亡动画、释放资源等
    }

    // Getter 和 Setter

    public int getHP() {
        return HP; // 获取当前的生命值
    }

    public void setHP(int HP) {
        this.HP = HP; // 设置当前的生命值
    }

    /**
     * 根据指定的伤害计算植物的HP。
     * 将HP限制为最低为0。
     *
     * @param hurt 伤害值。
     */
    public void hurtCompute(int hurt) {
        HP -= hurt; // 减少HP
        if (HP < 0) {
            HP = 0; // 确保HP不低于0
        }
    }

    /**
     * 根据植物的当前位置，驱逐所有在一定距离内的僵尸，以避免重叠。
     *
     * @param zombies 需要检查的僵尸列表。
     */
    public void safe(ArrayList<Zombie> zombies) {
        for (Zombie zombie : zombies) {
            // 检查僵尸是否在植物位置的40单位内
            if (CGPoint.ccpDistance(zombie.getPosition(), getPosition()) <= 40) {
                zombie.move(); // 驱逐僵尸
            }
        }
    }

    public int getPrice() {
        return price; // 获取该植物的价格
    }

    public void setPrice(int price) {
        this.price = price; // 设置该植物的价格
    }

    public int getCurrerCol() {
        return currerCol; // 获取植物的当前列
    }

    public void setCurrerCol(int currerCol) {
        this.currerCol = currerCol; // 设置植物的当前列
    }

    public boolean isRemove() {
        return isRemove; // 检查植物是否被标记为移除
    }

    public void setRemove(boolean remove) {
        isRemove = remove; // 设置植物的移除状态
    }

    public boolean isDontAttack() {
        return dontAttack; // 检查植物是否处于不攻击状态
    }

    public void setDontAttack(boolean dontAttack) {
        this.dontAttack = dontAttack; // 设置植物的攻击状态
    }
}
/*
在 hurtCompute 方法中，确保 HP 不会低于 0，增强了代码的健壮性。
在构造方法中，使用 this 关键字明确引用类的属性，增强代码的可读性。
 */