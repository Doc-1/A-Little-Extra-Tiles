package com.alet.components.structures.regestries;

import com.alet.ALET;
import com.alet.components.structures.type.premade.LittleAdjustableFixedStructure;
import com.alet.components.structures.type.premade.LittleFillingCabinet;
import com.alet.components.structures.type.premade.LittlePhotoImporter;
import com.alet.components.structures.type.premade.LittleTypeWriter;
import com.alet.components.structures.type.premade.PickupItemPremade;
import com.alet.components.structures.type.premade.signal.LittleSignalColoredDisplay;
import com.alet.components.structures.type.premade.signal.LittleSignalInputQuick;
import com.alet.components.structures.type.premade.signal.LittleSignalInputQuick.LittleStructureTypeInputQuick;
import com.alet.components.structures.type.premade.signal.LittleSignalOutputQuick;
import com.alet.components.structures.type.premade.signal.LittleSignalOutputQuick.LittleStructureTypeOutputQuick;
import com.alet.components.structures.type.premade.signal.LittleSignalSevenSegmentedDisplay;
import com.alet.components.structures.type.premade.signal.LittleStructureTypeCircuit;
import com.alet.components.structures.type.premade.signal.circuit.LittleCircuitAsciiDisplay;
import com.alet.components.structures.type.premade.signal.circuit.LittleCircuitClock;
import com.alet.components.structures.type.premade.signal.circuit.LittleCircuitMagnitudeComparator;
import com.alet.components.structures.type.premade.signal.circuit.LittleCircuitMath;
import com.alet.components.structures.type.premade.signal.circuit.LittleCircuitNVRAM;
import com.alet.components.structures.type.premade.signal.circuit.LittleCircuitNumberToAscii;
import com.alet.components.structures.type.premade.signal.circuit.LittleCircuitPulser;
import com.alet.components.structures.type.premade.signal.circuit.LittleCircuitROM;
import com.alet.components.structures.type.premade.signal.circuit.LittleCircuitRandomNumber;
import com.alet.components.structures.type.premade.signal.circuit.LittleCircuitSignalSwitch;
import com.alet.components.structures.type.premade.signal.circuit.LittleCircuitTransformer;
import com.alet.components.structures.type.premade.transfer.LittleTransferLittleHopper;
import com.creativemd.littletiles.common.structure.attribute.LittleStructureAttribute;
import com.creativemd.littletiles.common.structure.signal.logic.SignalMode;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalCable;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalCable.LittleStructureTypeCable;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput.LittleStructureTypeInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput.LittleStructureTypeOutput;

public class PremadeStructureRegistery {
    
    private static void premadeCircuitRegistery() {
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("memory_32", ALET.MODID, LittleCircuitNVRAM.class, LittleStructureAttribute.PREMADE, ALET.MODID))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("clock_10hz", ALET.MODID, LittleCircuitClock.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("clock_5hz", ALET.MODID, LittleCircuitClock.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("clock_2hz", ALET.MODID, LittleCircuitClock.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("clock_1hz", ALET.MODID, LittleCircuitClock.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid();
        
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("splitter_32_to_16", ALET.MODID, LittleCircuitTransformer.class, LittleStructureAttribute.PREMADE, ALET.MODID))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("splitter_16_to_4", ALET.MODID, LittleCircuitTransformer.class, LittleStructureAttribute.PREMADE, ALET.MODID))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("splitter_4_to_1", ALET.MODID, LittleCircuitTransformer.class, LittleStructureAttribute.PREMADE, ALET.MODID))
                .setNotSnapToGrid();
        
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("combiner_16_to_32", ALET.MODID, LittleCircuitTransformer.class, LittleStructureAttribute.PREMADE, ALET.MODID))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("combiner_4_to_16", ALET.MODID, LittleCircuitTransformer.class, LittleStructureAttribute.PREMADE, ALET.MODID))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("combiner_1_to_4", ALET.MODID, LittleCircuitTransformer.class, LittleStructureAttribute.PREMADE, ALET.MODID))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("number_to_ascii", ALET.MODID, LittleCircuitNumberToAscii.class, LittleStructureAttribute.PREMADE, ALET.MODID))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("ascii_display", ALET.MODID, LittleCircuitAsciiDisplay.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid();
        
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("signal_pulser", ALET.MODID, LittleCircuitPulser.class, LittleStructureAttribute.PREMADE, ALET.MODID))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("magnitude_comparator_32", ALET.MODID, LittleCircuitMagnitudeComparator.class, LittleStructureAttribute.PREMADE, ALET.MODID))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("random_generator_32bit", ALET.MODID, LittleCircuitRandomNumber.class, LittleStructureAttribute.PREMADE, ALET.MODID))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("math", ALET.MODID, LittleCircuitMath.class, LittleStructureAttribute.PREMADE, ALET.MODID))
                .setNotSnapToGrid();
        
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("signal_rom_32", "premade", LittleCircuitROM.class, LittleStructureAttribute.PREMADE, ALET.MODID))
                .setNotSnapToGrid();
        
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCircuit("switch_32", ALET.MODID, LittleCircuitSignalSwitch.class, LittleStructureAttribute.PREMADE, ALET.MODID))
                .setNotSnapToGrid();
        
    }
    
    private static void premadeSignalRegistery() {
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeInput("signal_input32", "premade", LittleSignalInput.class, LittleStructureAttribute.EXTRA_RENDERING, ALET.MODID, 32));
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeOutput("signal_output32", "premade", LittleSignalOutput.class, LittleStructureAttribute.EXTRA_RENDERING, ALET.MODID, 32));
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeCable("signal_cable32", "premade", LittleSignalCable.class, LittleStructureAttribute.EXTRA_RENDERING, ALET.MODID, 32));
        
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeOutputQuick("signal_quick_output1", "premade", LittleSignalOutputQuick.class, LittleStructureAttribute.EXTRA_RENDERING | LittleStructureAttribute.TICKING, ALET.MODID, 1))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeOutputQuick("signal_quick_output4", "premade", LittleSignalOutputQuick.class, LittleStructureAttribute.EXTRA_RENDERING | LittleStructureAttribute.TICKING, ALET.MODID, 4))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeOutputQuick("signal_quick_output16", "premade", LittleSignalOutputQuick.class, LittleStructureAttribute.EXTRA_RENDERING | LittleStructureAttribute.TICKING, ALET.MODID, 16))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeOutputQuick("signal_quick_output32", "premade", LittleSignalOutputQuick.class, LittleStructureAttribute.EXTRA_RENDERING | LittleStructureAttribute.TICKING, ALET.MODID, 32))
                .setNotSnapToGrid();
        
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeInputQuick("signal_quick_input1", "premade", LittleSignalInputQuick.class, LittleStructureAttribute.EXTRA_RENDERING | LittleStructureAttribute.TICKING, ALET.MODID, 1))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeInputQuick("signal_quick_input4", "premade", LittleSignalInputQuick.class, LittleStructureAttribute.EXTRA_RENDERING | LittleStructureAttribute.TICKING, ALET.MODID, 4))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeInputQuick("signal_quick_input16", "premade", LittleSignalInputQuick.class, LittleStructureAttribute.EXTRA_RENDERING | LittleStructureAttribute.TICKING, ALET.MODID, 16))
                .setNotSnapToGrid();
        LittleStructurePremade.registerPremadeStructureType(
            new LittleStructureTypeInputQuick("signal_quick_input32", "premade", LittleSignalInputQuick.class, LittleStructureAttribute.EXTRA_RENDERING | LittleStructureAttribute.TICKING, ALET.MODID, 32))
                .setNotSnapToGrid();
    }
    
    private static void premadeStructureRegistery() {
        LittleStructurePremade.registerPremadeStructureType("signal_color_display_16", ALET.MODID,
            LittleSignalColoredDisplay.class, LittleStructureAttribute.TICK_RENDERING).addOutput("pixel0", 4,
                SignalMode.EQUAL, true).addOutput("pixel1", 4, SignalMode.EQUAL, true).addOutput("pixel2", 4,
                    SignalMode.EQUAL, true).addOutput("pixel3", 4, SignalMode.EQUAL, true).addOutput("pixel4", 4,
                        SignalMode.EQUAL, true).addOutput("pixel5", 4, SignalMode.EQUAL, true).addOutput("pixel6", 4,
                            SignalMode.EQUAL, true).addOutput("pixel7", 4, SignalMode.EQUAL, true).addOutput("pixel8", 4,
                                SignalMode.EQUAL, true).addOutput("pixel9", 4, SignalMode.EQUAL, true).addOutput("pixel10",
                                    4, SignalMode.EQUAL, true).addOutput("pixel11", 4, SignalMode.EQUAL, true).addOutput(
                                        "pixel12", 4, SignalMode.EQUAL, true).addOutput("pixel13", 4, SignalMode.EQUAL, true)
                .addOutput("pixel14", 4, SignalMode.EQUAL, true).addOutput("pixel15", 4, SignalMode.EQUAL, true);
        
        LittleStructurePremade.registerPremadeStructureType("seven_segement_display", ALET.MODID,
            LittleSignalSevenSegmentedDisplay.class, LittleStructureAttribute.TICK_RENDERING).addOutput("a", 1,
                SignalMode.EQUAL, true).addOutput("b", 1, SignalMode.EQUAL, true).addOutput("c", 1, SignalMode.EQUAL, true)
                .addOutput("d", 1, SignalMode.EQUAL, true).addOutput("e", 1, SignalMode.EQUAL, true).addOutput("f", 1,
                    SignalMode.EQUAL, true).addOutput("g", 1, SignalMode.EQUAL, true).addOutput("dp", 1, SignalMode.EQUAL,
                        true);
        
        LittleStructurePremade.registerPremadeStructureType("little_hopper", ALET.MODID, LittleTransferLittleHopper.class,
            LittleStructureAttribute.TICKING | LittleStructureAttribute.NEIGHBOR_LISTENER).setNotSnapToGrid().addOutput(
                "inactive", 1, SignalMode.EQUAL, true);
        
        LittleStructurePremade.registerPremadeStructureType("photoimporter", ALET.MODID, LittlePhotoImporter.class);
        
        LittleStructurePremade.registerPremadeStructureType("typewriter", ALET.MODID, LittleTypeWriter.class);
        LittleStructurePremade.registerPremadeStructureType("jump_rod", ALET.MODID, PickupItemPremade.class)
                .setNotShowCreativeTab();
        LittleStructurePremade.registerPremadeStructureType("adjustable", ALET.MODID, LittleAdjustableFixedStructure.class)
                .setNotShowCreativeTab();
        LittleStructurePremade.registerPremadeStructureType("filling_cabinet", ALET.MODID, LittleFillingCabinet.class,
            LittleStructureAttribute.TICKING).addInput("accessed", 1);
    }
    
    public static void registerPremadeStructures() {
        premadeCircuitRegistery();
        premadeSignalRegistery();
        premadeStructureRegistery();
    }
    
}
