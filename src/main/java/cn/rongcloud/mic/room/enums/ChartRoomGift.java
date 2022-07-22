package cn.rongcloud.mic.room.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public enum ChartRoomGift {
    GIFT_STAR(1, "小心心", 1),
    GIFT_MICROPHONE(2, "话筒", 2),
    GIFT_MICROPHONE_1(3, "麦克风", 5),
    GIFT_CHICKEN(4, "萌小鸡", 10),
    GIFT_HANDLE(5, "手柄", 20),
    GIFT_TROPHY(6, "奖杯", 50),
    GIFT_ROCKET(7, "火箭", 100),
    GIFT_FIREWORKS(8, "礼花", 200),
    GIFT_ROSE(9, "玫瑰花", 10),
    GIFT_GUITAR(10, "吉他", 20);

    private @Getter
    @Setter(AccessLevel.PRIVATE)
    int id;
    private @Getter
    @Setter(AccessLevel.PRIVATE)
    String name;
    private @Getter
    @Setter(AccessLevel.PRIVATE)
    int value;

    ChartRoomGift(int id, String name, int value) {
        this.value = value;
        this.id = id;
        this.name = name;
    }

    public static ChartRoomGift getChartRoomGiftById(int id) {
        for (ChartRoomGift item : ChartRoomGift.values()) {
            if (item.getId() == id) {
                return item;
            }
        }
        throw new IllegalArgumentException();
    }

}
