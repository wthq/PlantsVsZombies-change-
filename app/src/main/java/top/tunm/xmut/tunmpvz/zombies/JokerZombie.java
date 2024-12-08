package top.tunm.xmut.tunmpvz.zombies;

import org.cocos2d.actions.CCScheduler;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.types.CGPoint;

import java.util.Random;

import top.tunm.xmut.tunmpvz.ToolsSet;
import top.tunm.xmut.tunmpvz.effect.AEffect;
import top.tunm.xmut.tunmpvz.layer.CombatLayer;
import top.tunm.xmut.tunmpvz.plant.Plant;

/**
 * Created by jingyuyan on 2018/12/18.
 * 小丑僵尸类，继承自 Zombie 类。
 */
public class JokerZombie extends Zombie {

    private CGPoint cgPoint; // 存储小丑僵尸的位置

    // 构造函数
    public JokerZombie(CombatLayer combatLayer, CGPoint start, CGPoint end) {
        super(combatLayer, start, end); // 调用父类构造函数
        setMoveInt(ToolsSet.jokerZombieInt); // 设置移动动画
        setMove(ToolsSet.jokerZombieMove); // 设置移动速度
        setAttackInt(ToolsSet.jokerZombieAttackInt); // 设置攻击动画
        setAttacka(ToolsSet.jokerZombieAttack); // 设置攻击方式
        setHP(450); // 设置初始生命值
        setAttack(10); // 设置初始攻击力
        setSpeed(30); // 设置初始速度
        move(); // 开始移动
        CCScheduler.sharedScheduler().schedule("trigger", this, 0.5f, false); // 定时触发
    }

    // 小丑僵尸的炸弹效果
    public void bomb() {
        cgPoint = getPosition(); // 获取当前的位置
        createBombEffect(); // 创建炸弹效果
        createWocaoEffect(); // 创建特效

        // 延迟1.3秒后调用 boom 方法
        CCDelayTime ccDelayTime = CCDelayTime.action(1.3f);
        CCCallFunc ccCallFunc = CCCallFunc.action(this, "boom");
        CCSequence ccSequence = CCSequence.actions(ccDelayTime, ccCallFunc);
        runAction(ccSequence); // 执行动作序列
    }

    // 创建炸弹效果的方法
    private void createBombEffect() {
        AEffect bombEffect = new AEffect("zombies/zombies_1/JokerZombie/bomb%02d.png", 7, 1.5f, 1.5f / 7f);
        bombEffect.setPosition(cgPoint.x, cgPoint.y - 20); // 设置炸弹效果位置
        getParent().addChild(bombEffect, 6); // 添加到父节点
    }

    // 创建特效的方法
    private void createWocaoEffect() {
        AEffect wocaoEffect = new AEffect("eff/wocao/eff%02d.png", 4, 1.5f, 1.5f / 4f);
        wocaoEffect.setPosition(CGPoint.ccp(getPosition().x, getPosition().y + 50)); // 设置特效位置
        getParent().addChild(wocaoEffect, 6); // 添加到父节点
    }

    // 小丑僵尸的爆炸效果
    public void boom() {
        createExplosionEffect(); // 创建爆炸效果
        handleExplosionDamage(); // 处理爆炸造成的伤害
        setHP(0); // 设置生命值为0
        removeSelf(); // 移除自己
    }

    // 创建爆炸效果的方法
    private void createExplosionEffect() {
        AEffect explosionEffect = new AEffect("eff/bomb2/Frame%02d.png", 5, 0.8f, 0.8f / 5);
        explosionEffect.setPosition(cgPoint.x, cgPoint.y); // 设置爆炸效果位置
        explosionEffect.setScale(3); // 设置爆炸效果缩放
        getParent().addChild(explosionEffect, 6); // 添加到父节点
    }

    // 处理爆炸造成的伤害
    private void handleExplosionDamage() {
        int col = (int) (cgPoint.x - 220) / 105; // 计算列
        int row = (int) (cgPoint.y - 40) / 120; // 计算行
        int baseCol = col - 1; // 基准列
        int baseRow = row - 1; // 基准行

        // 遍历影响范围内的植物
        for (int c = baseCol; c < baseCol + 3; c++) {
            for (int r = baseRow; r < baseRow + 3; r++) {
                if (c < 9 && c >= 0 && r < 5 && r >= 0) { // 检查范围
                    if (ToolsSet.currtCombatLayer.getCombatLines().get(r).isContainPlant(c)) { // 检查是否包含植物
                        Plant plant = ToolsSet.currtCombatLayer.getCombatLines().get(r).getPlants().get(c);
                        if (plant != null) { // 如果植物存在
                            plant.setHP(0); // 设置植物生命值为0
                            ToolsSet.currtCombatLayer.getCombatLines().get(r).getPlants().remove(c); // 从列表中移除植物
                            plant.setRemove(true); // 标记为移除
                            plant.removeSelf(); // 移除植物
                        }
                    }
                }
            }
        }
    }

    // 定时触发的方法
    public void trigger(float t) {
        Random random = new Random();
        float dis = Math.abs(getEnd().x - getStart().x); // 计算总距离
        float currDis = Math.abs(getEnd().x - getPosition().x); // 计算当前距离
        float tiggerRate = currDis / dis; // 计算触发率
        float range = random.nextFloat(); // 生成随机数

        // 判断是否触发炸弹
        if ((1 - tiggerRate) >= range && (1 - tiggerRate) >= 0.2) {
            bomb(); // 触发炸弹
            CCScheduler.sharedScheduler().unschedule("trigger", this); // 取消调度
        }
    }

    @Override
    // 僵尸死亡效果
    public void death(int dieState) {
        DeathEffectFactory.createDeathEffect(dieState, getPosition(), getParent()); // 使用工厂创建死亡效果
    }
}
/*
 death效果的实现，从DeathEffectFactory中提取实现
将 bomb、createBombEffect 和 createWocaoEffect 等方法抽象出来，使代码更易于阅读和维护。
JokerZombie 类不再直接负责死亡效果的具体实现，遵循了单一职责原则.
 */