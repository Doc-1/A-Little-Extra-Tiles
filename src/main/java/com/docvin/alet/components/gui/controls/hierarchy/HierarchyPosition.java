package com.docvin.alet.components.gui.controls.hierarchy;

public enum HierarchyPosition {
    CONTAINER(false, true),
    ITEM(false, false),
    ROOT_CONTAINER(true, true),
    ROOT_ITEM(true, false);

    private final boolean root;
    private final boolean container;

    HierarchyPosition(boolean root, boolean container) {
        this.root = root;
        this.container = container;
    }

    public boolean isContainer() {
        return container;
    }

    public boolean isRoot() {
        return root;
    }
}
