package com.docvin.alet.common.gui.override;

import com.creativemd.littletiles.client.gui.SubGuiRecipe;

public class OverrideBluePrintGui extends OverrideSubGui<SubGuiRecipe> {

    public OverrideBluePrintGui() {
        super(SubGuiRecipe.class, subGui -> {
            subGui.width = 500;
            subGui.height = 300;
            if (subGui.container != null)
                subGui.container.container.gui.resize();
        });
    }

}
