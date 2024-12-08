package top.tunm.xmut.tunmpvz.effect;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.types.CGPoint;

/**
 * AEffect类继承自Effect类，主要用于实现特定的效果相关逻辑，
 * 例如设置移除时间、执行移除相关的动作序列等功能。
 * 该类有多个构造函数以适应不同的初始化场景，并且包含了与效果移除相关的方法。
 * Created by jingyuyan on 2018/12/5.
 */
public class AEffect extends Effect {

    // 记录效果的移除时间，默认值为1，单位可能根据具体业务场景而定（比如秒等）
    private float removetime = 1;

    /**
     * 默认构造函数，调用父类构造函数初始化资源文件路径和图片帧数，
     * 并设置价格为0。资源文件路径默认使用"eff/Aeff/Frame%02d.png"，图片帧数为5。
     */
    public AEffect() {
        super("eff/Aeff/Frame%02d.png", 5);
        setPrice(0);
    }

    /**
     * 构造函数，根据传入的文件路径和图片帧数调用父类构造函数进行初始化，
     * 然后执行移除相关操作，并设置价格为0。
     *
     * @param filepath 效果对应的资源文件路径
     * @param i        效果对应的图片帧数
     */
    public AEffect(String filepath, int i) {
        super(filepath, i);
        removemine();
        setPrice(0);
    }

    /**
     * 构造函数，根据传入的文件路径、图片帧数和移除时间调用父类构造函数进行初始化，
     * 设置移除时间后执行移除相关操作，并设置价格为0。
     *
     * @param filepath  效果对应的资源文件路径
     * @param i         效果对应的图片帧数
     * @param s         效果的移除时间
     */
    public AEffect(String filepath, int i, int s) {
        super(filepath, i);
        removetime = s;
        removemine();
        setPrice(0);
    }

    /**
     * 构造函数，根据传入的文件路径、图片帧数、移除时间和延迟时间调用父类构造函数进行初始化，
     * 设置移除时间后执行移除相关操作，并设置价格为0。
     *
     * @param filepath  效果对应的资源文件路径
     * @param i         效果对应的图片帧数
     * @param s         效果的移除时间
     * @param delay     效果的延迟时间（具体作用根据业务逻辑而定）
     */
    public AEffect(String filepath, int i, int s, float delay) {
        super(filepath, i, delay);
        removetime = s;
        removemine();
        setPrice(0);
    }

    /**
     * 构造函数，根据传入的文件路径、图片帧数、移除时间（以浮点数形式传入）和延迟时间调用父类构造函数进行初始化，
     * 设置移除时间后执行移除相关操作，并设置价格为0。
     *
     * @param filepath  效果对应的资源文件路径
     * @param i         效果对应的图片帧数
     * @param s         效果的移除时间（以浮点数形式传入）
     * @param delay     效果的延迟时间（具体作用根据业务逻辑而定）
     */
    public AEffect(String filepath, int i, float s, float delay) {
        super(filepath, i, delay);
        removetime = s;
        removemine();
        setPrice(0);
    }

    /**
     * 执行移除相关的操作，创建一个延迟动作（根据removetime设置延迟时间）和一个调用remove方法的回调动作，
     * 将这两个动作组成一个动作序列，并让当前对象执行这个动作序列，以实现延迟后执行移除的效果。
     * 此外，原代码中存在一个被注释掉的调度相关代码（可能与定时攻击植物相关，具体需结合业务逻辑确定），
     * 此处暂未对其做进一步处理，若后续有需求可根据实际情况完善或移除。
     */
    public void removemine() {
        CCDelayTime ccDelayTime = CCDelayTime.action(removetime);
        CCCallFunc ccCallFunc = CCCallFunc.action(this, "remove");
        CCSequence ccSequence = CCSequence.actions(ccDelayTime, ccCallFunc);
        runAction(ccSequence);

//        CCScheduler.sharedScheduler().schedule("attackPlant",this,1,
//                false);
    }

    /**
     * 实际执行移除自身的操作，具体的移除逻辑应该是在父类或相关类中定义，此处调用相应的方法来完成移除动作。
     */
    public void remove() {
        removeSelf();
    }
}