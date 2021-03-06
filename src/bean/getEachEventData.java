package bean;

import java.util.List;

public class getEachEventData {

    public static Object[] getEachAttribute(EventAttribute e){
        Object[] attr = {
                Boolean.FALSE,
                e.getA_id(),
                e.getAttribute_name(),
        };
        return attr;
    }

    public static Object[] getData(TerrorEvent s) {
                        Object[] data={s.getIDX(),
                        s.getEventid            (),
                        s.getIyear              (),
                        s.getImonth             (),
                        s.getIday               (),
                        s.getApproxdate         (),
                        s.getExtended           (),
                        s.getResolution         (),
                        s.getCountry            (),
                        s.getCountry_txt         (),
                        s.getRegion             (),
                        s.getRegion_txt          (),
                        s.getProvstate          (),
                        s.getCity               (),
                        s.getLatitude           (),
                        s.getLongitude          (),
                        s.getSpecificity        (),
                        s.getVicinity           (),
                        s.getLocation           (),
                        s.getSummary            (),
                        s.getCrit1              (),
                        s.getCrit2              (),
                        s.getCrit3              (),
                        s.getDoubtterr          (),
                        s.getAlternative        (),
                        s.getAlternative_txt     (),
                        s.getMultiple           (),
                        s.getSuccess            (),
                        s.getSuicide            (),
                        s.getAttacktype1        (),
                        s.getAttacktype1_txt     (),
                        s.getAttacktype2        (),
                        s.getAttacktype2_txt     (),
                        s.getAttacktype3        (),
                        s.getAttacktype3_txt     (),
                        s.getTargtype1          (),
                        s.getTargtype1_txt       (),
                        s.getTargsubtype1       (),
                        s.getTargsubtype1_txt    (),
                        s.getCorp1              (),
                        s.getTarget1            (),
                        s.getNatlty1            (),
                        s.getNatlty1_txt         (),
                        s.getTargtype2          (),
                        s.getTargtype2_txt       (),
                        s.getTargsubtype2       (),
                        s.getTargsubtype2_txt    (),
                        s.getCorp2              (),
                        s.getTarget2            (),
                        s.getNatlty2            (),
                        s.getNatlty2_txt         (),
                        s.getTargtype3          (),
                        s.getTargtype3_txt       (),
                        s.getTargsubtype3       (),
                        s.getTargsubtype3_txt    (),
                        s.getCorp3              (),
                        s.getTarget3            (),
                        s.getNatlty3            (),
                        s.getNatlty3_txt         (),
                        s.getGname              (),
                        s.getGsubname           (),
                        s.getGname2             (),
                        s.getGsubname2          (),
                        s.getGname3             (),
                        s.getGsubname3          (),
                        s.getMotive             (),
                        s.getGuncertain1        (),
                        s.getGuncertain2        (),
                        s.getGuncertain3        (),
                        s.getIndividual         (),
                        s.getNperps             (),
                        s.getNperpcap           (),
                        s.getClaimed            (),
                        s.getClaimmode          (),
                        s.getClaimmode_txt       (),
                        s.getClaim2             (),
                        s.getClaimmode2         (),
                        s.getClaimmode2_txt      (),
                        s.getClaim3             (),
                        s.getClaimmode3         (),
                        s.getClaimmode3_txt      (),
                        s.getCompclaim          (),
                        s.getWeaptype1          (),
                        s.getWeaptype1_txt       (),
                        s.getWeapsubtype1       (),
                        s.getWeapsubtype1_txt    (),
                        s.getWeaptype2          (),
                        s.getWeaptype2_txt       (),
                        s.getWeapsubtype2       (),
                        s.getWeapsubtype2_txt    (),
                        s.getWeaptype3          (),
                        s.getWeaptype3_txt       (),
                        s.getWeapsubtype3       (),
                        s.getWeapsubtype3_txt    (),
                        s.getWeaptype4          (),
                        s.getWeaptype4_txt       (),
                        s.getWeapsubtype4       (),
                        s.getWeapsubtype4_txt    (),
                        s.getWeapdetail         (),
                        s.getNkill              (),
                        s.getNkillus            (),
                        s.getNkillter           (),
                        s.getNwound             (),
                        s.getNwoundus           (),
                        s.getNwoundte           (),
                        s.getProperty           (),
                        s.getPropextent         (),
                        s.getPropextent_txt      (),
                        s.getPropvalue          (),
                        s.getPropcomment        (),
                        s.getIshostkid          (),
                        s.getNhostkid           (),
                        s.getNhostkidus         (),
                        s.getNhours             (),
                        s.getNdays              (),
                        s.getDivert             (),
                        s.getKidhijcountry      (),
                        s.getRansom             (),
                        s.getRansomamt          (),
                        s.getRansomamtus        (),
                        s.getRansompaid         (),
                        s.getRansompaidus       (),
                        s.getRansomnote         (),
                        s.getHostkidoutcome     (),
                        s.getHostkidoutcome_txt  (),
                        s.getNreleased          (),
                        s.getAddnotes           (),
                        s.getScite1             (),
                        s.getScite2             (),
                        s.getScite3             (),
                        s.getDbsource           (),
                        s.getINT_LOG            (),
                        s.getINT_IDEO           (),
                        s.getINT_MISC          (),
                        s.getINT_ANY           ()};//把List**的数据赋给Object数组
            return data;
    }
}
