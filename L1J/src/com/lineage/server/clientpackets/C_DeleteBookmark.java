/**
 *                            License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS  
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). 
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.  
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR  
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND  
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE  
 * MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED 
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package com.lineage.server.clientpackets;

import com.lineage.server.ClientThread;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1BookMark;

/**
 * 处理收到由客户端传来删除书签的封包
 */
public class C_DeleteBookmark extends ClientBasePacket {
    private static final String C_DETELE_BOOKMARK = "[C] C_DeleteBookmark";

    public C_DeleteBookmark(final byte[] decrypt, final ClientThread client) {
        super(decrypt);
        final String bookmarkname = this.readS();
        final L1PcInstance pc = client.getActiveChar();
        L1BookMark.deleteBookmark(pc, bookmarkname);
    }

    @Override
    public String getType() {
        return C_DETELE_BOOKMARK;
    }
}
