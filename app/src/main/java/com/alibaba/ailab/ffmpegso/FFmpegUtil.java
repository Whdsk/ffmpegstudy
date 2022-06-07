package com.alibaba.ailab.ffmpegso;

/**
 * @Author gaohangbo
 * @Date 6/7/22 2:07 下午
 * @Describe
 */
public class FFmpegUtil {
    private static String[] insert(String[] cmd, int position, String inputPath) {
        return insert(cmd, position, inputPath, null);
    }

    /**
     * insert inputPath and outputPath into target array
     */
    private static String[] insert(String[] cmd, int position, String inputPath, String outputPath) {
        if (cmd == null || inputPath == null || position < 2) {
            return cmd;
        }
        int len = (outputPath != null ? (cmd.length + 2) : (cmd.length + 1));
        String[] result = new String[len];
        System.arraycopy(cmd, 0, result, 0, position);
        result[position] = inputPath;
        System.arraycopy(cmd, position, result, position + 1, cmd.length - position);
        if (outputPath != null) {
            result[result.length - 1] = outputPath;
        }
        return result;
    }
    /**
     * transform video, according to your assigning the output format
     *
     * @param inputPath  input file
     * @param outputPath output file
     * @return transform video success or not
     */
    public static String[] transformVideo(String inputPath, String outputPath) {
        //just copy codec
//        String transformVideoCmd = "ffmpeg -i %s -vcodec copy -acodec copy %s";
        // assign the frameRate, bitRate and resolution
//        String transformVideoCmd = "ffmpeg -i %s -r 25 -b 200 -s 1080x720 %s";
        // assign the encoder
//        ffmpeg -i %s -vcodec libx264 -acodec libmp3lame %s
        String transformVideoCmd = "ffmpeg -i -vcodec libx264 -acodec libmp3lame";
        return insert(transformVideoCmd.split(" "), 2, inputPath, outputPath);
    }

    /**
     * add watermark with image to video, you could assign the location and bitRate
     *
     * @param inputPath  input file
     * @param imgPath    the path of the image
     * @param location   the location in the video(1:top left 2:top right 3:bottom left 4:bottom right)
     * @param bitRate    bitRate
     * @param offsetXY   the offset of x and y in the video
     * @param outputPath output file
     * @return add watermark success or not
     */
    public static String[] addWaterMarkImg(String inputPath, String imgPath, int location, int bitRate,
                                           int offsetXY, String outputPath) {
        String mBitRate = bitRate + "k";
        String overlay = obtainOverlay(offsetXY, offsetXY, location);
        String waterMarkCmd = "ffmpeg -i -i -b:v %s -filter_complex %s -preset:v superfast";
        waterMarkCmd = String.format(waterMarkCmd, mBitRate, overlay);
        return insert(waterMarkCmd.split(" "), 2, inputPath, 4, imgPath, outputPath);
    }

    public static String[] insert(String[] cmd, int position1, String inputPath1,
                                  int position2, String inputPath2, String outputPath) {
        if (cmd == null || inputPath1 == null || position1 < 2 || inputPath2 == null || position2 < 4) {
            return cmd;
        }
        int len = (outputPath != null ? (cmd.length + 3) : (cmd.length + 2));
        String[] result = new String[len];
        System.arraycopy(cmd, 0, result, 0, position1);
        result[position1] = inputPath1;
        System.arraycopy(cmd, position1, result, position1 + 1, position2 - position1 - 1);
        result[position2] = inputPath2;
        System.arraycopy(cmd, position2 - 1, result, position2 + 1, cmd.length - (position2 - 1));
        if (outputPath != null) {
            result[result.length - 1] = outputPath;
        }
        return result;
    }

    //水印显示的位置
    private static String obtainOverlay(int offsetX, int offsetY, int location) {
        switch (location) {
            case 2:
                return "overlay='(main_w-overlay_w)-" + offsetX + ":" + offsetY + "'";
            case 3:
                return "overlay='" + offsetX + ":(main_h-overlay_h)-" + offsetY + "'";
            case 4:
                return "overlay='(main_w-overlay_w)-" + offsetX + ":(main_h-overlay_h)-" + offsetY + "'";
            case 1:
            default:
                return "overlay=" + offsetX + ":" + offsetY;
        }
    }
}
