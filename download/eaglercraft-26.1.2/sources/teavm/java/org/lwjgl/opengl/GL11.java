package org.lwjgl.opengl;

import java.util.HashMap;
import java.util.Map;

import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.typedarrays.Int32Array;
import org.teavm.jso.typedarrays.Uint8Array;

import net.lax1dude.eaglercraft.v2_6.internal.PlatformOpenGL;

/**
 * TeaVM-compatible shim for LWJGL's GL11 class.
 * Forwards all GL calls to the PlatformOpenGL layer (WebGL2 backend).
 * Methods for features not present in WebGL2 are implemented as no-ops.
 */
public class GL11 {

        // ================================================================
        // Internal texture ID mapping (int <-> JSObject)
        // ================================================================

        private static final Map<Integer, JSObject> textureMap = new HashMap<>();
        private static int nextTextureId = 1;

        /**
         * Returns the PlatformOpenGL JSObject for the given int texture ID,
         * or null if id == 0 (no texture bound).
         */
        public static JSObject getTextureObject(int id) {
                return id == 0 ? null : textureMap.get(id);
        }

        // ================================================================
        // GL Constants
        // ================================================================

        // --- Boolean ---
        public static final int GL_FALSE = 0;
        public static final int GL_TRUE = 1;

        // --- Errors ---
        public static final int GL_NO_ERROR = 0;
        public static final int GL_INVALID_ENUM = 0x0500;
        public static final int GL_INVALID_VALUE = 0x0501;
        public static final int GL_INVALID_OPERATION = 0x0502;
        public static final int GL_STACK_OVERFLOW = 0x0503;
        public static final int GL_STACK_UNDERFLOW = 0x0504;
        public static final int GL_OUT_OF_MEMORY = 0x0505;

        // --- Enable / Disable caps ---
        public static final int GL_DEPTH_TEST = 0x0B71;
        public static final int GL_BLEND = 0x0BE2;
        public static final int GL_CULL_FACE = 0x0B44;
        public static final int GL_SCISSOR_TEST = 0x0C11;
        public static final int GL_STENCIL_TEST = 0x0B90;
        public static final int GL_TEXTURE_2D = 0x0DE1;
        public static final int GL_DITHER = 0x0BD0;
        public static final int GL_POLYGON_OFFSET_FILL = 0x8037;
        public static final int GL_ALPHA_TEST = 0x0BC0;
        public static final int GL_FOG = 0x0B60;
        public static final int GL_LIGHTING = 0x0B50;
        public static final int GL_COLOR_MATERIAL = 0x0B57;
        public static final int GL_NORMALIZE = 0x0BA1;
        public static final int GL_RESCALE_NORMAL = 0x803A;
        public static final int GL_POINT_SMOOTH = 0x0B10;
        public static final int GL_LINE_SMOOTH = 0x0B20;
        public static final int GL_POLYGON_SMOOTH = 0x0B41;
        public static final int GL_COLOR_LOGIC_OP = 0x0BF2;
        public static final int GL_INDEX_LOGIC_OP = 0x0BF1;
        public static final int GL_MULTISAMPLE = 0x809D;
        public static final int GL_SAMPLE_ALPHA_TO_COVERAGE = 0x809E;
        public static final int GL_SAMPLE_ALPHA_TO_ONE = 0x809F;
        public static final int GL_SAMPLE_COVERAGE = 0x80A0;

        // --- Clear bits ---
        public static final int GL_COLOR_BUFFER_BIT = 0x00004000;
        public static final int GL_DEPTH_BUFFER_BIT = 0x00000100;
        public static final int GL_STENCIL_BUFFER_BIT = 0x00000400;

        // --- Blend factors ---
        public static final int GL_ZERO = 0;
        public static final int GL_ONE = 1;
        public static final int GL_SRC_COLOR = 0x0300;
        public static final int GL_ONE_MINUS_SRC_COLOR = 0x0301;
        public static final int GL_SRC_ALPHA = 0x0302;
        public static final int GL_ONE_MINUS_SRC_ALPHA = 0x0303;
        public static final int GL_DST_ALPHA = 0x0304;
        public static final int GL_ONE_MINUS_DST_ALPHA = 0x0305;
        public static final int GL_DST_COLOR = 0x0306;
        public static final int GL_ONE_MINUS_DST_COLOR = 0x0307;
        public static final int GL_SRC_ALPHA_SATURATE = 0x0308;
        public static final int GL_CONSTANT_COLOR = 0x8001;
        public static final int GL_ONE_MINUS_CONSTANT_COLOR = 0x8002;
        public static final int GL_CONSTANT_ALPHA = 0x8003;
        public static final int GL_ONE_MINUS_CONSTANT_ALPHA = 0x8004;

        // --- Blend equations ---
        public static final int GL_FUNC_ADD = 0x8006;
        public static final int GL_FUNC_SUBTRACT = 0x800A;
        public static final int GL_FUNC_REVERSE_SUBTRACT = 0x800B;
        public static final int GL_MIN = 0x8007;
        public static final int GL_MAX = 0x8008;

        // --- Depth functions ---
        public static final int GL_NEVER = 0x0200;
        public static final int GL_LESS = 0x0201;
        public static final int GL_EQUAL = 0x0202;
        public static final int GL_LEQUAL = 0x0203;
        public static final int GL_GREATER = 0x0204;
        public static final int GL_NOTEQUAL = 0x0205;
        public static final int GL_GEQUAL = 0x0206;
        public static final int GL_ALWAYS = 0x0207;

        // --- Face culling ---
        public static final int GL_FRONT = 0x0404;
        public static final int GL_BACK = 0x0405;
        public static final int GL_FRONT_AND_BACK = 0x0408;

        // --- Front face direction ---
        public static final int GL_CW = 0x0900;
        public static final int GL_CCW = 0x0901;

        // --- Primitive types ---
        public static final int GL_POINTS = 0x0000;
        public static final int GL_LINES = 0x0001;
        public static final int GL_LINE_LOOP = 0x0002;
        public static final int GL_LINE_STRIP = 0x0003;
        public static final int GL_TRIANGLES = 0x0004;
        public static final int GL_TRIANGLE_STRIP = 0x0005;
        public static final int GL_TRIANGLE_FAN = 0x0006;
        public static final int GL_QUADS = 0x0007;
        public static final int GL_QUAD_STRIP = 0x0008;
        public static final int GL_POLYGON = 0x0009;

        // --- Pixel formats ---
        public static final int GL_DEPTH_COMPONENT = 0x1902;
        public static final int GL_RED = 0x1903;
        public static final int GL_GREEN = 0x1904;
        public static final int GL_BLUE = 0x1905;
        public static final int GL_ALPHA = 0x1906;
        public static final int GL_RGB = 0x1907;
        public static final int GL_RGBA = 0x1908;
        public static final int GL_LUMINANCE = 0x1909;
        public static final int GL_LUMINANCE_ALPHA = 0x190A;
        public static final int GL_BGRA = 0x80E1;
        public static final int GL_RG = 0x8227;
        public static final int GL_RED_INTEGER = 0x8D94;
        public static final int GL_GREEN_INTEGER = 0x8D95;
        public static final int GL_BLUE_INTEGER = 0x8D96;
        public static final int GL_ALPHA_INTEGER = 0x8D97;
        public static final int GL_RGB_INTEGER = 0x8D98;
        public static final int GL_RGBA_INTEGER = 0x8D99;
        public static final int GL_RG_INTEGER = 0x8228;
        public static final int GL_DEPTH_STENCIL = 0x84F9;

        // --- Internal formats ---
        public static final int GL_R8 = 0x8229;
        public static final int GL_R16F = 0x822D;
        public static final int GL_R32F = 0x822E;
        public static final int GL_RG8 = 0x822B;
        public static final int GL_RG16F = 0x822F;
        public static final int GL_RG32F = 0x8230;
        public static final int GL_RGB8 = 0x8051;
        public static final int GL_RGB565 = 0x8D62;
        public static final int GL_RGB16F = 0x881B;
        public static final int GL_RGB32F = 0x8815;
        public static final int GL_RGBA4 = 0x8056;
        public static final int GL_RGB5_A1 = 0x8057;
        public static final int GL_RGBA8 = 0x8058;
        public static final int GL_RGBA16F = 0x881A;
        public static final int GL_RGBA32F = 0x8814;
        public static final int GL_SRGB8_ALPHA8 = 0x8C43;
        public static final int GL_DEPTH_COMPONENT16 = 0x81A5;
        public static final int GL_DEPTH_COMPONENT24 = 0x81A6;
        public static final int GL_DEPTH_COMPONENT32F = 0x8CAC;
        public static final int GL_DEPTH24_STENCIL8 = 0x88F0;
        public static final int GL_DEPTH32F_STENCIL8 = 0x8CAD;
        public static final int GL_R11F_G11F_B10F = 0x8C3A;
        public static final int GL_COMPRESSED_RGB_S3TC_DXT1_EXT = 0x83F0;
        public static final int GL_COMPRESSED_RGBA_S3TC_DXT1_EXT = 0x83F1;
        public static final int GL_COMPRESSED_RGBA_S3TC_DXT3_EXT = 0x83F2;
        public static final int GL_COMPRESSED_RGBA_S3TC_DXT5_EXT = 0x83F3;

        // --- Pixel types ---
        public static final int GL_BYTE = 0x1400;
        public static final int GL_UNSIGNED_BYTE = 0x1401;
        public static final int GL_SHORT = 0x1402;
        public static final int GL_UNSIGNED_SHORT = 0x1403;
        public static final int GL_INT = 0x1404;
        public static final int GL_UNSIGNED_INT = 0x1405;
        public static final int GL_FLOAT = 0x1406;
        public static final int GL_DOUBLE = 0x140A;
        public static final int GL_HALF_FLOAT = 0x140B;
        public static final int GL_UNSIGNED_SHORT_4_4_4_4 = 0x8033;
        public static final int GL_UNSIGNED_SHORT_5_5_5_1 = 0x8034;
        public static final int GL_UNSIGNED_SHORT_5_6_5 = 0x8363;
        public static final int GL_UNSIGNED_INT_8_8_8_8 = 0x8035;
        public static final int GL_UNSIGNED_INT_8_8_8_8_REV = 0x8367;
        public static final int GL_UNSIGNED_INT_2_10_10_10_REV = 0x8368;
        public static final int GL_UNSIGNED_INT_10F_11F_11F_REV = 0x8C3B;
        public static final int GL_UNSIGNED_INT_24_8 = 0x84FA;
        public static final int GL_FLOAT_32_UNSIGNED_INT_24_8_REV = 0x8DAD;

        // --- Texture units ---
        public static final int GL_TEXTURE0 = 0x84C0;
        public static final int GL_TEXTURE1 = 0x84C1;
        public static final int GL_TEXTURE2 = 0x84C2;
        public static final int GL_TEXTURE3 = 0x84C3;
        public static final int GL_TEXTURE4 = 0x84C4;
        public static final int GL_TEXTURE5 = 0x84C5;
        public static final int GL_TEXTURE6 = 0x84C6;
        public static final int GL_TEXTURE7 = 0x84C7;
        public static final int GL_TEXTURE8 = 0x84C8;
        public static final int GL_TEXTURE9 = 0x84C9;
        public static final int GL_TEXTURE10 = 0x84CA;
        public static final int GL_TEXTURE11 = 0x84CB;
        public static final int GL_TEXTURE12 = 0x84CC;
        public static final int GL_TEXTURE13 = 0x84CD;
        public static final int GL_TEXTURE14 = 0x84CE;
        public static final int GL_TEXTURE15 = 0x84CF;
        public static final int GL_TEXTURE16 = 0x84D0;
        public static final int GL_TEXTURE17 = 0x84D1;
        public static final int GL_TEXTURE18 = 0x84D2;
        public static final int GL_TEXTURE19 = 0x84D3;
        public static final int GL_TEXTURE20 = 0x84D4;
        public static final int GL_TEXTURE21 = 0x84D5;
        public static final int GL_TEXTURE22 = 0x84D6;
        public static final int GL_TEXTURE23 = 0x84D7;
        public static final int GL_TEXTURE24 = 0x84D8;
        public static final int GL_TEXTURE25 = 0x84D9;
        public static final int GL_TEXTURE26 = 0x84DA;
        public static final int GL_TEXTURE27 = 0x84DB;
        public static final int GL_TEXTURE28 = 0x84DC;
        public static final int GL_TEXTURE29 = 0x84DD;
        public static final int GL_TEXTURE30 = 0x84DE;
        public static final int GL_TEXTURE31 = 0x84DF;
        public static final int GL_ACTIVE_TEXTURE = 0x84E0;

        // --- Texture parameters ---
        public static final int GL_TEXTURE_MIN_FILTER = 0x2801;
        public static final int GL_TEXTURE_MAG_FILTER = 0x2800;
        public static final int GL_TEXTURE_WRAP_S = 0x2802;
        public static final int GL_TEXTURE_WRAP_T = 0x2803;
        public static final int GL_TEXTURE_WRAP_R = 0x8072;
        public static final int GL_TEXTURE_MAX_LEVEL = 0x813D;
        public static final int GL_TEXTURE_MAX_ANISOTROPY = 0x84FE;
        public static final int GL_MAX_TEXTURE_MAX_ANISOTROPY = 0x84FF;
        public static final int GL_TEXTURE_LOD_BIAS = 0x8501;
        public static final int GL_TEXTURE_COMPARE_MODE = 0x884C;
        public static final int GL_TEXTURE_COMPARE_FUNC = 0x884D;
        public static final int GL_TEXTURE_WIDTH = 0x1000;
        public static final int GL_TEXTURE_HEIGHT = 0x1001;
        public static final int GL_TEXTURE_INTERNAL_FORMAT = 0x1003;
        public static final int GL_TEXTURE_BORDER = 0x1005;
        public static final int GL_TEXTURE_RED_SIZE = 0x1006;
        public static final int GL_TEXTURE_GREEN_SIZE = 0x1007;
        public static final int GL_TEXTURE_BLUE_SIZE = 0x1008;
        public static final int GL_TEXTURE_ALPHA_SIZE = 0x1009;

        // --- Texture filter modes ---
        public static final int GL_NEAREST = 0x2600;
        public static final int GL_LINEAR = 0x2601;
        public static final int GL_NEAREST_MIPMAP_NEAREST = 0x2700;
        public static final int GL_LINEAR_MIPMAP_NEAREST = 0x2701;
        public static final int GL_NEAREST_MIPMAP_LINEAR = 0x2702;
        public static final int GL_LINEAR_MIPMAP_LINEAR = 0x2703;

        // --- Texture wrap modes ---
        public static final int GL_CLAMP_TO_EDGE = 0x812F;
        public static final int GL_REPEAT = 0x2901;
        public static final int GL_MIRRORED_REPEAT = 0x8370;
        public static final int GL_CLAMP_TO_BORDER = 0x812D;

        // --- Pixel store ---
        public static final int GL_UNPACK_ALIGNMENT = 0x0CF5;
        public static final int GL_PACK_ALIGNMENT = 0x0D05;
        public static final int GL_UNPACK_ROW_LENGTH = 0x0CF2;
        public static final int GL_UNPACK_SKIP_ROWS = 0x0CF3;
        public static final int GL_UNPACK_SKIP_PIXELS = 0x0CF4;
        public static final int GL_UNPACK_IMAGE_HEIGHT = 0x806E;
        public static final int GL_UNPACK_SKIP_IMAGES = 0x806D;
        public static final int GL_PACK_ROW_LENGTH = 0x0D02;
        public static final int GL_PACK_SKIP_ROWS = 0x0D03;
        public static final int GL_PACK_SKIP_PIXELS = 0x0D04;
        public static final int GL_PACK_IMAGE_HEIGHT = 0x806C;
        public static final int GL_PACK_SKIP_IMAGES = 0x806B;

        // --- Get parameters ---
        public static final int GL_MAX_TEXTURE_SIZE = 0x0D33;
        public static final int GL_MAX_TEXTURE_UNITS = 0x84E2;
        public static final int GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0x8B4D;
        public static final int GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS = 0x8B4C;
        public static final int GL_MAX_TEXTURE_IMAGE_UNITS = 0x8872;
        public static final int GL_MAX_CUBE_MAP_TEXTURE_SIZE = 0x851C;
        public static final int GL_MAX_RENDERBUFFER_SIZE = 0x84E8;
        public static final int GL_MAX_VIEWPORT_DIMS = 0x0D3A;
        public static final int GL_MAX_VERTEX_ATTRIBS = 0x8869;
        public static final int GL_MAX_VERTEX_UNIFORM_VECTORS = 0x8DFB;
        public static final int GL_MAX_VARYING_VECTORS = 0x8DFC;
        public static final int GL_MAX_FRAGMENT_UNIFORM_VECTORS = 0x8DFD;
        public static final int GL_VIEWPORT = 0x0BA2;
        public static final int GL_DEPTH_BITS = 0x0D56;
        public static final int GL_STENCIL_BITS = 0x0D57;
        public static final int GL_ALPHA_BITS = 0x0D55;
        public static final int GL_RED_BITS = 0x0D52;
        public static final int GL_GREEN_BITS = 0x0D53;
        public static final int GL_BLUE_BITS = 0x0D54;
        public static final int GL_SUBPIXEL_BITS = 0x0D50;
        public static final int GL_ALIASED_LINE_WIDTH_RANGE = 0x846E;
        public static final int GL_ALIASED_POINT_SIZE_RANGE = 0x846D;
        public static final int GL_DEPTH_RANGE = 0x0B70;
        public static final int GL_DEPTH_WRITEMASK = 0x0B72;
        public static final int GL_DEPTH_CLEAR_VALUE = 0x0B73;
        public static final int GL_COLOR_WRITEMASK = 0x0C23;
        public static final int GL_DOUBLEBUFFER = 0x0C32;
        public static final int GL_STEREO = 0x0C33;
        public static final int GL_SAMPLE_BUFFERS = 0x80A8;
        public static final int GL_SAMPLES = 0x80A9;
        public static final int GL_SAMPLE_COVERAGE_VALUE = 0x80AA;
        public static final int GL_SAMPLE_COVERAGE_INVERT = 0x80AB;

        // --- String queries ---
        public static final int GL_VENDOR = 0x1F00;
        public static final int GL_RENDERER = 0x1F01;
        public static final int GL_VERSION = 0x1F02;
        public static final int GL_EXTENSIONS = 0x1F03;
        public static final int GL_SHADING_LANGUAGE_VERSION = 0x8B8C;

        // --- Stencil operations ---
        public static final int GL_KEEP = 0x1E00;
        public static final int GL_REPLACE = 0x1E01;
        public static final int GL_INCR = 0x1E02;
        public static final int GL_DECR = 0x1E03;
        public static final int GL_INVERT = 0x150A;
        public static final int GL_INCR_WRAP = 0x8507;
        public static final int GL_DECR_WRAP = 0x8508;
        public static final int GL_STENCIL_INDEX = 0x1901;
        public static final int GL_STENCIL_FUNC = 0x0B92;
        public static final int GL_STENCIL_VALUE_MASK = 0x0B93;
        public static final int GL_STENCIL_REF = 0x0B97;
        public static final int GL_STENCIL_FAIL = 0x0B94;
        public static final int GL_STENCIL_PASS_DEPTH_FAIL = 0x0B95;
        public static final int GL_STENCIL_PASS_DEPTH_PASS = 0x0B96;
        public static final int GL_STENCIL_BACK_FUNC = 0x8800;
        public static final int GL_STENCIL_BACK_VALUE_MASK = 0x8CA4;
        public static final int GL_STENCIL_BACK_REF = 0x8CA3;
        public static final int GL_STENCIL_BACK_FAIL = 0x8801;
        public static final int GL_STENCIL_BACK_PASS_DEPTH_FAIL = 0x8802;
        public static final int GL_STENCIL_BACK_PASS_DEPTH_PASS = 0x8803;
        public static final int GL_STENCIL_CLEAR_VALUE = 0x0B91;

        // --- Fog (no-ops in WebGL2, constants still needed) ---
        public static final int GL_FOG_COLOR = 0x0B66;
        public static final int GL_FOG_DENSITY = 0x0B62;
        public static final int GL_FOG_END = 0x0B64;
        public static final int GL_FOG_START = 0x0B63;
        public static final int GL_FOG_MODE = 0x0B65;
        public static final int GL_FOG_INDEX = 0x0B61;
        public static final int GL_FOG_COORD_SRC = 0x8450;
        public static final int GL_EXP = 0x0800;
        public static final int GL_EXP2 = 0x0801;

        // --- Logic op ---
        public static final int GL_LOGIC_OP_MODE = 0x0BF0;
        public static final int GL_CLEAR = 0x1500;
        public static final int GL_AND = 0x1501;
        public static final int GL_AND_REVERSE = 0x1502;
        public static final int GL_COPY = 0x1503;
        public static final int GL_AND_INVERTED = 0x1504;
        public static final int GL_NOOP = 0x1505;
        public static final int GL_XOR = 0x1506;
        public static final int GL_OR = 0x1507;
        public static final int GL_NOR = 0x1508;
        public static final int GL_EQUIV = 0x1509;
        // GL_INVERT already defined above (0x150A)
        public static final int GL_OR_REVERSE = 0x150B;
        public static final int GL_COPY_INVERTED = 0x150C;
        public static final int GL_OR_INVERTED = 0x150D;
        public static final int GL_NAND = 0x150E;
        public static final int GL_SET = 0x150F;

        // --- Texture env (no-ops in WebGL2) ---
        public static final int GL_TEXTURE_ENV = 0x2300;
        public static final int GL_TEXTURE_ENV_MODE = 0x2200;
        public static final int GL_MODULATE = 0x2100;
        public static final int GL_DECAL = 0x2101;
        public static final int GL_ADD = 0x0104;
        public static final int GL_COMBINE = 0x8570;
        public static final int GL_COMBINE_RGB = 0x8571;
        public static final int GL_COMBINE_ALPHA = 0x8572;
        public static final int GL_INTERPOLATE = 0x8575;
        public static final int GL_SUBTRACT = 0x84E7;
        public static final int GL_SRC0_RGB = 0x8580;
        public static final int GL_SRC1_RGB = 0x8581;
        public static final int GL_SRC2_RGB = 0x8582;
        public static final int GL_SRC0_ALPHA = 0x8588;
        public static final int GL_SRC1_ALPHA = 0x8589;
        public static final int GL_SRC2_ALPHA = 0x858A;
        public static final int GL_OPERAND0_RGB = 0x8590;
        public static final int GL_OPERAND1_RGB = 0x8591;
        public static final int GL_OPERAND2_RGB = 0x8592;
        public static final int GL_OPERAND0_ALPHA = 0x8598;
        public static final int GL_OPERAND1_ALPHA = 0x8599;
        public static final int GL_OPERAND2_ALPHA = 0x859A;
        public static final int GL_RGB_SCALE = 0x8573;
        public static final int GL_ALPHA_SCALE = 0x8574;

        // --- Framebuffer ---
        public static final int GL_FRAMEBUFFER = 0x8D40;
        public static final int GL_RENDERBUFFER = 0x8D41;
        public static final int GL_COLOR_ATTACHMENT0 = 0x8CE0;
        public static final int GL_DEPTH_ATTACHMENT = 0x8D00;
        public static final int GL_STENCIL_ATTACHMENT = 0x8D20;
        public static final int GL_DEPTH_STENCIL_ATTACHMENT = 0x821A;
        public static final int GL_FRAMEBUFFER_COMPLETE = 0x8CD5;
        public static final int GL_DRAW_FRAMEBUFFER = 0x8CA9;
        public static final int GL_READ_FRAMEBUFFER = 0x8CA8;
        public static final int GL_FRAMEBUFFER_UNDEFINED = 0x8219;

        // --- Polygon mode ---
        public static final int GL_POINT = 0x1B00;
        public static final int GL_LINE = 0x1B01;
        public static final int GL_FILL = 0x1B02;

        // --- Matrix mode ---
        public static final int GL_MODELVIEW = 0x1700;
        public static final int GL_PROJECTION = 0x1701;
        public static final int GL_TEXTURE = 0x1702;

        // --- Hints ---
        public static final int GL_DONT_CARE = 0x1100;
        public static final int GL_FASTEST = 0x1101;
        public static final int GL_NICEST = 0x1102;
        public static final int GL_PERSPECTIVE_CORRECTION_HINT = 0x0C50;
        public static final int GL_POINT_SMOOTH_HINT = 0x0C51;
        public static final int GL_LINE_SMOOTH_HINT = 0x0C52;
        public static final int GL_FOG_HINT = 0x0C54;
        public static final int GL_GENERATE_MIPMAP_HINT = 0x8192;
        public static final int GL_TEXTURE_COMPRESSION_HINT = 0x84EF;

        // --- Blend color ---
        public static final int GL_BLEND_COLOR = 0x8005;
        public static final int GL_BLEND_DST = 0x0BE0;
        public static final int GL_BLEND_SRC = 0x0BE1;
        public static final int GL_BLEND_EQUATION = 0x8009;

        // --- Read/Draw buffer ---
        public static final int GL_NONE = 0;
        public static final int GL_LEFT = 0x0406;
        public static final int GL_RIGHT = 0x0407;

        // --- Lighting ---
        public static final int GL_LIGHT0 = 0x4000;
        public static final int GL_LIGHT1 = 0x4001;
        public static final int GL_LIGHT2 = 0x4002;
        public static final int GL_LIGHT3 = 0x4003;
        public static final int GL_LIGHT4 = 0x4004;
        public static final int GL_LIGHT5 = 0x4005;
        public static final int GL_LIGHT6 = 0x4006;
        public static final int GL_LIGHT7 = 0x4007;
        public static final int GL_AMBIENT = 0x1200;
        public static final int GL_DIFFUSE = 0x1201;
        public static final int GL_SPECULAR = 0x1202;
        public static final int GL_POSITION = 0x1203;
        public static final int GL_SPOT_DIRECTION = 0x1204;
        public static final int GL_SPOT_EXPONENT = 0x1205;
        public static final int GL_SPOT_CUTOFF = 0x1206;
        public static final int GL_CONSTANT_ATTENUATION = 0x1207;
        public static final int GL_LINEAR_ATTENUATION = 0x1208;
        public static final int GL_QUADRATIC_ATTENUATION = 0x1209;
        public static final int GL_EMISSION = 0x1600;
        public static final int GL_SHININESS = 0x1601;
        public static final int GL_AMBIENT_AND_DIFFUSE = 0x1602;
        public static final int GL_COLOR_INDEXES = 0x1603;

        // --- Material face ---
        public static final int GL_FRONT_FACE = 0x0B46;

        // --- Current state ---
        public static final int GL_CURRENT_COLOR = 0x0B00;
        public static final int GL_CURRENT_NORMAL = 0x0B02;
        public static final int GL_CURRENT_TEXTURE_COORDS = 0x0B03;

        // --- Pixel transfer ---
        public static final int GL_MAP_COLOR = 0x0D10;
        public static final int GL_MAP_STENCIL = 0x0D11;
        public static final int GL_INDEX_SHIFT = 0x0D12;
        public static final int GL_INDEX_OFFSET = 0x0D13;
        public static final int GL_RED_SCALE = 0x0D14;
        public static final int GL_RED_BIAS = 0x0D15;
        public static final int GL_GREEN_SCALE = 0x0D18;
        public static final int GL_GREEN_BIAS = 0x0D19;
        public static final int GL_BLUE_SCALE = 0x0D1A;
        public static final int GL_BLUE_BIAS = 0x0D1B;
        // GL_ALPHA_SCALE already defined above (0x8574 / also 0x0D1C in legacy)
        public static final int GL_ALPHA_BIAS = 0x0D1D;
        public static final int GL_DEPTH_SCALE = 0x0D1E;
        public static final int GL_DEPTH_BIAS = 0x0D1F;

        // --- Scissor / viewport boxes ---
        public static final int GL_SCISSOR_BOX = 0x0C10;
        public static final int GL_MAX_ATTRIB_STACK_DEPTH = 0x0D35;

        // --- Pixel map ---
        public static final int GL_PIXEL_MAP_I_TO_I = 0x0C70;
        public static final int GL_PIXEL_MAP_S_TO_S = 0x0C71;
        public static final int GL_PIXEL_MAP_I_TO_R = 0x0C72;
        public static final int GL_PIXEL_MAP_I_TO_G = 0x0C73;
        public static final int GL_PIXEL_MAP_I_TO_B = 0x0C74;
        public static final int GL_PIXEL_MAP_I_TO_A = 0x0C75;
        public static final int GL_PIXEL_MAP_R_TO_R = 0x0C76;
        public static final int GL_PIXEL_MAP_G_TO_G = 0x0C77;
        public static final int GL_PIXEL_MAP_B_TO_B = 0x0C78;
        public static final int GL_PIXEL_MAP_A_TO_A = 0x0C79;

        // --- Buffer objects ---
        public static final int GL_ARRAY_BUFFER = 0x8892;
        public static final int GL_ELEMENT_ARRAY_BUFFER = 0x8893;
        public static final int GL_STATIC_DRAW = 0x88E4;
        public static final int GL_DYNAMIC_DRAW = 0x88E8;
        public static final int GL_STREAM_DRAW = 0x88E0;

        // --- Shader ---
        public static final int GL_FRAGMENT_SHADER = 0x8B30;
        public static final int GL_VERTEX_SHADER = 0x8B31;
        public static final int GL_COMPILE_STATUS = 0x8B81;
        public static final int GL_LINK_STATUS = 0x8B82;
        public static final int GL_VALIDATE_STATUS = 0x8B83;
        public static final int GL_INFO_LOG_LENGTH = 0x8B84;
        public static final int GL_ACTIVE_UNIFORMS = 0x8B86;
        public static final int GL_ACTIVE_ATTRIBUTES = 0x8B89;

        // ================================================================
        // GL Methods
        // ================================================================

        // --- State management ---
        public static void glEnable(int cap) {
                PlatformOpenGL._wglEnable(cap);
        }

        public static void glDisable(int cap) {
                PlatformOpenGL._wglDisable(cap);
        }

        // --- Clear ---
        public static void glClearColor(float r, float g, float b, float a) {
                PlatformOpenGL._wglClearColor(r, g, b, a);
        }

        public static void glClearDepth(double depth) {
                PlatformOpenGL._wglClearDepth(depth);
        }

        public static void glClearStencil(int s) {
                PlatformOpenGL._wglClearStencil(s);
        }

        public static void glClear(int mask) {
                PlatformOpenGL._wglClear(mask);
        }

        // --- Viewport / Scissor ---
        public static void glViewport(int x, int y, int w, int h) {
                PlatformOpenGL._wglViewport(x, y, w, h);
        }

        public static void glScissor(int x, int y, int w, int h) {
                PlatformOpenGL._wglScissor(x, y, w, h);
        }

        // --- Blend ---
        public static void glBlendFunc(int sfactor, int dfactor) {
                PlatformOpenGL._wglBlendFunc(sfactor, dfactor);
        }

        // --- Depth ---
        public static void glDepthFunc(int func) {
                PlatformOpenGL._wglDepthFunc(func);
        }

        public static void glDepthMask(boolean flag) {
                PlatformOpenGL._wglDepthMask(flag);
        }

        public static void glDepthRange(double near, double far) {
                PlatformOpenGL._wglDepthRange(near, far);
        }

        // --- Color mask ---
        public static void glColorMask(boolean r, boolean g, boolean b, boolean a) {
                PlatformOpenGL._wglColorMask(r, g, b, a);
        }

        // --- Face culling ---
        public static void glCullFace(int mode) {
                PlatformOpenGL._wglCullFace(mode);
        }

        public static void glFrontFace(int dir) {
                PlatformOpenGL._wglFrontFace(dir);
        }

        // --- Line width ---
        public static void glLineWidth(float width) {
                PlatformOpenGL._wglLineWidth(width);
        }

        // --- Polygon offset ---
        public static void glPolygonOffset(float factor, float units) {
                PlatformOpenGL._wglPolygonOffset(factor, units);
        }

        // --- Pixel store ---
        public static void glPixelStorei(int pname, int param) {
                PlatformOpenGL._wglPixelStorei(pname, param);
        }

        // --- Read pixels ---
        public static void glReadPixels(int x, int y, int w, int h, int format, int type, Object pixels) {
                PlatformOpenGL._wglReadPixels(x, y, w, h, format, type, pixels != null ? (JSObject) pixels : null);
        }

        // --- Drawing ---
        public static void glDrawArrays(int mode, int first, int count) {
                PlatformOpenGL._wglDrawArrays(mode, first, count);
        }

        public static void glDrawElements(int mode, int count, int type, int offset) {
                PlatformOpenGL._wglDrawElements(mode, count, type, offset);
        }

        public static void glDrawElements(int mode, int count, int type, long offset) {
                PlatformOpenGL._wglDrawElements(mode, count, type, (int) offset);
        }

        // --- Flush / Finish ---
        public static void glFlush() {
                PlatformOpenGL._wglFlush();
        }

        public static void glFinish() {
                PlatformOpenGL._wglFinish();
        }

        // --- Error / String queries ---
        public static int glGetError() {
                return PlatformOpenGL._wglGetError();
        }

        public static String glGetString(int name) {
                return PlatformOpenGL._wglGetString(name);
        }

        // --- State queries (single value) ---

        public static boolean glIsEnabled(int cap) {
                return PlatformOpenGL._wglIsEnabled(cap);
        }

        public static int glGetInteger(int pname) {
                return PlatformOpenGL._wglGetInteger(pname);
        }

        // --- State queries ---

        public static void glGetIntegerv(int pname, int[] params, int offset) {
                int len = params.length - offset;
                Int32Array arr = Int32Array.create(len);
                PlatformOpenGL._wglGetIntegerv(pname, arr);
                for (int i = 0; i < len; i++) {
                        params[offset + i] = arr.get(i);
                }
        }

        /**
         * NIO IntBuffer version - handled by the java.nio shim layer.
         */
        public static void glGetIntegerv(int pname, Object params) {
                // TODO: NIO IntBuffer shim
        }

        public static void glGetFloatv(int pname, float[] params, int offset) {
                int len = params.length - offset;
                Float32Array arr = Float32Array.create(len);
                PlatformOpenGL._wglGetFloatv(pname, arr);
                for (int i = 0; i < len; i++) {
                        params[offset + i] = arr.get(i);
                }
        }

        /**
         * NIO FloatBuffer version - handled by the java.nio shim layer.
         */
        public static void glGetFloatv(int pname, Object params) {
                // TODO: NIO FloatBuffer shim
        }

        public static void glGetBooleanv(int pname, boolean[] params, int offset) {
                int len = params.length - offset;
                Uint8Array arr = Uint8Array.create(len);
                PlatformOpenGL._wglGetBooleanv(pname, arr);
                for (int i = 0; i < len; i++) {
                        params[offset + i] = arr.get(i) != 0;
                }
        }

        /**
         * NIO ByteBuffer version - handled by the java.nio shim layer.
         */
        public static void glGetBooleanv(int pname, Object params) {
                // TODO: NIO ByteBuffer shim
        }

        // --- Texture management ---

        public static void glActiveTexture(int texture) {
                PlatformOpenGL._wglActiveTexture(texture);
        }

        public static void glBindTexture(int target, int texture) {
                PlatformOpenGL._wglBindTexture(target, getTextureObject(texture));
        }

        public static int glGenTextures() {
                JSObject tex = PlatformOpenGL._wglGenTextures();
                int id = nextTextureId++;
                textureMap.put(id, tex);
                return id;
        }

        public static void glGenTextures(int n, int[] textures, int offset) {
                for (int i = 0; i < n; i++) {
                        textures[offset + i] = glGenTextures();
                }
        }

        /**
         * NIO IntBuffer version - handled by the java.nio shim layer.
         */
        public static void glGenTextures(int n, Object textures) {
                // TODO: NIO IntBuffer shim
        }

        public static void glDeleteTextures(int texture) {
                JSObject tex = textureMap.remove(texture);
                if (tex != null) {
                        PlatformOpenGL._wglDeleteTextures(tex);
                }
        }

        public static void glDeleteTextures(int n, int[] textures, int offset) {
                for (int i = 0; i < n; i++) {
                        glDeleteTextures(textures[offset + i]);
                }
        }

        /**
         * NIO IntBuffer version - handled by the java.nio shim layer.
         */
        public static void glDeleteTextures(int n, Object textures) {
                // TODO: NIO IntBuffer shim
        }

        public static boolean glIsTexture(int texture) {
                return texture != 0 && textureMap.containsKey(texture);
        }

        // --- Texture image ---

        public static void glTexImage2D(int target, int level, int internalformat, int width, int height,
                        int border, int format, int type, Object pixels) {
                PlatformOpenGL._wglTexImage2D(target, level, internalformat, width, height, border, format, type,
                                pixels != null ? (JSObject) pixels : null);
        }

        public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height,
                        int format, int type, Object pixels) {
                PlatformOpenGL._wglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
                                pixels != null ? (JSObject) pixels : null);
        }

        public static void glTexParameteri(int target, int pname, int param) {
                PlatformOpenGL._wglTexParameteri(target, pname, param);
        }

        public static void glTexParameterf(int target, int pname, float param) {
                PlatformOpenGL._wglTexParameterf(target, pname, param);
        }

        public static void glGenerateMipmap(int target) {
                PlatformOpenGL._wglGenerateMipmap(target);
        }

        // --- Read/Draw buffer ---

        public static void glReadBuffer(int mode) {
                PlatformOpenGL._wglReadBuffer(mode);
        }

        /**
         * No-op: glDrawBuffer is not supported in WebGL2.
         */
        public static void glDrawBuffer(int mode) {
                // Not supported in WebGL2
        }

        // ================================================================
        // No-op methods (features not supported in WebGL2)
        // ================================================================

        /**
         * No-op: glLogicOp is not supported in WebGL2.
         */
        public static void glLogicOp(int opcode) {
                // Not supported in WebGL2
        }

        /**
         * No-op: glFogi is not supported in WebGL2.
         */
        public static void glFogi(int pname, int param) {
                // Not supported in WebGL2
        }

        /**
         * No-op: glFogf is not supported in WebGL2.
         */
        public static void glFogf(int pname, float param) {
                // Not supported in WebGL2
        }

        /**
         * No-op: glFogfv is not supported in WebGL2 (array version).
         */
        public static void glFogfv(int pname, float[] params, int offset) {
                // Not supported in WebGL2
        }

        /**
         * No-op: glFogfv is not supported in WebGL2 (NIO buffer version).
         */
        public static void glFogfv(int pname, Object params) {
                // Not supported in WebGL2
        }

        /**
         * No-op: glTexEnvf is not supported in WebGL2.
         */
        public static void glTexEnvf(int target, int pname, float param) {
                // Not supported in WebGL2
        }

        /**
         * No-op: glTexEnvi is not supported in WebGL2.
         */
        public static void glTexEnvi(int target, int pname, int param) {
                // Not supported in WebGL2
        }

        /**
         * No-op: glHint has no meaningful effect in WebGL2.
         */
        public static void glHint(int target, int mode) {
                // No-op in WebGL2
        }

        /**
         * No-op: glShadeModel is fixed-function pipeline (not in WebGL2).
         */
        public static void glShadeModel(int mode) {
                // Not supported in WebGL2
        }

        /**
         * No-op: glAlphaFunc is fixed-function pipeline (not in WebGL2).
         */
        public static void glAlphaFunc(int func, float ref) {
                // Not supported in WebGL2
        }
}
