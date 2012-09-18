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
package com.lineage.server.serverpackets;

import com.lineage.server.Opcodes;

/**
 * 角色移除 (立即或非立即)
 */
public class S_DeleteCharOK extends ServerBasePacket {

    private static final String S_DELETE_CHAR_OK = "[S] S_DeleteCharOK";

    public static final int DELETE_CHAR_NOW = 0x05;

    public static final int DELETE_CHAR_AFTER_7DAYS = 0x51;

    /**
     * 角色移除 (立即或非立即)
     * 
     * @param type
     */
    public S_DeleteCharOK(final int type) {
        this.writeC(Opcodes.S_OPCODE_DETELECHAROK);
        this.writeC(type);
    }

    @Override
    public byte[] getContent() {
        return this.getBytes();
    }

    @Override
    public String getType() {
        return S_DELETE_CHAR_OK;
    }
}
