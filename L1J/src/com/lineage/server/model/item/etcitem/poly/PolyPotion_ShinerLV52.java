package com.lineage.server.model.item.etcitem.poly;

import static com.lineage.server.model.skill.L1SkillId.AWAKEN_ANTHARAS;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_FAFURION;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_VALAKAS;

import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.ItemExecutor;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 夏纳的变身卷轴(52级) - 49151
 * 
 * @author jrwz
 */
public class PolyPotion_ShinerLV52 implements ItemExecutor {

    public static ItemExecutor get() {
        return new PolyPotion_ShinerLV52();
    }

    private PolyPotion_ShinerLV52() {
    }

    /**
     * 道具执行
     * 
     * @param data
     *            参数
     * @param pc
     *            对象
     * @param item
     *            道具
     */
    @Override
    public void execute(final int[] data, final L1PcInstance pc,
            final L1ItemInstance item) {

        // 不能变身的状态
        // 取回觉醒技能ID
        final int awakeSkillId = pc.getAwakeSkillId();
        if ((awakeSkillId == AWAKEN_ANTHARAS)
                || (awakeSkillId == AWAKEN_FAFURION)
                || (awakeSkillId == AWAKEN_VALAKAS)) {
            pc.sendPackets(new S_ServerMessage(1384)); // 目前状态中无法变身。
            return;
        }

        // 变身时间 (秒)与变身编号
        short polyId = 0;
        final short time = 1800;

        switch (pc.get_sex()) {
            case 0:
                if (pc.isCrown()) {
                    polyId = 6842;
                } else if (pc.isKnight()) {
                    polyId = 6844;
                } else if (pc.isElf()) {
                    polyId = 6846;
                } else if (pc.isWizard()) {
                    polyId = 6848;
                } else if (pc.isDarkelf()) {
                    polyId = 6850;
                } else if (pc.isDragonKnight()) {
                    polyId = 7147;
                } else if (pc.isIllusionist()) {
                    polyId = 7149;
                }
                break;

            case 1:
                if (pc.isCrown()) {
                    polyId = 6843;
                } else if (pc.isKnight()) {
                    polyId = 6845;
                } else if (pc.isElf()) {
                    polyId = 6847;
                } else if (pc.isWizard()) {
                    polyId = 6849;
                } else if (pc.isDarkelf()) {
                    polyId = 6851;
                } else if (pc.isDragonKnight()) {
                    polyId = 7148;
                } else if (pc.isIllusionist()) {
                    polyId = 7150;
                }
                break;
        }

        L1PolyMorph.doPoly(pc, polyId, time, L1PolyMorph.MORPH_BY_ITEMMAGIC);

        // 删除道具
        pc.getInventory().removeItem(item, 1);
    }
}
