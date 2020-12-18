package aeren.leep.level;

public class PatternMatcher {
    public final int[] TILES = new int[13];
    public final int[][][] PATTERNS = new int[12][3][3];

    public PatternMatcher() {
        PATTERNS[0] = new int[][] {{1,1,-1}, {1,-1,0}, {-1,0,0}}; //upper left
        PATTERNS[1] = new int[][] {{-1, 1,1}, {0,-1,1}, {0,0,-1}}; //upper right
        PATTERNS[2] = new int[][] {{-1,0,0}, {1,-1,0}, {-1,0,0}}; //mid left
        PATTERNS[3] = new int[][] {{0,0,-1}, {0,-1,1}, {0,0,-1}}; //mid right
        PATTERNS[4] = new int[][] {{-1,1,-1}, {0,-1,0}, {-1,0,-1}}; //mid up
        PATTERNS[5] = new int[][] {{-1,0,-1}, {0,-1,0}, {-1,1,-1}}; //mid down
        PATTERNS[6] = new int[][] {{-1,0,0}, {1,-1,0}, {1,1,-1}}; //lower left
        PATTERNS[8] = new int[][] {{0,0,-1}, {0,-1,1}, {-1,1,1}}; //lower right
        PATTERNS[9] = new int[][] {{0,0,0}, {0,-1,0}, {0,0,1}}; //upper left 2
        PATTERNS[10] = new int[][] {{0,0,0}, {0,-1,0}, {1,0,0}}; //upper right 2
        PATTERNS[11] = new int[][] {{0,0,1}, {0,-1,0}, {0,0,0}}; //lower left 2
        PATTERNS[12] = new int[][] {{1,0,0}, {0,-1,0}, {0,0,0}}; //lower right 2
    }

    public boolean doPatternsMatch(int[][] p1, int[][] p2) {
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
}
