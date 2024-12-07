package top.tunm.xmut.tunmpvz.plant;

import org.cocos2d.actions.interval.CCDelayTime;
import java.util.ArrayList;
import top.tunm.xmut.tunmpvz.effect.AEffect;
import top.tunm.xmut.tunmpvz.zombies.Zombie;

/**
 * Spikeweed class represents a plant that damages zombies on contact.  
 */
public class Spikeweed extends Plant {

    private ArrayList<Zombie> zombies; // List of zombies affected by the spikeweed  
    private int hurt; // Damage dealt to zombies  

    public Spikeweed() {
        this("plant/Spikeweed/Frame%02d.png", 18, 100, 20); // Default values  
    }

    public Spikeweed(String format, int number, int price, int hurt) {
        super(format, number);
        setDontAttack(true); // Spikeweed does not attack  
        setPrice(price);
        this.zombies = new ArrayList<>();
        this.hurt = hurt;
    }

    /**
     * Applies damage to the specified zombie if it is not already affected.  
     * @param zombie The zombie to be damaged.  
     */
    public void att(Zombie zombie) {
        if (!zombies.contains(zombie)) {
            applyDamage(zombie);
        }
    }

    // Helper method to apply damage and create effect  
    private void applyDamage(Zombie zombie) {
        // Create and position damage effect  
        AEffect damageEffect = new AEffect("eff/ci/eff%02d.png", 7, 0.7f, 0.1f);
        damageEffect.setPosition(ccp(zombie.getPosition().x, zombie.getPosition().y - 20));
        getParent().addChild(damageEffect, 6);

        // Track the zombie and apply damage  
        zombies.add(zombie);
        zombie.hurtCompute(hurt);

        // Check if the zombie is dead and handle its removal  
        if (zombie.getHP() <= 0) {
            zombie.death(0);
            zombie.removeSelf();
        }
    }

    public ArrayList<Zombie> getZombies() {
        return zombies;
    }

    public void setZombies(ArrayList<Zombie> zombies) {
        this.zombies = zombies;
    }

    public int getHurt() {
        return hurt;
    }

    public void setHurt(int hurt) {
        this.hurt = hurt;
    }
}
/*
public Spikeweed()修改为调用重载构造函数 this("plant/Spikeweed/Frame%02d.png", 18, 100, 20);，以减少代码重复。
提取伤害应用和效果创建逻辑到新方法 applyDamage(Zombie zombie) 中，使 att 方法只负责判断和调用，逻辑更清晰。
 在 att 方法中直接判断僵尸是否在 zombies 列表中
 */