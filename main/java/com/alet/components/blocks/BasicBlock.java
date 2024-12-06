package com.alet.components.blocks;

import com.creativemd.littletiles.LittleTiles;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BasicBlock extends Block {
    
    public BasicBlock(String name) {
        super(Material.ROCK);
        setUnlocalizedName(name);
        setRegistryName(name);
        setHardness(1.5F);
        setCreativeTab(LittleTiles.littleTab);
    }
    
}
