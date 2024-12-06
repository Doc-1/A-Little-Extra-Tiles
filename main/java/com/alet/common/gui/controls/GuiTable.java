package com.alet.common.gui.controls;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.client.renderer.GlStateManager;

public class GuiTable extends GuiParent {
    
    public String title;
    public int cellWidth;
    public int cellHeight;
    public int rows;
    public int columns;
    public boolean cellsModifiable;
    public List<String[]> cellData = new ArrayList<String[]>();
    
    public GuiTable(String name, String title, int x, int y, int cellWidth, int cellHeight, int rows, int columns, boolean cellsModifiable) {
        super(name, x, y, 200, 400);
        this.title = title;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.rows = rows;
        this.columns = columns;
        this.cellsModifiable = cellsModifiable;
        addCell(new ArrayList<String[]>());
    }
    
    public void addCell(List<String[]> rows) {
        rows.add(new String[] { "Signal A", "Signal B", "Results" });
        rows.add(new String[] { "TRUE", "TRUE", "TRUE" });
        rows.add(new String[] { "FALSE", "FALSE", "FALSE" });
        rows.add(new String[] { "TRUE", "FALSE", "FALSE" });
        rows.add(new String[] { "FALSE", "TRUE", "FALSE" });
        for (int i = 0; i < rows.size(); i++) {
            cellData.add(i, rows.get(i));
        }
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(1, 1, 0);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                String text = cellData.get(i)[j];
                font.drawString(text, (j * cellWidth) + ((cellWidth / 2) - (font.getStringWidth(text) / 2)), (i * cellHeight) + (font.FONT_HEIGHT / 2), ColorUtils.WHITE, true);
                helper.drawLine(j * cellWidth, i * cellHeight, (j * cellWidth), (i * cellHeight) + cellHeight, ColorUtils.WHITE);
                helper.drawLine(-1, i * cellHeight, ((j + 1) * cellWidth) + 1, i * cellHeight, ColorUtils.WHITE);
            }
        
        helper.drawLine((cellWidth * columns), 0, (cellWidth * columns), (cellHeight * rows), ColorUtils.WHITE);
        helper.drawLine(-1, (cellHeight * rows), (cellWidth * columns) + 1, (cellHeight * rows), ColorUtils.WHITE);
        GlStateManager.popMatrix();
    }
    
}
