package com.mozi.moziserver.model.mappedenum;

public enum ImageDomain {
    ANIMAL("animal"),
    ANIMAL_ITEM("animalItem"),
    STICKER("sticker"),
    DETAIL_ISLAND("detailIsland"),
    QUESTION("question"),
    CONFIRM("confirm");

    private final String directory;

    ImageDomain(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }
}
