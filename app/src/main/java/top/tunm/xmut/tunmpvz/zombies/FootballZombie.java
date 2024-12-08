package top.tunm.xmut.tunmpvz.zombies;

import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

import top.tunm.xmut.tunmpvz.ToolsSet;
import top.tunm.xmut.tunmpvz.effect.AEffect;
import top.tunm.xmut.tunmpvz.layer.CombatLayer;

/**
 * Created by jingyuyan on 2018/12/16.
 * 橄榄球僵尸类，继承自 Zombie 类。
 */
public class FootballZombie extends Zombie {
    private boolean isLast; // 标记是否为最后阶段

    // 构造函数
    public FootballZombie(CombatLayer combatLayer, CGPoint start, CGPoint end) {
        super(combatLayer, start, end); // 调用父类构造函数
        initializeZombie(); // 初始化僵尸的属性和状态
    }

    // 初始化僵尸属性的方法
    private void initializeZombie() {
        setMoveInt(ToolsSet.footballZombieIntA); // 设置初始移动动画
        setMove(ToolsSet.footballZombieMoveA);   // 设置初始移动速度
        setAttackInt(ToolsSet.footballZombieAttackIntA); // 设置初始攻击动画
        setAttacka(ToolsSet.footballZombieAttackA); // 设置初始攻击方式
        setHP(600);   // 设置初始生命值
        setAttack(10); // 设置初始攻击力
        setSpeed(30);  // 设置初始速度
        move(); // 开始移动
    }

    @Override
    public void hurtCompute(int hurt) {
        // 计算受到的伤害并更新生命值
        setHP(Math.max(getHP() - hurt, 0)); // 确保HP不低于0
        // 检查生命值，如果低于200并且不是最后阶段，则转变状态
        if (getHP() <= 200 && !isLast) {
            transformZombie(); // 转变僵尸状态
        }
    }

    // 转变僵尸状态的方法
    private void transformZombie() {
        isLast = true; // 设置为最后阶段
        setMoveInt(ToolsSet.footballZombieIntB); // 设置转变后的移动动画
        setMove(ToolsSet.footballZombieMoveB);   // 设置转变后的移动速度
        setAttackInt(ToolsSet.footballZombieAttackIntB); // 设置转变后的攻击动画
        setAttacka(ToolsSet.footballZombieAttackB); // 设置转变后的攻击方式
        setSpeed(15); // 设置转变后的速度
        move(); // 开始新的移动
    }

    @Override
    public void death(int dieState) {
        // 根据死亡状态执行不同的死亡效果
        switch (dieState) {
            case 0: // 默认死亡效果
                handleDefaultDeath(); // 处理默认死亡效果
                break;
            case 1: // 被烧焦的效果
                handleBurnedDeath(); // 处理烧焦死亡效果
                break;
            default:
                break;
        }
    }

    // 处理默认死亡效果的方法
    private void handleDefaultDeath() {
        // 创建默认死亡效果
        createEffect("zombies/zombies_1/FootballZombie/die%02d.png", 7, 2, 0.181f, getPosition());
        // 创建头部掉落的效果
        createEffect("zombies/zombies_1/NewspaperZombie/head%02d.png", 10, 1.2f, 0.12f,
                CGPoint.ccp(getPosition().x + 20, getPosition().y - 30));
    }

    // 处理被烧焦的死亡效果的方法
    private void handleBurnedDeath() {
        // 创建被烧焦的死亡效果
        createEffect("zombies/zombies_1/BoomDie/Frame%02d.png", 20, 4, 0.2f, getPosition());
    }

    // 创建效果的通用方法
    private void createEffect(String path, int frameCount, float duration, float delay, CGPoint position) {
        AEffect effect = new AEffect(path, frameCount, duration, delay); // 创建效果对象
        effect.setPosition(position); // 设置效果的位置
        getParent().addChild(effect, 6); // 将效果添加到当前层级
    }
}
/*
 从FootballZombie提取initializeZombie() 方法初始化僵尸，以提高可读性和重用性。
 在 hurtCompute 方法中使用 Math.max 来确保 HP 不低于 0，使代码更加简洁。
 从hurtCompute提取transformZombie()方法变身逻辑，以提高可读性。
 death分为 handleDefaultDeath() 和 handleBurnedDeath() 方法，分别处理不同的死亡状态,提取 createEffect() 方法用于创建效果.
 */