package top.tunm.xmut.tunmpvz.zombies;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.types.CGPoint;

import java.util.ArrayList;
import java.util.Locale;

import top.tunm.xmut.tunmpvz.ToolsSet;
import top.tunm.xmut.tunmpvz.effect.AEffect;
import top.tunm.xmut.tunmpvz.layer.CombatLayer;

/**
 * 读报僵尸类，继承自 Zombie 类。
 */
public class NewspaperZombie extends Zombie {

    private ZombieState state; // 当前状态

    public NewspaperZombie(CombatLayer combatLayer, CGPoint start, CGPoint end) {
        super(combatLayer, start, end);
        state = new NormalState(this); // 初始化为普通状态
        initializeZombie(); // 初始化僵尸的属性和状态
    }

    // 初始化僵尸属性的方法
    private void initializeZombie() {
        state.initialize(); // 让当前状态初始化僵尸属性
        move(); // 开始移动
    }

    @Override
    public void hurtCompute(int hurt) {
        setHP(Math.max(getHP() - hurt, 0)); // 确保HP不低于0
        state.handleHurt(); // 让当前状态处理伤害
    }

    @Override
    public void death(int dieState) {
        state.handleDeath(dieState); // 让当前状态处理死亡
    }

    // 状态接口
    interface ZombieState {
        void initialize();
        void handleHurt();
        void handleDeath(int dieState);
    }

    // 普通状态类
    class NormalState implements ZombieState {
        private NewspaperZombie zombie;

        NormalState(NewspaperZombie zombie) {
            this.zombie = zombie;
        }

        @Override
        public void initialize() {
            zombie.setMoveInt(ToolsSet.newspaperZombieIntB);
            zombie.setMove(ToolsSet.newspaperZombieMoveB);
            zombie.setAttackInt(ToolsSet.newspaperZombieAttackIntB);
            zombie.setAttacka(ToolsSet.newspaperZombieAttackB);
            zombie.setHP(500);
            zombie.setAttack(15);
            zombie.setSpeed(15);
        }

        @Override
        public void handleHurt() {
            if (zombie.getHP() <= 200) {
                zombie.state = new LastState(zombie); // 转变为最后状态
                zombie.state.initialize(); // 初始化最后状态
            }
        }

        @Override
        public void handleDeath(int dieState) {
            // 处理死亡效果
            switch (dieState) {
                case 0:
                    createDeathEffect("zombies/zombies_1/NewspaperZombie/die%02d.png", 11, 2, 0.181f);
                    createDeathEffect("zombies/zombies_1/NewspaperZombie/head%02d.png", 10, 1.2f, 0.12f, CGPoint.ccp(zombie.getPosition().x + 20, zombie.getPosition().y - 30));
                    break;
                case 1:
                    createDeathEffect("zombies/zombies_1/BoomDie/Frame%02d.png", 20, 4, 0.2f);
                    break;
                default:
                    break;
            }
        }

        // 创建死亡效果
        private void createDeathEffect(String path, int frameCount, float duration, float delay) {
            AEffect effect = new AEffect(path, frameCount, duration, delay);
            effect.setPosition(zombie.getPosition());
            zombie.getParent().addChild(effect, 6);
        }

        private void createDeathEffect(String path, int frameCount, float duration, float delay, CGPoint position) {
            AEffect effect = new AEffect(path, frameCount, duration, delay);
            effect.setPosition(position);
            zombie.getParent().addChild(effect, 6);
        }
    }

    // 最后阶段状态类
    class LastState implements ZombieState {
        private NewspaperZombie zombie;

        LastState(NewspaperZombie zombie) {
            this.zombie = zombie;
        }

        @Override
        public void initialize() {
            ArrayList<CCSpriteFrame> frames = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                CCSpriteFrame ccSpriteFrame = CCSprite.sprite(String.format(Locale.CHINA,
                        "zombies/zombies_1/NewspaperZombie/last%02d.png", i)).displayedFrame();
                frames.add(ccSpriteFrame);
            }

            zombie.setMoveInt(ToolsSet.newspaperZombieIntA);
            zombie.setMove(ToolsSet.newspaperZombieMoveA);
            zombie.setAttackInt(ToolsSet.newspaperZombieAttackIntA);
            zombie.setAttacka(ToolsSet.newspaperZombieAttackA);
            zombie.setSpeed(15); // 保持速度

            CCAnimation ccAnimation = CCAnimation.animationWithFrames(frames, 0.1f);
            CCAnimate ccAnimate = CCAnimate.action(ccAnimation, true);
            CCRepeatForever ccRepeatForever = CCRepeatForever.action(ccAnimate);
            zombie.runAction(ccRepeatForever);

            CCDelayTime ccDelayTime = CCDelayTime.action(1);
            CCCallFunc ccCallFunc = CCCallFunc.action(zombie, "move");
            CCSequence ccSequence = CCSequence.actions(ccDelayTime, ccCallFunc);
            zombie.runAction(ccSequence);
        }

        @Override
        public void handleHurt() {
            // 最后阶段可以添加其他逻辑
        }

        @Override
        public void handleDeath(int dieState) {
            // 处理死亡效果
            switch (dieState) {
                case 0:
                    createDeathEffect("zombies/zombies_1/NewspaperZombie/die%02d.png", 11, 2, 0.181f);
                    createDeathEffect("zombies/zombies_1/NewspaperZombie/head%02d.png", 10, 1.2f, 0.12f, CGPoint.ccp(zombie.getPosition().x + 20, zombie.getPosition().y - 30));
                    break;
                case 1:
                    createDeathEffect("zombies/zombies_1/BoomDie/Frame%02d.png", 20, 4, 0.2f);
                    break;
                default:
                    break;
            }
        }

        // 创建死亡效果
        private void createDeathEffect(String path, int frameCount, float duration, float delay) {
            AEffect effect = new AEffect(path, frameCount, duration, delay);
            effect.setPosition(zombie.getPosition());
            zombie.getParent().addChild(effect, 6);
        }

        private void createDeathEffect(String path, int frameCount, float duration, float delay, CGPoint position) {
            AEffect effect = new AEffect(path, frameCount, duration, delay);
            effect.setPosition(position);
            zombie.getParent().addChild(effect, 6);
        }
    }
}
/*
引入了 ZombieState 接口和两个实现类（NormalState 和 LastState），以管理僵尸的不同状态。这样，状态的逻辑被封装在各自的类中，减少了 NewspaperZombie 类的复杂性。
NewspaperZombie 类只负责管理状态和初始化，遵循单一责任原则。
 */