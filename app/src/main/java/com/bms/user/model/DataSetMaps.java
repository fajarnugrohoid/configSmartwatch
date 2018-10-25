package com.bms.user.model;

import java.util.ArrayList;

public class DataSetMaps {

    ArrayList<ModelSettingSmartwatch> modelChooseMapsArrayList=new ArrayList<ModelSettingSmartwatch>();
    private ModelSettingSmartwatch modelChooseMaps;

    public ArrayList<ModelSettingSmartwatch> getDataSettingMaps(){
        modelChooseMaps = new ModelSettingSmartwatch();
        modelChooseMaps.setIdx(0);
        modelChooseMaps.setMapsName("cipatat topografi");
        modelChooseMaps.setMapsPath("/Download/Cipatat/Cipatat/Topografi");
        modelChooseMaps.setImageFileNameEnding(".png");
        modelChooseMaps.setMinZoomLvl(11);
        modelChooseMaps.setMaxZoomLvl(16);
        modelChooseMaps.setFirstZoomLvl(12);
        modelChooseMaps.setTileSizePixel(256);
        modelChooseMaps.setSdrLat("-6.830750");
        modelChooseMaps.setSdrLon("107.384688");
        modelChooseMapsArrayList.add(modelChooseMaps);

        modelChooseMaps = new ModelSettingSmartwatch();
        modelChooseMaps.setIdx(1);
        modelChooseMaps.setMapsName("cipatat google map");
        modelChooseMaps.setMapsPath("/Download/Cipatat/Cipatat/Google Map");
        modelChooseMaps.setImageFileNameEnding(".png");
        modelChooseMaps.setMinZoomLvl(11);
        modelChooseMaps.setMaxZoomLvl(16);
        modelChooseMaps.setFirstZoomLvl(12);
        modelChooseMaps.setTileSizePixel(256);
        modelChooseMaps.setSdrLat("-6.830750");
        modelChooseMaps.setSdrLon("107.384688");
        modelChooseMapsArrayList.add(modelChooseMaps);

        modelChooseMaps = new ModelSettingSmartwatch();
        modelChooseMaps.setIdx(2);
        modelChooseMaps.setMapsName("cipatat google satelit");
        modelChooseMaps.setMapsPath("/Download/Cipatat/Cipatat/Google Satelite");
        modelChooseMaps.setImageFileNameEnding(".png");
        modelChooseMaps.setMinZoomLvl(11);
        modelChooseMaps.setMaxZoomLvl(16);
        modelChooseMaps.setFirstZoomLvl(12);
        modelChooseMaps.setTileSizePixel(256);
        modelChooseMaps.setSdrLat("-6.830750");
        modelChooseMaps.setSdrLon("107.384688");
        modelChooseMapsArrayList.add(modelChooseMaps);

        modelChooseMaps = new ModelSettingSmartwatch();
        modelChooseMaps.setIdx(3);
        modelChooseMaps.setMapsName("monas google map");
        modelChooseMaps.setMapsPath("/Download/monas/googlemap");
        modelChooseMaps.setImageFileNameEnding(".png");
        modelChooseMaps.setMinZoomLvl(17);
        modelChooseMaps.setMaxZoomLvl(21);
        modelChooseMaps.setFirstZoomLvl(17);
        modelChooseMaps.setTileSizePixel(256);
        modelChooseMaps.setSdrLat("-6.181279");
        modelChooseMaps.setSdrLon("106.832606");
        modelChooseMapsArrayList.add(modelChooseMaps);

        modelChooseMaps = new ModelSettingSmartwatch();
        modelChooseMaps.setIdx(4);
        modelChooseMaps.setMapsName("mabes google map");
        modelChooseMaps.setMapsPath("/Download/mabes/googlemap");
        modelChooseMaps.setImageFileNameEnding(".jpg");
        modelChooseMaps.setMinZoomLvl(11);
        modelChooseMaps.setMaxZoomLvl(21);
        modelChooseMaps.setFirstZoomLvl(17);
        modelChooseMaps.setTileSizePixel(256);
        modelChooseMaps.setSdrLat("-6.181279");
        modelChooseMaps.setSdrLon("106.832606");
        modelChooseMaps.setSdrLat("-6.181279");
        modelChooseMaps.setSdrLon("106.832606");
        modelChooseMapsArrayList.add(modelChooseMaps);

        return modelChooseMapsArrayList;
    }

}
