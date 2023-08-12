package com.javarush.task.task27.task2712.ad;

import java.util.ArrayList;
import java.util.List;

public class StatisticAdvertisementManager {
    private static StatisticAdvertisementManager statisticAdvertisementManager;
    private AdvertisementStorage advertisementStorage = AdvertisementStorage.getInstance();

    private StatisticAdvertisementManager() {}

    public static StatisticAdvertisementManager getInstance() {
        if (statisticAdvertisementManager == null) {
            statisticAdvertisementManager = new StatisticAdvertisementManager();
        }
        return statisticAdvertisementManager;
    }

    public List<Advertisement> getVideos(boolean isActive) {
        List<Advertisement> result = new ArrayList<>();
        for (Advertisement ad : advertisementStorage.list()) {
            if (!isActive ^ ad.isActive()) {
                result.add(ad);
            }
        }

        return result;
    }
}
