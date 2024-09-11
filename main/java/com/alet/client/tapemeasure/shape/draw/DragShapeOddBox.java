package com.alet.client.tapemeasure.shape.draw;

import com.creativemd.littletiles.common.util.place.PlacementPosition;

public class DragShapeOddBox {
	
	public static int thickness = 0;
	public static PlacementPosition minPos;
	public static PlacementPosition maxPos;
	
	/*
	public DragShapeOddBox() {
		super("odd box");
	}
	
	/** Increase or decreases the given LittleVec
	 * 
	 * @return Returns the edited LittleVec object */
	/*
	public LittleVec editVec(LittleVec editVec, int modX, int modY, int modZ) {
		LittleVec editedVec = new LittleVec(editVec.x + modX, editVec.y + modY, editVec.z + modZ);
		return editedVec;
	}
	
	/** Moves the LittleVec away from the face you entered.
	 * 
	 * @return Returns the edited LittleVec object */
	/*
	public LittleVec addDistence(LittleVec editVec, int distence, EnumFacing facing) {
		editVec.set(facing.getAxis(), editVec.get(facing.getAxis()) + (facing.getAxisDirection().getOffset() * distence));
		return editVec;
	}
	
	@Override
	public void addExtraInformation(NBTTagCompound nbt, List<String> list) {
		
		list.add("thickness: " + nbt.getInteger("thickness") + " tiles");
		
		list.add("type: solid");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public List<GuiControl> getCustomSettings(NBTTagCompound nbt, LittleGridContext context) {
		List<GuiControl> controls = new ArrayList<>();
		
		controls.add(new GuiSteppedSlider("thickness", 5, 20, 100, 14, nbt.getInteger("thickness"), 2, context.size));
		
		return controls;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void saveCustomSettings(GuiParent gui, NBTTagCompound nbt, LittleGridContext context) {
		
		GuiSteppedSlider slider = (GuiSteppedSlider) gui.get("thickness");
		nbt.setInteger("thickness", (int) slider.value);
		thickness = (int) slider.value - 1;
	}
	
	@Override
	public void rotate(NBTTagCompound nbt, Rotation rotation) {
		LittleSlice slice = LittleSlice.values()[nbt.getInteger("slice")];
		slice = slice.rotate(rotation);
		nbt.setInteger("slice", slice.ordinal());
	}
	
	@Override
	public void flip(NBTTagCompound nbt, Axis axis) {
		LittleSlice slice = LittleSlice.values()[nbt.getInteger("slice")];
		slice = slice.flip(axis);
		nbt.setInteger("slice", slice.ordinal());
	}
	
	@Override
	public LittleBoxes getBoxes(LittleBoxes boxes, LittleVec min, LittleVec max, EntityPlayer player, NBTTagCompound nbt, boolean preview, LittleAbsoluteVec originalMin, LittleAbsoluteVec originalMax) {
		//boxes.add(new LittleTransformableBox(min.x, min.y, min.z, max.x, max.y, max.z, LittleSlice.values()[nbt.getInteger("slice")],3.0F,5.0F,0.0F,0.0F));
		
		EnumFacing facing = minPos.facing;
		EnumFacing facingMax = maxPos.facing;
		
		int halfedThickness = thickness / 2;
		int context = ItemMultiTiles.currentContext.size;
		
		double minX = originalMin.getPosX() - (halfedThickness / context);
		double minY = originalMin.getPosY() - (halfedThickness / context);
		double minZ = originalMin.getPosZ() - (halfedThickness / context);
		
		double maxX = originalMax.getPosX() + ((thickness - halfedThickness) / context);
		double maxY = originalMax.getPosY() + ((thickness - halfedThickness) / context);
		double maxZ = originalMax.getPosZ() + ((thickness - halfedThickness) / context);
		
		//LittleAbsoluteVec offset = new LittleAbsoluteVec(ItemLittleChisel.lastMax.getPos(), ItemMultiTiles.currentContext);
		//LittleBox newBox = new LittleBox(new LittleBox(min.getRelative(offset).getVec(ItemMultiTiles.currentContext)), new LittleBox(max.getRelative(offset).getVec(ItemMultiTiles.currentContext)));
		
		LittleBox box = new LittleBox(min, max);
		LittleVec finMin = editVec(min, -halfedThickness, -halfedThickness, -halfedThickness);
		LittleVec finMax = editVec(max, thickness - halfedThickness, thickness - halfedThickness, thickness - halfedThickness);
		
		RayTraceResult result = player.rayTrace(6.0, (float) 0.1);
		LittleAbsoluteVec pos = new LittleAbsoluteVec(result, ItemMultiTiles.currentContext);
		
		finMax = addDistence(finMax, halfedThickness, facing);
		finMin = addDistence(finMin, halfedThickness, facing);
		
		//System.out.println("minX: "+finMin.x+" minY: "+finMin.y+" minZ: "+finMin.z);
		//System.out.println("maxX: "+finMax.x+" maxY: "+finMax.y+" maxZ: "+finMax.z);
		
		System.out.println("minX: " + min.x + " minY: " + min.y + " minZ: " + min.z);
		System.out.println("minX: " + finMin.x + " minY: " + finMin.y + " minZ: " + finMin.z);
		System.out.println("maxX: " + max.x + " maxY: " + max.y + " maxZ: " + max.z);
		System.out.println("maxX: " + finMax.x + " maxY: " + finMax.y + " maxZ: " + finMax.z);
		
		//System.out.println("OminX: "+originalMin.getPosX()+" OminY: "+originalMin.getPosY()+" OminZ: "+originalMin.getPosZ());
		//System.out.println("OmaxX: "+originalMax.getPosX()+" OmaxY: "+originalMax.getPosY()+" OmaxZ: "+originalMax.getPosZ());
		
		System.out.println(facing);
		System.out.println(facingMax);
		System.out.println("halfedThickness: " + halfedThickness);
		//finMin = editVec(finMin, 0, 0, -1);
		switch (facing + "+" + facingMax) {
		case "north+up":
		case "east+up":
		case "south+up":
		case "west+up":
			System.out.println("da");
			finMax.add(facingMax);
			
			break;
		default:
			break;
		}
		
		//LittleBox box2 = new LittleBox(finMin, editVec(finMin, 1,1,1));
		//LittleBox box3 = new LittleBox(finMax, editVec(finMax, -1,-1,-1));
		LittleBox box3 = new LittleBox(min, max);
		box = new LittleBox(finMin, finMax);
		
		//boxes.add(box2);
		boxes.add(box3);
		boxes.add(box);
		return boxes;
	}*/
}
