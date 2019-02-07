package syi;

public class C {
    public class ShiPainter {
        public static final String
            GUI_NORMAL = "normal",
            GUI_PRO = "pro";
    }
    public class EngineM {
        public static final int
            // iHint
            H_FLINE = 0,
            H_LINE = 1,
            H_BEZI = 2,
            H_RECT = 3,
            H_FRECT = 4, // Filled rectangle
            H_OVAL = 5,
            H_FOVAL = 6, // Filled oval
            H_FILL = 7,
            H_TEXT = 8,
            H_COPY = 9,
            H_CLEAR = 10,
            H_SP = 11, // Line smoothing
            H_VTEXT = 12, // Vertical text
            H_UNKNOWN13 = 13,
            H_L = 14,
            // iPen
            P_SOLID = 0, // Pencil
            P_PEN = 1, // Pen
            P_SUISAI = 2, // Airbrush / EPen / Watercolor
            P_SUISAI2 = 3, // Watercolor (with color variation)
            P_WHITE = 4, // Eraser
            P_SWHITE = 5, // Soft eraser
            P_LIGHT = 6, // Dodge
            P_DARK = 7, // Burn
            P_BOKASHI = 8, // Blur
            P_MOSAIC = 9,
            P_FILL = 10,
            P_LPEN = 11, // Dodge Pen ?
            P_UNKNOWN12 = 12,
            P_UNKNOWN13 = 13,
            P_NULL = 14,
            P_UNKNOWN15 = 15,
            P_UNKNOWN16 = 16,
            P_LR = 17, // Flip horizontal
            P_UD = 18, // Flip vertical
            P_R = 19, // Rotate
            P_FUSION = 20, // Combine
            // iPenM
            PM_PEN = 0,
            PM_SUISAI = 1, // Suisai means watercolor
            PM_MANY = 2,
            // iMask
            M_N = 0, // Normal
            M_M = 1, // Multiply
            M_R = 2, // Invert
            M_ADD = 3, // Additive
            M_SUB = 4; // Subtract
        public static final int
            // Flags group 1 (iSOB)
            F1_ALL_LAYERS = 1,
            F1_AFIX = 2,
            F1O = 4, // isOver
            F1C = 8, // isCount
            F1A = 16, // isAnti
            F1S = 32, // iSOB
            // Flags group 2
            F2H = 1, // iHint
            F2PM = 2, // iPenM
            F2M = 4, // iMask
            F2P = 8, // iPen
            F2T = 16, // iTT
            F2L = 32, // iLayer
            F2LS = 64, // iLayerSrc
            // Flags group 3
            F3A = 1, // iAlpha
            F3C = 2, // iColor
            F3CM = 4, // iColorMask
            F3S = 8, // iSize
            F3E = 16, // iCount
            F3SA = 32, // iSA
            F3SS = 64, // iSS
            DEF_COUNT = -8;
        public static final String ENCODE = "UTF8";
    }
    public class EngineMg {
        public static final int
            // iHint
            H_FLINE = 0,
            H_LINE = 1,
            H_BEZI = 2,
            H_RECT = 3,
            H_FRECT = 4,
            H_OVAL = 5,
            H_FOVAL = 6,
            H_FILL = 7,
            H_TEXT = 8,
            H_COPY = 9,
            H_CLEAR = 10,
            H_SP = 11,
            H_L = 14,
            // iPen
            P_SOLID = 0,
            P_PEN = 1,
            P_SUISAI = 2,
            P_SUISAI2 = 3,
            P_WHITE = 4,
            P_SWHITE = 5,
            P_LIGHT = 6,
            P_DARK = 7,
            P_BOKASHI = 8,
            P_MOSAIC = 9,
            P_FILL = 10,
            P_LPEN = 11,
            P_UNKNOWN12 = 12,
            P_UNKNOWN13 = 13,
            P_NULL = 14,
            P_UNKNOWN15 = 15,
            P_UNKNOWN16 = 16,
            P_LR = 17,
            P_UD = 18,
            P_R = 19,
            P_FUSION = 20,
            // iPenM
            PM_PEN = 0,
            PM_SUISAI = 1, // Suisai means watercolor
            PM_MANY = 2,
            // iMask
            M_N = 0, // Normal
            M_M = 1, // Multiply
            M_R = 2, // Invert
            M_ADD = 3, // Additive
            M_SUB = 4; // Subtract
        public static final int
            // Flags group 1 (iSOB)
            F1_ALL_LAYERS = 1,
            F1_AFIX = 2,
            F1O = 4,
            F1C = 8,
            F1A = 16,
            F1S = 32,
            // Flags group 2
            F2H = 1,
            F2PM = 2,
            F2M = 4,
            F2P = 8,
            F2T = 16,
            F2L = 32,
            F2LS = 64,
            // Flags group 3
            F3A = 1,
            F3C = 2,
            F3CM = 4,
            F3S = 8,
            F3E = 16,
            F3SA = 32,
            F3SS = 64,
            DEF_COUNT = -8;
        public static final String ENCODE = "UTF8";
    }
    public class EngineMgLine {
        public static final byte
            M_LINE = 0,
            M_SUISAI = 1,
            M_TEXT = 2,
            M_X = 5,
            M_XX = 6,
            M_TONE = 7,
            M_BOKASHI = 8,
            M_LIGHT = 9,
            M_DARK = 10,
            M_WHITE = 19,
            M_RECT = 20,
            M_FRECT = 21,
            M_OVAL = 22,
            M_FOVAL = 23,
            M_RWHITE = 39,
            M_MOVE = 40,
            M_BRECT = 41,
            M_ABS_LR = 42,
            M_ABS_TB = 43,
            M_LIE = 44,
            M_FUSION = 45,
            M_V_F = 60,
            M_V_L = 61,
            M_V_B = 62,
            M_PASTE = -2,
            M_DELETE = 100,
            M_EMPTY = 101,
            M_HEADER = 102,
            M_ARCHIVE = 103,
            M_PING = 104,
            M_PAINTBBS = 105,
            M_PRELINE = 106,
            M_PAINTCHAT = 107,
            M_IMAGE = 108,
            M_OUT = 110,
            N = 110,
            M = 111,
            R = 112,
            ADD = 113,
            SUB = 114,
            MK_M = 1,
            MK_S = 2,
            MK_C = 4,
            MK_MC = 8,
            MK_A = 16,
            MK_L = 32;
    }
}
