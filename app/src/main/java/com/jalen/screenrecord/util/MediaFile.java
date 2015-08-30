package com.jalen.screenrecord.util;

import java.util.HashMap;
import java.util.Locale;

/**
 * <div>Created by xxx on 2015/8/28. </div>
 *
 * <div>参考系统源码：/frameworks/base/media/java/android/media/MediaFile.java</div>
 */
public class MediaFile {

    // Video file types （这种写法提高了判断时的执行效率）
    public static final int FILE_TYPE_MP4     = 21;
    public static final int FILE_TYPE_M4V     = 22;
    public static final int FILE_TYPE_3GPP    = 23;
    public static final int FILE_TYPE_3GPP2   = 24;
    public static final int FILE_TYPE_WMV     = 25;
    public static final int FILE_TYPE_ASF     = 26;
    public static final int FILE_TYPE_MKV     = 27;
    public static final int FILE_TYPE_MP2TS   = 28;
    public static final int FILE_TYPE_AVI     = 29;
    public static final int FILE_TYPE_WEBM    = 30;
    private static final int FIRST_VIDEO_FILE_TYPE = FILE_TYPE_MP4; // 视频第一种文件类型
    private static final int LAST_VIDEO_FILE_TYPE = FILE_TYPE_WEBM; // 视频最后一种文件类型

    // More video file types
    public static final int FILE_TYPE_MP2PS   = 200;
    private static final int FIRST_VIDEO_FILE_TYPE2 = FILE_TYPE_MP2PS;
    private static final int LAST_VIDEO_FILE_TYPE2 = FILE_TYPE_MP2PS;

    // Audio file types
    public static final int FILE_TYPE_MP3     = 1;
    public static final int FILE_TYPE_M4A     = 2;
    public static final int FILE_TYPE_WAV     = 3;
    public static final int FILE_TYPE_AMR     = 4;
    public static final int FILE_TYPE_AWB     = 5;
    public static final int FILE_TYPE_WMA     = 6;
    public static final int FILE_TYPE_OGG     = 7;
    public static final int FILE_TYPE_AAC     = 8;
    public static final int FILE_TYPE_MKA     = 9;
    public static final int FILE_TYPE_FLAC    = 10;
    private static final int FIRST_AUDIO_FILE_TYPE = FILE_TYPE_MP3;
    private static final int LAST_AUDIO_FILE_TYPE = FILE_TYPE_FLAC;

    // MIDI file types
    public static final int FILE_TYPE_MID     = 11;
    public static final int FILE_TYPE_SMF     = 12;
    public static final int FILE_TYPE_IMY     = 13;
    private static final int FIRST_MIDI_FILE_TYPE = FILE_TYPE_MID;
    private static final int LAST_MIDI_FILE_TYPE = FILE_TYPE_IMY;

    // Image file types
    public static final int FILE_TYPE_JPEG    = 31;
    public static final int FILE_TYPE_GIF     = 32;
    public static final int FILE_TYPE_PNG     = 33;
    public static final int FILE_TYPE_BMP     = 34;
    public static final int FILE_TYPE_WBMP    = 35;
    public static final int FILE_TYPE_WEBP    = 36;
    private static final int FIRST_IMAGE_FILE_TYPE = FILE_TYPE_JPEG;
    private static final int LAST_IMAGE_FILE_TYPE = FILE_TYPE_WEBP;

    // Other popular file types
    public static final int FILE_TYPE_TEXT          = 100;
    public static final int FILE_TYPE_HTML          = 101;
    public static final int FILE_TYPE_PDF           = 102;
    public static final int FILE_TYPE_XML           = 103;
    public static final int FILE_TYPE_MS_WORD       = 104;
    public static final int FILE_TYPE_MS_EXCEL      = 105;
    public static final int FILE_TYPE_MS_POWERPOINT = 106;
    public static final int FILE_TYPE_ZIP           = 107;

    /**
     * 媒体类型，包含两个属性：文件类型、MIMETYPE
     */
    public static class MediaFileType {
        public final int fileType;
        public final String mimeType;

        MediaFileType(int fileType, String mimeType) {
            this.fileType = fileType;
            this.mimeType = mimeType;
        }
    }

    private static final HashMap<String, MediaFileType> sFileTypeMap
            = new HashMap<String, MediaFileType>();
    /**
     * <MimeType, FileType>映射关系集合
     */
    private static final HashMap<String, Integer> sMimeTypeMap
            = new HashMap<String, Integer>();

    /**
     * 添加文件类型与MimeType的yin
     * @param extension 文件拓展名
     * @param fileType 文件类型
     * @param mimeType MIMETYPE
     */
    static void addFileType(String extension, int fileType, String mimeType) {
        sFileTypeMap.put(extension, new MediaFileType(fileType, mimeType));
        sMimeTypeMap.put(mimeType, Integer.valueOf(fileType));
    }

    static {
        addFileType("MP3", FILE_TYPE_MP3, "audio/mpeg");
        addFileType("MPGA", FILE_TYPE_MP3, "audio/mpeg");
        addFileType("M4A", FILE_TYPE_M4A, "audio/mp4");
        addFileType("WAV", FILE_TYPE_WAV, "audio/x-wav");
        addFileType("AMR", FILE_TYPE_AMR, "audio/amr");
        addFileType("AWB", FILE_TYPE_AWB, "audio/amr-wb");
        addFileType("OGG", FILE_TYPE_OGG, "audio/ogg");
        addFileType("OGG", FILE_TYPE_OGG, "application/ogg");
        addFileType("OGA", FILE_TYPE_OGG, "application/ogg");
        addFileType("AAC", FILE_TYPE_AAC, "audio/aac");
        addFileType("AAC", FILE_TYPE_AAC, "audio/aac-adts");
        addFileType("MKA", FILE_TYPE_MKA, "audio/x-matroska");

        addFileType("MID", FILE_TYPE_MID, "audio/midi");
        addFileType("MIDI", FILE_TYPE_MID, "audio/midi");
        addFileType("XMF", FILE_TYPE_MID, "audio/midi");
        addFileType("RTTTL", FILE_TYPE_MID, "audio/midi");
        addFileType("SMF", FILE_TYPE_SMF, "audio/sp-midi");
        addFileType("IMY", FILE_TYPE_IMY, "audio/imelody");
        addFileType("RTX", FILE_TYPE_MID, "audio/midi");
        addFileType("OTA", FILE_TYPE_MID, "audio/midi");
        addFileType("MXMF", FILE_TYPE_MID, "audio/midi");

        addFileType("MPEG", FILE_TYPE_MP4, "video/mpeg");
        addFileType("MPG", FILE_TYPE_MP4, "video/mpeg");
        addFileType("MP4", FILE_TYPE_MP4, "video/mp4");
        addFileType("M4V", FILE_TYPE_M4V, "video/mp4");
        addFileType("3GP", FILE_TYPE_3GPP, "video/3gpp");
        addFileType("3GPP", FILE_TYPE_3GPP, "video/3gpp");
        addFileType("3G2", FILE_TYPE_3GPP2, "video/3gpp2");
        addFileType("3GPP2", FILE_TYPE_3GPP2, "video/3gpp2");
        addFileType("MKV", FILE_TYPE_MKV, "video/x-matroska");
        addFileType("WEBM", FILE_TYPE_WEBM, "video/webm");
        addFileType("TS", FILE_TYPE_MP2TS, "video/mp2ts");
        addFileType("AVI", FILE_TYPE_AVI, "video/avi");

        addFileType("WBMP", FILE_TYPE_WBMP, "image/vnd.wap.wbmp");
        addFileType("WEBP", FILE_TYPE_WEBP, "image/webp");

        addFileType("MPG", FILE_TYPE_MP2PS, "video/mp2p");
        addFileType("MPEG", FILE_TYPE_MP2PS, "video/mp2p");

        addFileType("TXT", FILE_TYPE_TEXT, "text/plain");
        addFileType("HTM", FILE_TYPE_HTML, "text/html");
        addFileType("HTML", FILE_TYPE_HTML, "text/html");
        addFileType("PDF", FILE_TYPE_PDF, "application/pdf");
        addFileType("DOC", FILE_TYPE_MS_WORD, "application/msword");
        addFileType("XLS", FILE_TYPE_MS_EXCEL, "application/vnd.ms-excel");
        addFileType("PPT", FILE_TYPE_MS_POWERPOINT, "application/mspowerpoint");
        addFileType("FLAC", FILE_TYPE_FLAC, "audio/flac");
        addFileType("ZIP", FILE_TYPE_ZIP, "application/zip");
        addFileType("MPG", FILE_TYPE_MP2PS, "video/mp2p");
        addFileType("MPEG", FILE_TYPE_MP2PS, "video/mp2p");
    }

    /**
     * 判断该文件类型是否属于视频文件
     * @param fileType 文件类型
     * @return true or false
     */
    public static boolean isVideoFileType(int fileType) {
        return (fileType >= FIRST_VIDEO_FILE_TYPE &&
                fileType <= LAST_VIDEO_FILE_TYPE)
                || (fileType >= FIRST_VIDEO_FILE_TYPE2 &&
                fileType <= LAST_VIDEO_FILE_TYPE2);
    }

    /**
     * 根据文件路径判断该文件是否属于音频文件
     * @param filePath 文件路径
     * @return true or false
     */
    public static boolean isVideoFile(String filePath){
        MediaFileType mediaFileType = getFileType(filePath);
        if (mediaFileType == null){
            return false;
        }
        return isVideoFileType(mediaFileType.fileType);
    }
    /**
     * 判断该文件类型是否属于音频文件
     * @param fileType 文件类型
     * @return true or false
     */
    public static boolean isAudioFileType(int fileType) {
        return ((fileType >= FIRST_AUDIO_FILE_TYPE &&
                fileType <= LAST_AUDIO_FILE_TYPE) ||
                (fileType >= FIRST_MIDI_FILE_TYPE &&
                        fileType <= LAST_MIDI_FILE_TYPE));
    }
    /**
     * 判断该文件类型是否属于图片文件
     * @param fileType 文件类型
     * @return true or false
     */
    public static boolean isImageFileType(int fileType) {
        return (fileType >= FIRST_IMAGE_FILE_TYPE &&
                fileType <= LAST_IMAGE_FILE_TYPE);
    }

    /**
     * 根据MimeType判断文件类型
     * @param mimeType MimeType
     * @return 文件类型
     */
    public static int getFileTypeForMimeType(String mimeType) {
        Integer value = sMimeTypeMap.get(mimeType);
        return (value == null ? 0 : value.intValue());
    }

    /**
     * 根据文件路径获取文件的MIMETYPE值
     * @param path 文件路径
     * @return MIMETYPE值
     */
    public static String getMimeTypeForFile(String path) {
        MediaFileType mediaFileType = getFileType(path);
        return (mediaFileType == null ? null : mediaFileType.mimeType);
    }

    /**
     * 根据文件路径获取文件的文件类型 <br/>
     * 其实就是截取文件名称的拓展名，然后去sFileTypeMap取到文件类型值
     * @param path 文件路径
     * @return 媒体文件类型
     */
    public static MediaFileType getFileType(String path) {
        int lastDot = path.lastIndexOf('.');
        if (lastDot < 0)
            return null;
        return sFileTypeMap.get(path.substring(lastDot + 1).toUpperCase(Locale.ROOT));
    }
}
