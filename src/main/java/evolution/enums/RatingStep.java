package evolution.enums;

public enum RatingStep {
    BEGINNER_1(0),
    BEGINNER_2(1),
    BEGINNER_3(2),
    NOT_AWFUL_1(3),
    NOT_AWFUL_2(4),
    NOT_AWFUL_3(5),
    CAN_PLAY_1(6),
    CAN_PLAY_2(7),
    CAN_PLAY_3(8),
    FINALLY_PLAYER_1(9),
    FINALLY_PLAYER_2(10),
    FINALLY_PLAYER_3(11),
    CAN_THINK_1(12),
    CAN_THINK_2(13),
    CAN_THINK_3(14),
    UNSTOPPABLE_1(15),
    UNSTOPPABLE_2(16),
    UNSTOPPABLE_3(17),
    WILL_BE_BANNED_1(18),
    WILL_BE_BANNED_2(19),
    WILL_BE_BANNED_3(20);

    public final Integer order;

    RatingStep(Integer order) {
        this.order = order;
    }

    public static RatingStep getByOrder(Integer order){
        for (RatingStep ratingStep: RatingStep.values()) {
            if (ratingStep.order.equals(order)){
                return ratingStep;
            }
        }
        return BEGINNER_1;
    }
}
