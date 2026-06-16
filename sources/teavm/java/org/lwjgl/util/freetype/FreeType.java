package org.lwjgl.util.freetype;

public final class FreeType {
    public static final long FT_ENCODING_UNICODE = 0L;
    public static final int FT_LOAD_DEFAULT = 0;
    public static final int FT_LOAD_RENDER = 1;
    public static final long FT_FLOOR = 1L;

    public static long ft_library_new() { return 0L; }
    public static void ft_library_done(long library) {}
    public static long ft_face_init(long library, long buffer, long size) { return 0L; }
    public static void ft_face_done(long face) {}
}
