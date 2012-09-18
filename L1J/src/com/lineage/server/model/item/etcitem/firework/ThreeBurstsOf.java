package com.lineage.server.model.item.etcitem.firework;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.ItemExecutor;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 三连发 烟火 - 40136
 * 
 * @author jrwz
 */
public class ThreeBurstsOf implements ItemExecutor {

    public static ItemExecutor get() {
        return new ThreeBurstsOf();
    }

    private ThreeBurstsOf() {
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

        final short soundid = 2046;
        final S_SkillSound s_skillsound = new S_SkillSound(pc.getId(), soundid);
        pc.sendPackets(s_skillsound);
        pc.broadcastPacket(s_skillsound);
        pc.getInventory().removeItem(item, 1);
    }

}
