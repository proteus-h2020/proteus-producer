package com.treelogic.proteus.kafka;


import java.io.Serializable;


public class KafkaRecord<T> implements Serializable {

	private static final long serialVersionUID = -3346957834708114516L;
	
	public T id;
    public T positionX;
    public T positionY;

    public T C0001, C0002, C0003, C0004, C0005, C0006, C0007, C0008, C0009, C0010;
    public T C0011, C0012, C0013, C0014, C0015, C0016, C0017, C0018, C0019, C0020;
    public T C0021, C0022, C0023, C0024, C0025, C0026, C0027, C0028, C0029, C0030;
    public T C0031, C0032, C0033, C0034, C0035, C0036, C0037, C0038, C0039, C0040;
    public T C0041, C0042, C0043;

    public T C0045, C0048, C0049, C0055, C0044, C0046;


    public KafkaRecord(){}

    public KafkaRecord(T v, T v1, T v2, T v3, T v4, T v5, T v6, T v7, T v8, T v9, T v10, T v11, T v12, T v13, T v14, T v15, T v16, T v17, T v18, T v19, T v20, T v21, T v22, T v23, T v24, T v25, T v26, T v27, T v28, T v29, T v30, T v31, T v32, T v33, T v34, T v35, T v36, T v37, T v38, T v39, T v40, T v41, T v42, T v43, T v44, T v45, T v46) {
        id = v;
        positionX = v1;
        positionY = v2;
        C0005 = v3;
        C0007 = v4;
        C0030 = v5;
        C0001 = v6;
        C0004 = v7;
        C0016 = v8;
        C0017 = v9;
        C0015 = v10;
        C0041 = v11;
        C0006 = v12;
        C0028 = v13;
        C0024 = v14;
        C0032 = v15;
        C0009 = v16;
        C0042 = v17;
        C0021 = v18;
        C0010 = v19;
        C0018 = v20;
        C0043 = v21;
        C0033 = v22;
        C0034 = v23;
        C0023 = v24;
        C0049 = v25;
        C0044 = v26;
        C0020 = v27;
        C0055 = v28;
        C0002 = v29;
        C0008 = v30;
        C0027 = v31;
        C0046 = v32;
        C0048 = v33;
        C0037 = v34;
        C0012 = v35;
        C0014 = v36;
        C0045 = v37;
        C0011 = v38;
        C0025 = v39;
        C0019 = v40;
        C0036 = v41;
        C0029 = v42;
        C0003 = v43;
        C0026 = v45;
        C0035 = v46;
    }


    //public static float C0044, C0045, C0046, C0047, C0048, C0049, C0050;
    //public static float C0051, C0052, C0053, C0054, C0055, C0056, C0057, C0058, C0059, C0060;

    // Getters

    public T getPositionX(){return positionX;}
    public T getPositionY(){return positionY;}
    public T getID(){return id;}

    public T getC0001(){return C0001;}
    public T getC0002(){return C0002;}
    public T getC0003(){return C0003;}
    public T getC0004(){return C0004;}
    public T getC0005(){return C0005;}
    public T getC0006(){return C0006;}
    public T getC0007(){return C0007;}
    public T getC0008(){return C0008;}
    public T getC0009(){return C0009;}
    public T getC0010(){return C0010;}

    public T getC0011(){return C0011;}
    public T getC0012(){return C0012;}
    public T getC0013(){return C0013;}
    public T getC0014(){return C0014;}
    public T getC0015(){return C0015;}
    public T getC0016(){return C0016;}
    public T getC0017(){return C0017;}
    public T getC0018(){return C0018;}
    public T getC0019(){return C0019;}
    public T getC0020(){return C0020;}

    public T getC0021(){return C0021;}
    public T getC0022(){return C0022;}
    public T getC0023(){return C0023;}
    public T getC0025(){return C0025;}
    public T getC0026(){return C0026;}
    public T getC0027(){return C0027;}
    public T getC0028(){return C0028;}
    public T getC0029(){return C0029;}
    public T getC0030(){return C0030;}

    public T getC0031(){return C0031;}
    public T getC0032(){return C0032;}
    public T getC0033(){return C0033;}
    public T getC0034(){return C0034;}
    public T getC0035(){return C0035;}
    public T getC0036(){return C0036;}
    public T getC0037(){return C0037;}
    public T getC0038(){return C0038;}
    public T getC0039(){return C0039;}

    public T getC0040(){return C0040;}
    public T getC0041(){return C0041;}
    public T getC0042(){return C0042;}
    public T getC0043(){return C0043;}


    public T getC0044(){return C0044;}
    public T getC0045(){return C0045;}
    public T getC0046(){return C0046;}

    //public float getC0047(){return C0047;}
    public T getC0048(){return C0048;}
    public T getC0049(){return C0049;}

    /*
    public float getC0050(){return C0050;}
    public float getC0051(){return C0051;}
    public float getC0052(){return C0052;}
    public float getC0053(){return C0053;}
    public float getC0054(){return C0054;}
    */
    public T getC0055(){return C0055;}
    /*
    public float getC0056(){return C0056;}
    public float getC0057(){return C0057;}
    public float getC0058(){return C0058;}
    public float getC0059(){return C0059;}
    public float getC0060(){return C0060;}
    */


    // Getters

    public void setPositionx(T positionx){this.positionX = positionx;}
    public void setPositiony(T positiony){this.positionY = positiony;}
    public void setID(T id){this.id = id;}

    public void setC0001(T C0001){this.C0001 = C0001;}
    public void setC0002(T C0002){this.C0002 = C0002;}
    public void setC0003(T C0003){this.C0003 = C0003;}
    public void setC0004(T C0004){this.C0004 = C0004;}
    public void setC0005(T C0005){this.C0005 = C0005;}
    public void setC0006(T C0006){this.C0006 = C0006;}
    public void setC0007(T C0007){this.C0007 = C0007;}
    public void setC0008(T C0008){this.C0008 = C0008;}
    public void setC0009(T C0009){this.C0009 = C0009;}
    public void setC0010(T C0010){this.C0010 = C0010;}

    public void setC0011(T C0011){this.C0011 = C0011;}
    public void setC0012(T C0012){this.C0012 = C0012;}
    public void setC0013(T C0013){this.C0013 = C0013;}
    public void setC0014(T C0014){this.C0014 = C0014;}
    public void setC0015(T C0015){this.C0015 = C0015;}
    public void setC0016(T C0016){this.C0016 = C0016;}
    public void setC0017(T C0017){this.C0017 = C0017;}
    public void setC0018(T C0018){this.C0018 = C0018;}
    public void setC0019(T C0019){this.C0019 = C0019;}
    public void setC0020(T C0020){this.C0020 = C0020;}

    public void setC0021(T C0021){this.C0021 = C0021;}
    public void setC0022(T C0022){this.C0022 = C0022;}
    public void setC0023(T C0023){this.C0023 = C0023;}
    public void setC0024(T C0024){this.C0024 = C0024;}
    public void setC0025(T C0025){this.C0025 = C0025;}
    public void setC0026(T C0026){this.C0026 = C0026;}
    public void setC0027(T C0027){this.C0027 = C0027;}
    public void setC0028(T C0028){this.C0028 = C0028;}
    public void setC0029(T C0029){this.C0029 = C0029;}
    public void setC0030(T C0030){this.C0030 = C0030;}

    public void setC0031(T C0031){this.C0031 = C0031;}
    public void setC0032(T C0032){this.C0032 = C0032;}
    public void setC0033(T C0033){this.C0033 = C0033;}
    public void setC0034(T C0034){this.C0034 = C0034;}
    public void setC0035(T C0035){this.C0035 = C0035;}
    public void setC0036(T C0036){this.C0036 = C0036;}
    public void setC0037(T C0037){this.C0037 = C0037;}
    public void setC0038(T C0038){this.C0038 = C0038;}
    public void setC0039(T C0039){this.C0039 = C0039;}
    public void setC0040(T C0040){this.C0040 = C0040;}

    public void setC0041(T C0041){this.C0041 = C0041;}
    public void setC0042(T C0042){this.C0042 = C0042;}
    public void setC0043(T C0043){this.C0043 = C0043;}


    public void setC0044(T C0044){this.C0044 = C0044;}
    public void setC0045(T C0045){this.C0045 = C0045;}
    public void setC0046(T C0046){this.C0046 = C0046;}
    //public void setC0047(float C0047){this.C0047 = C0047;}
    public void setC0048(T C0048){this.C0048 = C0048;}
    public void setC0049(T C0049){this.C0049 = C0049;}
    //public void setC0050(float C0050){this.C0050 = C0050;}

    /*
    public void setC0051(float C0051){this.C0051 = C0051;}
    public void setC0052(float C0052){this.C0052 = C0052;}
    public void setC0053(float C0053){this.C0053 = C0053;}
    public void setC0054(float C0054){this.C0054 = C0054;}
    */
    public void setC0055(T C0055){this.C0055 = C0055;}
    /*
    public void setC0056(float C0056){this.C0056 = C0056;}
    public void setC0057(float C0057){this.C0057 = C0057;}
    public void setC0058(float C0058){this.C0058 = C0058;}
    public void setC0059(float C0059){this.C0059 = C0059;}
    public void setC0060(float C0060){this.C0060 = C0050;}
    */

}
