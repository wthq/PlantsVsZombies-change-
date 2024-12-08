package top.tunm.xmut.tunmpvz.zombies;

import org.cocos2d.nodes.CCNode;
import org.cocos2d.types.CGPoint;
import top.tunm.xmut.tunmpvz.effect.AEffect;

/**
 * 死亡效果工厂类
 */
public class DeathEffectFactory {
    public static void createDeathEffect(int dieState, CGPoint position, CCNode parent) {
        switch (dieState) {
            case 0:
                createDefaultDeathEffect(position, parent);
                break;
            case 1:
                createBurnedDeathEffect(position, parent);
                break;
            default:
                break;
        }
    }

    private static void createDefaultDeathEffect(CGPoint position, CCNode parent) {
        AEffect deathEffect = new AEffect("zombies/zombies_1/JokerZombie/die%02d.png", 9, 1.5f, 1.5f / 9f);
        deathEffect.setPosition(position);
        parent.addChild(deathEffect, 6);

        AEffect headEffect = new AEffect("zombies/zombies_1/NewspaperZombie/head%02d.png", 10, 1.2f, 0.12f);
        headEffect.setPosition(CGPoint.ccp(position.x + 20, position.y - 30));
        parent.addChild(headEffect, 6);
    }

    private static void createBurnedDeathEffect(CGPoint position, CCNode parent) {
        AEffect burnedEffect = new AEffect("zombies/zombies_1/BoomDie/Frame%02d.png", 20, 4, 0.2f);
        burnedEffect.setPosition(position);
        parent.addChild(burnedEffect, 6);
    }
}
/*
开闭原则:通过创建 DeathEffectFactory 类来生成死亡效果，我们可以在不修改 JokerZombie 类的情况下，添加新的死亡效果类型，只需在工厂类中添加逻辑即可。
 */