package com.javarush.task.task27.task2712.statistic;

import com.javarush.task.task27.task2712.kitchen.Cook;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventType;
import com.javarush.task.task27.task2712.statistic.event.VideoSelectedEventDataRow;

import java.text.SimpleDateFormat;
import java.util.*;

public class StatisticManager {
    private static StatisticManager statisticManager;
    private StatisticStorage statisticStorage = new StatisticStorage();
    private StatisticManager()  {}

    public static StatisticManager getInstance() {
        if (statisticManager == null) {
            statisticManager = new StatisticManager();
        }
        return statisticManager;
    }

    private class StatisticStorage {
        private Map<EventType, List<EventDataRow>> storage = new HashMap<>();
        private StatisticStorage() {
            for (EventType type: EventType.values()) {
                this.storage.put(type, new ArrayList<>());
            }
        }
        private void put(EventDataRow data) {
            EventType type = data.getType();
            if (!this.storage.containsKey(type)) {
                throw new UnsupportedOperationException();
            }
            this.storage.get(type).add(data);
        }

        private List<EventDataRow> get(EventType eventType) {
            if (!this.storage.containsKey(eventType)) {
                throw new UnsupportedOperationException();
            }
            return this.storage.get(eventType);
        }
    }

    public void register(EventDataRow data) {
        this.statisticStorage.put(data);
    }

    public Map<String, Long> getProfitMap() {
        Map<String, Long> result = new HashMap<>(); // date, amount
        List<EventDataRow> rows = statisticStorage.get(EventType.SELECTED_VIDEOS);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Long total = 0L;
        for (EventDataRow row : rows) {
            VideoSelectedEventDataRow dataRow = (VideoSelectedEventDataRow) row;
            String date = simpleDateFormat.format(dataRow.getDate());
            if (!result.containsKey(date)) {
                result.put(date, 0L);
            }
            total += dataRow.getAmount();
            result.put(date, result.get(date) + dataRow.getAmount());
        }
        result.put("Total", total);

        return result;
    }

    public Map<String, Map<String, Integer>> getCookWorkloadingMap() {
        Map<String, Map<String, Integer>> result = new HashMap<>(); //date, nameCook, time
        List<EventDataRow> rows = statisticStorage.get(EventType.COOKED_ORDER);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        for (EventDataRow row : rows) {
            CookedOrderEventDataRow dataRow = (CookedOrderEventDataRow) row;
            String date = simpleDateFormat.format(dataRow.getDate());
            if (!result.containsKey(date)) {
                result.put(date, new HashMap<String, Integer>());
            }
            Map<String, Integer> cookMap = result.get(date);
            String cookName = dataRow.getCookName();
            if (!cookMap.containsKey(cookName)) {
                cookMap.put(cookName, 0);
            }
            Integer totalTime = cookMap.get(cookName);
            cookMap.put(cookName, totalTime + dataRow.getTime());
        }

        return result;
    }

}
