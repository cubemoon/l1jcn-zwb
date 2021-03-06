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
package com.lineage.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lineage.L1DatabaseFactory;
import com.lineage.server.datatables.TownTable;
import com.lineage.server.model.L1World;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.gametime.L1GameTime;
import com.lineage.server.model.gametime.L1GameTimeAdapter;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.utils.SQLUtil;

/**
 * 城镇时间控制器
 */
public class HomeTownTimeController {

    private class L1TownFixedProcListener extends L1GameTimeAdapter {
        public L1TownFixedProcListener() {
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onDayChanged(final L1GameTime time) {
            HomeTownTimeController.this.fixedProc(time);
        }
    }

    private static Logger _log = Logger.getLogger(HomeTownTimeController.class
            .getName());

    private static HomeTownTimeController _instance;

    private static L1TownFixedProcListener _listener;

    private static void clearHomeTownID() {
        Connection con = null;
        PreparedStatement pstm = null;

        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con
                    .prepareStatement("UPDATE characters SET HomeTownID = 0 WHERE HomeTownID = -1");
            pstm.execute();
        } catch (final SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public static HomeTownTimeController getInstance() {
        if (_instance == null) {
            _instance = new HomeTownTimeController();
        }
        return _instance;
    }

    /**
     * 取得报酬
     * 
     * @return int 报酬
     */
    public static int getPay(final int objid) {
        Connection con = null;
        PreparedStatement pstm1 = null;
        PreparedStatement pstm2 = null;
        ResultSet rs1 = null;
        int pay = 0;

        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm1 = con
                    .prepareStatement("SELECT Pay FROM characters WHERE objid = ? FOR UPDATE");

            pstm1.setInt(1, objid);
            rs1 = pstm1.executeQuery();

            if (rs1.next()) {
                pay = rs1.getInt("Pay");
            }

            pstm2 = con
                    .prepareStatement("UPDATE characters SET Pay = 0 WHERE objid = ?");
            pstm2.setInt(1, objid);
            pstm2.execute();
        } catch (final SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs1);
            SQLUtil.close(pstm1);
            SQLUtil.close(pstm2);
            SQLUtil.close(con);
        }

        return pay;
    }

    private static String totalContribution(final int townId) {
        Connection con = null;
        PreparedStatement pstm1 = null;
        ResultSet rs1 = null;
        PreparedStatement pstm2 = null;
        ResultSet rs2 = null;
        PreparedStatement pstm3 = null;
        ResultSet rs3 = null;
        PreparedStatement pstm4 = null;
        PreparedStatement pstm5 = null;

        int leaderId = 0;
        String leaderName = null;

        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm1 = con
                    .prepareStatement("SELECT objid, char_name FROM characters WHERE HomeTownID = ? ORDER BY Contribution DESC");

            pstm1.setInt(1, townId);
            rs1 = pstm1.executeQuery();

            if (rs1.next()) {
                leaderId = rs1.getInt("objid");
                leaderName = rs1.getString("char_name");
            }

            double totalContribution = 0;
            pstm2 = con
                    .prepareStatement("SELECT SUM(Contribution) AS TotalContribution FROM characters WHERE HomeTownID = ?");
            pstm2.setInt(1, townId);
            rs2 = pstm2.executeQuery();
            if (rs2.next()) {
                totalContribution = rs2.getInt("TotalContribution");
            }

            double townFixTax = 0;
            pstm3 = con
                    .prepareStatement("SELECT town_fix_tax FROM town WHERE town_id = ?");
            pstm3.setInt(1, townId);
            rs3 = pstm3.executeQuery();
            if (rs3.next()) {
                townFixTax = rs3.getInt("town_fix_tax");
            }

            double contributionUnit = 0;
            if (totalContribution != 0) {
                contributionUnit = Math.floor(townFixTax / totalContribution
                        * 100) / 100;
            }
            pstm4 = con
                    .prepareStatement("UPDATE characters SET Contribution = 0, Pay = Contribution * ? WHERE HomeTownID = ?");
            pstm4.setDouble(1, contributionUnit);
            pstm4.setInt(2, townId);
            pstm4.execute();

            pstm5 = con
                    .prepareStatement("UPDATE town SET leader_id = ?, leader_name = ?, tax_rate = 0, tax_rate_reserved = 0, sales_money = 0, sales_money_yesterday = sales_money, town_tax = 0, town_fix_tax = 0 WHERE town_id = ?");
            pstm5.setInt(1, leaderId);
            pstm5.setString(2, leaderName);
            pstm5.setInt(3, townId);
            pstm5.execute();
        } catch (final SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs1);
            SQLUtil.close(rs2);
            SQLUtil.close(rs3);
            SQLUtil.close(pstm1);
            SQLUtil.close(pstm2);
            SQLUtil.close(pstm3);
            SQLUtil.close(pstm4);
            SQLUtil.close(pstm5);
            SQLUtil.close(con);
        }

        return leaderName;
    }

    private HomeTownTimeController() {
        this.startListener();
    }

    public void dailyProc() {
        _log.info("城镇系统：开始处理每日事项");
        TownTable.getInstance().updateTaxRate();
        TownTable.getInstance().updateSalesMoneyYesterday();
        TownTable.getInstance().load();
    }

    void fixedProc(final L1GameTime time) {
        final Calendar cal = time.getCalendar();
        final int day = cal.get(Calendar.DAY_OF_MONTH); // Calendar.DAY_OF_WEEK
                                                        // 取得周几之值

        if (day == 25) {
            this.monthlyProc();
        } else {
            this.dailyProc();
        }
    }

    public void monthlyProc() {
        _log.info("城镇系统：开始处理每月事项");
        L1World.getInstance().setProcessingContributionTotal(true);
        final Collection<L1PcInstance> players = L1World.getInstance()
                .getAllPlayers();
        for (final L1PcInstance pc : players) {
            try {
                // 储存所有线上玩家的资讯
                pc.save();
            } catch (final Exception e) {
                _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }

        for (int townId = 1; townId <= 10; townId++) {
            final String leaderName = totalContribution(townId);
            if (leaderName != null) {
                final S_PacketBox packet = new S_PacketBox(
                        S_PacketBox.MSG_TOWN_LEADER, leaderName);
                for (final L1PcInstance pc : players) {
                    if (pc.getHomeTownId() == townId) {
                        pc.setContribution(0);
                        pc.sendPackets(packet);
                    }
                }
            }
        }
        TownTable.getInstance().load();

        for (final L1PcInstance pc : players) {
            if (pc.getHomeTownId() == -1) {
                pc.setHomeTownId(0);
            }
            pc.setContribution(0);
            try {
                // 储存所有线上玩家的资讯
                pc.save();
            } catch (final Exception e) {
                _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
        clearHomeTownID();
        L1World.getInstance().setProcessingContributionTotal(false);
    }

    private void startListener() {
        if (_listener == null) {
            _listener = new L1TownFixedProcListener();
            L1GameTimeClock.getInstance().addListener(_listener);
        }
    }
}
