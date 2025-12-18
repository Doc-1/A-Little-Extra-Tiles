package com.docvin.alet.common.gui.override;

import com.creativemd.littletiles.client.gui.SubGuiRecipe;

public class SubGuiBluePrintOverride extends SubGuiOverride<SubGuiRecipe> {

    public SubGuiBluePrintOverride() {
        super(SubGuiRecipe.class, subGui -> {
            subGui.width = 500;
            subGui.height = 300;
            if (subGui.container != null)
                subGui.container.container.gui.resize();
        });
    }

}
