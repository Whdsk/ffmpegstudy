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


}
