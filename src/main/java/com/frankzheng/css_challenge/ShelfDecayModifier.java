package com.frankzheng.css_challenge;

public enum ShelfDecayModifier {
    notPlacedToShelf(0),
    singleTempShelf(1),
    overflowShelf(2);

    private ShelfDecayModifier(int value) {
        this.value = value;
    }

    private final int value;

    public int getValue() {
        return value;
    }
}
