package aeren.leep.level;

public class PatternMatcher {
    public static final int[][][] PATTERNS = new int[][][] {{{1,1,-1}, {1,0,0}, {-1,0,0}},   //upper left
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
            for (int j = 0; j < p1[i].length; j++) {
                if (p1[i][j] == -1 || p2[i][j] == -1)
                    continue;

                if (p1[i][j] != p2[i][j])
                    return false;
            }
        }

        return true;
    }

    public static int[][] extractPattern(int[][] whole, int x, int y) {
        int[][] pattern = new int[3][3];
        for (int i = y - 1; i < y + 2; i++) {
            for (int j = x - 1; j < x + 2; j++) {
                if ((i >= whole.length || i < 0) || (j >= whole[i].length || j < 0)) {
                    pattern[i - y + 1][j - x + 1] = 0;
                    continue;
                }

                pattern[i - y + 1][j - x + 1] = whole[i][j];
            }
        }

        return pattern;
    }
}
