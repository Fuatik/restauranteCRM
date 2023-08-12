package com.javarush.task.task27.task2712.ad;

import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.VideoSelectedEventDataRow;

import java.util.ArrayList;
import java.util.List;

import static com.javarush.task.task27.task2712.ConsoleHelper.writeMessage;

public class AdvertisementManager {
    private final AdvertisementStorage storage = AdvertisementStorage.getInstance();
    private int timeSeconds;
    private List<Advertisement> optimalVideoSet;
    private long maxAmount;
    private int totalTimeSecondsLeft;

    public AdvertisementManager(int timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public void processVideos() {
        this.totalTimeSecondsLeft = Integer.MAX_VALUE;
        optimiseVideoSet(new ArrayList<>(), timeSeconds, 0L);
        VideoSelectedEventDataRow row = new VideoSelectedEventDataRow(optimalVideoSet, maxAmount,
                timeSeconds - totalTimeSecondsLeft);
        StatisticManager.getInstance().register(row);
        displayAd();
    }

    public void optimiseVideoSet(List<Advertisement> totalList, int currentTimeSecondsLeft, long currentMaxAmount) {
        if (currentTimeSecondsLeft < 0) {
            return;
        } else if (currentMaxAmount > maxAmount
                || currentMaxAmount == maxAmount && (totalTimeSecondsLeft > currentTimeSecondsLeft
                || totalTimeSecondsLeft == currentTimeSecondsLeft && totalList.size() < optimalVideoSet.size())) {
            this.totalTimeSecondsLeft = currentTimeSecondsLeft;
            this.optimalVideoSet = totalList;
            this.maxAmount = currentMaxAmount;
            if (currentTimeSecondsLeft == 0) {
                return;
            }
        }
        List<Advertisement> tmpList = getActualAds();
        tmpList.removeAll(totalList);
        for (Advertisement ad : tmpList) {
            if (!ad.isActive()) continue;
            List<Advertisement> currentList = new ArrayList<>(totalList);
            currentList.add(ad);
            optimiseVideoSet(currentList, currentTimeSecondsLeft - ad.getDuration(),
                    currentMaxAmount + ad.getAmountPerOneDisplaying());
        }
    }

    public List<Advertisement> getActualAds() {
        List<Advertisement> actualAds = new ArrayList<>();
        for (Advertisement ad: storage.list()) {
            if (ad.isActive()) {
                actualAds.add(ad);
            }
        }
        return actualAds;
    }

    public void displayAd() {
        if (optimalVideoSet == null || optimalVideoSet.isEmpty()) {
            throw new NoVideoAvailableException();
        }
        optimalVideoSet.sort((o1, o2) -> {
            long l = o2.getAmountPerOneDisplaying() - o1.getAmountPerOneDisplaying();
            return (int) (l != 0 ? l : o2.getDuration() - o1.getDuration());
        });

        for (Advertisement ad : optimalVideoSet) {
            displayOnTablet(ad);
            ad.revalidate();
        }
    }

    public void displayOnTablet(Advertisement ad) {
        System.out.println(ad.getName() + " is displaying... " + ad.getAmountPerOneDisplaying() + ", "
                + (1000 * ad.getAmountPerOneDisplaying() / ad.getDuration()));
    }
}
