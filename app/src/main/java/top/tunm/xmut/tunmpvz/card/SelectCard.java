package top.tunm.xmut.tunmpvz.card;

import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.ccColor3B;

import top.tunm.xmut.tunmpvz.ToolsSet;

/**
 * Created by jingyuyan on 2018/12/12.
 */
public class SelectCard {

    private int id;
    private CCSprite back;
    private CCLabel checkNum;

    /**
     * 构造函数，根据传入的id初始化SelectCard对象相关属性
     *
     * @param id 卡片的标识id
     */
    public SelectCard(int id) {
        this.id = id;
        // 根据是否为夜晚模式来选择卡片背面对应的图片资源创建CCSprite对象
        back = ToolsSet.isIsNight()? CCSprite.sprite("interface/check.png") : CCSprite.sprite("interface/check0.png");
        // 创建显示序号的CCLabel对象，设置相应文本、字体和字号
        checkNum = CCLabel.makeLabel((id + 1) + "", "hkbd.ttf", 25);
        // 设置卡片背面精灵的缩放比例
        back.setScale(2f);
        // 设置序号标签的颜色为黑色
        checkNum.setColor(ccColor3B.ccBLACK);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CCSprite getBack() {
        return back;
    }

    public void setBack(CCSprite back) {
        this.back = back;
    }

    public CCLabel getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(CCLabel checkNum) {
        this.checkNum = checkNum;
    }
}