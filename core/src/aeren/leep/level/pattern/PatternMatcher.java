package aeren.leep.level.pattern;

public class PatternMatcher {
    //patterns for auto tiling
    public static final int[][][] PATTERNS = new int[][][] {{{0,0,0}, {0,0,0}, {0,0,0}},     //mid
                                                            {{1,1,-1}, {1,0,0}, {-1,0,0}},   //upper left
                                                            {{-1, 1,1}, {0,0,1}, {0,0,-1}},  //upper right
                                                            {{-1,0,0}, {1,0,0}, {-1,0,0}},   //mid left
                                                            {{0,0,-1}, {0,0,1}, {0,0,-1}},   //mid right
                                                            {{-1,1,-1}, {0,0,0}, {-1,0,-1}}, //mid up
                                                            {{-1,0,-1}, {0,0,0}, {-1,1,-1}}, //mid down
                                                            {{-1,0,0}, {1,0,0}, {1,1,-1}},   //lower left
                                                            {{0,0,-1}, {0,0,1}, {-1,1,1}},   //lower right
                                                            {{0,0,0}, {0,0,0}, {0,0,1}},     //upper left 2
                                                            {{0,0,0}, {0,0,0}, {1,0,0}},     //upper right 2
                                                            {{0,0,1}, {0,0,0}, {0,0,0}},     //lower left 2
                                                            {{1,0,0}, {0,0,0}, {0,0,0}}};    //lower right 2

    public static boolean doPatternsMatch(int[][] p1, int[][] p2) {
        for (int i = 0; i < p1.length; i++) {
            for (int j = 0; j < p1[0].length; j++) {
                if (p1[i][j] == -1 || p2[i][j] == -1)
                    continue;

                if (p1[i][j] != p2[i][j])
                    return false;
            }
        }

        return true;
    }

    public static int[][] extractTilePattern(int[][] whole, int x, int y) {
        return extractPattern(whole, PATTERNS[0], x, y);
    }

    public static int[][] extractPattern(int[][] whole, int[][] model, int x, int y) {
        int[][] p2 = new int[model.length][model[0].length];

        for (int i = y - 1; i < y + model.length - 1; i++) {
            for (int j = x - 1; j < x + model[0].length - 1; j++) {
                if ((i >= whole.length || i < 0) || (j >= whole[0].length || j < 0)) {
                    p2[i - y + 1][j - x + 1] = 0;
                    continue;
                }

                p2[i - y + 1][j - x + 1] = whole[i][j];
            }
        }

        return p2;
    }

    public static void applyPattern(int[][] whole, int[][] pattern, int x, int y) {
        for (int i = y; i < y + pattern.length; i++) {
            for (int j = x; j < x + pattern[0].length; j++) {
                if ((i >= whole.length || i < 0) || (j >= whole[0].length || j < 0))
                    return;

                whole[i][j] = pattern[i - y][j - x];
            }
        }
    }
}
