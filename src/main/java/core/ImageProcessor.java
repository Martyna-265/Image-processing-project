package core;

public class ImageProcessor {

    // ==========================================================
    // PIXEL OPERATIONS
    // ==========================================================

    public static int[][][] applyBrightnessOffset(int[][][] originalMatrix, int offset) {
        if (originalMatrix == null) return null;

        int height = originalMatrix.length;
        int width = originalMatrix[0].length;
        int[][][] newMatrix = new int[height][width][3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int c = 0; c < 3; c++) {
                    int newValue = originalMatrix[y][x][c] + offset;
                    newMatrix[y][x][c] = Math.min(Math.max(newValue, 0), 255);
                }
            }
        }
        return newMatrix;
    }

    public static int[][][] applyBrightnessRange(int[][][] originalMatrix, int N1, int N2) {
        if (originalMatrix == null) return null;

        int height = originalMatrix.length;
        int width = originalMatrix[0].length;
        int[][][] newMatrix = new int[height][width][3];

        int J_min = 255, J_max = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int c = 0; c < 3; c++) {
                    int val = originalMatrix[y][x][c];
                    if (val < J_min) J_min = val;
                    if (val > J_max) J_max = val;
                }
            }
        }

        if (J_max == J_min) { return originalMatrix; }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int c = 0; c < 3; c++) {
                    int originalValue = originalMatrix[y][x][c];
                    double ratio = (double) (originalValue - J_min) / (J_max - J_min);
                    int newValue = (int) Math.round(ratio * (N2 - N1)) + N1;
                    newMatrix[y][x][c] = Math.min(Math.max(newValue, 0), 255);
                }
            }
        }
        return newMatrix;
    }

    public static int[][][] applyContrastPower(int[][][] originalMatrix, double alpha) {
        if (originalMatrix == null) return null;

        int height = originalMatrix.length;
        int width = originalMatrix[0].length;
        int[][][] newMatrix = new int[height][width][3];

        int J_max = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int c = 0; c < 3; c++) {
                    int val = originalMatrix[y][x][c];
                    if (val > J_max) J_max = val;
                }
            }
        }

        if (J_max == 0) return originalMatrix;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int c = 0; c < 3; c++) {
                    int originalValue = originalMatrix[y][x][c];
                    double ratio = (double) (originalValue) / J_max;
                    int newValue = (int) Math.round(255 * Math.pow(ratio, alpha));
                    newMatrix[y][x][c] = Math.min(Math.max(newValue, 0), 255);
                }
            }
        }
        return newMatrix;
    }

    public static int[][][] applyContrastLog(int[][][] originalMatrix) {
        if (originalMatrix == null) return null;

        int height = originalMatrix.length;
        int width = originalMatrix[0].length;
        int[][][] newMatrix = new int[height][width][3];

        int J_max = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int c = 0; c < 3; c++) {
                    int val = originalMatrix[y][x][c];
                    if (val > J_max) J_max = val;
                }
            }
        }

        if (J_max == 0) return originalMatrix;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int c = 0; c < 3; c++) {
                    int originalValue = originalMatrix[y][x][c];
                    double ratio = Math.log(1 + originalValue) / Math.log(1 + J_max);
                    int newValue = (int) Math.round(255 * ratio);
                    newMatrix[y][x][c] = Math.min(Math.max(newValue, 0), 255);
                }
            }
        }
        return newMatrix;
    }

    public static int[][][] applyGrayscale(int[][][] originalMatrix) {
        if (originalMatrix == null) return null;

        int height = originalMatrix.length;
        int width = originalMatrix[0].length;
        int[][][] newMatrix = new int[height][width][3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = originalMatrix[y][x][0];
                int g = originalMatrix[y][x][1];
                int b = originalMatrix[y][x][2];
                int gray = (int)(0.299 * r + 0.587 * g + 0.114 * b);

                newMatrix[y][x][0] = gray;
                newMatrix[y][x][1] = gray;
                newMatrix[y][x][2] = gray;
            }
        }

        return newMatrix;
    }

    public static int[][][] applyNegative(int[][][] originalMatrix) {
        if (originalMatrix == null) return null;

        int height = originalMatrix.length;
        int width = originalMatrix[0].length;
        int[][][] newMatrix = new int[height][width][3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                newMatrix[y][x][0] = 255 - originalMatrix[y][x][0];
                newMatrix[y][x][1] = 255 - originalMatrix[y][x][1];
                newMatrix[y][x][2] = 255 - originalMatrix[y][x][2];
            }
        }
        return newMatrix;
    }

    public static int[][][] applySegmentation(int[][][] originalMatrix, double t) {
        if (originalMatrix == null) return null;

        int height = originalMatrix.length;
        int width = originalMatrix[0].length;
        int[][][] newMatrix = new int[height][width][3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int c = 0; c < 3; c++) {
                    int originalValue = originalMatrix[y][x][c];
                    newMatrix[y][x][c] = (originalValue <= t) ? 255 : 0;
                }
            }
        }
        return newMatrix;
    }

    // ==========================================================
    // CONVOLUTION FILTERS
    // ==========================================================

    public static int[][][] applyConvolution(int[][][] originalMatrix, double[][] mask, OptionPanel.BoundaryMode currentBoundaryMode) {
        if (originalMatrix == null || mask == null) return null;

        int height = originalMatrix.length;
        int width = originalMatrix[0].length;
        int maskSize = mask.length;
        int offset = maskSize / 2;

        int outHeight = (currentBoundaryMode == OptionPanel.BoundaryMode.CROP) ? height - 2 * offset : height;
        int outWidth = (currentBoundaryMode == OptionPanel.BoundaryMode.CROP) ? width - 2 * offset : width;

        if (outHeight <= 0 || outWidth <= 0) return originalMatrix;

        int[][][] newMatrix = new int[outHeight][outWidth][3];

        double weightSum = 0;
        for (int i = 0; i < maskSize; i++) {
            for (int j = 0; j < maskSize; j++) {
                weightSum += mask[i][j];
            }
        }
        if (weightSum == 0) weightSum = 1;

        for (int y = 0; y < outHeight; y++) {
            for (int x = 0; x < outWidth; x++) {

                int origY = (currentBoundaryMode == OptionPanel.BoundaryMode.CROP) ? y + offset : y;
                int origX = (currentBoundaryMode == OptionPanel.BoundaryMode.CROP) ? x + offset : x;

                if (currentBoundaryMode == OptionPanel.BoundaryMode.KEEP_ORIGINAL) {
                    if (origY < offset || origY >= height - offset || origX < offset || origX >= width - offset) {
                        newMatrix[y][x][0] = originalMatrix[origY][origX][0];
                        newMatrix[y][x][1] = originalMatrix[origY][origX][1];
                        newMatrix[y][x][2] = originalMatrix[origY][origX][2];
                        continue;
                    }
                }

                double r = 0, g = 0, b = 0;

                for (int my = 0; my < maskSize; my++) {
                    for (int mx = 0; mx < maskSize; mx++) {
                        int pixelY = origY + my - offset;
                        int pixelX = origX + mx - offset;
                        double weight = mask[my][mx];

                        int[] rgb = getPixelWithBoundary(originalMatrix, pixelX, pixelY, width, height, currentBoundaryMode);

                        r += rgb[0] * weight;
                        g += rgb[1] * weight;
                        b += rgb[2] * weight;
                    }
                }

                newMatrix[y][x][0] = Math.min(Math.max((int)(r / weightSum), 0), 255);
                newMatrix[y][x][1] = Math.min(Math.max((int)(g / weightSum), 0), 255);
                newMatrix[y][x][2] = Math.min(Math.max((int)(b / weightSum), 0), 255);
            }
        }

        return newMatrix;
    }

    private static int[] getPixelWithBoundary(int[][][] matrix, int x, int y, int width, int height, OptionPanel.BoundaryMode mode) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return matrix[y][x];
        }

        switch (mode) {
            case PAD_BLACK: return new int[]{0, 0, 0};
            case PAD_WHITE: return new int[]{255, 255, 255};
            case PAD_GRAY:  return new int[]{128, 128, 128};
            case MIRROR:
                int mx = x;
                if (mx < 0) mx = Math.abs(mx);
                if (mx >= width) mx = 2 * width - mx - 2;
                mx = Math.max(0, Math.min(mx, width - 1));

                int my = y;
                if (my < 0) my = Math.abs(my);
                if (my >= height) my = 2 * height - my - 2;
                my = Math.max(0, Math.min(my, height - 1));

                return matrix[my][mx];
            case REPLICATE:
            case CROP:
            case KEEP_ORIGINAL:
            default:
                int sx = Math.max(0, Math.min(x, width - 1));
                int sy = Math.max(0, Math.min(y, height - 1));
                return matrix[sy][sx];
        }
    }

    public static double[][] getAveragingMask(int size, int centerWeight) {
        double[][] mask = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                mask[i][j] = 1.0;
            }
        }
        mask[size / 2][size / 2] = (double) centerWeight;
        return mask;
    }

    public static double[][] getGaussianMask(double sigma) {
        int size = (int) Math.ceil(6 * sigma);
        if (size % 2 == 0) size++;

        double[][] mask = new double[size][size];
        int offset = size / 2;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int cx = x - offset;
                int cy = y - offset;
                mask[y][x] = (1.0 / (2.0 * Math.PI * sigma * sigma)) * Math.exp(-(cx * cx + cy * cy) / (2.0 * sigma * sigma));
            }
        }
        return mask;
    }

    public static double[][] getSharpeningMask(String type) {
        if ("Strong".equals(type)) {
            return new double[][] {
                    {-1, -1, -1},
                    {-1,  9, -1},
                    {-1, -1, -1}
            };
        } else {
            return new double[][] {
                    { 0, -1,  0},
                    {-1,  5, -1},
                    { 0, -1,  0}
            };
        }
    }
}
